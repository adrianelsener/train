package ch.adrianelsener.train.pi.twi.accessor.cmd;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import ch.adrianelsener.train.pi.twi.accessor.TwiAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwiCmdAccessor implements TwiAccessor {
    private final static Logger logger = LoggerFactory.getLogger(TwiCmdAccessor.class);

    public TwiCmdAccessor() {
        super();
    }

    @Override
    public void write(final TwiDevice device, final AccelerationDto accelerationDto) {
        logger.debug("AccelerationDto is: {}", accelerationDto);
        setAcceleration(accelerationDto.getAcceleration().getAcceleration()).onDevice(device);
        setStepsize(accelerationDto.getAcceleration().getStepsize()).onDevice(device);
        setSpeed(accelerationDto.getSpeed().getSpeed()).onDevice(device);
        setDirection(accelerationDto.getDirection().getAvrDirectionValue()).onDevice(device);
    }

    private CmdSetter setDirection(final int avrDirectionValue) {
        return new DirectionSetter(avrDirectionValue);
    }

    private CmdSetter setSpeed(final int speed) {
        return new SpeedSetter(speed);
    }

    private CmdSetter setAcceleration(final int acceleration) {
        return new AccelerationSetter(acceleration);
    }

    private CmdSetter setStepsize(final int stepsize) {
        return new StepSizeSetter(stepsize);
    }

    @Override
    public Result read(final TwiDevice device) {
        int currentSpeed = readCurrentSpeed().fromDevice(device);
        return Result.ok(new AccelerationDto().setSpeed(currentSpeed));
    }

    private CmdReader<Integer> readCurrentSpeed() {
        return new CurrentSpeedReader();
    }

}
