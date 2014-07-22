package ch.adrianelsener.train.gui;

import ch.adrianelsener.train.denkovi.Board;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.*;

public class BoardIdTest {
    @Test
    public void createWithStringNumber() {
        final BoardId testee = BoardId.create("7");
        // Assert
        assertThat(testee.toUiString(), is(equalTo("7")));
    }

    @Test
    public void valueOfFromSimpleNumberString() {
        final BoardId testee = BoardId.fromValue("6");
        // Assert
        assertThat(testee.toUiString(), is(equalTo("6")));
    }

    @Test
    public void serializeSimpleNumberId() {
        final BoardId testee = BoardId.fromValue("5");
        // Act
        final String result = testee.toSerializable();
        // Assert
        assertThat(result, is(equalTo("5")));
    }

    @Test
    public void createMultipleString() {
        final BoardId testee = BoardId.create("7", "8");
        // Assert
        assertThat(testee.toUiString(), is(equalTo("7/8")));
    }

    @Test
    public void valueOfFromMultiNumber() {
        final BoardId testee = BoardId.fromValue("8/9");
        // Assert
        assertThat(testee.toUiString(), is(equalTo("8/9")));
    }

    @Test
    public void serializeMultiId() {
        final BoardId testee = BoardId.fromValue("8/9");
        // Act
        final String result = testee.toSerializable();
        // Assert
        assertThat(result, is(equalTo("8/9")));
    }
}