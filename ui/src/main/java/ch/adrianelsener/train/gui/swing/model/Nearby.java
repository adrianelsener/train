package ch.adrianelsener.train.gui.swing.model;

import java.awt.Point;

public interface Nearby {
    boolean isNear(Point point);

    Point getNextConnectionpoint(Point origin);
}
