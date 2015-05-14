#include <stdio.h>
#include <avr/interrupt.h>

#ifndef F_CPU
#define F_CPU 8000000UL
#endif

#include "twi_sample/TWI_Slave/General.h"
#include "twi_sample/TWI_Slave/TWI_Slave.h"
#include "twi_sample/TWI_Slave/Delay.h"

int main (void)
	{
	uint8_t		TWIS_ResonseType;

    // Clear any interrupt
	cli ();

    // Wait 0.5 second for POR
	Delay_ms (500);

    // Initiate the TWI slave interface
	TWIS_Init (15, 100000);



    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output
    TCCR1A = (1<<WGM10)|(1<<COM1A1)   // Set up the two Control registers of Timer1.
             |(1<<COM1B1);             // Wave Form Generation is Fast PWM 8 Bit,
	TCCR1B = _BV(CS10);

	uint8_t destOcr = 100;
	OCR1A = destOcr;
    uint8_t changeSpeed = 0;
	uint8_t waits = 0;
	uint16_t waited = 0;

	while (1) {

        // Is something to do for the TWI slave interface ?
		if (TWIS_ResonseRequired (&TWIS_ResonseType)) {
			switch (TWIS_ResonseType) {
                // TWI requests to read a byte from the master.
				case TWIS_ReadBytes:
					waits = TWIS_ReadAck ();
					changeSpeed = TWIS_ReadAck ();
					destOcr = TWIS_ReadAck();
							TWIS_ReadNack ();
					TWIS_Stop ();
					break;

                 // TWI requests to write a byte to the master.
				case TWIS_WriteBytes:
					TWIS_Write (OCR1A);
					TWIS_Write (destOcr);
					TWIS_Write (changeSpeed);
				    TWIS_Stop ();
					break;
			}
		}
		if (OCR1A != destOcr) {
			if (waited >= (waits * 256)) {
				uint8_t step;
				uint8_t nextVal = OCR1A;
				if (OCR1A > destOcr) {
					step = 0 - changeSpeed;
				} else {
					step = changeSpeed;
				}
				nextVal += step;
				if ((nextVal > destOcr && step > 0) || (nextVal < destOcr && step < 0)) {
					nextVal = destOcr;
				}
				OCR1A = destOcr;
				waited = 0;
			} else {
				waited++;
			}
		}
	}
	return 0;
}
