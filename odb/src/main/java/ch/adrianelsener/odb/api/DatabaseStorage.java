package ch.adrianelsener.odb.api;

import com.google.common.collect.ImmutableCollection;

public interface DatabaseStorage<T extends Datacontainer> {
    ImmutableCollection<T> loadFromStorage();

    void saveToStorage(Iterable<T> elements);
}
