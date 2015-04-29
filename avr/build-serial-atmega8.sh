#!/bin/bash
if [ ! -d "target/main/c" ]; then
  mkdir -p target/main/c
fi

#avr-g++ src/main/led_fader_t13_code/test1.c -mmcu=atmega8 -Os -o target/main/led_fader_t13_code/fader.elf
#avr-objcopy -O ihex target/main/led_fader_t13_code/fader.elf target/main/led_fader_t13_code/fader.hex
#echo 'sudo avrdude -p atmega8 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:target/main/led_fader_t13_code/fader.hex:i'


avr-gcc src/main/c/Serial.c src/main/c/twi/twislave.c -mmcu=atmega8 -Os -o target/main/c/Serial.elf
avr-objcopy -O ihex target/main/c/Serial.elf target/main/c/Serial.hex
rm target/main/c/Serial.elf
echo 'sudo avrdude -p atmega8 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:target/main/c/Serial.hex:i'
avrdude -p atmega8 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:target/main/c/Serial.hex:i
