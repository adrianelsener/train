package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.denkovi.Pin;

public enum Occupied {
    _01(Pin._17);
    private Pin pin;

    Occupied(Pin pin) {
        this.pin = pin;
    }

    public Pin getPin() {
        return pin;
    }
}
