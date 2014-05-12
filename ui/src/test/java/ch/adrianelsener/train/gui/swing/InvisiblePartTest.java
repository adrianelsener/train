package ch.adrianelsener.train.gui.swing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.awt.Graphics2D;

import org.testng.annotations.Test;

public class InvisiblePartTest {

    @Test
    public void noopOnPaint() {
        final InvisiblePart testee = InvisiblePart.create();
        final Graphics2D g = mock(Graphics2D.class);
        // Act
        testee.paint(g);
        // Assert
        verifyZeroInteractions(g);
    }
}
