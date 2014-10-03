package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.Pin;
import ch.adrianelsener.train.denkovi.PinState;

public class SwitchBoardV1 extends AbstractBoard implements SwitchBoardDriver {
    private final Config boardProps;

    public SwitchBoardV1(final Board board, final Config config, final int boardNummer) {
        super(board);
        String rbPrefix = "RB.";
        if (boardNummer < 10) {
            rbPrefix += "0";
        }
        final ConfKey boardKey = ConfKey.createForBoard(rbPrefix + boardNummer);
        boardProps = config.getAll(boardKey);
    }

    @Override
    public void turn(final SwitchWithState statedRelay) {
        setStatePins(statedRelay);
        final String toggleNumber = boardProps.getChild("T");
        final Pin togglePin = Pin.forPDotNotation(toggleNumber);
        togglePin(togglePin);
    }

    @Override
    public boolean isRead() {
        return true;
    }

    private void setStatePins(final SwitchWithState statedRelay) {
        for (int i = 0; i < 4; i++) {
            setStatePin(statedRelay, i);
        }
    }

    private void setStatePin(final SwitchWithState statedRelay, final int i) {
        final PinState pinState = getPinStateForId(statedRelay, i);
        set(pinState);
    }

    private PinState getPinStateForId(final SwitchWithState statedRelay, final int i) {
        final String mappedPin = boardProps.getChild("0" + (i + 1)).substring(2);
        final Pin pin1 = Pin.forNumber(mappedPin);
        return statedRelay.getStates().get(i).forPin(pin1);
    }

    @Override
    protected Config getBoardCfg() {
        return boardProps;
    }
}
