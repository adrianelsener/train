package ch.adrianelsener.train.gui.swing.common;

import org.hamcrest.core.CombinableMatcher;
import org.testng.annotations.Test;

import java.awt.*;

import static ch.adrianelsener.train.gui.swing.common.PointMatchers.hasX;
import static ch.adrianelsener.train.gui.swing.common.PointMatchers.hasY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.testng.Assert.*;

public class PointMoverTest {

    @Test
    public void move_pointWithX_pointIsMovedInX() {
        final Point start = new Point(0,0);
        final PointMover testee = PointMover.use(start);
        // act
        final Point result = testee.moveTo(new Point(3, 0));
        // assert
        assertThat(result, both(hasX(equalTo(3))).and(hasY(equalTo(0))));
    }

    @Test
    public void move_pointWithY_pointIsMovedInY() {
        final Point start = new Point(0,0);
        final PointMover testee = PointMover.use(start);
        // act
        final Point result = testee.moveTo(new Point(0, 6));
        // assert
        assertThat(result, both(hasX(equalTo(0))).and(hasY(equalTo(6))));
    }

    @Test
    public void move_pointWithXAndY_pointIsMovedInXAndY() {
        final Point start = new Point(7,9);
        final PointMover testee = PointMover.use(start);
        // act
        final Point result = testee.moveTo(new Point(-2, -3));
        // assert
        assertThat(result, both(hasX(equalTo(5))).and(hasY(equalTo(6))));
    }
}