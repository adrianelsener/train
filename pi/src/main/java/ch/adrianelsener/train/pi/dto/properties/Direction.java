package ch.adrianelsener.train.pi.dto.properties;

import com.google.common.collect.FluentIterable;

public enum Direction {
    FORWARD(1),//
    BACKWARD(2),//
    STOP(0);

    private final int avrDirectionValue;

    Direction(final int avrDirectionValue) {
        this.avrDirectionValue = avrDirectionValue;
    }

    public int getAvrDirectionValue() {
        return avrDirectionValue;
    }

    public static Direction fromTwiValue(final int i) {
        return FluentIterable.of(Direction.values())//
                .firstMatch(direction -> direction.avrDirectionValue == i).get();
    }
}
