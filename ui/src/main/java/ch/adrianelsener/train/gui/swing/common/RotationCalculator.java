package ch.adrianelsener.train.gui.swing.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.awt.*;

public class RotationCalculator {
    private final Point center;
    private final double sin;
    private final double cos;

    private int calcY(final double sin, final double cos, final double leftX, final double leftY) {
        return Double.valueOf((-(leftX - center.x) * sin + (leftY - center.y) * cos) + center.y).intValue();
    }

    private int calcX(final double sin, final double cos, final double leftX, final double leftY) {
        return Double.valueOf(((leftX - center.x) * cos + (leftY - center.y) * sin) + center.x).intValue();
    }

    public Point calc(Point original) {
        return new Point(calcX(sin, cos, original.x, original.y), calcY(sin, cos, original.x, original.y));
    }

    public RotationCalculator(final Point center, final double angle) {
        this.center = center;
        sin = Math.sin(Math.toRadians(angle));
        cos = Math.cos(Math.toRadians(angle));
    }

    public Point calc(int x, int y) {
        return calc(new Point(x, y));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
