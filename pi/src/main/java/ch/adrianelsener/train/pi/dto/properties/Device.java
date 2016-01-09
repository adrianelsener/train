package ch.adrianelsener.train.pi.dto.properties;

import org.apache.commons.lang3.Validate;

public class Device extends AbstractProperty {
    private final int deviceNr;
    private final String accessorClass;

    public Device() {
        this.deviceNr = -1;
        accessorClass = null;
    }

    public Device(final int deviceNr, final String accessorClass) {
        Validate.inclusiveBetween(0, 255, deviceNr);
        this.deviceNr = deviceNr;
        this.accessorClass = accessorClass;
    }

    public int getDeviceNr() {
        return deviceNr;
    }

    public String getAccessorClass() {
        return accessorClass;
    }
}
