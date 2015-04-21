package ch.adrianelsener.train.serial.experimental;

/**
 * Created by els on 19.04.15.
 */
public class SerialSender {
    private Medium medium;
    private Baud baud;

    public SerialSender() {

    }

    public void connect(Medium medium, Baud baud) {
        this.medium = medium;
        this.baud = baud;

    }

    public void send(byte data) {
        Sender sender = new Sender(medium, baud);
        sender.send(data);
        Thread senderThread = new Thread(sender);
        senderThread.start();
    }

    private static class Sender implements Runnable {
        private final Medium medium;
        private final Baud baud;
        boolean notStopped;

        public Sender(Medium medium, Baud baud) {
            this.medium = medium;
            this.baud = baud;
        }

        @Override
        public void run() {

            while (notStopped) {

            }
        }

        public void send(byte data) {

        }
    }
}
