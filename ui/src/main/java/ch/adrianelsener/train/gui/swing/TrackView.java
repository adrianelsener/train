package ch.adrianelsener.train.gui.swing;

enum TrackView {
    Default {
        @Override
        public TrackView other() {
            return Inverted;
        }
    }, Inverted {
        @Override
        public TrackView other() {
            return Default;
        }
    };

    public abstract TrackView other();

}
