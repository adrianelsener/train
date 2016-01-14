package ch.adrianelsener.train.pi.twi.accessor;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.Speed;
import ch.adrianelsener.train.pi.twi.TwiAccessException;
import ch.adrianelsener.train.pi.twi.TwiAccessor;
import ch.adrianelsener.train.pi.twi.TwiInitializationException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.function.Function;

public class TwiPi4JAccessor implements TwiAccessor {

    public TwiPi4JAccessor() {
        super();
    }

    private I2CBus getI2CBus() {
        try {
            return I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (IOException e) {
            throw new TwiInitializationException(e);
        }
    }

    private static I2CDevice toI2CDevice(final Integer i, final I2CBus i2CBus) {
        try {
            return i2CBus.getDevice(i);
        } catch (IOException e) {
            throw new TwiInitializationException(new IllegalArgumentException(e));
        }
    }

    @Override
    public void write(final Device device, final AccelerationDto accelerationDto) {
        byte[] sendbytes = accelerationDtoToBytearray().apply(accelerationDto);
        final I2CDevice i2CDevice = getI2CDevice(device);
        try {
            i2CDevice.write(sendbytes, 0, sendbytes.length);
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
//            (wait,steps,destination,direction[1,2])
//            Integer.valueOf(s).byteValue()
    }

    private I2CDevice getI2CDevice(final Device device) {
        final I2CBus i2CBus = getI2CBus();
        return toI2CDevice(device.getDeviceNr(), i2CBus);
    }

    private static Function<AccelerationDto, byte[]> accelerationDtoToBytearray() {
        return (dto -> new byte[]{
                intToByte().apply(dto.getAcceleration().getStepsize()),//
                intToByte().apply(dto.getAcceleration().getAcceleration()),//
                intToByte().apply(dto.getSpeed().getSpeed()),//
                intToByte().apply(dto.getDirection().getAvrDirectionValue()),//
        });
    }

    private static Function<Integer, Byte> intToByte() {
        return Integer::byteValue;
    }

    @Override
    public Result read(final Device device) {
        int bytesToRead = 1;
        return readFromTwi(device, bytesToRead, byteArrayToResult());
    }

    private <RESULT> RESULT readFromTwi(final Device device, final int bytesToRead, final Function<byte[], RESULT> toResult) {
        byte[] bytes = new byte[bytesToRead];
        try {
            final I2CDevice i2CDevice = getI2CDevice(device);
            int bytesRead = i2CDevice.read(bytes, 0, bytes.length);
            if (bytes.length != bytesRead) {
                throw new TwiAccessException("Expected lenght differs from read lenght:\n Expected: '{}' but received '{}'", bytesToRead, bytesRead);
            }
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
        return toResult.apply(bytes);
    }

    private Function<byte[], Result> byteArrayToResult() {
        return (bytes -> {
            AccelerationDto accelerationDto = new AccelerationDto()//
//                        .setAcceleration(Byte.toUnsignedInt(bytes[3]), Byte.toUnsignedInt(bytes[2]))//
//                        .setDirection(Direction.fromTwiValue(Byte.toUnsignedInt(bytes[4])))//
                    .setSpeed(new Speed(Byte.toUnsignedInt(bytes[0])));
            return Result.ok(accelerationDto);
                    /*
                      TWIS_Write(OCR1A);
                    TWIS_Write(data.destOcr);
                    TWIS_Write(data.changeSpeed);
                    TWIS_Write(data.waits);
                    TWIS_Write(data.direction);
    */
        });
    }
}
