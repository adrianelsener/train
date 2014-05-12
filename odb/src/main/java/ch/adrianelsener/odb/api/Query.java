package ch.adrianelsener.odb.api;

import com.google.common.collect.ImmutableCollection;

import java.util.Optional;
import java.util.function.Predicate;

public interface Query<T> {
    ImmutableCollection<T> search(Predicate<T> predicate);

    ImmutableCollection<T> getAll();

    Optional<T> filterUnique(OdbPredicate<T> predicate);
}
