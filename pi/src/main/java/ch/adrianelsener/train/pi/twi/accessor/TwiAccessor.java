package ch.adrianelsener.train.pi.twi.accessor;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;

public interface TwiAccessor {
    void write(final TwiDevice device, final AccelerationDto accelerationDto);

    Result read(TwiDevice device);
}
