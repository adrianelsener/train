package ch.adrianelsener.train.pi.twi.accessor;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import ch.adrianelsener.train.pi.dto.properties.TwiHolderDevice;
import org.testng.annotations.Test;

public class IpTwiSpeedBoardDriverTest {
    private final Device device;
    private int currentSpeed = 0;

    public IpTwiSpeedBoardDriverTest() {
        final TwiHolderDevice twiHolderDevice = createTwiHolderDevice();
        final TwiDevice twiDevice = createTwiDevice();
        device = new Device(twiHolderDevice, twiDevice);
    }

    private TwiDevice createTwiDevice() {
        return new TwiDevice(7, 1, TwiDevice.Accessor.CMD);
    }

    private TwiHolderDevice createTwiHolderDevice() {
        String ip = "172.16.100.120";
        String port = "8080";
        String path = "/train/api/speed";
        String protocol = "GSON_REST";
        String connectionString = new StringBuilder("http://").append(ip).append(":").append(port).append(path).toString();
        return new TwiHolderDevice(connectionString, TwiHolderDevice.Call.valueOf(protocol));
    }

    @Test
    public void setSpeed() {
        Command cmd = Command.builder()//
                .setData(new AccelerationDto().setAcceleration(1, 1).setSpeed(5))//
                .setMode(Mode.SET_SPEED)//
                .setDevice(device)//
                .build();
        Result result = cmd.call();
        System.out.printf("%s\n", result);
//        currentSpeed = result.getResult().get().getSpeed().getSpeed();
    }

}
