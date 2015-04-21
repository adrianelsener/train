package ch.adrianelsener.train.serial.experimental;

/**
 * Created by els on 19.04.15.
 */
public class SerialReceiver {

    private Medium connection;
    private Baud baud;

    public SerialReceiver() {
    }

    public void connect(Medium connection, Baud baud) {
        this.connection = connection;
        this.baud = baud;
    }

    public byte getData() {
        return 0;
    }

}
