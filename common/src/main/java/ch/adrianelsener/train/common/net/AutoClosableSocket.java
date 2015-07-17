package ch.adrianelsener.train.common.net;

import ch.adrianelsener.train.common.io.RuntimeIoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class AutoClosableSocket implements AutoCloseable {
    private final Socket socket;

    public AutoClosableSocket(String address, int port) {
        try {
            final InetAddress inetAddress = InetAddress.getByName(address);
            socket = new Socket(inetAddress, port);
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    public AutoClosableSocket(final InetAddress address, final int port) {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    public AutoClosableSocket(NetAddress address) {
        this(address.getAddress(), address.getPort());
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public int getPort() {
        return socket.getPort();
    }
}
