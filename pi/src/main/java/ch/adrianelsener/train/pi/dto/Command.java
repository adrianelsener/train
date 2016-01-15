package ch.adrianelsener.train.pi.dto;

import ch.adrianelsener.train.pi.dto.properties.Device;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Command {
    private final Mode mode;
    private final AccelerationDto data;
    private final Device device;

    private Command(Builder builder) {
        mode = builder.mode;
        data = builder.data;
        device = builder.device;
    }

    public Result execute() {
        return mode.apply(data, device);
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
        public AccelerationDto data;
        public Device device;

        private Builder() {
            super();
        }

        public Builder setData(AccelerationDto data) {
            this.data = data;
            return this;
        }

        public Builder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder setDevice(Device device) {
            this.device = device;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }

}
