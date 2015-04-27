#!/bin/bash
cBaseSrcFolder=src/main/c/
cBaseTargetFolder=target/main/c/
if [ ! -d $cBaseTargetFolder ]; then
  mkdir -p $cBaseTargetFolder
fi

cTwiSlaveSrc=${cBaseSrcFolder}twi_sample/TWI_Slave/
cTwiSlaveFiles="${cTwiSlaveSrc}Delay.c ${cTwiSlaveSrc}TWI_Slave.c"
baseArtefact=twi-slave-from-sample

#echo $cTwiSlaveFiles

#avr-g++ src/main/led_fader_t13_code/test1.c -mmcu=atmega8 -Os -o target/main/led_fader_t13_code/fader.elf
#avr-objcopy -O ihex target/main/led_fader_t13_code/fader.elf target/main/led_fader_t13_code/fader.hex
#echo 'sudo avrdude -p atmega8 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:target/main/led_fader_t13_code/fader.hex:i'

#ls $cTwiSlaveSrc

avr-gcc $cBaseSrcFolder$baseArtefact.c $cTwiSlaveFiles -mmcu=atmega8 -Os -o $cBaseTargetFolder$baseArtefact.elf
avr-objcopy -O ihex $cBaseTargetFolder$baseArtefact.elf $cBaseTargetFolder$baseArtefact.hex
rm $cBaseTargetFolder$baseArtefact.elf
echo "sudo avrdude -p atmega8 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:$cBaseTargetFolder$baseArtefact.hex:i"
#avrdude -p atmega8 -e -c avr910 -P /dev/ttyUSB0 -U flash:w:${cBaseTargetFolder}${baseArtefact}.hex:i
