package ch.adrianelsener.train.gui.swing.common;

import org.testng.annotations.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RotationCalculatorTest {

    @Test
    public void rotate90degree() {
        final Point center = new Point(0, 0);
        final Point original = new Point(0, 10);
        final RotationCalculator testee = new RotationCalculator(center, 90);
        // act
        final Point result = testee.calc(original);
        // assert
        assertThat(result, is(equalTo(new Point(10, 0))));
    }

    @Test
    public void rotate90degreeWithComponents() {
        final Point center= new Point(0, 0);
        final RotationCalculator testee = new RotationCalculator(center, 90);
        // act
        final Point result = testee.calc(10, 0);
        // assert
        assertThat(result, is(equalTo(new Point(0, -10))));
    }
}