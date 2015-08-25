package ch.adrianelsener.train.gui.swing.events;

public class RasterEnabledEvent {
    private final boolean enbaled;

    public RasterEnabledEvent(final boolean enbaled) {
        this.enbaled = enbaled;
    }

    public static RasterEnabledEvent create(final boolean enbaled) {
        return new RasterEnabledEvent(enbaled);
    }

    public boolean isEnbaled() {
        return enbaled;
    }
}
