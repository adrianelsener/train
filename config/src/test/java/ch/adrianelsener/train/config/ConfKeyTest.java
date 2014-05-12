package ch.adrianelsener.train.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.testng.annotations.Test;

public class ConfKeyTest {
    @Test
    public void confKeyEqual() {
        ConfKey testee = ConfKey.create("foo.bar");
        // Act
        assertEquals(ConfKey.create("foo.bar"), testee);
    }

    @Test
    public void isNotSubKey() {
        final ConfKey parentKey = ConfKey.create("foo.bar");
        final ConfKey testee = ConfKey.create("foo");
        // Act
        boolean result = testee.isSubkey(parentKey);
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void isSubkey() {
        final ConfKey parentKey = ConfKey.create("foo");
        final ConfKey testee = ConfKey.create("foo.bar");
        // Act
        boolean result = testee.isSubkey(parentKey);
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void createSubKey() {
        final ConfKey testee = ConfKey.create("foo");
        // Act
        final ConfKey result = testee.createSubKey("bar");
        // Assert
        assertThat(result, is(equalTo(ConfKey.create("foo.bar"))));

    }
}
