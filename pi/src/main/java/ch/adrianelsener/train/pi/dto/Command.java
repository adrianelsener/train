package ch.adrianelsener.train.pi.dto;

import com.google.gson.JsonElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

public class Command {
    private final Mode mode;
    private final Map<Mode.Key, JsonElement> data;

    private Command(Builder builder) {
        mode = builder.mode;
        data = builder.data;
    }

    public Result execute() {
        return mode.apply(data);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Mode mode;
        public Map<Mode.Key, JsonElement> data;

        private Builder() {
            super();
        }

        public Builder setData(Map<Mode.Key, JsonElement> data) {
            this.data = data;
            return this;
        }

        public Builder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }

}
