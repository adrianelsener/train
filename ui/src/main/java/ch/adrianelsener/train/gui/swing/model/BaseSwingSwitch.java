package ch.adrianelsener.train.gui.swing.model;

import java.awt.*;

public abstract class BaseSwingSwitch implements TrackPart {
    protected final Point center;
    protected final double angle;

    protected BaseSwingSwitch(Point center, double angle) {
        this.center = center;
        this.angle = angle;
    }
}