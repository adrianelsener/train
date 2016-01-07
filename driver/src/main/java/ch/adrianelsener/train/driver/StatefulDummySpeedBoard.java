package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.common.net.NetAddress;

/**
 * Implementation of SpeedBoardDriver which returns last value set.
 * Step size for #faster #slower is 1
 */
public class StatefulDummySpeedBoard implements SpeedBoardDriver {
    private int speed = 0;

    public StatefulDummySpeedBoard(NetAddress notUsed) {

    }

    @Override
    public void faster() {
        speed++;
    }

    @Override
    public void slower() {
        speed--;
    }

    @Override
    public void setSpeed(final int estimated) {
        speed = estimated;
    }

    @Override
    public int getCurrentSpeed() {
        return speed;
    }
}
