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



    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output
    TCCR1A = (1<<WGM10)|(1<<COM1A1)   // Set up the two Control registers of Timer1.
             |(1<<COM1B1);             // Wave Form Generation is Fast PWM 8 Bit,
	TCCR1B = _BV(CS10);
    OCR1A = 100;                       // Dutycycle of OC1A < 0%



    // Endless loop
	while (1) {

        // Is something to do for the TWI slave interface ?
		if (TWIS_ResonseRequired (&TWIS_ResonseType)) {
			switch (TWIS_ResonseType) {
                // TWI requests to read a byte from the master.
                // It is implicitely assumed, that the master
                // sends 8 bytes.
				case TWIS_ReadBytes:
				while(1) {
					OCR1A = TWIS_ReadAck ();
				}
//					for (i=0;i<7;i++) {
//						byte[i] = TWIS_ReadAck ();
//						printf ("Byte read: %d\n",byte[i]);
//					}
//					if (byte[1] > 0) {
//						OCR1A = byte[1];
//					}
//					byte[7] = TWIS_ReadNack ();
//					printf ("Byte read: %d\n",byte[7]);
//					TWIS_Stop ();
					break;

                 // TWI requests to write a byte to the master.
                 // It is implicitely assumed, that the master
                 // is prepared to receive 8 bytes.

				case TWIS_WriteBytes:
					TWIS_Write (j);
//					for (i=0;i<8;i++)
//						{
//							TWIS_Write (j++);
//						}
//				    TWIS_Stop ();
//				    TWIS_Init (15, 100000);
					break;
			}
		}
        // Do something else ....... e.g.:
// 		i++;
 		if (j > 250) {
 			j = 0;
 		}
	}
	return 0;
}