package ch.adrianelsener.train.pi.twi;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TwiCmdTest {
    public static void main(String[] args) throws IOException {
        new TwiCmdTest().sendFromConsole();
    }

    public void sendFromConsole() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.printf("Device nr: ");
        String line = in.nextLine();
        Optional<String> stringLine = Optional.of(line);
        Optional<Integer> devNr = stringLine.filter(StringUtils::isNumeric).map(Integer::valueOf);

        String val = readNextStepWithOutput(in);
        while (!val.startsWith("e")) {
            if (val.startsWith("r")) {
                final Process setProcess = createStartedSetProcess(devNr, 0x00);
                waitForProcess(setProcess);
                ProcessBuilder processBuilder = new ProcessBuilder("/usr/sbin/i2cget", "-y", "1", "0x"+Integer.toHexString(devNr.get()));
                final Process getProcess = processBuilder.start();
                final InputStream inputStream = getProcess.getInputStream();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(getProcess.getErrorStream()));
                errorReader.lines().forEach(l -> System.out.println("err: "+l));
                final BufferedReader getReader = new BufferedReader(new InputStreamReader(inputStream));
                waitForProcess(getProcess);
                final String readLine = getReader.readLine();
                System.out.printf("PWM -> %s\n", readLine);
            } else {
                List<String> splittedStrings = Splitter.on(",").splitToList(val);
                List<Integer> splitted = splittedStrings.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
                for (int dataPosition = 0; dataPosition < splitted.size(); dataPosition++) {
                    createStartedSetProcess(devNr, dataPosition, splitted.get(dataPosition));
                }
            }
            val = readNextStepWithOutput(in);
        }

    }

    private Process createStartedSetProcess(final Optional<Integer> devNr, final int dataPosition) throws IOException {
        ProcessBuilder setProcessBuilder = new ProcessBuilder("/usr/sbin/i2cset", "-y", "1", "0x"+Integer.toHexString(devNr.get()), "0x"+Integer.toHexString(dataPosition));
        return setProcessBuilder.start();
    }

    private Process createStartedSetProcess(final Optional<Integer> devNr, final int dataPosition, final int data) throws IOException {
        List<String> parameters = new ArrayList<>();
        parameters.add("/usr/sbin/i2cset");
        parameters.add("-y");
        parameters.add("1");
        parameters.add("0x"+Integer.toHexString(devNr.get()));
        parameters.add("0x"+Integer.toHexString(dataPosition));
        parameters.add("0x"+Long.toHexString(data));
        ProcessBuilder setProcessBuilder = new ProcessBuilder(parameters);
        parameters.forEach(System.out::println);
        System.out.println("Data was : "+data);
        return setProcessBuilder.start();
    }

    private int waitForProcess(final Process setProcess) throws IOException {
        while (setProcess.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        final int exitValue = setProcess.exitValue();
        System.out.printf("Exit value is %s\n", exitValue);
        return exitValue;
    }

    private String readNextStepWithOutput(final Scanner in) {
        final String val;
        System.out.printf("next value (wait,steps,destination,direction[1,2]) - (e)xit) (r)ead: ");
        val = in.nextLine();
        return val;
    }
}
