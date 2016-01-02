package ch.adrianelsener.train.pi.dto.properties;

import org.testng.annotations.Test;

public class SpeedTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void construct_belowMin_exception() {
        new Speed(-1);
    }

    @Test
    public void construct_minimum_ok() {
        new Speed(0);
    }

    @Test
    public void construct_maximum_ok() {
        new Speed(250);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void construct_overMax_exception() {
        new Speed(251);
    }
}