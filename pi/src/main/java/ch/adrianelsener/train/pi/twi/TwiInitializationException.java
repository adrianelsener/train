package ch.adrianelsener.train.pi.twi;

import java.io.IOException;

public class TwiInitializationException extends RuntimeException {
    public TwiInitializationException(final Exception e) {
        super(e);
    }
}
