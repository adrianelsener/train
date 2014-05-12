package ch.adrianelsener.csvdb;

import ch.adrianelsener.odb.api.DatabaseStorage;
import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.odb.api.ObjectFactory;
import com.google.common.base.Charsets;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CsvReader<T extends Datacontainer> implements DatabaseStorage<T> {

    private final File file;
    private final ObjectFactory<T> objectFactory;

    public CsvReader(final File file, final ObjectFactory<T> objectFactory) {
        this.file = file;
        this.objectFactory = objectFactory;
    }

    @Override
    public ImmutableCollection<T> loadFromStorage() {
        ImmutableCollection<T> poits;
        try {
            final InputStream inputStream = file.toURI().toURL().openStream();
            final List<String> readLines = IOUtils.readLines(inputStream, Charset.forName("UTF-8"));
            final Collection<List<String>> cls = Collections2.transform(readLines, input -> Lists.newArrayList(input.split(",")));
            poits = ImmutableList.copyOf(Collections2.transform(cls, input -> objectFactory.createFrom(input)));
        } catch (IOException e1) {
            throw new IllegalArgumentException(e1);
        }
        return poits;
    }

    @Override
    public void saveToStorage(Iterable<T> parts) {
        final Iterator<String> asStringIter = Iterators.transform(parts.iterator(), input -> {
            final Collection<Object> datas = input.getDataToPersist();
            final StringBuffer sb = new StringBuffer();
            for (Object data : datas) {
                sb.append(data).append(",");
            }
            return sb.toString();
        });
        final List<String> asString = Lists.newArrayList();
        Iterators.addAll(asString, asStringIter);
        try {
            final OutputStream outputStream = new FileOutputStream(file, false);
            IOUtils.writeLines(asString, null, outputStream, Charsets.UTF_8);
        } catch (IOException e1) {
            throw new IllegalStateException(e1);
        }
    }

}
