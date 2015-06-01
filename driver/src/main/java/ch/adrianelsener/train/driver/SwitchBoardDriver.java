package ch.adrianelsener.train.driver;

public interface SwitchBoardDriver {

    void turn(SwitchWithState statedRelay);

    /**
     * Returns true if board is ready to use;
     */
    boolean isRead();
}