package ch.adrianelsener.train.gui.swing.model;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class SwitchMatchers {
    private SwitchMatchers() {
        super();
    }
    public static Matcher<BaseSwingSwitch> hasY(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<BaseSwingSwitch, Integer>(integerMatcher, "getY", "getY") {
            @Override
            protected Integer featureValueOf(BaseSwingSwitch swingSwitch) {
                return swingSwitch.center.y;
            }
        };
    }

    public static Matcher<BaseSwingSwitch> hasX(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<BaseSwingSwitch, Integer>(integerMatcher, "getX", "getX") {
            @Override
            protected Integer featureValueOf(BaseSwingSwitch trackPart) {
                return trackPart.center.x;
            }
        };
    }

}
