package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;

public class SwitchBoardV2 extends AbstractBoard implements SwitchBoardDriver {
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

    }

    @Override
    public boolean isRead() {
        return true;
    }

}
