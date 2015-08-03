package ch.adrianelsener.train.denkovi;

public interface Board {

    void close();

    void set(PinState pinState);

    int read(Pin pin);

    /**
     * If the board is ready for operation it will be return true
     **/
    boolean isReady();
}