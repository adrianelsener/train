package ch.adrianelsener.odb.api;

public interface OdbFunction<T> {
    T apply(T original);
}
