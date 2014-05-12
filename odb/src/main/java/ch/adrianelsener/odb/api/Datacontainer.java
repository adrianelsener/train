package ch.adrianelsener.odb.api;

import java.util.Collection;

public interface Datacontainer {
    Collection<Object> getDataToPersist();
}
