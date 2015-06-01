package ch.adrianelsener.train.driver;

public interface SpeedBoardDriver {
    void faster();

    void slower();

    void setSpeed(int estimated);

    int getCurrentSpeed();
}
