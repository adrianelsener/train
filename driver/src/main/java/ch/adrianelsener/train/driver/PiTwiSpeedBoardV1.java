package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.common.net.NetAddress;
import ch.adrianelsener.train.pi.client.TcpGsonClient;
import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Mode;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.tcp.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PiTwiSpeedBoardV1 implements SpeedBoardDriver {
    private final static Logger logger = LoggerFactory.getLogger(PiTwiSpeedBoardV1.class);
    private final TcpClient tcpClient;

    public PiTwiSpeedBoardV1(final NetAddress tcpClient) {
        this.tcpClient = new TcpGsonClient(NetAddress.create("172.16.100.120", 2323));
    }

    @Override
    public void faster() {
    }

    @Override
    public void slower() {
    }

    @Override
    public void setSpeed(final int estimated) {
        final AccelerationDto acceleration = new AccelerationDto().setSpeed(estimated);
        final Device device = new Device(9, 0, Device.Accessor.CMD);
        logger.debug("Send command {}", acceleration);
        tcpClient.sendCommand(Command.builder().setMode(Mode.SPEED).setData(acceleration).setDevice(device).build());
    }

    @Override
    public int getCurrentSpeed() {
        final Device device = new Device(9, 0, Device.Accessor.CMD);
        Result result = tcpClient.sendCommand(Command.builder().setMode(Mode.READ_SPEED).setDevice(device).build());
        logger.debug("Received for get {}", result);
        return result.getResult().get().getSpeed().getSpeed();
    }
}
