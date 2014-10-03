package ch.adrianelsener.train.gui;

import java.util.Map;

import ch.adrianelsener.train.driver.SwitchBoardDriver;
import ch.adrianelsener.train.driver.SwitchWithState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class SwitchBoardToggler implements SwitchCallback {
    private final static Logger logger = LoggerFactory.getLogger(SwitchBoardToggler.class);
    private final Map<BoardId, SwitchBoardDriver> boards = Maps.newHashMap();

    private SwitchBoardToggler() {
        super();
    }

    @Override
    public boolean isReady() {
        return boards.values().stream().allMatch(board -> board.isRead());
    }

    @Override
    public void toggleSwitch(final SwitchId switchId, final BoardId boardId, final boolean isOn) {
        logger.debug("Send signal switch {} , board {}", switchId, boardId);
        final SwitchBoardDriver boardDriver = boards.get(boardId);
        logger.debug("got board driver {}", boardDriver);
        final SwitchWithState weicheMitState = switchId.mapToWeicheMitState(isOn);
        logger.debug("found weicheMitState {}", weicheMitState);
        boardDriver.turn(weicheMitState);
    }

    public static SwitchCallback create() {
        return new SwitchBoardToggler();
    }

    @Override
    public void addBoard(final BoardId boardId, final SwitchBoardDriver board) {
        boards.put(boardId, board);
    }
}