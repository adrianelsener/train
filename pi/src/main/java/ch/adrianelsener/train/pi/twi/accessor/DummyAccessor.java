package ch.adrianelsener.train.pi.twi.accessor;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;

public class DummyAccessor implements TwiAccessor {
    @Override
    public void write(TwiDevice device, AccelerationDto accelerationDto) {

    }

    @Override
    public Result read(TwiDevice device) {
        return Result.ok(null);
    }
}
