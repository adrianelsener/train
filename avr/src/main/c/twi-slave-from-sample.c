//#include <stdio.h>
//#include <avr/interrupt.h>
//
//#ifndef F_CPU
//#define F_CPU 8000000UL
//#endif
//
//#include "twi_sample/TWI_Slave/General.h"
//#include "twi_sample/TWI_Slave/TWI_Slave.h"
//#include "twi_sample/TWI_Slave/Delay.h"
//
//#define CLIENT_NR 15
//#define TWI_SPEED 100000
//#define INITIAL_OCR_SPEED 0
//#define INITIAL_DIRECTION 1
//
//struct DATA {
//	uint8_t destOcr;
//	uint8_t stepSize;
//	uint8_t nrOfWaitsBetweenSteps;
//	uint16_t waited; // indicates how many waits are done
//	uint8_t direction; // [1,2]
//};
//
//struct DATA writeReadTwiData(struct DATA data) {
//	uint8_t		TWIS_ResonseType;
//    // Is something to do for the TWI slave interface ?
//	if (TWIS_ResonseRequired (&TWIS_ResonseType)) {
//		switch (TWIS_ResonseType) {
//            // TWI requests to read a byte from the master.
//			case TWIS_ReadBytes:
//				data.nrOfWaitsBetweenSteps = TWIS_ReadAck();
//				data.stepSize = TWIS_ReadAck();
//				data.destOcr = TWIS_ReadAck();
//				data.direction = TWIS_ReadAck();
//				TWIS_ReadNack();
//				TWIS_Stop();
//				break;
//             // TWI requests to write a byte to the master.
//			case TWIS_WriteBytes:
////				TWIS_Write(OCR1A);
//				TWIS_Write(OCR2);
////				TWIS_Write(data.destOcr);
////				TWIS_Write(data.changeSpeed);
////				TWIS_Write(data.waits);
////				TWIS_Write(data.direction);
//			    TWIS_Stop();
//				break;
//		}
//	}
//	return data;
//}
//
//void setDirectionToForward() {
//	//PB3 / PB4
//    PORTB |= (1 << PB5);  //PB3 im PORTB setzen
//    PORTB &= ~(1 << PB4); //PB4 im PORTB löschen
//}
//
//void setDirectionToBackward() {
//    PORTB &= ~(1 << PB5); //PB3 im PORTB löschen
//    PORTB |= (1 << PB4);  //PB4 im PORTB setzen
//}
//
//void setDirection(struct DATA data) {
//	if (1 == data.direction) {
//		setDirectionToForward();
//	} else if (2 == data.direction) {
//		setDirectionToBackward();
//	}
//}
//
//struct DATA setOcr(struct DATA data) {
//	if (OCR1A != data.destOcr) {
//		if (data.waited >= (data.nrOfWaitsBetweenSteps * 32)) {
//			int8_t stepWithSign;
//			if (OCR1A > data.destOcr) {
//				stepWithSign = 0 - data.stepSize;
//			} else {
//				stepWithSign = data.stepSize;
//			}
//			uint8_t nextVal = OCR1A + stepWithSign;
//			if (((nextVal > data.destOcr) && (stepWithSign > 0)) || ((nextVal < data.destOcr) && (stepWithSign < 0))) {
//				nextVal = data.destOcr;
//			}
//			OCR1A = nextVal;
//			data.waited = 0;
//		} else {
//			data.waited++;
//		}
//	}
//	return data;
//}
//
//struct DATA setOcr2(struct DATA data) {
//	if (OCR2 != data.destOcr) {
//		if (data.waited >= (data.nrOfWaitsBetweenSteps * 32)) {
//			int8_t stepWithSign;
//			if (OCR2 > data.destOcr) {
//				stepWithSign = 0 - data.stepSize;
//			} else {
//				stepWithSign = data.stepSize;
//			}
//			uint8_t nextVal = OCR2 + stepWithSign;
//			if (((nextVal > data.destOcr) && (stepWithSign > 0)) || ((nextVal < data.destOcr) && (stepWithSign < 0))) {
//				nextVal = data.destOcr;
//			}
//			if (nextVal == 0) {
//				OCR2 = nextVal;
//				TCCR2 &= ~(1 << CS21); // set prescaler to 8 and starts PWM
//			} else {
//				TCCR2 |= (1 << CS21);
//				OCR2 = nextVal;
//			}
//			data.waited = 0;
//		} else {
//			data.waited++;
//		}
//	}
//	return data;
//}
//
//void initOcr() {
//	    TCCR1A = (1<<WGM10)|(1<<COM1A1)   // Set up the two Control registers of Timer1.
//	             |(1<<COM1B1);             // Wave Form Generation is Fast PWM 8 Bit,
//		TCCR1B = _BV(CS10);
//}
//
//void initOcr2() {
//	//	OCR2 = 128; // set PWM for 50% duty cycle
//	OCR2 = 0;
//	TCCR2 |= (1 << COM21); // set non-inverting mode
//	TCCR2 |= (1 << WGM21) | (1 << WGM20); // set fast PWM Mode
//	TCCR2 |= (1 << CS21); // set prescaler to 8 and starts PWM
//}
//
//int _main (void) {
//    // Clear any interrupt
//	cli ();
//
//    // Wait 0.5 second for POR
//	Delay_ms (500);
//
//    // Initiate the TWI slave interface
//	TWIS_Init (CLIENT_NR,TWI_SPEED);
//
//
//    DDRB = 0x06;                      // Set Port PB1 and PB2 as Output (0x06 -> 110b)
//	DDRB |= (1 << PB3);					// PB4 as Output
//	DDRB |= (1 << PB5);					// PB5 as Output
//	DDRB |= (1 << PB4);					// PB4 as Output
//
//
//	// --------- runs... start
////	initOcr();
//	// --------- runs... end
//	// --------- test... begin
//	initOcr2();
//	// --------- test... end
//
//	struct DATA data = {
//		.destOcr = INITIAL_OCR_SPEED,
//	    .stepSize = 0,
//		.nrOfWaitsBetweenSteps = 0,
//		.waited = 0,
//		.direction = INITIAL_DIRECTION
//	};
//
////	OCR1A = data.destOcr;
//	OCR2 = data.destOcr;
//
//	while (1) {
//		data = writeReadTwiData(data);
//		setDirection(data);
//		data = setOcr2(data);
//	}
//	return 0;
//}
