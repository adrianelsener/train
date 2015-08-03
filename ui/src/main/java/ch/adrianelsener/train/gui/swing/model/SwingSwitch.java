package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.swing.common.InRangeCalculator;
import ch.adrianelsener.train.gui.swing.common.RotationCalculator;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.awt.*;

public abstract class SwingSwitch implements TrackPart {
    protected final Point center;
    protected final double angle;
    protected final Point leftPoint;
    protected final Point topRightPoint;
    protected final Point bottomRightPoint;
    protected final InRangeCalculator inRangeCalc = InRangeCalculator.create();

    protected SwingSwitch(final Point point) {
        this(point, 0);
    }

    protected SwingSwitch(final Point center, final double angle) {
        this.center = center;
        this.angle = angle;
        new RotationCalculator(center, angle);
        final double sin = Math.sin(Math.toRadians(angle));
        final double cos = Math.cos(Math.toRadians(angle));

        final double leftX = center.x - 10;
        final double leftY = center.y;

        leftPoint = new Point(calcX(sin, cos, leftX, leftY), calcY(sin, cos, leftX, leftY));

        final double topRightX = center.x + 15;
        final double topRightY = center.y + 10;

        topRightPoint = new Point(calcX(sin, cos, topRightX, topRightY), calcY(sin, cos, topRightX, topRightY));

        final double bottomRightX = center.x + 15;
        final double bottomRightY = center.y - 10;

        bottomRightPoint = new Point(calcX(sin, cos, bottomRightX, bottomRightY), calcY(sin, cos, bottomRightX, bottomRightY));
    }

    @Override
    public void paint(final Graphics2D g) {
        drawLable(g);
        drawSwitch(g);
        drawDirection(g);
    }

    private void drawSwitch(final Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.blue);
        g.drawLine(leftPoint.x, leftPoint.y, center.x, center.y);
        g.drawLine(center.x, center.y, topRightPoint.x, topRightPoint.y);
        g.drawLine(center.x, center.y, bottomRightPoint.x, bottomRightPoint.y);
    }

    protected abstract void drawDirection(final Graphics2D g);

    protected abstract void drawLable(final Graphics2D g);


    private int calcY(final double sin, final double cos, final double leftX, final double leftY) {
        return Double.valueOf((-(leftX - center.x) * sin + (leftY - center.y) * cos) + center.y).intValue();
    }

    private int calcX(final double sin, final double cos, final double leftX, final double leftY) {
        return Double.valueOf(((leftX - center.x) * cos + (leftY - center.y) * sin) + center.x).intValue();
    }

    @Override
    public boolean isNear(final Point point) {
        return onXCoordinates(point) && onYCoordinates(point);
    }

    private boolean onYCoordinates(final Point point) {
        return point.y >= (center.y - 10) && point.y <= (center.y + 10);
    }

    private boolean onXCoordinates(final Point point) {
        return point.x >= (center.x - 10) && point.x <= (center.x + 15);
    }

    @Override
    public Point getNextConnectionpoint(final Point origin) {
        if (inRangeCalc.isInRange(origin, leftPoint, 10)) {
            return leftPoint;
        } else if (inRangeCalc.isInRange(origin, topRightPoint, 10)) {
            return topRightPoint;
        } else if (inRangeCalc.isInRange(origin, bottomRightPoint, 10)) {
            return bottomRightPoint;
        } else {
            throw new IllegalArgumentException("getNextConnectionpoint soll nur aufgerufen werden wenn auch etwas in der naehe ist");
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public ImmutableCollection<Point> getInConnectors() {
        return ImmutableList.of(leftPoint);
    }

    @Override
    public ImmutableCollection<Point> getOutConnectors() {
        return ImmutableList.of(topRightPoint, bottomRightPoint);
    }


    protected abstract SwingSwitch createNew(final Point center, final double angle);

    @Override
    public SwingSwitch rotate() {
        return createNew(center, angle + 45);
    }

    @Override
    public SwingSwitch moveTo(final Point newLocation) {
        return createNew(newLocation, angle);
    }

    @Override
    public SwingSwitch move(Point direction) {
        final Point newCenter = new Point(center.x + direction.x, center.y + direction.y);
        return createNew(newCenter, angle);
    }

    @Override
    public boolean isPipe() {
        return true;
    }

    enum SwitchMode {
        Real, Dummy
    }

}