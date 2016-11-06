package ch.adrianelsener.train.pi.twi.accessor.cmd;

import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import ch.adrianelsener.train.pi.twi.accessor.TwiAccessException;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

abstract class CmdReader<T> {
    private final static Logger logger = LoggerFactory.getLogger(CmdReader.class);
    private final static int READ_OFFSET = 1;
    private final CmdExecutor cmdExecutor = new CmdExecutor();

    public T fromDevice(final TwiDevice device) {
        final ImmutableList<String> command = createCommand(device);
        final Process getProcess = cmdExecutor.executeGetCmd(command);
        final String line = readCmdResult(getProcess);
        return convert(line);
    }

    private ImmutableList<String> createCommand(final TwiDevice device) {
        final int offset = device.getSubDevice() * READ_OFFSET;
        return ImmutableList.of(
                cmdExecutor.toPrefixedHexString(device.getDeviceBusNr()),
                cmdExecutor.toPrefixedHexString(offset + getReadPosition()));
    }

    private String readCmdResult(final Process getProcess) {
        final InputStream inputStream = getProcess.getInputStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(getProcess.getErrorStream()));
        errorReader.lines().forEach(l -> logger.error("err: {}", l));
        final BufferedReader getReader = new BufferedReader(new InputStreamReader(inputStream));
        waitForProcess(getProcess);
        return readLine(getReader);
    }

    private static String readLine(final BufferedReader getReader) {
        final String readLine;
        try {
            readLine = getReader.readLine();
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
        return readLine;
    }

    private static int waitForProcess(final Process setProcess) {
        while (setProcess.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new TwiAccessException(e);
            }
        }
        return setProcess.exitValue();
    }

    protected abstract T convert(final String line);

    protected abstract int getReadPosition();
}
