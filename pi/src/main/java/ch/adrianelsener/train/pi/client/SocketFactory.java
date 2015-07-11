package ch.adrianelsener.train.pi.client;

import java.net.InetAddress;

class SocketFactory {
    public AutoClosableSocket create(InetAddress address, int port) {
        return new AutoClosableSocket(address, port);
    }

    public AutoClosableSocket create(String address, int port) {
        return new AutoClosableSocket(address, port);
    }
}
