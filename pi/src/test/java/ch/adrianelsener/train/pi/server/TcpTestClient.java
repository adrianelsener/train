package ch.adrianelsener.train.pi.server;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import ch.adrianelsener.train.pi.dto.properties.TwiHolderDevice;
import java.io.IOException;

public class TcpTestClient {
    public static void main(String[] args) throws IOException {
        new TcpTestClient().send("foo");
    }

    private void send(final String message) throws IOException {
        sendSetSpeed();
    }

    private Result sendSetSpeed() throws IOException {
        Command cmd = Command.builder()//
                .setData(new AccelerationDto().setAcceleration(20, 4))//
                .setMode(Mode.SPEED)//
                .setDevice(new Device(new TwiHolderDevice("http://127.0.0.1:8080/train/api/speed", TwiHolderDevice.Call.GSON_REST), new TwiDevice(6, 1, TwiDevice.Accessor.DUMMY)))//
                .build();
        Result result = cmd.call();
        System.out.printf("%s\n", result);
        return result;
    }
}
