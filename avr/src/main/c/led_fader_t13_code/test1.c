#include <avr/io.h>

#define F_CPU 1200000UL  // 1,2 MHz
#include <util/delay.h>

int main (void)
{
	DDRB=1;	// Ausgang PB0
	
	TCCR0A=(1<<COM0A1) | (1<<WGM00) | (1<<WGM01);	// PWM Phase Correct, Set OCR0A at TOP
	TCCR0B=_BV(CS01) ;							// Prescaler 8
	
	int a=250;
	
	
	while (1)	{
	
		while (a>0){
			OCR0A = a; 
			_delay_ms(800);
			a--;
		}
		
		while (a<255){
			OCR0A = a; 
			_delay_ms(800);
			a++;
		}
		
	}
return 0;
}




