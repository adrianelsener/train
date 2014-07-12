package ch.adrianelsener.train.denkovi;

import ch.adrianelsener.train.denkovi.Pin;

public class PinState {
    final Pin pin;
    final State state;

    public PinState(Pin rel, State stat) {
        pin = rel;
        state = stat;
    }

    public static PinState Pon(Pin r) {
        return new PinState(r, State.On);
    }

    public static PinState Poff(Pin r) {
        return new PinState(r, State.Off);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pin == null) ? 0 : pin.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PinState other = (PinState) obj;
        if (pin != other.pin) {
            return false;
        }
        if (state != other.state) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PinState [pin=" + pin + ", state=" + state + "]";
    }

}