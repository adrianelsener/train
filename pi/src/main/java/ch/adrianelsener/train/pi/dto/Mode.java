package ch.adrianelsener.train.pi.dto;

import ch.adrianelsener.train.pi.twi.TwiAccessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Mode {
    SPEED {
        public Result apply(final AccelerationDto data) {
            AccelerationDto accel = data;
            log.debug("ACCEL: {}", accel);
            TwiAccessorFactory.TwiAccessor twiAccessor = new TwiAccessorFactory().open(15);
            twiAccessor.write(data);
            Result result = twiAccessor.read();
            log.debug("read {}", result);
            return result;
        }
    };
    private final static Logger log = LoggerFactory.getLogger(Mode.class);

    public abstract Result apply(final AccelerationDto data);

}
