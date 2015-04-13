
;----------------------------------------------------------------------
; Titel : Beispiel "LED an" fuer ATmega8 / example "LED on" for ATmega8
;----------------------------------------------------------------------
; Funktion / function : LED wird angeschalten / LED is turned on
; Schaltung / connection : PORTB.0=LED1
;----------------------------------------------------------------------
.equ		F_CPU, 3686400
#define	 	__SFR_OFFSET 0
#include	 <avr/io.h>
;----------------------------------------------------------------------
begin:
  rjmp	main	; 1	POWER ON RESET
  reti		; 2	Int0-Interrupt
  reti		; 3	Int1-Interrupt
  reti		; 4	TC2 Compare Match
  reti		; 5	TC2 Overflow
  reti		; 6	TC1 Capture
  reti		; 7	TC1 Compare Match A
  reti		; 8	TC1 Compare Match B
  reti		; 9	TC1 Overflow
  reti		;10	TC0 Overflow
  reti		;11	SPI, STC Serial Transfer Complete
  reti		;12	UART Rx Complete
  reti		;13	UART Data Register Empty
  reti		;14	UART Tx Complete
  reti		;15	ADC Conversion Complete
  reti		;16	EEPROM Ready
  reti		;17	Analog Comperator
  reti		;18	TWI (I C) Serial Interface
  reti		;19	Strore Program Memory Ready
;------------------------------------------------------------------------
main:
  ldi	r16,lo8(RAMEND)
  out	SPL,r16
  ldi	r16,hi8(RAMEND)
  out	SPH,r16 ;init Stack
  sbi	DDRB,0
;------------------------------------------------------------------------
mainloop:
  wdr
  ldi	r16,0b00000001 ;LED ON
  out	PORTB,r16
  rjmp	mainloop
;------------------------------------------------------------------------

