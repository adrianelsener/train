package ch.adrianelsener.train.pi.dto.properties;

import org.testng.annotations.Test;

public class AccelerationTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void construct_belowMin_exception() {
        new Acceleration(-1, 0);
    }

    @Test
    public void construct_minimum_ok() {
        new Acceleration(0, 0);
    }

    @Test
    public void construct_maximum_ok() {
        new Acceleration(250, 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void construct_overMax_exception() {
        new Acceleration(251, 0);
    }
}