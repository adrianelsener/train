package ch.adrianelsener.train.gui.swing.common;

import java.awt.*;

public class PointMover {
    private final Point original;

    private PointMover(Point point) {
        this.original = point;
    }

    public static PointMover use(Point startPoint) {
        return new PointMover(startPoint);
    }

    public Point moveTo(Point movement) {
        return new Point(original.x + movement.x, original.y + movement.y);
    }
}
