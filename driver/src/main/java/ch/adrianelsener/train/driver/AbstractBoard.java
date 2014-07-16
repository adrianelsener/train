package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.Pin;
import ch.adrianelsener.train.denkovi.PinState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractBoard {
    private final static Logger logger = LoggerFactory.getLogger(AbstractBoard.class);
    private static final long SLEEP_TIME = 10;
    private final Board board;

    protected AbstractBoard(final Board board) {
        this.board = board;
    }

    protected void togglePin(final Pin togglePin) {
        set(PinState.Pon(togglePin));
        set(PinState.Poff(togglePin));
    }

    protected void set(final PinState pinState) {
        board.set(pinState);
        sleep();
    }

    protected int get(final Pin pin) {
        logger.debug("read pin {} from board", pin);
        return board.read(pin);
    }

    protected void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (final InterruptedException e) {
            logger.error("Problem during sleep", e);
        }
    }

    protected Pin getPinForName(final String pinName) {
        final String upPinNr = getBoardCfg().getChild(pinName);
        logger.debug("Got {} for {} pin", upPinNr, pinName);
        final Pin upPIn = Pin.forPDotNotation(upPinNr);
        return upPIn;
    }

    protected abstract Config getBoardCfg();

}
