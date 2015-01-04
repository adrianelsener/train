package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;

public class SwitchBoardV2 extends AbstractBoard implements SwitchBoardDriver {
    public SwitchBoardV2(final Board board, final Config config, final int boardNummer) {
        super(board);
    }

    @Override
    protected Config getBoardCfg() {
        return null;
    }

    @Override
    public void turn(SwitchWithState statedRelay) {

    }

    @Override
    public boolean isRead() {
        return true;
    }
}
