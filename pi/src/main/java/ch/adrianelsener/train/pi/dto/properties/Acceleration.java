package ch.adrianelsener.train.pi.dto.properties;

import org.apache.commons.lang3.Validate;

public class Acceleration extends AbstractProperty {
    private final int accel;
    private final int stepsize;

    public Acceleration(int accel, int stepsize) {
        Validate.inclusiveBetween(0, 250, accel, "accel is out of range");
        Validate.inclusiveBetween(0, 250, stepsize, "stepsize is out of range");
        this.accel = accel;
        this.stepsize = stepsize;
    }

    public Acceleration() {
        this(0, 0);
    }

    public int getAcceleration() {
        return accel;
    }

    public int getStepsize() {
        return stepsize;
    }
}
