package ch.adrianelsener.train.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.Pin;
import ch.adrianelsener.train.denkovi.PinState;

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
