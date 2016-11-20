package ch.adrianelsener.train.pi.twi.accessor.cmd;

import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

abstract class CmdSetter {
    public static final Logger logger = LoggerFactory.getLogger(CmdSetter.class);
    private final static int SET_OFFSET = 4;
    @Inject
    private CmdExecutor cmdExecutor = new CmdExecutor();
    protected int subDev;
    protected int devNr;

    public Process onDevice(final TwiDevice device) {
        this.subDev = device.getSubDevice();
        this.devNr = device.getDeviceBusNr();
        final ImmutableList<String> parameters = createCommandList();
        logger.debug("Before executing: {}", this);
        return cmdExecutor.executeSetCmd(parameters);
    }

    private ImmutableList<String> createCommandList() {
        return ImmutableList.of(//
                cmdExecutor.toPrefixedHexString(devNr),//
                cmdExecutor.toPrefixedHexString(getOffset() + dataPosition()),//
                cmdExecutor.toPrefixedHexString(dataToSet()));
    }

    private int getOffset() {
        return subDev * SET_OFFSET;
    }

    protected abstract int dataPosition();

    protected abstract int dataToSet();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
