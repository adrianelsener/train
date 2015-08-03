package ch.adrianelsener.train.gui.swing.model;

import org.testng.annotations.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    public void move_noop_returnsSame() {
        final InvisiblePart testee = InvisiblePart.create();
        // act
        final InvisiblePart result = testee.move(new Point(7, 2));
        // assert
        assertThat(result, is(testee));
    }
}
