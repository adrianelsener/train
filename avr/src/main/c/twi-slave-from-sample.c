#include <stdio.h>
#include <avr/interrupt.h>

#ifndef F_CPU
#define F_CPU 8000000UL
#endif

#include "twi_sample/TWI_Slave/General.h"
#include "twi_sample/TWI_Slave/TWI_Slave.h"
#include "twi_sample/TWI_Slave/Delay.h"

#define CLIENT_NR 15

struct DATA {
	uint8_t destOcr;
	uint8_t changeSpeed;
	uint8_t waits;
	uint8_t waited; // indicates duration of single wait
	uint8_t direction; // [1,2]
};

struct DATA writeReadTwiData(struct DATA data) {
	uint8_t		TWIS_ResonseType;
    // Is something to do for the TWI slave interface ?
	if (TWIS_ResonseRequired (&TWIS_ResonseType)) {
		switch (TWIS_ResonseType) {
            // TWI requests to read a byte from the master.
			case TWIS_ReadBytes:
				data.waits = TWIS_ReadAck();
				data.changeSpeed = TWIS_ReadAck();
				data.destOcr = TWIS_ReadAck();
				data.direction = TWIS_ReadAck();
				TWIS_ReadNack();
				TWIS_Stop();
				break;
             // TWI requests to write a byte to the master.
			case TWIS_WriteBytes:
				TWIS_Write(OCR1A);
				TWIS_Write(data.destOcr);
				TWIS_Write(data.changeSpeed);
				TWIS_Write(data.direction);
			    TWIS_Stop();
				break;
		}
	}
	return data;
}

void setDirectionToForward() {
	//PB3 / PB4
    PORTB |= (1 << PB3);  //PB3 im PORTB setzen
    PORTB &= ~(1 << PB4); //PB4 im PORTB löschen
}

void setDirectionToBackward() {
    PORTB &= ~(1 << PB3); //PB3 im PORTB löschen
    PORTB |= (1 << PB4);  //PB4 im PORTB setzen
}

void setDirection(struct DATA data) {
	if (1 == data.direction) {
		setDirectionToForward();
	} else if (2 == data.direction) {
		setDirectionToBackward();
	}
}

struct DATA setOcr(struct DATA data) {
	if (OCR1A != data.destOcr) {
		if (data.waited >= (data.waits * 32)) {
			int8_t step;
			uint8_t nextVal = OCR1A;
			if (OCR1A > data.destOcr) {
				step = 0 - data.changeSpeed;
			} else {
				step = data.changeSpeed;
			}
			nextVal += step;
			if ((nextVal > data.destOcr && step > 0) || (nextVal < data.destOcr && step < 0)) {
				nextVal = data.destOcr;
			}
			OCR1A = nextVal;
			data.waited = 0;
		} else {
			data.waited++;
		}
	}
	return data;
}

int main (void) {
    // Clear any interrupt
	cli ();

    // Wait 0.5 second for POR
	Delay_ms (500);

    // Initiate the TWI slave interface
	TWIS_Init (CLIENT_NR, 100000);


    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output (0x06 -> 110b)
	DDRB |= (1 << PB3);					// PB3 as Output
	DDRB |= (1 << PB4);					// PB4 as Output

    TCCR1A = (1<<WGM10)|(1<<COM1A1)   // Set up the two Control registers of Timer1.
             |(1<<COM1B1);             // Wave Form Generation is Fast PWM 8 Bit,
	TCCR1B = _BV(CS10);

	struct DATA data = {
		.destOcr = 100,
	    .changeSpeed = 0,
		.waits = 0,
		.waited = 0,
		.direction = 1
	};

	OCR1A = data.destOcr;

	while (1) {
		data = writeReadTwiData(data);
		setDirection(data);
		data = setOcr(data);
	}
	return 0;
}
