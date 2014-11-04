package ch.adrianelsener.train.gui.swing.events;

import java.awt.*;

public class MousePositionEvent {
    private final Point point;

    private MousePositionEvent(Point point) {
        this.point = point;
    }

    public Point getPosition() {
        return point;
    }

    public static MousePositionEvent create(Point point) {
        return new MousePositionEvent(point);
    }
}
