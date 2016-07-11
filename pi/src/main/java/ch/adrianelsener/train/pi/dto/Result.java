package ch.adrianelsener.train.pi.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Optional;

public abstract class Result extends AbstractDto {

    public static Result ok(AccelerationDto acceleration) {
        return new OkResult(acceleration);
    }

    public static Result nok() {
        return new NokResult();
    }

    public abstract Optional<AccelerationDto> getResult();

    public abstract boolean isOk();

    private static class NokResult extends Result {
        NokResult() {
            super();
        }

        @Override
        public Optional<AccelerationDto> getResult() {
            return Optional.empty();
        }

        @Override
        public boolean isOk() {
            return false;
        }
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class OkResult extends Result {
        private final AccelerationDto acceleration;

        OkResult(AccelerationDto acceleration) {
            this.acceleration = acceleration;
        }

        @Override
        public Optional<AccelerationDto> getResult() {
            return Optional.of(acceleration);
        }

        @Override
        public boolean isOk() {
            return true;
        }
    }
}
