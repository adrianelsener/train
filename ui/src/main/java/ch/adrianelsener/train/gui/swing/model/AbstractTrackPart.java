package ch.adrianelsener.train.gui.swing.model;

import java.awt.Point;

public abstract class AbstractTrackPart implements TrackPart {
    protected boolean isInRange(final Point origin, final Point defining, final int range) {
        return (Math.abs(origin.x - defining.x) <= range && Math.abs(origin.y - defining.y) <= 10);
    }

}
