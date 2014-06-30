package ch.adrianelsener.train.gui.swing.events;

import java.awt.*;

public class UpdateMoveDraftPart  {
    private final Point destPoint;

    private UpdateMoveDraftPart(final Point destination) {
        destPoint = destination;
    }

    public static UpdateMoveDraftPart create(final Point destination) {
        return new UpdateMoveDraftPart(destination);
    }

    public Point getDestination() {
        return destPoint;
    }
}
