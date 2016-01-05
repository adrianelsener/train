package ch.adrianelsener.train.pi.twi;

import com.google.common.base.Splitter;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TwiTest2 {
    public static void main(String[] args) throws IOException {
        new TwiTest2().sendFromConsole();
    }

    public void sendFromConsole() throws IOException {
        Scanner in = new Scanner(System.in);
        I2CBus i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
        System.out.printf("Device nr: ");
        String line = in.nextLine();
        Optional<String> stringLine = Optional.of(line);
        Optional<Integer> devNr = stringLine.filter(StringUtils::isNumeric).map(Integer::valueOf);
        Optional<I2CDevice> i2CDevice = devNr.map(i -> toI2CDevice(i, i2CBus));
        String val = readNextStepWithOutput(in);
        while (!val.startsWith("e")) {
            if (val.startsWith("r")) {
                try {
                    int read = i2CDevice.get().read(0x00);
                    System.out.printf("read 0x00 -> %s", read);
                } catch (IOException e) {
                    System.out.printf("got an ioex...");
                    e.printStackTrace();
                }
            } else {
                try {
                    i2CDevice.get().write(0x00, (byte) 0x17);
                } catch (IOException e) {
                    System.out.printf("got an ioex...");
                    e.printStackTrace();
                }
            }
            val = readNextStepWithOutput(in);
        }

    }

    private String readNextStepWithOutput(final Scanner in) {
        final String val;
        System.out.printf("next value (wait,steps,destination,direction[1,2]) - (e)xit) (r)ead: ");
        val = in.nextLine();
        return val;
    }


    private I2CDevice toI2CDevice(final Integer i, final I2CBus i2CBus) {
        try {
            return i2CBus.getDevice(i);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}