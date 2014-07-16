package ch.adrianelsener.csvdb;

import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.odb.api.ObjectFactory;
import ch.adrianelsener.testing.RulesForTestNg;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsvReaderTest extends RulesForTestNg {
    @Rule
    private TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testLoadFromFile() throws Exception {
        tmpFolder.create();
        final File file = tmpFolder.newFile();
        FileUtils.write(file, "T,742,103,726,140,", Charsets.UTF_8);
        final ObjectFactory<Sample> objectFactory = mock(ObjectFactory.class);
        final Sample dummy = mock(Sample.class);
        when(objectFactory.createFrom(anyCollectionOf(String.class))).thenReturn(dummy);
        final CsvReader<Sample> testee = CsvReader.create(file, objectFactory);
        // Act
        final Collection<Sample> result = testee.loadFromStorage();
        // Assert
        assertThat(result, contains(dummy));
        verify(objectFactory).createFrom(Lists.newArrayList("T","742","103","726","140"));
    }

    @Test
    public void testSaveToFile() throws Exception {
        tmpFolder.create();
        final File file = tmpFolder.newFile();
        final CsvReader<Datacontainer> testee = CsvReader.create(file, null);
        final Datacontainer sampleToWrite = new Sample();
        // Act
        testee.saveToStorage(Lists.newArrayList(sampleToWrite));
        // Assert
        final String result = FileUtils.readFileToString(file, Charsets.UTF_8);
        assertThat(result, is(Matchers.equalToIgnoringWhiteSpace("X,foo,bar,")));
    }

    private static class Sample implements Datacontainer {

        @Override
        public Collection<Object> getDataToPersist() {
            return Lists.newArrayList("X", "foo", "bar");
        }
    }

}
