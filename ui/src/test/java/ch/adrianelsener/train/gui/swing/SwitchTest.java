package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;
import com.google.common.collect.Collections2;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SwitchTest {
    private final Point middle = new Point(30, 30);
    @Mock
    private Graphics2D g;
    @Mock
    private ToggleCallback toggler;
    @Captor
    private ArgumentCaptor<Integer> startXCaptor;
    @Captor
    private ArgumentCaptor<Integer> startYCaptor;
    @Captor
    private ArgumentCaptor<Integer> endXCaptor;
    @Captor
    private ArgumentCaptor<Integer> endYCaptor;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nearIsInside() {
        final Switch testee = Switch.create(middle);
        // Act
        final boolean result = testee.isNear(middle);
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void nearIsOutsideOnTop() {
        final Switch testee = Switch.create(middle);
        // Act
        final boolean result = testee.isNear(new Point(30, 30 - 11));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void nearIsOutsideOnBottom() {
        final Switch testee = Switch.create(middle);
        // Act
        final boolean result = testee.isNear(new Point(30, 30 + 11));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void nearIsOutsideOnLeft() {
        final Switch testee = Switch.create(middle);
        // Act
        final boolean result = testee.isNear(new Point(30 - 11, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void nearIsOutsideOnRight() {
        final Switch testee = Switch.create(middle);
        // Act
        final boolean result = testee.isNear(new Point(30 + 16, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void getNextConnectionpoint_Left() {
        final Switch testee = Switch.create(middle);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(27, 30));
        // Assert
        assertThat(result, is(equalTo(new Point(20, 30))));
    }

    @Test
    public void getNextConnectionpoint_RightTop() {
        final Switch testee = Switch.create(middle);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(32, 32));
        // Assert
        assertThat(result, is(equalTo(new Point(45, 40))));

    }

    @Test
    public void getNextConnectionpoint_RightBottom() {
        // new Point(30, 30)
        final Switch testee = Switch.create(middle);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(32, 28));
        // Assert
        assertThat(result, is(equalTo(new Point(45, 20))));
    }

    @Test
    public void createMirrorReturnsMirrored() {
        final Switch testee = Switch.create(middle);
        // Act
        final Switch result = testee.createMirror();
        // Assert
        final Switch mirrored = new Switch(middle, 45, SwitchId.createDummy(), BoardId.createDummy(), false, Switch.SwitchMode.Real, TrackView.Default);
        assertThat(result, is(equalTo(mirrored)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvString() {
        final Switch testee = Switch.create(middle).toggle(toggler).invertView(true);
        // Act
        final Collection<String> strings = Collections2.transform(testee.getDataToPersist(), input -> input.toString());
        final Iterator<String> iterator = strings.iterator();
        iterator.next();
        final Switch result = Switch.createSwitch(iterator);
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void regularDrawing() {
        final Switch testee = Switch.create(middle);
        testee.paint(g);
        // Assert
        verify(g, times(4)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());
        assertThat(startXCaptor.getAllValues().get(3), is(equalTo(startXCaptor.getAllValues().get(2))));
        assertThat(startYCaptor.getAllValues().get(3), is(equalTo(startYCaptor.getAllValues().get(2))));
        assertThat(endXCaptor.getAllValues().get(3), is(equalTo(endXCaptor.getAllValues().get(2))));
        assertThat(endYCaptor.getAllValues().get(3), is(equalTo(endYCaptor.getAllValues().get(2))));
    }

    @Test
    public void toggledDrawing() {
        final Switch testee = Switch.create(middle).toggle(toggler);
        testee.paint(g);
        // Assert
        verify(g, times(4)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());
        assertThat(startXCaptor.getAllValues().get(3), is(equalTo(startXCaptor.getAllValues().get(1))));
        assertThat(startYCaptor.getAllValues().get(3), is(equalTo(startYCaptor.getAllValues().get(1))));
        assertThat(endXCaptor.getAllValues().get(3), is(equalTo(endXCaptor.getAllValues().get(1))));
        assertThat(endYCaptor.getAllValues().get(3), is(equalTo(endYCaptor.getAllValues().get(1))));
    }

    @Test
    public void testInvertedView() {
        final Switch testee = Switch.create(middle).invertView(true);
        testee.paint(g);
        // Assert
        verify(g, times(4)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());
        assertThat(startXCaptor.getAllValues().get(3), is(equalTo(startXCaptor.getAllValues().get(1))));
        assertThat(startYCaptor.getAllValues().get(3), is(equalTo(startYCaptor.getAllValues().get(1))));
        assertThat(endXCaptor.getAllValues().get(3), is(equalTo(endXCaptor.getAllValues().get(1))));
        assertThat(endYCaptor.getAllValues().get(3), is(equalTo(endYCaptor.getAllValues().get(1))));
    }
}
