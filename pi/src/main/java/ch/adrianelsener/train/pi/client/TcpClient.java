package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by els on 7/10/15.
 */
public class TcpClient {
    private final SocketFactory socketFactory;
    @VisibleForTesting
    TcpClient(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public TcpClient() {
        this(new SocketFactory());
    }

    private Result sendSetSpeed() throws IOException {
        Gson gson = new Gson();
        Command cmd = Command.builder()//
                .setData(ImmutableMap.of(Mode.Key.ACCELERATION, gson.toJsonTree(new AccelerationDto())))//
                .setMode(Mode.SPEED)//
                .build();
        Result result = sendCommand(cmd);
        System.out.printf("%s\n", result);
        return result;
    }

    public Result sendCommand(final Command cmd) {
        AutoClosableSocket socket = new AutoClosableSocket("127.0.0.1", 2323);
        sendData(cmd, socket);
        Result receivedDto = receiveData(socket);
        socket.close();
        return receivedDto;
    }


    private class AutoClosableSocket implements AutoCloseable {
        private final Socket socket;

        public AutoClosableSocket(String address, int port) {
            try {
                final InetAddress inetAddress = InetAddress.getByName(address);
                socket = new Socket(inetAddress, port);
            } catch (IOException e) {
                throw new RuntimeIoException(e);
            }
        }

        @Override
        public void close() throws Exception {
            socket.close();
        }
    }

    private class RuntimeIoException extends RuntimeException {
        public RuntimeIoException(IOException e) {
            super(e);
        }
    }

    private Result receiveData(final Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
        String line = inputReader.readLine();
        return new Gson().fromJson(line, Result.class);
    }

    private void sendData(final Command cmd, final Socket socket) throws IOException {
        DataOutputStream streamWriter = new DataOutputStream(socket.getOutputStream());
        String dtoAsJsonString = new Gson().toJson(cmd);
        PrintWriter writer = new PrintWriter(streamWriter);
        writer.println(dtoAsJsonString);
        writer.flush();
    }
}
