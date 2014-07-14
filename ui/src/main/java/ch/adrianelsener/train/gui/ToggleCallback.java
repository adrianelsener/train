package ch.adrianelsener.train.gui;

import ch.adrianelsener.train.driver.SwitchBoardDriver;

public interface ToggleCallback {

    void toggleSwitch(final SwitchId toggleId, final BoardId boardId, final boolean on);

    void addBoard(BoardId fromValue, SwitchBoardDriver weichenBoard);
}
