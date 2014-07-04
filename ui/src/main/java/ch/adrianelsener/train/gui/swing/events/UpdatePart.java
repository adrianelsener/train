package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.ToggleCallback;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Optional;

public abstract class UpdatePart {

    private final Point point;

    protected UpdatePart(Point point) {
        this.point = point;
    }

    protected Point getPoint() {
        return point;
    }

    public abstract void doTransformation(Odb<TrackPart> db, PointCalculator pointCalc, Optional<Point> startPoint);

    public static UpdatePart createMirror(Point point) {
        return new UpdatePartMirror(point);
    }

    public static UpdatePart deletePart(Point point) {
        return new DeletePart(point);
    }

    public static UpdatePart movePart(Point point) {
        return new MovePart(point);
    }

    public static TogglePart createToggle(Point point) {
        return new TogglePart(point);
    }

    private static class UpdatePartMirror extends UpdatePart {
        public UpdatePartMirror(Point point) {
            super(point);
        }

        @Override
        public void doTransformation(Odb<TrackPart> db, PointCalculator pointCalc, Optional<Point> startPoint) {
            final Point rasterPoint = pointCalc.calculatePoint(getPoint());
            db.replace(part -> part.isNear(rasterPoint), TrackPart::createMirror);
        }
    }

    private static class DeletePart extends UpdatePart {
        private final static Logger logger = LoggerFactory.getLogger(DeletePart.class);

        public DeletePart(Point point) {
            super(point);
        }

        @Override
        public void doTransformation(Odb<TrackPart> db, PointCalculator pointCalc, Optional<Point> startPoint) {
            Point mousePoint = pointCalc.calculatePoint(getPoint());
            logger.debug("Delete part near to {}", mousePoint);
            db.delete(part -> part.isNear(mousePoint));
        }
    }

    public static class TogglePart {
        private final Point point;

        public TogglePart(Point point) {
            this.point = point;
        }

        public void toggle(ToggleCallback toggler, Odb<TrackPart> db) {
            db.replace(part -> part.isNear(point), part -> part.toggle(toggler));
        }

    }

    private static class MovePart extends UpdatePart {
        public MovePart(Point point) {
            super(point);
        }

        @Override
        public void doTransformation(Odb<TrackPart> db, PointCalculator pointCalc, Optional<Point> startPoint) {
            Point endPoint = pointCalc.calculatePoint(getPoint());
            db.replace(part -> part.isNear(startPoint.get()), part -> part.moveTo(endPoint));
        }
    }
}
