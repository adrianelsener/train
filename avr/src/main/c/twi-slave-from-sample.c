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
    OCR1A = 100;                       // Dutycycle of OC1A < 0%

    uint8_t count = 0;
    uint8_t first;

    // Endless loop
	while (1) {

        // Is something to do for the TWI slave interface ?
		if (TWIS_ResonseRequired (&TWIS_ResonseType)) {
			switch (TWIS_ResonseType) {
                // TWI requests to read a byte from the master.
				case TWIS_ReadBytes:
					first = TWIS_ReadAck ();
					OCR1A = TWIS_ReadAck();
							TWIS_ReadNack ();
//					OCR1A = TWIS_ReadNack ();

					TWIS_Stop ();
					break;

                 // TWI requests to write a byte to the master.
                 // It is implicitely assumed, that the master
                 // is prepared to receive 8 bytes.

				case TWIS_WriteBytes:
					TWIS_Write (OCR1A);
					TWIS_Write (first);
				    TWIS_Stop ();
					break;
			}
		}
	}
	return 0;
}
