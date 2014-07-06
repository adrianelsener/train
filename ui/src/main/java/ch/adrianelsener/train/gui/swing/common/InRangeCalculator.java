package ch.adrianelsener.train.gui.swing.common;

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

}
