package ch.adrianelsener.train.pi.twi.accessor;

import ch.adrianelsener.train.pi.dto.AccelerationDto;
import ch.adrianelsener.train.pi.dto.Result;
import ch.adrianelsener.train.pi.twi.TwiAccessException;
import ch.adrianelsener.train.pi.twi.TwiAccessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TwiCmdAccessor implements TwiAccessor {

    private final int devNr;

    public TwiCmdAccessor(final int devNr) {
        this.devNr = devNr;
    }

    @Override
    public void write(final AccelerationDto accelerationDto) {

    }

    @Override
    public Result read() {
        final Process setProcess = createStartedSetProcess(devNr, 0x00);
        waitForProcess(setProcess);
        ProcessBuilder processBuilder = new ProcessBuilder("/usr/sbin/i2cget", "-y", "1", toPrefixedHexString(devNr));
        final Process getProcess;
        getProcess = startProcess(processBuilder);
        final InputStream inputStream = getProcess.getInputStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(getProcess.getErrorStream()));
//            errorReader.lines().forEach(l -> System.out.println("err: "+l));
        final BufferedReader getReader = new BufferedReader(new InputStreamReader(inputStream));
        waitForProcess(getProcess);
        final String readLine = readLine(getReader);
        return Result.ok(new AccelerationDto().setSpeed(Integer.parseInt(readLine)));
    }

    private String readLine(final BufferedReader getReader) {
        final String readLine;
        try {
            readLine = getReader.readLine();
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
        return readLine;
    }

    private Process startProcess(final ProcessBuilder processBuilder) {
        final Process getProcess;
        try {
            getProcess = processBuilder.start();
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
        return getProcess;
    }

    private Process createStartedSetProcess(final int devNr, final int dataPosition) {
        ProcessBuilder setProcessBuilder = new ProcessBuilder("/usr/sbin/i2cset", "-y", "1", toPrefixedHexString(devNr), toPrefixedHexString(dataPosition));
        try {
            return setProcessBuilder.start();
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
    }

    private Process createStartedSetProcess(final Optional<Integer> devNr, final int dataPosition, final int data) throws IOException {
        List<String> parameters = new ArrayList<>();
        parameters.add("/usr/sbin/i2cset");
        parameters.add("-y");
        parameters.add("1");
        parameters.add(toPrefixedHexString(devNr.get()));
        parameters.add(toPrefixedHexString(dataPosition));
        parameters.add(toPrefixedHexString(data));
        ProcessBuilder setProcessBuilder = new ProcessBuilder(parameters);
        parameters.forEach(System.out::println);
        System.out.println("Data was : " + data);
        return setProcessBuilder.start();
    }

    private int waitForProcess(final Process setProcess) {
        while (setProcess.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new TwiAccessException(e);
            }
        }
        final int exitValue = setProcess.exitValue();
        return exitValue;
    }

    private static String toPrefixedHexString(int i) {
        return "0x" + Integer.toHexString(i);
    }
}
