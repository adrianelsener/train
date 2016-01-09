package ch.adrianelsener.train.pi.twi;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;

public interface TwiAccessor {
    void write(AccelerationDto accelerationDto);

    Result read();
}
