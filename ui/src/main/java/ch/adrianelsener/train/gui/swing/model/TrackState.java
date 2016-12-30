package ch.adrianelsener.train.gui.swing.model;

enum TrackState {
    On {
        @Override
        public TrackState other() {
            return Off;
        }
    }, Off {
        @Override
        public TrackState other() {
            return On;
        }
    };

    public abstract TrackState other();

    public boolean isOn() {
        return On == this;
    }
}
