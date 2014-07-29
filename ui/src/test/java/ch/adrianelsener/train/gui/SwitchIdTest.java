package ch.adrianelsener.train.gui;

import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.*;

public class SwitchIdTest {
    @Test
    public void numericSwitchId() {
        final SwitchId testee = SwitchId.fromValue("8");
        // Assert
        assertThat(testee.toUiString(), is(equalTo("08")));
    }

    @Test
    public void serializeNumbericSwitch() {
        final SwitchId testee = SwitchId.fromValue("8");
        // Act
        final String result = testee.toSerializable();
        // Assert
        assertThat(result, is(equalTo("8")));
    }
}