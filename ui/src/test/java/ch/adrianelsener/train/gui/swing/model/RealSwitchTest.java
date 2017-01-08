package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.testing.RulesForTestNg;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.TrackView;
import com.google.common.collect.Collections2;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

import static ch.adrianelsener.train.gui.swing.model.SwitchMatchers.hasX;
import static ch.adrianelsener.train.gui.swing.model.SwitchMatchers.hasY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RealSwitchTest extends RulesForTestNg {
    private final Point middle = new Point(30, 30);
    @Mock
    private Graphics2D g;
    @Mock
    private SwitchCallback toggler;
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
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final boolean result = testee.isNear(middle);
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void nearIsOutsideOnTop() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final boolean result = testee.isNear(new Point(30, 30 - 11));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void nearIsOutsideOnBottom() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final boolean result = testee.isNear(new Point(30, 30 + 11));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void nearIsOutsideOnLeft() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final boolean result = testee.isNear(new Point(30 - 11, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void nearIsOutsideOnRight() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final boolean result = testee.isNear(new Point(30 + 16, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void getNextConnectionpoint_Left() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(27, 30));
        // Assert
        assertThat(result, is(equalTo(new Point(20, 30))));
    }

    @Test
    public void getNextConnectionpoint_RightTop() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(36, 32));
        // Assert
        assertThat(result, is(equalTo(new Point(45, 40))));
    }

    @Test
    public void getNextConnectionpoint_RightBottom() {
        // new Point(30, 30)
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(36, 28));
        // Assert
        assertThat(result, is(equalTo(new Point(45, 20))));
    }

    @Test
    public void createMirrorReturnsMirrored() {
        final SwingSwitch testee = new RealSwitch(middle);
        // Act
        final SwingSwitch result = testee.rotate();
        // Assert
        final SwingSwitch mirrored = new RealSwitch(middle, 45, SwitchId.createDummy(), BoardId.createDummy(), false, TrackView.Default);
        assertThat(result, is(equalTo(mirrored)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvString() {
        final RealSwitch testee = new RealSwitch(middle).toggle(toggler).invertView(true);
        // Act
        final Collection<String> strings = Collections2.transform(testee.getDataToPersist(), input -> input.toString());
        final Iterator<String> iterator = strings.iterator();
        iterator.next();
        final RealSwitch result = TrackFactory.instance().create(iterator);
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void regularDrawing() {
        final SwingSwitch testee = new RealSwitch(middle);
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
        final SwingSwitch testee = new RealSwitch(middle).toggle(toggler);
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
        final SwingSwitch testee = new RealSwitch(middle).invertView(true);
        testee.paint(g);
        // Assert
        verify(g, times(4)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());
        assertThat(startXCaptor.getAllValues().get(3), is(equalTo(startXCaptor.getAllValues().get(1))));
        assertThat(startYCaptor.getAllValues().get(3), is(equalTo(startYCaptor.getAllValues().get(1))));
        assertThat(endXCaptor.getAllValues().get(3), is(equalTo(endXCaptor.getAllValues().get(1))));
        assertThat(endYCaptor.getAllValues().get(3), is(equalTo(endYCaptor.getAllValues().get(1))));
    }

    @Test
    public void move_direction_newCenterIsMoved() {
        final RealSwitch testee = new RealSwitch(middle);
        final Point distance = new Point(3, 5);
        // act
        final SwingSwitch result = testee.move(distance);
        // assert
        assertThat(result, both(hasX(equalTo(middle.x + 3)))
                .and(hasY(equalTo(middle.y + 5))));
    }

}
