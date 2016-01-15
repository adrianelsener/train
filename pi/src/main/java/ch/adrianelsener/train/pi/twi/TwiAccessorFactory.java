package ch.adrianelsener.train.pi.twi;

import ch.adrianelsener.train.pi.dto.properties.Device;

import java.lang.reflect.Constructor;

public class TwiAccessorFactory {
    public TwiAccessor open(Device device) {
        try {
            final Constructor<?> constructor = Class.forName(device.getAccessorClass()).getConstructor();
            final Object accessor = constructor.newInstance();
            return (TwiAccessor) accessor;
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
