package ch.adrianelsener.train.pi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

public class TwiTest {
    public static void main(String[] args) throws IOException {
//        Scanner in = new Scanner(System.in);
//
//        String val = in.nextLine();
//        byte b = val.getBytes()[0];
//        System.out.printf("%x", b);
        new TwiTest().sendFromConsole();
    }

    public void sendFromConsole() throws IOException {
        Scanner in = new Scanner(System.in);
        I2CBus i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
        System.out.printf("Device nr: ");
        String line = in.nextLine();
        Optional<String> stringLine = Optional.of(line);
        Optional<Integer> devNr = stringLine.filter(StringUtils::isNumeric).map(Integer::valueOf);
        final Consumer<? super Integer> i2cBusConsumer;
        Optional<I2CDevice> i2CDevice = devNr.map(i -> toI2CDevice(i, i2CBus));
        System.out.printf("next value: ");
        String val = in.nextLine();
        while (!val.startsWith("e")) {
            if (StringUtils.isNumeric(val)) {
                int intVal = Integer.parseInt(val);
                i2CDevice.get().write((byte) intVal);
            }
            System.out.printf("next value: ");
            val = in.nextLine();
        }

    }

    private I2CDevice toI2CDevice(final Integer i, final I2CBus i2CBus) {
        try {
            return i2CBus.getDevice(i);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
