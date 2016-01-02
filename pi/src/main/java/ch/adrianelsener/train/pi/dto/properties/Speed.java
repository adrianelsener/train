package ch.adrianelsener.train.pi.dto.properties;

import org.apache.commons.lang3.Validate;

public class Speed extends AbstractProperty {
    private final int speed;

    public Speed(int speed) {
        Validate.inclusiveBetween(0, 250, speed, "speed is out of range");
        this.speed = speed;
    }

    public Speed() {
        this(0);
    }

    public int getSpeed() {
        return speed;
    }
}
