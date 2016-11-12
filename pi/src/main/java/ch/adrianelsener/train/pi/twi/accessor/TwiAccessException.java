package ch.adrianelsener.train.pi.twi.accessor;

public class TwiAccessException extends RuntimeException {
    public TwiAccessException(Throwable e) {
        super(e);
    }

    public TwiAccessException(final String message, Object... replacements) {
        super(String.format(message, replacements));
    }
}
