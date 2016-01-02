package ch.adrianelsener.train.pi.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Mode {
    SPEED {
        public Result apply(final AccelerationDto data) {
            AccelerationDto accel = data;
            log.debug("ACCEL: %s\n", accel);
            return new Result(Result.Status.OK);
        }
    };
    private final static Logger log = LoggerFactory.getLogger(Mode.class);

    public abstract Result apply(final AccelerationDto data);

}
