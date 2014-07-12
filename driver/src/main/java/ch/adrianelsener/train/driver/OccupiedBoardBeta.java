package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;

public class OccupiedBoardBeta extends AbstractBoard implements OccupiedBoardDriver{
    private final Config boardCfg;

    public OccupiedBoardBeta(Config cfg, Board board) {
        super(board);
        final ConfKey boardKey = ConfKey.createForBoard("OB.00");
        boardCfg = cfg.getAll(boardKey);
    }

    @Override
    public int read(Occupied occupied) {
        return get(occupied.getPin());
    }

    @Override
    protected Config getBoardCfg() {
        return boardCfg;
    }
}
