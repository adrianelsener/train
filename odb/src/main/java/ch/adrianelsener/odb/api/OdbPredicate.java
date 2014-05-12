package ch.adrianelsener.odb.api;

import java.util.function.Predicate;

public interface OdbPredicate<T> extends Predicate<T> {
    @Override
    boolean test(T original);
}
