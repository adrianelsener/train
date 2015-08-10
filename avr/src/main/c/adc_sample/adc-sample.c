#include <avr/io.h>

int main2(void) {

    DDRB = 0x03;                      // Set PB0 and PB1 as output


    ADMUX = (1<<REFS0);               // Set Reference to AVCC and input to ADC0
    ADCSRA = (1<<ADFR)|(1<<ADEN)      // Enable ADC, set prescaler to 16
            |(1<<ADPS2);              // Fadc=Fcpu/prescaler=1000000/16=62.5kHz
                                      // Fadc should be between 50kHz and 200kHz


    ADCSRA |= (1<<ADSC);              // Start the first conversion

    for(;;)                           // Endless loop
    {                                 // main() will never be left

        PORTB^= 0x02;                 // Toggle PB1

        if(ADC > 512)                 // Is tht ADC vaue greater than 512?
            PORTB |= 0x01;            // Set PB0
        else                          // Is the ADC not greater than 512
           PORTB &= ~0x01;            // Reset PB0
    }

    return 0;                         // This line will never be executed
}
