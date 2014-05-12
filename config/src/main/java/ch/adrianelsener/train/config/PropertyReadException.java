package ch.adrianelsener.train.config;

public class PropertyReadException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PropertyReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
