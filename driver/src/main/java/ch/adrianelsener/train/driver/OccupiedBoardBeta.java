package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.denkovi.Board;

/**
 * Created by els on 7/11/14.
 */
public class OccupiedBoardBeta implements OccupiedBoardDriver {
    public OccupiedBoardBeta(Config cfg, Board board) {

    }

    @Override
    public int read(Occupied occupied) {
        return 0;
    }
}
