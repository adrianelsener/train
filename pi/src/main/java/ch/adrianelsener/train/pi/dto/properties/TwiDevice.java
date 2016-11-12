package ch.adrianelsener.train.pi.dto.properties;

import ch.adrianelsener.train.pi.twi.accessor.DummyAccessor;
import ch.adrianelsener.train.pi.twi.accessor.TwiAccessor;
import ch.adrianelsener.train.pi.twi.accessor.cmd.TwiCmdAccessor;
import ch.adrianelsener.train.pi.twi.accessor.pi4j.TwiPi4JAccessor;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TwiDevice {
    public final static TwiDevice NOT_INITIALIZED = new TwiDevice();

    private final int deviceBusNr;
    private final int subDevice;
    private final Accessor accessor;

    public TwiDevice(int deviceBusNr, int subDevice, final Accessor accessor) {
        Validate.inclusiveBetween(0, 255, deviceBusNr);
        Validate.inclusiveBetween(0, 1, subDevice);
        this.deviceBusNr = deviceBusNr;
        this.subDevice = subDevice;
        this.accessor = accessor;
    }

    private TwiDevice() {
        deviceBusNr = -1;
        subDevice = -1;
        accessor = Accessor.DUMMY;
    }

    /**
     * Devices number for the twi bus
     *
     * @return
     */
    public int getDeviceBusNr() {
        return deviceBusNr;
    }

    /**
     * Device internal number. For example pwm 2
     *
     * @return
     */
    public int getSubDevice() {
        return subDevice;
    }

    public String getAccessorClass() {
        return accessor.getAccessorClass();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    public enum Accessor {
        PI4J(TwiPi4JAccessor.class),//
        CMD(TwiCmdAccessor.class),//
        DUMMY(DummyAccessor.class);

        private final Class<? extends TwiAccessor> accessorClass;

        Accessor(final Class<? extends TwiAccessor> accessorClass) {
            this.accessorClass = accessorClass;
        }

        public String getAccessorClass() {
            return accessorClass.getName();
        }
    }

}
