package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public class TcpClient {
    private final SocketFactory socketFactory;
    private final GsonWrapper gson;
    @VisibleForTesting
    TcpClient(SocketFactory socketFactory, GsonWrapper gson) {
        this.socketFactory = socketFactory;
        this.gson = gson;
    }

    public TcpClient() {
        this(new SocketFactory(), new GsonWrapper());
    }

    private Result sendSetSpeed() {
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
        return gson.fromJson(inputStream, Result.class);
    }

    private void sendData(final Command cmd, final AutoClosableSocket socket) {
        DataOutputStream streamWriter = new DataOutputStream(socket.getOutputStream());
        String dtoAsJsonString = gson.toJson(cmd);
        PrintWriter writer = new PrintWriter(streamWriter);
        writer.println(dtoAsJsonString);
        writer.flush();
    }

}
