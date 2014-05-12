package ch.adrianelsener.train.driver;

public interface SpeedBoardDriver {
    public void faster();

    public void slower();

    public void setSpeed(int estimated);

    public int getCurrentSpeed();
}
