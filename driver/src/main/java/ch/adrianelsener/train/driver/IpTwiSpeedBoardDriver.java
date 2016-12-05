package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.Direction;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import ch.adrianelsener.train.pi.dto.properties.TwiHolderDevice;
import org.apache.commons.lang3.tuple.Pair;

public class IpTwiSpeedBoardDriver implements SpeedBoardDriver {
    private final Device device;
    private int currentSpeed = 0;

    public IpTwiSpeedBoardDriver(Config cfg) {
        final TwiHolderDevice twiHolderDevice = createTwiHolderDevice(cfg);
        final TwiDevice twiDevice = createTwiDevice(cfg);
        device = new Device(twiHolderDevice, twiDevice);
    }

    private TwiDevice createTwiDevice(Config cfg) {
        String twiadr = cfg.getChild("TWIADR");
        String dev = cfg.getChild("DEV");
        String accessor = cfg.getChild("ACCESSOR");
        return new TwiDevice(Integer.valueOf(twiadr), Integer.valueOf(dev), TwiDevice.Accessor.valueOf(accessor));
    }

    private TwiHolderDevice createTwiHolderDevice(Config cfg) {
        String ip = cfg.getChild("IP");
        String port = cfg.getChild("PORT");
        String path = cfg.getChild("PATH");
        String protocol = cfg.getChild("PROTOCOL");
        String connectionString = new StringBuilder("http://").append(ip).append(":").append(port).append(path).toString();
        return new TwiHolderDevice(connectionString, TwiHolderDevice.Call.valueOf(protocol));
    }

    @Override
    public void faster() {

    }

    @Override
    public void slower() {

    }

    @Override
    public void setSpeed(int estimated) {
        final Pair<Integer, Direction> effective = getEffektiveDirections(estimated);
        Command cmd = Command.builder()//
                .setData(new AccelerationDto().setAcceleration(1, 1) //
                .setSpeed(effective.getLeft()).setDirection(effective.getRight()))//
                .setMode(Mode.SET_SPEED)//
                .setDevice(device)//
                .build();
        Result result = cmd.call();
        System.out.printf("%s\n", result);
//        currentSpeed = result.getResult().get().getSpeed().getSpeed();
    }

    private Pair<Integer, Direction> getEffektiveDirections(int estimated) {
        final Pair<Integer, Direction> effective;
        if (estimated < 0) {
            effective = Pair.of(Math.abs(estimated), Direction.BACKWARD);
        } else {
            effective = Pair.of(estimated, Direction.FORWARD);
        }
        return effective;
    }

    @Override
    public int getCurrentSpeed() {
        return currentSpeed;
    }
}
