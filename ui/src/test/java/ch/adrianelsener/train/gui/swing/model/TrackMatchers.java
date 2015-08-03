package ch.adrianelsener.train.gui.swing.model;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.awt.*;

class TrackMatchers {
    private TrackMatchers() {
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

    public static Matcher<? super Track> hasStart(Matcher<Point> integerMatcher) {
        return new FeatureMatcher<Track, Point>(integerMatcher, "getStartpoint", "getStartpoint") {
            @Override
            protected Point featureValueOf(Track switchTrack) {
                return switchTrack.startPoint;
            }
        };
    }

    public static Matcher<? super Track> hasEnd(Matcher<Point> integerMatcher) {
        return new FeatureMatcher<Track, Point>(integerMatcher, "getEndpoint", "getEndpoint") {
            @Override
            protected Point featureValueOf(Track switchTrack) {
                return switchTrack.endPoint;
            }
        };
    }

}
