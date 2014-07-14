package ch.adrianelsener.train.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.adrianelsener.train.driver.SwitchBoardV1;

public class DummyToggler implements SwitchCallback {
    private final static Logger logger = LoggerFactory.getLogger(DummyToggler.class);

    @Override
    public void toggleSwitch(final SwitchId switchId, final BoardId boardId, final boolean on) {
        logger.info("Toggle command for board {} switchId {} and state {}", boardId, switchId, on);
    }

    public static SwitchCallback create() {
        return new DummyToggler();
    }

    @Override
    public void addBoard(final BoardId fromValue, final SwitchBoardV1 weichenBoard) {
        logger.debug("Nothing to do with {}", weichenBoard);
    }

}