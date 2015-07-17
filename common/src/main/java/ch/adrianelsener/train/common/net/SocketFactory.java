package ch.adrianelsener.train.common.net;

import java.net.InetAddress;

public class SocketFactory {
    public AutoClosableSocket create(InetAddress address, int port) {
        return new AutoClosableSocket(address, port);
    }

    public AutoClosableSocket create(String address, int port) {
        return new AutoClosableSocket(address, port);
    }

    public AutoClosableSocket create(NetAddress address) {
        return new AutoClosableSocket(address);
    }
}
