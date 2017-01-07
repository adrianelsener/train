package ch.adrianelsener.train.gui.swing.model;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.awt.*;

class TrackMatchers {
    private TrackMatchers() {
        super();
    }


    public static Matcher<Track> hasStart(Matcher<Point> integerMatcher) {
        return new FeatureMatcher<Track, Point>(integerMatcher, "getStartpoint", "getStartpoint") {
            @Override
            protected Point featureValueOf(Track switchTrack) {
                return switchTrack.startPoint;
            }
        };
    }

    public static Matcher<Track> hasEnd(Matcher<Point> integerMatcher) {
        return new FeatureMatcher<Track, Point>(integerMatcher, "getEndpoint", "getEndpoint") {
            @Override
            protected Point featureValueOf(Track switchTrack) {
                return switchTrack.endPoint;
            }
        };
    }

}
