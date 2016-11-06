package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.common.net.AutoClosableSocket;
import ch.adrianelsener.train.common.net.NetAddress;
import ch.adrianelsener.train.common.net.SocketFactory;
import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.tcp.TcpClient;
import com.google.common.annotations.VisibleForTesting;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public class TcpGsonClient implements TcpClient {
    private final NetAddress address;
    private final SocketFactory socketFactory;
    private final GsonWrapper gson;
    @VisibleForTesting
    TcpGsonClient(NetAddress address, SocketFactory socketFactory, GsonWrapper gson) {
        this.address = address;
        this.socketFactory = socketFactory;
        this.gson = gson;
    }

    public TcpGsonClient(NetAddress address) {
        this(address, new SocketFactory(), new GsonWrapper());
    }

    // TODO should be used
    private Result sendSetSpeed() {
        Command cmd = Command.builder()//
                .setData(new AccelerationDto())//
                .setMode(Mode.SET_SPEED)//
                .setDevice(new Device())//
                .build();
        Result result = sendCommand(cmd);
        System.out.printf("%s\n", result);
        return result;
    }

    public Result sendCommand(final Command cmd) {
        try (AutoClosableSocket socket = socketFactory.create(address)) {
            sendData(cmd, socket);
            return receiveData(socket);
        }
    }

    private Result receiveData(final AutoClosableSocket socket) {
        final InputStream inputStream = socket.getInputStream();
        return gson.fromJson(inputStream, Result.class);
    }

    private void sendData(final Command cmd, final AutoClosableSocket socket) {
        final DataOutputStream streamWriter = new DataOutputStream(socket.getOutputStream());
        final String dtoAsJsonString = gson.toJson(cmd);
        final PrintWriter writer = new PrintWriter(streamWriter);
        writer.println(dtoAsJsonString);
        writer.flush();
    }

}
