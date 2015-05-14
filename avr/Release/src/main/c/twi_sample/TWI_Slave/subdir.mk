################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/main/c/twi_sample/TWI_Slave/Delay.c \
../src/main/c/twi_sample/TWI_Slave/TWI_Slave.c 

OBJS += \
./src/main/c/twi_sample/TWI_Slave/Delay.o \
./src/main/c/twi_sample/TWI_Slave/TWI_Slave.o 

C_DEPS += \
./src/main/c/twi_sample/TWI_Slave/Delay.d \
./src/main/c/twi_sample/TWI_Slave/TWI_Slave.d 


# Each subdirectory must supply rules for building sources it contributes
src/main/c/twi_sample/TWI_Slave/%.o: ../src/main/c/twi_sample/TWI_Slave/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: AVR Compiler'
	avr-gcc -Wall -Os -fpack-struct -fshort-enums -ffunction-sections -fdata-sections -std=gnu99 -funsigned-char -funsigned-bitfields -mmcu=atmega8 -DF_CPU=8000000UL -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


