package ch.adrianelsener.train.gui.swing.model;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class SwitchMatchers {
    private SwitchMatchers() {
        super();
    }
    public static Matcher<? super SwingSwitch> hasY(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<SwingSwitch, Integer>(integerMatcher, "getY", "getY") {
            @Override
            protected Integer featureValueOf(SwingSwitch swingSwitch) {
                return swingSwitch.center.y;
            }
        };
    }

    public static Matcher<? super SwingSwitch> hasX(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<SwingSwitch, Integer>(integerMatcher, "getX", "getX") {
            @Override
            protected Integer featureValueOf(SwingSwitch trackPart) {
                return trackPart.center.x;
            }
        };
    }

}
