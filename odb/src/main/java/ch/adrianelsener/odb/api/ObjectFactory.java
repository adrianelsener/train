package ch.adrianelsener.odb.api;

import java.util.Collection;

public interface ObjectFactory<T> {
    public T createFrom(Collection<String> input);
}
