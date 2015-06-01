package ch.adrianelsener.odb.api;

import java.util.Collection;

public interface ObjectFactory<T> {
    T createFrom(Collection<String> input);
}
