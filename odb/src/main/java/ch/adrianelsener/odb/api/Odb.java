package ch.adrianelsener.odb.api;

public interface Odb<T extends Datacontainer> extends Query<T>, Update<T> {
    void flush();

    Odb<T> setStorage(DatabaseStorage<T> storage);

    Odb<T> init();

    boolean isInitialized();
}
