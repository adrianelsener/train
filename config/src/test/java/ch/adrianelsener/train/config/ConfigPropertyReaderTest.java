package ch.adrianelsener.train.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.testng.annotations.Test;

public class ConfigPropertyReaderTest {
    @Test
    public void readIPBoardConfig() {
        ConfigReader testee = new ConfigPropertyReader(createInputStream("foo.bar=42"));
        // Act
        Config result = testee.getConfig();
        // Assert
        assertThat(result.get(ConfKey.create("foo.bar")), is(equalTo("42")));
    }

    private InputStream createInputStream(String string) {
        InputStream propertyStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        return propertyStream;
    }
}
