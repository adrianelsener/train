package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.TrackPart;

import java.awt.*;
import java.util.Optional;

/**
 * Created by els on 7/1/14.
 */
public abstract class CreationAction {
    private final Point endPoint;

    protected CreationAction(Point endPoint) {
        this.endPoint = endPoint;
    }


    protected Point getEndPoint() {
        return endPoint;
    }
    public abstract TrackPart createDraftPart(final Optional<Point> point, PointCalculator pointCalculator);

}
