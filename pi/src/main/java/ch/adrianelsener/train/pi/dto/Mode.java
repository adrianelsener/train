package ch.adrianelsener.train.pi.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public enum Mode {
    SPEED {
        public Result apply(final Map<Key, JsonElement> data) {
            final Gson gson = new Gson();
            AccelerationDto accel = gson.fromJson(data.get(Key.ACCELERATION), AccelerationDto.class);
            log.debug("ACCEL: %s\n", accel);
            return new Result(Result.Status.OK);
        }
    };
    private final static Logger log = LoggerFactory.getLogger(Mode.class);

    public enum Key {
        ACCELERATION,//
        ;
    }

    public abstract Result apply(final Map<Mode.Key, JsonElement> data);

}
