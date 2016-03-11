package ch.adrianelsener.train.pi.twi.accessor.cmd;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.twi.TwiAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwiCmdAccessor implements TwiAccessor {
    private final static Logger logger = LoggerFactory.getLogger(TwiCmdAccessor.class);

    public TwiCmdAccessor() {
        super();
    }

    @Override
    public void write(final Device device, final AccelerationDto accelerationDto) {
        setStepsize(accelerationDto.getAcceleration().getStepsize()).onDevice(device);
        setAcceleration(accelerationDto.getAcceleration().getAcceleration()).onDevice(device);
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
    public Result read(final Device device) {
        int currentSpeed = readCurrentSpeed().fromDevice(device);
        return Result.ok(new AccelerationDto().setSpeed(currentSpeed));
    }

    private CmdReader<Integer> readCurrentSpeed() {
        return new CurrentSpeedReader();
    }

}
