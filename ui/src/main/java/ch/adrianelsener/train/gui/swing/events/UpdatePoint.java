package ch.adrianelsener.train.gui.swing.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.awt.*;

public abstract class UpdatePoint {
    private final Point point;

    UpdatePoint(final Point point) {
        this.point = point;
    }
    public Point getPoint(){
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static CreationStartPoint createCreationStartPoint(final Point point) {
        return new CreationStartPoint(point);
    }

    public static DetailCoordinatesPoint createDetailCoordinates(Point point) {
        return new DetailCoordinatesPoint(point);
    }

    public static class CreationStartPoint extends UpdatePoint {
        private CreationStartPoint(Point point) {
            super(point);
        }
    }

    public static class DetailCoordinatesPoint extends UpdatePoint{
        DetailCoordinatesPoint(Point point) {
            super(point);
        }
    }
}
