package ch.adrianelsener.train.gui.swing.common;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.awt.*;

public class PointMatchers {
    private PointMatchers() {
        super();
    }

    public static Matcher<Point> hasX(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<Point, Integer>(integerMatcher, "x", "x") {
            @Override
            protected Integer featureValueOf(Point point) {
                return point.x;
            }
        };
    }

    public static Matcher<Point> hasY(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<Point, Integer>(integerMatcher, "y", "y") {
            @Override
            protected Integer featureValueOf(Point point) {
                return point.y;
            }
        };
    }
}
