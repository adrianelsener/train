package ch.adrianelsener.train.pi.dto;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccelerationDtoTest extends AbstractDtoTestCase {
    @Override
    protected AccelerationDto createTestee() {
        return new AccelerationDto();
    }

    @Test
    public void setSpeed_value10_getSpeedHas10() {
        final AccelerationDto testee = new AccelerationDto();
        // act
        final AccelerationDto result = testee.setSpeed(12);
        // assert
        assertThat(result, is(speed(equalTo(12))));
    }

    private static Matcher<AccelerationDto> speed(final Matcher<Integer> matcher) {
        return new FeatureMatcher<AccelerationDto, Integer>(matcher, "getSpeed", "getSpeed") {
            @Override
            protected Integer featureValueOf(final AccelerationDto actual) {
                return actual.getSpeed().getSpeed();
            }
        };
    }
}
