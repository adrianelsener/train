package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.Pin;
import ch.adrianelsener.train.denkovi.PinState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SwitchBoardV2 extends AbstractBoard implements SwitchBoardDriver {
    private static final Logger logger = LoggerFactory.getLogger(SwitchBoardV2.class);
    private Config boardProps;


    public SwitchBoardV2(final Board board, final Config config, final int boardNumber) {
        super(board, config, boardNumber);
    }

    @Override
    protected String getBoardPrefix() {
        return "RB";
    }

    @Override
    public void turn(SwitchWithState statedRelay) {
        setStatePins(statedRelay);
        final String toggleNumber = getBoardCfg().getChild("T");
        final Pin togglePin = Pin.forPDotNotation(toggleNumber);
        togglePin(togglePin);
    }

    @Override
    public boolean isRead() {
        return true;
    }

    private void setStatePins(final SwitchWithState statedRelay) {
        setBoardSelectionPins(statedRelay);
        setStateSelectionPins(statedRelay);
    }

    private void setStateSelectionPins(SwitchWithState statedRelay) {
        for (int i = 0; i < 4; i++) {
            setPin(statedRelay, "S", i);
        }
    }

    private void setBoardSelectionPins(SwitchWithState statedRelay) {
        for (int i = 0; i < 4; i++) {
            setPin(statedRelay, "B", i);
        }
    }

    private void setPin(final SwitchWithState statedRelay, final String prefix, final int i) {
        final PinState pinState = getPinStateForId(statedRelay, prefix, i);
        logger.debug("Set {}", pinState);
        set(pinState);
    }

    private PinState getPinStateForId(final SwitchWithState statedRelay, final String prefix, final int i) {
        final String mappedPin = getBoardCfg().getChild(prefix + (i + 1)).substring(2);
        final Pin pin1 = Pin.forNumber(mappedPin);
        return statedRelay.getStates().get(i).forPin(pin1);
    }
}
