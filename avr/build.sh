#!/bin/bash
if [ ! -d "target/main/led_fader_t13_code" ]; then
  mkdir -p target/main/led_fader_t13_code
fi

avr-g++ src/main/led_fader_t13_code/test1.c -mmcu=attiny2313 -Os -o target/main/led_fader_t13_code/fader.elf
avr-objcopy -O ihex target/main/led_fader_t13_code/fader.elf target/main/led_fader_t13_code/fader.hex
echo 'sudo avrdude -p t2313 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:target/main/led_fader_t13_code/fader.hex:i'