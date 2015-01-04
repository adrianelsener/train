package ch.adrianelsener.train.gui.swing.model;

import org.testng.annotations.Test;

import java.awt.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

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
