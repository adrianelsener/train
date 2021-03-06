package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchCallback;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import org.testng.annotations.Test;

import java.awt.*;

import static ch.adrianelsener.train.gui.swing.common.PointMatchers.hasX;
import static ch.adrianelsener.train.gui.swing.common.PointMatchers.hasY;
import static ch.adrianelsener.train.gui.swing.model.TrackMatchers.hasEnd;
import static ch.adrianelsener.train.gui.swing.model.TrackMatchers.hasStart;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

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
        final Track result = testee.rotate();
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvString() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final Track result = TrackFactory.instance().fromStringIterable(Collections2.transform(testee.getDataToPersist(), Object::toString));
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void inConnectorsHasStartAndEndPoint() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final ImmutableCollection<Point> result = testee.getInConnectors();
        // Assert
        assertThat(result, containsInAnyOrder(equalTo(startPoint), equalTo(endPoint)));
    }
    @Test
    public void outConnectorsHasStartAndEndPoint() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        // Act
        final ImmutableCollection<Point> result = testee.getOutConnectors();
        // Assert
        assertThat(result, containsInAnyOrder(equalTo(startPoint), equalTo(endPoint)));
    }

    @Test
    public void toggle_doesNothingOnCallback() {
        final SimpleTrack testee = new SimpleTrack(startPoint, endPoint);
        final SwitchCallback callback = mock(SwitchCallback.class);
        // act
        testee.toggle(callback);
        // assert
        verifyZeroInteractions(callback);
    }

    @Test
    public void toggle_returnsSameObject() {
        final SimpleTrack testee = new SimpleTrack(startPoint, endPoint);
        final SwitchCallback callback = mock(SwitchCallback.class);
        // act
        final SimpleTrack result = testee.toggle(callback);
        // assert
        assertThat(result, is(sameInstance(testee)));
    }

    @Test
    public void applyState_doesNothingOnCallbac() {
        final Track testee = new SimpleTrack(startPoint, endPoint);
        final SwitchCallback callback = mock(SwitchCallback.class);
        // act
        testee.applyState(callback);
        // assert
        verifyZeroInteractions(callback);
    }

    @Test
    public void move_xAndY_startAndEndpointMoved() {
        final SimpleTrack testee = new SimpleTrack(startPoint, endPoint);
        final Point direction = new Point(7, 9);
        // act
        final SimpleTrack result = testee.move(direction);
        // assert
        assertThat(result,
                both(hasStart(both(hasX(equalTo(startPoint.x + 7)))
                        .and(hasY(equalTo(startPoint.y + 9)))))
                        .and(hasEnd(both(hasX(equalTo(endPoint.x + 7)))
                                .and(hasY(equalTo(endPoint.y + 9))))));
    }
}
