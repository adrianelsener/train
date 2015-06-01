package ch.adrianelsener.csvdb;

import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.odb.api.Query;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsvOdbTest {
    @Test
    public void containsAllFromReader() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sample = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sample));
        final Query<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        ImmutableCollection<Datacontainer> result = testee.getAll();
        // Assert
        assertThat(result, contains(sample));
    }

    @Test
    public void filteringReturnsItemIfPredicateMatches() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sample = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sample));
        final Query<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        final Predicate<Datacontainer> predicate = datacontainer -> true;
        ImmutableCollection<Datacontainer> result = testee.search(predicate);
        // Assert
        assertThat(result, contains(sample));

    }
    @Test
    public void filteringReturnsItemNotIfPredicateNotMatches() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sample = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sample));
        new ByteArrayInputStream("foo,bar".getBytes(Charsets.UTF_8));
        final Query<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        final Predicate<Datacontainer> predicate = datacontainer -> false;
        ImmutableCollection<Datacontainer> result = testee.search(predicate);
        // Assert
        assertThat(result, not(contains(sample)));
    }

    @Test
    public void addEntryAndPersistWillReturnedByQuery() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sample = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of());
        final Odb<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        testee.add(sample);
        final ImmutableCollection<Datacontainer> result = testee.getAll();
        // Assert
        assertThat(result, contains(sample));
    }

    @Test
    public void flushPersistsAllElements() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sampleFromStore = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sampleFromStore));
        final Datacontainer sampleToAdded = mock(Datacontainer.class);
        final Odb<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        testee.add(sampleToAdded);
        // Act
        testee.flush();
        // Assert
        ArgumentCaptor<Iterable<Datacontainer>> captor = createCaptor(Iterable.class);
        verify(csvReader).saveToStorage(captor.capture());
        final Iterable<Datacontainer> allValues = captor.getValue();
        assertThat(allValues, containsInAnyOrder(sampleFromStore, sampleToAdded));
    }

    private ArgumentCaptor<Iterable<Datacontainer>> createCaptor(final Class<Iterable> iterableClass) {
        final Class foo = iterableClass;
        return ArgumentCaptor.forClass((Class<Iterable<Datacontainer>>) foo);
    }

    @Test
    public void replaceWillPlaceNewObjectInsteadOfOriginal() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sampleFromStore = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sampleFromStore));
        final Datacontainer sampleToAdded = mock(Datacontainer.class);
        final Odb<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        testee.replace(part -> part == sampleFromStore, part -> sampleToAdded);
        // Assert
        final ImmutableCollection<Datacontainer> result = testee.getAll();
        assertThat(result, contains(sampleToAdded));
    }

    @Test
    public void deleteByPredicate() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sampleFromStore = mock(Datacontainer.class);
        final Datacontainer sampleToAdded = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sampleFromStore, sampleToAdded));
        final Odb<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        testee.delete(part -> part == sampleFromStore);
        // Assert
        final ImmutableCollection<Datacontainer> result = testee.getAll();
        assertThat(result, contains(sampleToAdded));
    }

    @Test
    public void filterUnique() {
        final CsvReader<Datacontainer> csvReader = mock(CsvReader.class);
        final Datacontainer sampleFromStore = mock(Datacontainer.class);
        final Datacontainer sampleToAdded = mock(Datacontainer.class);
        when(csvReader.loadFromStorage()).thenReturn(ImmutableList.of(sampleFromStore, sampleToAdded));
        final Odb<Datacontainer> testee = CsvOdb.create(Datacontainer.class).storage(csvReader).build().init();
        // Act
        final Optional<Datacontainer> result = testee.filterUnique(part -> part == sampleFromStore);
        // Assert
        assertThat(result.orElse(null), equalTo(sampleFromStore));
    }

    @Test
    public void throwsExceptionWhileNoReaderDefined() {
        final Odb<Datacontainer> testee = CsvOdb.create(Datacontainer.class).build();
        // Act
        final ImmutableCollection<Datacontainer> result = testee.getAll();
        // Assert
        assertThat(result, is(emptyCollectionOf(Datacontainer.class)));
    }

}
