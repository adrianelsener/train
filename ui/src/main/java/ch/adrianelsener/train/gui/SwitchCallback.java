package ch.adrianelsener.train.gui;

import ch.adrianelsener.train.driver.SwitchBoardDriver;

public interface SwitchCallback {
    boolean isReady();

    void toggleSwitch(final SwitchId toggleId, final BoardId boardId, final boolean on);

    void addBoard(BoardId fromValue, SwitchBoardDriver weichenBoard);
}
