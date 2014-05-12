package ch.adrianelsener.train.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

public class ConfigTest {

    @Test
    public void getAll() {
        final Config testee = new Config();
        final ConfKey parent = ConfKey.create("FOO");
        final ConfKey child1 = ConfKey.create(parent, "1");
        final ConfKey child2 = ConfKey.create(parent, "2");
        testee.put(parent, "bar");
        testee.put(child1, "one");
        testee.put(child2, "two");
        // Act
        final Config all = testee.getAll(parent);
        // Assert
        assertThat(all.get(parent), is(equalTo("bar")));
        assertThat(all.get(child1), is(equalTo("one")));
        assertThat(all.get(child2), is(equalTo("two")));
    }

    @Test
    public void getChildReturnsTheChild() {
        final Config testee = new Config();
        final ConfKey parent = ConfKey.create("FOO");
        final ConfKey child1 = ConfKey.create(parent, "1");
        final ConfKey child2 = ConfKey.create(parent, "2");
        testee.put(parent, "bar");
        testee.put(child1, "one");
        testee.put(child2, "two");
        final Config all = testee.getAll(parent);
        // Act
        final String result = all.getChild("2");
        // Assert
        assertThat(result, is(equalTo("two")));
    }

}
