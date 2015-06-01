package ch.adrianelsener.train.gui.swing.common;

import org.testng.annotations.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class InRangeCalculatorTest {
    private final InRangeCalculator testee = new InRangeCalculator();

    @Test
    public void samePointIsInRange() {
        final Point point  = new Point(1, 3);
        // Act
        final boolean result = testee.isInRange(point, point, 0);
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void falseIfXIsOutside() {
        final Point pointA = new Point(1, 3);
        final Point pointB = new Point(2, 3);
        // Act
        final boolean result = testee.isInRange(pointA, pointB, 0);
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void falseIfYIsOutside() {
        final Point pointA = new Point(1, 3);
        final Point pointB = new Point(1, 4);
        // Act
        final boolean result = testee.isInRange(pointA, pointB, 0);
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void falseIfXandYIsOutside() {
        final Point pointA = new Point(1, 3);
        final Point pointB = new Point(2, 4);
        // Act
        final boolean result = testee.isInRange(pointA, pointB, 0);
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void falseIfXandYIsInsideRange() {
        final Point pointA = new Point(1, 3);
        final Point pointB = new Point(8, 11);
        // Act
        final boolean result = testee.isInRange(pointA, pointB, 8);
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void rangeIsExcluded() {
        final Point pointA = new Point(1, 3);
        final Point pointB = new Point(8, 11);
        // Act
        final boolean result = testee.isInRange(pointA, pointB, 7);
        // Assert
        assertThat(result, is(false));
    }

}