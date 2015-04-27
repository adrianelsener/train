#include <stdlib.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "twi/twislave.h"

#ifndef F_CPU
#define F_CPU 8000000UL
#endif
#include <util/delay.h>

//###################### Slave-Adresse
#define SLAVE_ADRESSE 0x50 								// Slave-Adresse

//###################### Macros
#define uniq(LOW,HEIGHT)	((HEIGHT << 8)|LOW)			// 2x 8Bit 	--> 16Bit
#define LOW_BYTE(x)        	(x & 0xff)					// 16Bit 	--> 8Bit
#define HIGH_BYTE(x)       	((x >> 8) & 0xff)			// 16Bit 	--> 8Bit

#define sbi(ADDRESS,BIT) 	((ADDRESS) |= (1<<(BIT)))	// set Bit
#define cbi(ADDRESS,BIT) 	((ADDRESS) &= ~(1<<(BIT)))	// clear Bit
#define	toggle(ADDRESS,BIT)	((ADDRESS) ^= (1<<BIT))		// Bit umschalten

#define	bis(ADDRESS,BIT)	(ADDRESS & (1<<BIT))		// bit is set?
#define	bic(ADDRESS,BIT)	(!(ADDRESS & (1<<BIT)))		// bit is clear?


//###################### Variablen
	uint16_t 	Variable=2345;				//Counter
	uint16_t	buffer;
	uint16_t	low, hight;

void Initialisierung(void) {
	cli();
	//### PORTS

	//### TWI
	init_twi_slave(SLAVE_ADRESSE);			//TWI als Slave mit Adresse SLAVE_ADRESSE starten

	sei();
}

int main(void) {

	DDRD=0xff; //PORTD as OUTPUT
    PORTD=0x00;


    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output

//	TCCR1A=(1<<COM0A1) | (1<<WGM00) | (1<<WGM01);	// PWM Phase Correct, Set OCR0A at TOP
//	TCCR0B=_BV(CS01) ;							// Prescaler 8



     TCCR1A = (1<<WGM10)|(1<<COM1A1)   // Set up the two Control registers of Timer1.
             |(1<<COM1B1);             // Wave Form Generation is Fast PWM 8 Bit,

	 TCCR1B = _BV(CS10);

     OCR1A = 0;                       // Dutycycle of OC1A = 25%
//     OCR1B = 127;                      // Dutycycle of OC1B = 50%


	Initialisierung();

	int up = 0;
	int pwmVal = 0;
	int a = 250;
	while(1) {
		//############################ write Data in txbuffer

		// 8Bit variable
		txbuffer[2]=3;
		txbuffer[3]=4;
		txbuffer[4]=5;
		txbuffer[5]=6;

		// 16Bit Variable --> 2x 8Bit Variable
		buffer		= Variable;
		txbuffer[0]	= LOW_BYTE(buffer);			//16bit --> 8bit
		txbuffer[1]	= HIGH_BYTE(buffer);		//16bit --> 8bit


		//############################ read Data form rxbuffer

		// 8Bit variable
		Variable	= rxbuffer[2];

		// 2x 8Bit Variable -->16Bit Variable
		low			= rxbuffer[0];
		hight		= rxbuffer[1];
		Variable	= uniq(low,hight);			// 2x 8Bit  --> 16Bit

		PORTD = hight;
		OCR1A = low;


		if (up == 1) {
			if (pwmVal < 255) {
				pwmVal++;
			} else {
				up = 0;
			}
		}
		if (up == 0) {
			if (pwmVal > 0) {
				pwmVal--;
			} else {
				up = 1;
			}
		}
		OCR1A = pwmVal;

		_delay_ms(10);

		//############################
	}
}



