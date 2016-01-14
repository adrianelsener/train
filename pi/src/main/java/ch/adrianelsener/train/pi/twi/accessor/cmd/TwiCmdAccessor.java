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

    private static class StepSizeSetter extends CmdSetter {
        private final int stepsize;

        public StepSizeSetter(final int stepsize) {
            this.stepsize = stepsize;
        }

        @Override
        protected int dataPosition() {
            return 0x00;
        }

        @Override
        protected int dataToSet() {
            return stepsize;
        }
    }

    private static class AccelerationSetter extends CmdSetter {
        private final int acceleration;

        public AccelerationSetter(final int acceleration) {

            this.acceleration = acceleration;
        }

        @Override
        protected int dataPosition() {
            return 0x01;
        }

        @Override
        protected int dataToSet() {
            return acceleration;
        }
    }

    private static class SpeedSetter extends CmdSetter {
        private final int speed;

        public SpeedSetter(final int speed) {
            this.speed = speed;
        }

        @Override
        protected int dataPosition() {
            return 0x02;
        }

        @Override
        protected int dataToSet() {
            return speed;
        }
    }

    private static class DirectionSetter extends CmdSetter {
        private final int avrDirectionValue;

        public DirectionSetter(final int avrDirectionValue) {

            this.avrDirectionValue = avrDirectionValue;
        }

        @Override
        protected int dataPosition() {
            return 0x03;
        }

        @Override
        protected int dataToSet() {
            return avrDirectionValue;
        }
    }

    private static class CurrentSpeedReader extends CmdReader<Integer> {
        @Override
        protected Integer convert(final String line) {
            return Integer.valueOf(line);
        }

        @Override
        protected int getReadPosition() {
            return 0x00;
        }
    }
}
