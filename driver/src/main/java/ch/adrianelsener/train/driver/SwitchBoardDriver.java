package ch.adrianelsener.train.driver;

public interface SwitchBoardDriver {

    public void turn(SwitchWithState statedRelay);

    /**
     * Returns true if board is ready to use;
     * @return
     */
    boolean isRead();
}