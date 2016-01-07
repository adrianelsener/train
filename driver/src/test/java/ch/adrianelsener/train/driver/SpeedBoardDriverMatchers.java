package ch.adrianelsener.train.driver;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

class SpeedBoardDriverMatchers {
    public static Matcher<SpeedBoardDriver> currentSpeed(final Matcher<Integer> matcher) {
        return new FeatureMatcher<SpeedBoardDriver, Integer>(matcher, "getCurrentSpeed", "getCurrentSpeed") {
            @Override
            protected Integer featureValueOf(final SpeedBoardDriver actual) {
                return actual.getCurrentSpeed();
            }
        };
    }
}
