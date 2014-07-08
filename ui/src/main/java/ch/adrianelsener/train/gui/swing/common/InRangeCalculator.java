package ch.adrianelsener.train.gui.swing.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.awt.*;

public class InRangeCalculator {
    public boolean isInRange(final Point origin, final Point defining, final int range) {
        if (Math.abs(origin.x - defining.x) <= range) {
            if (Math.abs(origin.y - defining.y) <= range) {
                return true;
            }
        }
        return false;
    }

    public static InRangeCalculator create() {
        return new InRangeCalculator();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
