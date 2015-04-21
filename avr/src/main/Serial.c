#include<avr/io.h>
#include<inttypes.h>
#ifndef F_CPU
#define F_CPU 4000000UL
#endif
#include<util/delay.h>
#include<stdint.h>



#define BAUD 9600UL          // Baudrate

// Berechnungen
#define UBRR_VAL ((F_CPU+BAUD*8)/(BAUD*16)-1)   // clever runden
#define BAUD_REAL (F_CPU/(16*(UBRR_VAL+1)))     // Reale Baudrate
#define BAUD_ERROR ((BAUD_REAL*1000)/BAUD)      // Fehler in Promille, 1000 = kein Fehler.

#if ((BAUD_ERROR<990) || (BAUD_ERROR>1010))
  #error Systematischer Fehler der Baudrate grösser 1% und damit zu hoch!
#endif





void uart_init(void)
{
  UBRRH = UBRR_VAL >> 8;
  UBRRL = UBRR_VAL;


     /*Set Frame Format


     >> Asynchronous mode
     >> No Parity
     >> 1 StopBit
     >> char size 8

     */

     UCSRC=(1<<URSEL)|(3<<UCSZ0);


     //Enable The receiver and transmitter
     UCSRB=(1<<RXEN)|(1<<TXEN);
//
//    UCSRC = (1<<URSEL)|(1<<UCSZ1)|(1<<UCSZ0);  // Asynchron 8N1
//    UCSRB |= (1<<RXEN);                        // UART RX einschalten
}


char USARTReadChar()
{
   //Wait untill a data is available

   while(!(UCSRA & (1<<RXC)))
   {
      //Do nothing
   }

   //Now USART has got data from host
   //and is available is buffer

   return UDR;
}

//
///* Zeichen empfangen */
//uint8_t uart_getc(void)
//{
//    while (!(UCSRA & (1<<RXC)))   // warten bis Zeichen verfuegbar
//        ;
//    return UDR;                   // Zeichen aus UDR an Aufrufer zurueckgeben
//}
//


//This fuction writes the given "data" to
//the USART which then transmit it via TX line
void USARTWriteChar(char data)
{
   //Wait untill the transmitter is ready

   while(!(UCSRA & (1<<UDRE)))
   {
      //Do nothing
   }

   //Now write the data to USART buffer

   UDR=data;
}



int main(void) {
    uart_init();

    char data;

    while (1) {
       //Read data
       data=USARTReadChar();

       /* Now send the same data but but surround it in
       square bracket. For example if user sent 'a' our
       system will echo back '[a]'.

       */

       USARTWriteChar('[');
       USARTWriteChar(data);
       USARTWriteChar(']');
 }

//  while (1)
//  {
//    if ( (UCSRA & (1<<RXC)) )
//    {
//      // Zeichen wurde empfangen, jetzt abholen
//      uint8_t c;
//      c = uart_getc();
//      // hier etwas mit c machen z.B. auf PORT ausgeben
//      DDRC = 0xFF; // PORTC Ausgang
//      PORTC = c;
//    }
//    else
//    {
//      // Kein Zeichen empfangen, Restprogramm ausführen...
//    }
//  }
  return 0; // never reached
}