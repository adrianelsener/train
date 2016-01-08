package ch.adrianelsener.train.pi.dto.properties;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class DeviceTest {
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void create_below0_Exception() {
        new Device(-1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void create_above255_Exception() {
        new Device(256);
    }

    @Test
    public void create_minimum_containsMinimum() {
        // arrange & act
        final Device testee = new Device(0);
        // assert
        assertThat(testee, is(deviceNr(equalTo(0))));
    }

    private static Matcher<Device> deviceNr(final Matcher<Integer> matcher) {
        return new FeatureMatcher<Device, Integer>(matcher, "getDeviceNr", "getDeviceNr") {
            @Override
            protected Integer featureValueOf(final Device actual) {
                return actual.getDeviceNr();
            }
        };
    }
}