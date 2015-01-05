package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;

public class OccupiedBoardBeta extends AbstractBoard implements OccupiedBoardDriver {

    public OccupiedBoardBeta(Config cfg, Board board) {
        super(board, cfg, 0);
    }

    @Override
    protected String getBoardPrefix() {
        return "OB";
    }

    @Override
    public int read(Occupied occupied) {
        return get(occupied.getPin());
    }

}
