package ch.adrianelsener.train.gui.swing;

import com.google.common.eventbus.EventBus;
import org.hamcrest.MatcherAssert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.awt.event.ActionListener;

import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.mockito.Mockito.verify;

public class MainPopupMenuTest {
    @Test
    public void containsAllEntriesOfDrawMode() {
        final MainPopupMenu testee = new MainPopupMenu();
        // Act
        MatcherAssert.assertThat(testee.getComponents(), arrayWithSize(DrawMode.values().length + 1));
    }

    @Test
    public void action_callsBus() {
        final EventBus bus = Mockito.mock(EventBus.class);
        final MainPopupMenu testee = new MainPopupMenu();
        testee.setBus(bus);
        // Act
        testee.getComponents()[0].getListeners(ActionListener.class)[0].actionPerformed(null);
        // Assert
        ArgumentCaptor<DrawMode> captor = ArgumentCaptor.forClass(DrawMode.class);
        verify(bus).post(captor.capture());
    }
}