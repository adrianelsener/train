package ch.adrianelsener.train.pi.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by els on 7/10/15.
 */
class SocketFactory {
    /**
     * Creates a Socket or throws an IllegalArgumentException
     */
    public Socket create(InetAddress address, int port) {
        try {
            return new Socket(address, port);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
