package ch.adrianelsener.odb.api;

public interface Update<T> {
    void replace(OdbPredicate<T> original, OdbFunction<T> replacement);

    void delete(OdbPredicate<T> filter);

    void add(T toAdd);
}
