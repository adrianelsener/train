package ch.adrianelsener.train.pi.twi.accessor.cmd;

import ch.adrianelsener.train.pi.twi.TwiAccessException;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class CmdExecutor {
    private final static Logger logger = LoggerFactory.getLogger(CmdExecutor.class);
    private final static ImmutableList<String> SET_COMMAND = ImmutableList.of(
            "/usr/sbin/i2cset", "-y", "1");
    private final static ImmutableList<String> GET_COMMAND = ImmutableList.of(
            "/usr/sbin/i2cget", "-y", "1");

    public Process executeSetCmd(final ImmutableList<String> parameters) {
        final ImmutableList<String> command = ImmutableList.<String>builder()//
                .addAll(SET_COMMAND)//
                .addAll(parameters).build();
        return executeCmd(command);
    }

    public Process executeGetCmd(final ImmutableList<String> parameters) {
        final Process setProcess = executeSetCmd(parameters);
        final int setResult = waitForProcess(setProcess);
        if (0 != setResult) {
            logger.warn("Problems while setting read position. Got Result '{}'", setResult);
        }
        final ImmutableList<String> command = ImmutableList.<String>builder()//
                .addAll(GET_COMMAND).build();
        return executeCmd(command);
    }

    private Process executeCmd(final ImmutableList<String> parameters) {
        final ProcessBuilder setProcessBuilder = new ProcessBuilder(parameters);
        parameters.forEach(l -> logger.debug("{}", l));
        try {
            return setProcessBuilder.start();
        } catch (IOException e) {
            throw new TwiAccessException(e);
        }
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

    public String toPrefixedHexString(int i) {
        return "0x" + Integer.toHexString(i);
    }
}
