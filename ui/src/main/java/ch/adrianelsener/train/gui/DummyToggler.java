package ch.adrianelsener.train.gui;

import ch.adrianelsener.train.driver.SwitchBoardDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.adrianelsener.train.driver.SwitchBoardV1;

public class DummyToggler implements SwitchCallback {
    private final static Logger logger = LoggerFactory.getLogger(DummyToggler.class);

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void toggleSwitch(final SwitchId switchId, final BoardId boardId, final boolean on) {
        logger.info("Toggle command for board {} switchId {} and state {}", boardId, switchId, on);
    }

    public static SwitchCallback create() {
        return new DummyToggler();
    }

    @Override
    public void addBoard(final BoardId fromValue, final SwitchBoardDriver weichenBoard) {
        logger.debug("Nothing to do with {}", weichenBoard);
    }

}