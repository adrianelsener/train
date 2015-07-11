package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TcpClient {
    private final SocketFactory socketFactory;
    @VisibleForTesting
    TcpClient(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public TcpClient() {
        this(new SocketFactory());
    }

    private Result sendSetSpeed() {
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
        try (AutoClosableSocket socket = socketFactory.create("127.0.0.1", 2323)) {
            sendData(cmd, socket);
            Result receivedDto = receiveData(socket);
            return receivedDto;
        }
    }

    private Result receiveData(final AutoClosableSocket socket) {
        InputStream inputStream = socket.getInputStream();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
        String line = null;
        try {
            line = inputReader.readLine();
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
        return new Gson().fromJson(line, Result.class);
    }

    private void sendData(final Command cmd, final AutoClosableSocket socket) {
        DataOutputStream streamWriter = new DataOutputStream(socket.getOutputStream());
        String dtoAsJsonString = new Gson().toJson(cmd);
        PrintWriter writer = new PrintWriter(streamWriter);
        writer.println(dtoAsJsonString);
        writer.flush();
    }

}
