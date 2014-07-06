package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.swing.model.TrackPart;

import java.awt.*;

public abstract class PointCalculator {
    private final int rasterSize;
    private final Odb<TrackPart> db;

    protected PointCalculator(final int rasterSize, Odb<TrackPart> db) {
        this.rasterSize = rasterSize;
        this.db = db;
    }

    public static class RasterEnabled extends PointCalculator{
        public RasterEnabled(int rasterSize,Odb<TrackPart> db) {
            super(rasterSize, db);
        }

        @Override
        public Point calculateRasterPoint(Point originalPoint) {
            return calculateRasterPointInternal(originalPoint, super.rasterSize);
        }
    }
    public static class RasterDisabled extends PointCalculator{
        public RasterDisabled(Odb<TrackPart> db) {
            super(0, db);
        }

        @Override
        public Point calculateRasterPoint(Point originalPoint) {
            return originalPoint;
        }
    }

    protected abstract Point calculateRasterPoint(final Point originalPoint);

    public Point calculatePoint(final Point originalPoint) {
        final Point rasterPoint = calculateRasterPoint(originalPoint);
        final Point nearTo = findNearestConnectionPoint(rasterPoint);
        return nearTo;
    }

    private Point findNearestConnectionPoint(Point originalPoint) {
        final Point endPoint = db.filterUnique(part -> part.isNear(originalPoint)).map(part -> part.getNextConnectionpoint(originalPoint)).orElse(originalPoint);
        return endPoint;
    }

    protected Point calculateRasterPointInternal(final Point originalPoint, final int rasterSize) {
        final Point mousePoint;
        final int x = originalPoint.x - originalPoint.x % rasterSize;
        final int y = originalPoint.y - originalPoint.y % rasterSize;
        mousePoint = new Point(x, y);
        return mousePoint;
    }
}
