package ch.adrianelsener.csvdb;

import ch.adrianelsener.odb.api.DatabaseStorage;
import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.odb.api.OdbFunction;
import ch.adrianelsener.odb.api.OdbPredicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CsvOdb<T extends Datacontainer> implements Odb<T> {
    private final static Logger logger = LoggerFactory.getLogger(CsvOdb.class);
    private final List<T> elements = Lists.newArrayList();
    private CsvOdbState currentState = CsvOdbState.UNINITIALIZED;
    private DatabaseStorage<T> csvReader;

    private CsvOdb(CsvOdbBuilder<T> builder) {
        csvReader = builder.getCsvReader();
        currentState = builder.getState();
    }

    public static <T extends Datacontainer> CsvOdbBuilder<T> create(Class<T> clazz) {
        logger.debug("Create logger for {}", clazz);
        return new CsvOdbBuilder<>();
    }

    public static <T extends Datacontainer> Odb<T> create(final CsvOdbBuilder<T> builder) {
        return createProxy(new CsvOdb<>(builder), DynamicInvocationHandler.class);
    }

    static <T extends Datacontainer> Odb<T> createProxy(final CsvOdb<T> db, final Class<? extends InvocationHandler> invocationHandler) {
        try {
            logger.debug("Create proxy for {}", invocationHandler);
            InvocationHandler handler = invocationHandler.getConstructor(CsvOdb.class).newInstance(db);
            final Odb<T> proxyDb = Reflection.newProxy(Odb.class, handler);
            logger.debug("Created proxy {}", proxyDb);
            return proxyDb;
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("could not instantiate CsvOdb", e);
        }
    }

    @AlwaysAllowed
    @Override
    public ImmutableCollection<T> search(final Predicate<T> predicate) {
        logger.debug("Search for predicate {}", predicate);
        final Collection<T> filtered = Collections2.filter(getAll(), predicate::test);
        logger.debug("search for predicate {} gave {}", predicate, filtered);
        return ImmutableList.copyOf(filtered);
    }

    @InitOrSave
    @ReaderMustBeSet
    @Override
    public Odb<T> init() {
        logger.debug("init storage");
        final ImmutableCollection<T> loaded = csvReader.loadFromStorage();
        elements.addAll(loaded);
        logger.debug("loaded {}", elements);
        currentState = CsvOdbState.INITIALIZED;
        return this;
    }

    @AlwaysAllowed
    @Override
    public boolean isInitialized() {
        return currentState == CsvOdbState.INITIALIZED;
    }

    @ReaderMustBeSet
    @Override
    public Odb<T> setStorage(final DatabaseStorage<T> storage) {
        logger.debug("replace storage with {}", storage);
        csvReader = storage;
        currentState = CsvOdbState.UNINITIALIZED;
        return this;
    }

    @AlwaysAllowed
    @Override
    public ImmutableCollection<T> getAll() {
        return ImmutableList.copyOf(elements);
    }

    @AlwaysAllowed
    @Override
    public Optional<T> filterUnique(final OdbPredicate<T> predicate) {
        for (T current : elements) {
            if (predicate.test(current)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    @AlwaysAllowed
    @Override
    public void replace(final OdbPredicate<T> predicate, final OdbFunction<T> replacement) {
        final Iterator<T> each = elements.iterator();
        final List<T> toReplace = Lists.newArrayList();
        while (each.hasNext()) {
            final T current = each.next();
            if (predicate.test(current)) {
                toReplace.add(current);
                each.remove();
            }
        }
        toReplace.forEach(elem -> elements.add(replacement.apply(elem)));
    }

    @AlwaysAllowed
    @Override
    public void delete(final OdbPredicate<T> predicate) {
        elements.removeIf(predicate);
    }

    @AlwaysAllowed
    @Override
    public void add(@Nonnull final T toAdd) {
        elements.add(toAdd);
    }

    @ReaderMustBeSet
    @Override
    public void flush() {
        csvReader.saveToStorage(elements);
        currentState = CsvOdbState.INITIALIZED;
    }

    @AlwaysAllowed
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @ReaderMustBeSet
    @InitOrSave
    CsvOdbState getCurrentState() {
        return currentState;
    }

    private enum CsvOdbState {
        READER_MUST_BE_SET(ReaderMustBeSet.class, AlwaysAllowed.class)//
        , UNINITIALIZED(ReaderMustBeSet.class, InitOrSave.class, AlwaysAllowed.class)//
        , INITIALIZED() {
            @Override
            public boolean isAllowed(final Method method, Object foo) {
                return true;
            }
        };
        private final static Logger logger = LoggerFactory.getLogger(CsvOdbState.class);

        private final ImmutableCollection<Class<? extends Annotation>> allowedMethods;

        @SafeVarargs
        private CsvOdbState(Class<? extends Annotation>... allowed) {
            allowedMethods = ImmutableList.copyOf(allowed);
        }

        public boolean isAllowed(Method method, Object foo) throws NoSuchMethodException {
            final Method method1 = foo.getClass().getMethod(method.getName(), method.getParameterTypes());
            for (Annotation annot : method1.getDeclaredAnnotations()) {
                final Class<? extends Annotation> annotationType = annot.annotationType();
                logger.debug("Method {} has annotation {}", method.getName(), annotationType);
                if (allowedMethods.contains(annotationType)) {
                    return true;
                }
            }
            return false;
        }

        public String getMessage() {
            return String.format("Method annotated with '%s' must be calld first", allowedMethods);
        }
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface AlwaysAllowed {

    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface InitOrSave {

    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface ReaderMustBeSet {

    }

    public static class CsvOdbBuilder<T extends Datacontainer> {
        private final static Logger logger = LoggerFactory.getLogger(CsvOdbBuilder.class);
        private DatabaseStorage<T> csvReader;
        private CsvOdbState currentState = CsvOdbState.READER_MUST_BE_SET;

        public CsvOdbBuilder<T> storage(final DatabaseStorage<T> csvReader) {
            this.csvReader = csvReader;
            currentState = CsvOdbState.UNINITIALIZED;
            return this;
        }

        CsvOdbState getState() {
            return currentState;
        }

        public Odb<T> build() {
            logger.debug("db created");
            return CsvOdb.create(this);
        }


        DatabaseStorage<T> getCsvReader() {
            return csvReader;
        }

    }

    static class DynamicInvocationHandler implements InvocationHandler {
        private final static Logger logger = LoggerFactory.getLogger(DynamicInvocationHandler.class);
        private final CsvOdb<?> db;

        public DynamicInvocationHandler(CsvOdb<?> db) {
            this.db = db;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            final CsvOdbState state = db.getCurrentState();
            logger.debug("invoke method {} with rule {}", method.getName(), state);
            if (state.isAllowed(method, db)) {
                return method.invoke(db, args);
            }
            throw new IllegalStateException(state.getMessage());
        }
    }

}
