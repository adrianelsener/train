package ch.adrianelsener.train.driver;

import org.testng.annotations.Test;

import static ch.adrianelsener.train.driver.SpeedBoardDriverMatchers.currentSpeed;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class StatefulDummySpeedBoardTest {

    @Test
    public void inital_getCurrentIs0() {
        // arrange
        // act
        final StatefulDummySpeedBoard testee = new StatefulDummySpeedBoard(null);
        // assert
        assertThat(testee, is(currentSpeed(equalTo(0))));
    }

    @Test
    public void faster_getCurrentIsIncremented() {
        // arrange
        final StatefulDummySpeedBoard testee = new StatefulDummySpeedBoard(null);
        // act
        testee.faster();
        // assert
        assertThat(testee, is(currentSpeed(equalTo(1))));
    }

    @Test
    public void slower_getCurrent_isDecremented() {
        // arrange
        final StatefulDummySpeedBoard testee = new StatefulDummySpeedBoard(null);
        // act
        testee.slower();
        // assert
        assertThat(testee, is(currentSpeed(equalTo(-1))));
    }

    @Test
    public void setSpeed_getCurrent_isSameAsSet() throws Exception {
        // arrange
        final StatefulDummySpeedBoard testee = new StatefulDummySpeedBoard(null);
        // act
        testee.setSpeed(42);
        // assert
        assertThat(testee, is(currentSpeed(equalTo(42))));
    }
}