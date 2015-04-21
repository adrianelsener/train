package ch.adrianelsener.train.serial.experimental;

public interface Medium {
    void put(boolean data);

    boolean get();
}
