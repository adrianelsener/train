package ch.adrianelsener.train.gui.swing;

import com.google.common.collect.Collections2;
import org.testng.annotations.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleTrackTest {
    private final Point startPoint = new Point(30, 30);
    private final Point endPoint = new Point(100, 30);

    @Test
    public void pointIsNearby() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 30));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnTop() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 19));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnTop() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 25));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnBottom() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 41));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnBottom() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 35));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnRight() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(106, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnRight() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(99, 30));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnLeft() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(23, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnLeft() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(31, 30));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void getNextConnectionPointLeft() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(40, 30));
        // Assert
        assertThat(result, is(equalTo(startPoint)));
    }

    @Test
    public void getNextConnectionPointRight() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(96, 30));
        // Assert
        assertThat(result, is(equalTo(endPoint)));
    }

    @Test
    public void drawCorrect() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        final Graphics2D g = mock(Graphics2D.class);
        // Act
        testee.paint(g);
        // Assert
        verify(g).drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        verify(g).setColor(Color.blue);
    }

    @Test
    public void createMirrorReturnsItself() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final Track result = testee.createMirror();
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvString() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final Track result = Track.fromStringIterable(Collections2.transform(testee.getDataToPersist(), input -> input.toString()));
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

}
