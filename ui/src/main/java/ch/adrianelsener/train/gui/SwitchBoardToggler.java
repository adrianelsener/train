package ch.adrianelsener.train.gui;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.adrianelsener.train.driver.SwitchBoardV1;

import com.google.common.collect.Maps;

public class SwitchBoardToggler implements ToggleCallback {
    private final static Logger logger = LoggerFactory.getLogger(SwitchBoardToggler.class);
    private final Map<BoardId, SwitchBoardV1> boards = Maps.newHashMap();

    private SwitchBoardToggler() {
        super();
    }

    @Override
    public void toggleSwitch(final SwitchId switchId, final BoardId boardId, final boolean isOn) {
        logger.debug("Send signal switch {} , board {}", switchId, boardId);
        boards.get(boardId).turn(switchId.mapToWeicheMitState(isOn));
    }

    public static ToggleCallback create() {
        return new SwitchBoardToggler();
    }

    @Override
    public void addBoard(final BoardId boardId, final SwitchBoardV1 board) {
        boards.put(boardId, board);
    }
}