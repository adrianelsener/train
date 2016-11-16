package ch.adrianelsener.train.pi.dto;

import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import ch.adrianelsener.train.pi.twi.accessor.TwiAccessor;
import ch.adrianelsener.train.pi.twi.accessor.TwiAccessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Mode {
    SET_SPEED {
        public Result apply(final AccelerationDto accel, final TwiDevice device) {
            log.debug("ACCEL: {}", accel);
            TwiAccessor twiAccessor = new TwiAccessorFactory().open(device);
            twiAccessor.write(device, accel);
            Result result = Result.ok();
            log.debug("read {}", result);
            return result;
        }
    },//
    READ_SPEED {
        @Override
        public Result apply(final AccelerationDto data, final TwiDevice device) {
            TwiAccessor twiAccessor = new TwiAccessorFactory().open(device);
            Result result = twiAccessor.read(device);
            log.debug("read {}", result);
            return result;
        }
    };
    private final static Logger log = LoggerFactory.getLogger(Mode.class);

    public abstract Result apply(final AccelerationDto data, final TwiDevice device);
}
