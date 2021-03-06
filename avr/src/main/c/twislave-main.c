#include <stdlib.h>
#include <stdint.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>
#include <inttypes.h>

#include "twislave/twislave.h"

#ifndef F_CPU
#define F_CPU 16000000UL
#endif

#define INITIAL_OCR_SPEED 0
#define INITIAL_DIRECTION 1

#define SLAVE_ADRESSE 0x08

#define sbi(ADDRESS,BIT) 	((ADDRESS) |= (1<<(BIT)))	// set Bit
#define cbi(ADDRESS,BIT) 	((ADDRESS) &= ~(1<<(BIT)))	// clear Bit
#define	toggle(ADDRESS,BIT)	((ADDRESS) ^= (1<<BIT))		// Bit umschalten

#define	bis(ADDRESS,BIT)	(ADDRESS & (1<<BIT))		// bit is set?
#define	bic(ADDRESS,BIT)	(!(ADDRESS & (1<<BIT)))		// bit is clear?

#define waitToReset 2000

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

struct PINSTATE {
	uint16_t countSinceLastReset;
	uint8_t state;
};
struct PINSTATE pinstates[8];

void initTwi(void) {
	cli();
	init_twi_slave(SLAVE_ADRESSE);
	sei();
}

void initOcr1() {
	OCR1A = 0;
	TCCR1A |= _BV(COM1A1) | _BV(COM1B1);
	TCCR1B |= _BV(WGM13);
	ICR1H = 0x00; //Top = 0xFFFF;
	ICR1L = 0xFF; //Normalerweise über ein TEMP-Register schreiben, aber da	der Timer noch nicht läuft sollte das nicht nötig sein
	TCCR1B |= _BV(CS11); // kein Prescaler und Timer starten

	OCR1A = 0;
}

struct DATA readTwiData(struct DATA data) {
	if (received[1] == 1) {
		data.nrOfWaitsBetweenSteps1 = rxbuffer[0];
		data.stepSize1 = rxbuffer[1];
		data.destOcr1 = rxbuffer[2];
		data.direction1 = rxbuffer[3];
		data.nrOfWaitsBetweenSteps2 = rxbuffer[4];
		data.stepSize2 = rxbuffer[5];
		data.destOcr2 = rxbuffer[6];
		data.direction2 = rxbuffer[7];
		received[1] = 0;
	}
	return data;
}

struct DATA setOcr(struct DATA data) {
	if (OCR1B != data.destOcr2) {
		if (data.waited2 >= (data.nrOfWaitsBetweenSteps2 * 32)) {
			int8_t stepWithSign;
			if (OCR1B > data.destOcr2) {
				stepWithSign = 0 - data.stepSize2;
			} else {
				stepWithSign = data.stepSize2;
			}
			uint8_t nextVal = OCR1B + stepWithSign;
			if (((nextVal > data.destOcr2) && (stepWithSign > 0))
					|| ((nextVal < data.destOcr2) && (stepWithSign < 0))) {
				nextVal = data.destOcr2;
			}
			OCR1B = nextVal;
			data.waited2 = 0;
		} else {
			data.waited2++;
		}
	}
	if (OCR1A != data.destOcr1) {
		if (data.waited1 >= (data.nrOfWaitsBetweenSteps1 * 32)) {
			int16_t stepWithSign;
			if (OCR1A > data.destOcr1) {
				stepWithSign = 0 - data.stepSize1;
			} else {
				stepWithSign = data.stepSize1;
			}
			uint16_t nextVal = OCR1A + stepWithSign;
			if (((nextVal > data.destOcr1) && (stepWithSign > 0))
					|| ((nextVal < data.destOcr1) && (stepWithSign < 0))) {
				nextVal = data.destOcr1;
			}
			OCR1A = nextVal;
			data.waited1 = 0;
		} else {
			data.waited1++;
		}
	}

	txbuffer[0] = OCR1A;
	txbuffer[1] = OCR1B;

	txbuffer[3] = data.destOcr1;
	txbuffer[4] = data.destOcr2;
	return data;
}

void setDirectionToForward1() {
	//PB3 / PB0
	PORTB |= (1 << PB0);  //im PORTB setzen
	PORTB &= ~(1 << PB3); //im PORTB löschen
}

void setDirectionToBackward1() {
	PORTB &= ~(1 << PB0); //im PORTB löschen
	PORTB |= (1 << PB3);  //im PORTB setzen
}

void setDirection1(struct DATA data) {
	if (1 == data.direction1) {
		setDirectionToForward1();
	} else if (2 == data.direction1) {
		setDirectionToBackward1();
	}
}

void setDirectionToForward2() {
	//PB5 / PB4
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
	DDRB |= (1 << PB1); 				// PB1 is output (PWM1A)
	DDRB |= (1 << PB2);					// PB2 as output (PWM1B)
	DDRB |= (1 << PB0);					// PB0 as output (-> directin PWM1B)
	DDRB |= (1 << PB3);					// PB3 as output (-> direction PWM1B)
	DDRB |= (1 << PB5);					// PB5 as output (-> direction PWM1A)
	DDRB |= (1 << PB4);					// PB4 as output (-> direction PWM1A)
}

void initPortDAsInputWithPullUp() {
	DDRD = 0x00;
	PORTD = 0xFF; // Pullup
}

void readInputState(int offset) {
	uint8_t tmp = 0x00;
	int pinOffset = offset * 4;
	for (int pinToCheck = PIND0 + pinOffset; pinToCheck <= PIND3 + pinOffset; pinToCheck++) {
		int isPinSet = (PIND & (1 << pinToCheck));
		if (isPinSet) {
			if (pinstates[pinToCheck].countSinceLastReset <= 0) {
				pinstates[pinToCheck].state = 1;
			} else {
				pinstates[pinToCheck].countSinceLastReset = pinstates[pinToCheck].countSinceLastReset - 1;
			}
		} else {
			pinstates[pinToCheck].state = 0;
			pinstates[pinToCheck].countSinceLastReset = waitToReset;
		}
		tmp ^= (-pinstates[pinToCheck].state ^ tmp) & (1 << (pinToCheck - pinOffset));
	}
	txbuffer[11 + offset] = tmp;
}

void readInputStates() {
	readInputState(0);
	readInputState(1);
}

void initPinState() {
	for (int i = 0; i < 8; i++) {
		pinstates[i].countSinceLastReset = 0;
		pinstates[i].state = 1;
	}
}

int main(void) {
	initTwi();

	initPorts();
	initOcr1();
	initPortDAsInputWithPullUp();
	initPinState();

	struct DATA data = { //
			.destOcr1 = INITIAL_OCR_SPEED, //
					.destOcr2 = INITIAL_OCR_SPEED, //
					.stepSize1 = 0, //
					.stepSize2 = 0, //
					.nrOfWaitsBetweenSteps1 = 0, //
					.nrOfWaitsBetweenSteps2 = 0, //
					.waited1 = 0, //
					.waited2 = 0, //
					.direction1 = INITIAL_DIRECTION, //
					.direction2 = INITIAL_DIRECTION };

	OCR1B = INITIAL_OCR_SPEED;
	OCR1A = INITIAL_OCR_SPEED;

	while (1) {
		data = readTwiData(data);
		readInputStates();
		setDirection1(data);
		setDirection2(data);
		data = setOcr(data);
	}
	return 0;
}
