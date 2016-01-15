package ch.adrianelsener.train.pi.dto.properties;

import ch.adrianelsener.train.pi.twi.TwiAccessor;
import ch.adrianelsener.train.pi.twi.accessor.TwiPi4JAccessor;
import ch.adrianelsener.train.pi.twi.accessor.cmd.TwiCmdAccessor;
import org.apache.commons.lang3.Validate;

public class Device extends AbstractProperty {
    private final int deviceNr;
    private final int subDeviceNr;
    private final Accessor accessor;

    public enum Accessor {
        PI4J(TwiPi4JAccessor.class),//
        CMD(TwiCmdAccessor.class),//
        DUMMY(null);

        private final Class<? extends TwiAccessor> accessorClass;

        Accessor(final Class<? extends TwiAccessor> accessorClass) {
            this.accessorClass = accessorClass;
        }

        public String getAccessorClass() {
            return accessorClass.getName();
        }
    }

    public Device() {
        subDeviceNr = -1;
        this.deviceNr = -1;
        accessor = Accessor.CMD;
    }

    public Device(final int deviceNr, final int subDeviceNr, final Accessor accessor) {
        Validate.inclusiveBetween(0, 255, deviceNr);
        Validate.inclusiveBetween(0, 1, subDeviceNr);
        this.deviceNr = deviceNr;
        this.subDeviceNr = subDeviceNr;
        this.accessor = accessor;
    }

    public int getDeviceNr() {
        return deviceNr;
    }

    public String getAccessorClass() {
        return accessor.getAccessorClass();
    }

    public int getSubDeviceNr() {
        return subDeviceNr;
    }
}
