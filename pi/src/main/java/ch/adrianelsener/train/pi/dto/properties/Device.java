package ch.adrianelsener.train.pi.dto.properties;

import org.apache.commons.lang3.Validate;

public class Device extends AbstractProperty {
    private final int deviceNr;

    public Device() {
        this.deviceNr = -1;
    }

    public Device(final int deviceNr) {
        Validate.inclusiveBetween(0, 255, deviceNr);
        this.deviceNr = deviceNr;
    }

    public int getDeviceNr() {
        return deviceNr;
    }
}
