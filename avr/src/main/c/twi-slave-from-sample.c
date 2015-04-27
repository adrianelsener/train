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
	uint8_t		i=0;
	uint8_t		j=0;
	uint8_t		byte[8];
	uint8_t		TWIS_ResonseType;

    // Clear any interrupt
	cli ();

    // Wait 0.5 second for POR
	Delay_ms (500);

    // Initiate the TWI slave interface
	TWIS_Init (15, 100000);

    // Endless loop
	while (1) {

        // Is something to do for the TWI slave interface ?
		if (TWIS_ResonseRequired (&TWIS_ResonseType)) {
			switch (TWIS_ResonseType) {
                // TWI requests to read a byte from the master.
                // It is implicitely assumed, that the master
                // sends 8 bytes.
				case TWIS_ReadBytes:
					for (i=0;i<7;i++) {
						byte[0] = TWIS_ReadAck ();
					}
					byte[7] = TWIS_ReadNack ();
					TWIS_Stop ();
					break;

                 // TWI requests to write a byte to the master.
                 // It is implicitely assumed, that the master
                 // is prepared to receive 8 bytes.

				case TWIS_WriteBytes:
					for (i=0;i<8;i++)
						{
						TWIS_Write (j++);
						}
				    TWIS_Stop ();
					break;
			}
		}
        // Do something else ....... e.g.:
 		i++;
	}
	return 0;
}