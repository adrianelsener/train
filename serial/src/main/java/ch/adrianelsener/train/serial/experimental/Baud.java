package ch.adrianelsener.train.serial.experimental;

/**
 * Created by els on 19.04.15.
 */
public class Baud {
    private final int baudrate;

    public Baud(int baudrate) {
        this.baudrate = baudrate;
    }

    public static Baud create(int baudrate) {
        return new Baud(baudrate);
    }
}
