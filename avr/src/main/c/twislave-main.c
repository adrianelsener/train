/*#################################################################################################
	Title	: TWI SLave
	Author	: Martin Junghans <jtronics@gmx.de>
	Hompage	: www.jtronics.de
	Software: AVR-GCC / Programmers Notpad 2
	License	: GNU General Public License 
	
	Aufgabe	:
	Betrieb eines AVRs mit Hardware-TWI-Schnittstelle als Slave. 
	Zu Beginn muss init_twi_slave mit der gew�nschten Slave-Adresse als Parameter aufgerufen werden. 
	Der Datenaustausch mit dem Master erfolgt �ber die Buffer rxbuffer und txbuffer, auf die von Master und Slave zugegriffen werden kann. 
	rxbuffer und txbuffer sind globale Variablen (Array aus uint8_t).
	
	Ablauf:
	Die Ansteuerung des rxbuffers, in den der Master schreiben kann, erfolgt �hnlich wie bei einem normalen I2C-EEPROM.
	Man sendet zun�chst die Bufferposition, an die man schreiben will, und dann die Daten. Die Bufferposition wird 
	automatisch hochgez�hlt, sodass man mehrere Datenbytes hintereinander schreiben kann, ohne jedesmal
	die Bufferadresse zu schreiben.
	Um den txbuffer vom Master aus zu lesen, �bertr�gt man zun�chst in einem Schreibzugriff die gew�nschte Bufferposition und
	liest dann nach einem repeated start die Daten aus. Die Bufferposition wird automatisch hochgez�hlt, sodass man mehrere
	Datenbytes hintereinander lesen kann, ohne jedesmal die Bufferposition zu schreiben.

	Abgefangene Fehlbedienung durch den Master:
	- Lesen �ber die Grenze des txbuffers hinaus
	- Schreiben �ber die Grenzen des rxbuffers hinaus
	- Angabe einer ung�ltigen Schreib/Lese-Adresse
	- Lesezuggriff, ohne vorher Leseadresse geschrieben zu haben
	
	LICENSE:
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

//#################################################################################################*/

#include <stdlib.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "twislave/twislave.h"

#ifndef F_CPU
#define F_CPU 16000000UL
#endif

#define INITIAL_OCR_SPEED 0
#define INITIAL_DIRECTION 1


//###################### Slave-Adresse
#define SLAVE_ADRESSE 0x09 								// Slave-Adresse

//###################### Macros
#define uniq(LOW,HEIGHT)	((HEIGHT << 8)|LOW)			// 2x 8Bit 	--> 16Bit
#define LOW_BYTE(x)        	(x & 0xff)					// 16Bit 	--> 8Bit
#define HIGH_BYTE(x)       	((x >> 8) & 0xff)			// 16Bit 	--> 8Bit


#define sbi(ADDRESS,BIT) 	((ADDRESS) |= (1<<(BIT)))	// set Bit
#define cbi(ADDRESS,BIT) 	((ADDRESS) &= ~(1<<(BIT)))	// clear Bit
#define	toggle(ADDRESS,BIT)	((ADDRESS) ^= (1<<BIT))		// Bit umschalten

#define	bis(ADDRESS,BIT)	(ADDRESS & (1<<BIT))		// bit is set?
#define	bic(ADDRESS,BIT)	(!(ADDRESS & (1<<BIT)))		// bit is clear?

struct DATA {
	uint8_t destOcr;
	uint8_t stepSize;
	uint8_t nrOfWaitsBetweenSteps;
	uint16_t waited; // indicates how many waits are done
	uint8_t direction; // [1,2]
};

////###################### Variablen
//	uint16_t 	Variable=2345;				//Zaehler
//	uint16_t	buffer;
//	uint16_t	low, hight;

//################################################################################################### Initialisierung
void Initialisierung(void) {
	cli();
	//### PORTS	
	
	//### TWI 
	init_twi_slave(SLAVE_ADRESSE);			//TWI als Slave mit Adresse slaveadr starten 
	
	sei();
}


void initOcr2t() {
	//	OCR2 = 128; // set PWM for 50% duty cycle
	OCR2 = 0;
	TCCR2 |= (1 << COM21); // set non-inverting mode
	TCCR2 |= (1 << WGM21) | (1 << WGM20); // set fast PWM Mode
	TCCR2 |= (1 << CS21); // set prescaler to 8 and starts PWM
}
struct DATA readTwiData(struct DATA data) {
	if (received[1] == 1) {
				data.nrOfWaitsBetweenSteps = rxbuffer[0];
				data.stepSize = rxbuffer[1];
				data.destOcr = rxbuffer[2];
				data.direction = rxbuffer[3];
				received[1] = 0;
	}
	return data;
}

struct DATA setOcr2t(struct DATA data) {
	if (OCR2 != data.destOcr) {
		if (data.waited >= (data.nrOfWaitsBetweenSteps * 32)) {
			int8_t stepWithSign;
			if (OCR2 > data.destOcr) {
				stepWithSign = 0 - data.stepSize;
			} else {
				stepWithSign = data.stepSize;
			}
			uint8_t nextVal = OCR2 + stepWithSign;
			if (((nextVal > data.destOcr) && (stepWithSign > 0)) || ((nextVal < data.destOcr) && (stepWithSign < 0))) {
				nextVal = data.destOcr;
			}
			if (nextVal == 0) {
				OCR2 = nextVal;
				TCCR2 &= ~(1 << CS21); // set prescaler to 8 and starts PWM
			} else {
				TCCR2 |= (1 << CS21);
				OCR2 = nextVal;
			}
			data.waited = 0;
		} else {
			data.waited++;
		}
		txbuffer[0] = OCR2;
	}
	return data;
}

void setDirectionToForwardt() {
	//PB3 / PB4
    PORTB |= (1 << PB5);  //PB3 im PORTB setzen
    PORTB &= ~(1 << PB4); //PB4 im PORTB löschen
}

void setDirectionToBackwardt() {
    PORTB &= ~(1 << PB5); //PB3 im PORTB löschen
    PORTB |= (1 << PB4);  //PB4 im PORTB setzen
}

void setDirectiont(struct DATA data) {
	if (1 == data.direction) {
		setDirectionToForwardt();
	} else if (2 == data.direction) {
		setDirectionToBackwardt();
	}
}
int main(void){
 
	Initialisierung();

    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output (0x06 -> 110b)
	DDRB |= (1 << PB3);					// PB4 as Output
	DDRB |= (1 << PB5);					// PB5 as Output
	DDRB |= (1 << PB4);					// PB4 as Output

	
	initOcr2t();
	// --------- test... end

	struct DATA data = {
		.destOcr = INITIAL_OCR_SPEED,
	    .stepSize = 0,
		.nrOfWaitsBetweenSteps = 0,
		.waited = 0,
		.direction = INITIAL_DIRECTION
	};

	OCR2 = data.destOcr;

	while (1) {
		data = readTwiData(data);
		setDirectiont(data);
		data = setOcr2t(data);
	}
	return 0;
}
//
//
//
//	while(1) {
//		//############################ write Data in txbuffer
//
//		// 8Bit variable
//		txbuffer[2]=3;
//		txbuffer[3]=4;
//		txbuffer[4]=5;
//		txbuffer[5]=6;
//
//		// 16Bit Variable --> 2x 8Bit Variable
//		buffer		= Variable;
//		txbuffer[0]	= LOW_BYTE(buffer);			//16bit --> 8bit
//		txbuffer[1]	= HIGH_BYTE(buffer);		//16bit --> 8bit
//
//
//		//############################ read Data form rxbuffer
//
//		// 8Bit variable
//		Variable	= rxbuffer[2];
//
//		// 2x 8Bit Variable -->16Bit Variable
//		low			= rxbuffer[0];
//		hight		= rxbuffer[1];
//		Variable	= uniq(low,hight);			// 2x 8Bit  --> 16Bit
//
//
//		//############################
//		} //end.while
//	return 0;
//} //end.main



