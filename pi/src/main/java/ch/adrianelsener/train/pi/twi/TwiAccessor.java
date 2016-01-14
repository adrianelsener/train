package ch.adrianelsener.train.pi.twi;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;

public interface TwiAccessor {
    void write(final Device device, final AccelerationDto accelerationDto);

    Result read(Device device);
}
