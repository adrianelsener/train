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


#define SLAVE_ADRESSE 0x09

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

void initTwi(void) {
	cli();
	init_twi_slave(SLAVE_ADRESSE);
	sei();
}

void initOcr() {
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

struct DATA setOcr(struct DATA data) {
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

void setDirectionToForward() {
	//PB3 / PB4
    PORTB |= (1 << PB5);  //PB3 im PORTB setzen
    PORTB &= ~(1 << PB4); //PB4 im PORTB löschen
}

void setDirectionToBackward() {
    PORTB &= ~(1 << PB5); //PB3 im PORTB löschen
    PORTB |= (1 << PB4);  //PB4 im PORTB setzen
}

void setDirection(struct DATA data) {
	if (1 == data.direction) {
		setDirectionToForward();
	} else if (2 == data.direction) {
		setDirectionToBackward();
	}
}
int main(void){
 
	initTwi();

    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output (0x06 -> 110b)
	DDRB |= (1 << PB3);					// PB4 as Output
	DDRB |= (1 << PB5);					// PB5 as Output
	DDRB |= (1 << PB4);					// PB4 as Output

	
	initOcr();
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
		setDirection(data);
		data = setOcr(data);
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



