package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.Pin;
import ch.adrianelsener.train.denkovi.PinState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedBoardV1 extends AbstractBoard implements SpeedBoardDriver {
    private final static Logger logger = LoggerFactory.getLogger(SpeedBoardV1.class);
    private final Config boardCfg;

    public SpeedBoardV1(final Config cfg, final Board board) {
        super(board, cfg, 0);
        final ConfKey boardKey = ConfKey.createForBoard("SB.00");
        boardCfg = cfg.getAll(boardKey);
    }

    @Override
    protected String getBoardPrefix() {
        return "SB";
    }

    @Override
    public void faster() {
        logger.debug("Go faster");
        final Pin stepPin = getSpeedStepPin();
        final Pin upPin = getUpPin();
        final Pin downPin = getDownPin();
        set(PinState.Pon(upPin));
        set(PinState.Poff(downPin));
        togglePin(stepPin);
    }

    private Pin getUpPin() {
        return getPinForName("Up");
    }

    @Override
    public void slower() {
        logger.debug("Go slower");
        final Pin stepPin = getSpeedStepPin();
        final Pin upPin = getUpPin();
        final Pin downPin = getDownPin();
        set(PinState.Poff(upPin));
        set(PinState.Pon(downPin));
        togglePin(stepPin);
    }

    private Pin getDownPin() {
        return getPinForName("Down");
    }

    private Pin getSpeedStepPin() {
        return getPinForName("SpeedStep");
    }

    @Override
    public void setSpeed(final int estimated) {
        // TODO Auto-generated method stub
    }

    @Override
    public int getCurrentSpeed() {
        return 0;
    }

    @Override
    protected Config getBoardCfg() {
        return boardCfg;
    }

}
