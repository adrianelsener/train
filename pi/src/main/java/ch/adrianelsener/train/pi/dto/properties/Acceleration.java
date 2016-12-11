package ch.adrianelsener.train.pi.dto.properties;

import org.apache.commons.lang3.Validate;

public class Acceleration extends AbstractProperty {
    private final int waitBetweenSteps;
    private final int stepsize;

    public Acceleration(int waitBetweenSteps, int stepsize) {
        Validate.inclusiveBetween(0, 250, waitBetweenSteps, "waitBetweenSteps is out of range");
        Validate.inclusiveBetween(0, 250, stepsize, "stepsize is out of range");
        this.waitBetweenSteps = waitBetweenSteps;
        this.stepsize = stepsize;
    }

    public Acceleration() {
        this(0, 0);
    }

    public int getWaitBetweenSteps() {
        return waitBetweenSteps;
    }

    public int getStepsize() {
        return stepsize;
    }
}
