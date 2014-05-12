package ch.adrianelsener.train.gui.swing;

import java.awt.Point;

public interface Nearby {
    public boolean isNear(Point point);

    public Point getNextConnectionpoint(Point origin);
}
