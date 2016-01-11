#include <stdlib.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>

#include "twislave/twislave.h"

#ifndef F_CPU
#define F_CPU 8000000UL
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
	uint8_t destOcr1;
	uint8_t stepSize1;
	uint8_t nrOfWaitsBetweenSteps1;
	uint16_t waited1; // indicates how many waits are done
	uint8_t direction1; // [1,2]
	uint8_t destOcr2;
	uint8_t stepSize2;
	uint8_t nrOfWaitsBetweenSteps2;
	uint16_t waited2; // indicates how many waits are done
	uint8_t direction2; // [1,2]
};

void initTwi(void) {
	cli();
	init_twi_slave(SLAVE_ADRESSE);
	sei();
}

void initOcr2() {
	OCR2 = 0;
	TCCR2 |= (1 << COM21); // set non-inverting mode
	TCCR2 |= (1 << WGM21) | (1 << WGM20); // set fast PWM Mode
	TCCR2 |= (1 << CS21); // set prescaler to 8 and starts PWM
}

void initOcr1() {
	OCR1A = 0;
	TCCR1A |= (1 << COM1A1); // set non-inverting mode
//	TCCR1A |= (1 << WGM11) | (1 << WGM10); // set fast PWM Mode
	TCCR1A |= (1 << WGM10); // set fast PWM Mode 8-bit
	TCCR1A |= (1 << CS11); // set prescaler to 8 and starts PWM
}

struct DATA readTwiData(struct DATA data) {
	if (received[1] == 1) {
		if (1 == rxbuffer[4]) {
			data.nrOfWaitsBetweenSteps1 = rxbuffer[0];
			data.stepSize1 = rxbuffer[1];
			data.destOcr1 = rxbuffer[2];
			data.direction1 = rxbuffer[3];
		} else if (2 == rxbuffer[4]) {
			data.nrOfWaitsBetweenSteps2 = rxbuffer[0];
			data.stepSize2 = rxbuffer[1];
			data.destOcr2 = rxbuffer[2];
			data.direction2 = rxbuffer[3];
		}
		received[1] = 0;
	}
	return data;
}

struct DATA setOcr(struct DATA data) {
	if (OCR2 != data.destOcr2) {
		if (data.waited2 >= (data.nrOfWaitsBetweenSteps2 * 32)) {
			int8_t stepWithSign;
			if (OCR2 > data.destOcr2) {
				stepWithSign = 0 - data.stepSize2;
			} else {
				stepWithSign = data.stepSize2;
			}
			uint8_t nextVal = OCR2 + stepWithSign;
			if (((nextVal > data.destOcr2) && (stepWithSign > 0)) || ((nextVal < data.destOcr2) && (stepWithSign < 0))) {
				nextVal = data.destOcr2;
			}
			if (nextVal == 0) {
				OCR2 = nextVal;
				TCCR2 &= ~(1 << CS21); // set prescaler to 8 and starts PWM
			} else {
				TCCR2 |= (1 << CS21);
				OCR2 = nextVal;
			}
			data.waited2 = 0;
		} else {
			data.waited2++;
		}
	}
	if (OCR1A != data.destOcr1) {
		if (data.waited1 >= (data.nrOfWaitsBetweenSteps1 * 32)) {
			int8_t stepWithSign;
			if (OCR1A > data.destOcr1) {
				stepWithSign = 0 - data.stepSize1;
			} else {
				stepWithSign = data.stepSize1;
			}
			uint8_t nextVal = OCR1A + stepWithSign;
			if (((nextVal > data.destOcr1) && (stepWithSign > 0)) || ((nextVal < data.destOcr1) && (stepWithSign < 0))) {
				nextVal = data.destOcr1;
			}
			if (nextVal == 0) {
				OCR1A = nextVal;
				TCCR1A &= ~(1 << CS11); // set prescaler to 8 and starts PWM
			} else {
				TCCR1A |= (1 << CS11);
				OCR1A = nextVal;
			}
			data.waited1 = 0;
		} else {
			data.waited1++;
		}
	}

	txbuffer[0] = OCR1A;
	txbuffer[1] = OCR2;
	return data;
}

void setDirectionToForward1() {
	//PB3 / PB4
    PORTB |= (1 << PB0);  //im PORTB setzen
    PORTB &= ~(1 << PB2); //im PORTB löschen
}

void setDirectionToBackward1() {
    PORTB &= ~(1 << PB0); //im PORTB löschen
    PORTB |= (1 << PB2);  //im PORTB setzen
}

void setDirection1(struct DATA data) {
	if (1 == data.direction1) {
		setDirectionToForward1();
	} else if (2 == data.direction1) {
		setDirectionToBackward1();
	}
}

void setDirectionToForward2() {
	//PB3 / PB4
    PORTB |= (1 << PB5);  //PB3 im PORTB setzen
    PORTB &= ~(1 << PB4); //PB4 im PORTB löschen
}

void setDirectionToBackward2() {
    PORTB &= ~(1 << PB5); //PB3 im PORTB löschen
    PORTB |= (1 << PB4);  //PB4 im PORTB setzen
}

void setDirection2(struct DATA data) {
	if (1 == data.direction2) {
		setDirectionToForward2();
	} else if (2 == data.direction2) {
		setDirectionToBackward2();
	}
}

void initPorts() {
    DDRB |= (1 << PB1); 				// PB1 is output (PWM1)
    DDRB |= (1 << PB0);					// PB0 as output (-> directin PWM1)
    DDRB |= (1 << PB2);					// PB2 as output (-> directin PWM1)
	DDRB |= (1 << PB3);					// PB3 as output (PWM2)
	DDRB |= (1 << PB5);					// PB5 as output (-> direction PWM2)
	DDRB |= (1 << PB4);					// PB4 as output (-> direction PWM2)
}

int main(void){
	initTwi();

	initPorts();

	initOcr1();
	initOcr2();

	struct DATA data = {
		.destOcr1 = INITIAL_OCR_SPEED,
		.destOcr2 = INITIAL_OCR_SPEED,
	    .stepSize1 = 0,
	    .stepSize2 = 0,
		.nrOfWaitsBetweenSteps1 = 0,
		.nrOfWaitsBetweenSteps2 = 0,
		.waited1 = 0,
		.waited2 = 0,
		.direction1 = INITIAL_DIRECTION,
		.direction2 = INITIAL_DIRECTION
	};

	OCR2 = INITIAL_OCR_SPEED;
	OCR1A = INITIAL_OCR_SPEED;

	while (1) {
		data = readTwiData(data);
		setDirection1(data);
		setDirection2(data);
		data = setOcr(data);
	}
	return 0;
}
