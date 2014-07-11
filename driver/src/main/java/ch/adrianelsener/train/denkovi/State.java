package ch.adrianelsener.train.denkovi;

public enum State {
    On {
        @Override
        public String getValue(final Pin relays, final int oldState) {
            return relays.getOnValue(oldState);
        }
    },
    Off {
        @Override
        public String getValue(final Pin relays, final int oldState) {
            return relays.getOffValue(oldState);
        }
    };

    public abstract String getValue(Pin relays, int oldState);
}
