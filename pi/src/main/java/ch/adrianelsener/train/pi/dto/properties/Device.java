package ch.adrianelsener.train.pi.dto.properties;

import ch.adrianelsener.train.pi.dto.Command;
import ch.adrianelsener.train.pi.dto.Result;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Device extends AbstractProperty {
    private final TwiDevice twiDevice;
    private final TwiHolderDevice holderDevice;

    public Device() {
        this.twiDevice = TwiDevice.NOT_INITIALIZED;
        holderDevice  = TwiHolderDevice.NOT_INITIALIZED;
    }

    public Device(TwiHolderDevice holderDevice, final TwiDevice twiDevice) {
        this.holderDevice = holderDevice;
        this.twiDevice = twiDevice;
    }

    public TwiDevice getTwiDevice() {
        return twiDevice;
    }

    public Result call(Command command) {
        return holderDevice.getCallType().doCall(holderDevice, command);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
