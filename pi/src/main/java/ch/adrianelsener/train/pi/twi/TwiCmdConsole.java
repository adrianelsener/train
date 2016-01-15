package ch.adrianelsener.train.pi.twi;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.dto.properties.Acceleration;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.Direction;
import com.google.common.base.Splitter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TwiCmdConsole {
    private int busDevice;
    private final TwiAccessorFactory twiAccessorFactory = new TwiAccessorFactory();

    public static void main(String[] args) {
        final TwiCmdConsole cmdConsole = new TwiCmdConsole();
        cmdConsole.getBusDevice();
        cmdConsole.sendAndReceive();
    }

    private void getBusDevice() {
        Scanner in = new Scanner(System.in);
        System.out.printf("Device nr: ");
        String line = in.nextLine();
        busDevice = Integer.parseInt(line);
    }

    private void sendAndReceive() {
        Scanner in = new Scanner(System.in);
        String val = readNextStepWithOutput(in);
        while (!val.startsWith("e")) {
            if (val.startsWith("r")) {
                final Result read1 = read(0);
                final Result read2 = read(1);
                System.out.printf("PWM1 -> %s", read1.getResult().get().getSpeed().getSpeed());
                System.out.printf("PWM2 -> %s", read2.getResult().get().getSpeed().getSpeed());
            } else if (val.startsWith("s")) {
                final Device dev1 = createDevice(0, busDevice);
                final Device dev2 = createDevice(1, busDevice);
                final AccelerationDto accel = new AccelerationDto().setAcceleration(new Acceleration(1, 1)).setSpeed(0).setDirection(Direction.STOP);
                twiAccessorFactory.open(dev1).write(dev1, accel);
                twiAccessorFactory.open(dev1).write(dev2, accel);
            } else {
                List<Integer> splittedStrings = Splitter.on(",").splitToList(val).stream().map(Integer::parseInt).collect(Collectors.toList());
                Integer pwm = splittedStrings.get(0);
                Integer speed = splittedStrings.get(1);
                Device dev = createDevice(pwm, busDevice);
                final TwiAccessor accessor = twiAccessorFactory.open(dev);
                final AccelerationDto accel = new AccelerationDto().setAcceleration(new Acceleration(1, 1)).setSpeed(Math.abs(speed)).setDirection(Direction.forSpeed(speed));
                accessor.write(dev, accel);
            }
            val = readNextStepWithOutput(in);
        }
    }

    private Result read(final int subDev) {
        final Device dev1 = createDevice(subDev, busDevice);
        final TwiAccessor twiAccessor1 = twiAccessorFactory.open(dev1);
        return twiAccessor1.read(dev1);
    }

    private static Device createDevice(final int subDev, final int busDevice) {
        return new Device(busDevice, subDev, Device.Accessor.CMD);
    }

    private String readNextStepWithOutput(final Scanner in) {
        final String val;
        System.out.printf("next value (pwm [0,1], speed [-255/255]) - (e)xit) (r)ead (s)top: ");
        val = in.nextLine();
        return val;
    }

}
