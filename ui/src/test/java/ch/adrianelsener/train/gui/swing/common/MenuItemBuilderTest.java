package ch.adrianelsener.train.gui.swing.common;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.event.ActionListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.mockito.Mockito.mock;

public class MenuItemBuilderTest {

    @Test
    public void createHasName() {
        final JMenuItem result = MenuItemBuilder.create("foo").build();
        // Assert
        assertThat(result, hasText(equalTo("foo")));
    }

    @Test
    public void createHasListener() {
        final ActionListener listener = mock(ActionListener.class);
        // Act
        final JMenuItem result = MenuItemBuilder.create("foo").addActionListener(listener).build();
        // Assert
        assertThat(result, hasListener(equalTo(listener)));
    }

    private Matcher<? super JMenuItem> hasListener(Matcher<ActionListener> ... matcher) {
        return new FeatureMatcher<JMenuItem, ActionListener[]>(arrayContainingInAnyOrder(matcher), "actionListener", "actionListener") {
            @Override
            protected ActionListener[] featureValueOf(JMenuItem actual) {
                return actual.getActionListeners();
            }
        };
    }

    private Matcher<? super JMenuItem> hasText(Matcher<String> name) {
        return new FeatureMatcher<JMenuItem, String>(name, "name", "name") {
            @Override
            protected String featureValueOf(JMenuItem actual) {
                return actual.getText();
            }
        };
    }

}