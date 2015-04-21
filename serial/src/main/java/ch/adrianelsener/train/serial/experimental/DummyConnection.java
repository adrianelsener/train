package ch.adrianelsener.train.serial.experimental;

/**
 * Created by els on 19.04.15.
 */
public class DummyConnection implements Medium {

    private boolean currentState;

    @Override
    public void put(boolean data) {
        currentState = data;
    }

    @Override
    public boolean get() {
        return currentState;
    }
}
