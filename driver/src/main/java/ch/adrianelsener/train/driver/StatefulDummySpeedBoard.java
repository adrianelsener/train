package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.common.net.NetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of SpeedBoardDriver which returns last value set.
 * Step size for #faster #slower is 1
 */
public class StatefulDummySpeedBoard implements SpeedBoardDriver {
    private final static Logger logger = LoggerFactory.getLogger(StatefulDummySpeedBoard.class);
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
        logger.debug("Set speed to '{}'", estimated);
        speed = estimated;
    }

    @Override
    public int getCurrentSpeed() {
        return speed;
    }
}
