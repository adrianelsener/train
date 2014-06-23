package ch.adrianelsener.odb.api;


/**
 * Created by els on 5/20/14.
 */
public interface DbUpdater<T extends Datacontainer> {
    void apply(Odb<T> db);
}
