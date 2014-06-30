package ch.adrianelsener.train.gui.swing.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class Track extends AbstractTrackPart implements TrackPart {
    private static final Logger logger = LoggerFactory.getLogger(Track.class);
    protected final Point startPoint;
    protected final Point endPoint;

    protected Track(final Point startPoint, final Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public static Track createSimpleTrack(final Point startPoint, final Point endPoint) {
        return new SimpleTrack(startPoint, endPoint);
    }

    public static SwitchTrack createSwitchTrack(final Point point, final Point endPoint) {
        return new SwitchTrack(point, endPoint);
    }

    public static Track fromStringIterable(final Iterable<String> iter) {
        return fromIterable(iter);

    }

    private static Track fromIterable(final Iterable<String> split) {
        final Iterator<String> iterator = split.iterator();
        final String identifier = iterator.next();
        if ("T".equals(identifier)) {
            return SimpleTrack.createSimpleTrack(iterator);
        } else {
            return SwitchTrack.createSwitchTrack(iterator);
        }
    }

    public Point getStart() {
        return startPoint;
    }

    public Point getEnd() {
        return endPoint;
    }

    @Override
    public void paint(final Graphics2D g) {
        paintLable(g);
        paintPart(g);
    }

    private void paintPart(final Graphics2D g) {
        g.setColor(getLineColor());
        g.setStroke(new BasicStroke(2));
        g.drawLine(getStart().x, getStart().y, getEnd().x, getEnd().y);
    }

    protected abstract void paintLable(final Graphics2D g);

    protected abstract Color getLineColor();

    @Override
    public boolean isNear(final Point point) {

        final int smallestX = Math.min(startPoint.x, endPoint.x);
        final int largestX = Math.max(startPoint.x, endPoint.x);
        final int smallestY = Math.min(startPoint.y, endPoint.y);
        final int largestY = Math.max(startPoint.y, endPoint.y);

        logger.debug("Look if point {} is near to {}", point, this);
        if (point.x - 6 < largestX && point.x + 6 > smallestX) {
            logger.trace("Is in X-range");
            if (point.y - 6 < largestY && point.y + 6 > smallestY) {
                return true;
            }
            logger.trace("But not in Y");
        }
        logger.trace("Not in X-Range");

        return false;
    }

    @Override
    public Point getNextConnectionpoint(final Point origin) {
        if (isInRange(origin, startPoint, 10)) {
            return startPoint;
        } else if (isInRange(origin, endPoint, 10)) {
            return endPoint;
        } else {
            throw new IllegalArgumentException("Point is not near to something");
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public Track createMirror() {
        return this;
    }

    @Override
    public Collection<Object> getDataToPersist() {
        final List<Object> objects = Lists.newArrayList();
        objects.add(getCsvIdentifier());
        objects.add(startPoint.x);
        objects.add(startPoint.y);
        objects.add(endPoint.x);
        objects.add(endPoint.y);
        objects.addAll(individualStorageProperties());
        return objects;
    }

    protected abstract Collection<Object> individualStorageProperties();

    protected abstract String getCsvIdentifier();

    @Override
    public TrackPart invertView(final boolean inverted) {
        throw new NotImplementedException();
    }
}
