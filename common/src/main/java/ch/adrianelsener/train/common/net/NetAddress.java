package ch.adrianelsener.train.common.net;

import ch.adrianelsener.train.common.io.RuntimeIoException;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Representation of an network address with port
 */
public class NetAddress {
    private final InetAddress address;
    private final int port;


    public static NetAddress create(InetAddress address, int port) {
        return new NetAddress(address, port);
    }

    public static NetAddress create(String address, int port) {
        try {
            return new NetAddress(InetAddress.getByName(address), port);
        } catch (UnknownHostException e) {
            throw new RuntimeIoException(e);
        }
    }

    private NetAddress(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
