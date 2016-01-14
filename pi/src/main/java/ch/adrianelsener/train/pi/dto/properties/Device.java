package ch.adrianelsener.train.pi.dto.properties;

import org.apache.commons.lang3.Validate;

public class Device extends AbstractProperty {
    private final int deviceNr;
    private final int subDeviceNr;
    private final String accessorClass;

    public Device() {
        subDeviceNr = -1;
        this.deviceNr = -1;
        accessorClass = null;
    }

    public Device(final int deviceNr, final int subDeviceNr, final String accessorClass) {
        Validate.inclusiveBetween(0, 255, deviceNr);
        Validate.inclusiveBetween(0, 1, subDeviceNr);
        this.deviceNr = deviceNr;
        this.subDeviceNr = subDeviceNr;
        this.accessorClass = accessorClass;
    }

    public int getDeviceNr() {
        return deviceNr;
    }

    public String getAccessorClass() {
        return accessorClass;
    }

    public int getSubDeviceNr() {
        return subDeviceNr;
    }
}
