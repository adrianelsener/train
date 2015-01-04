package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchCallback;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.awt.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.*;

public class TrackPartsTest {

    @Test
    public void containsNotAfterInitialization() {
        final TrackParts testee = new TrackParts();
        final TrackPart part = createMockTrackPart();
        // Act
        final boolean result = testee.contains(part);
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void containsAfterAdd() {
        final TrackParts testee = new TrackParts();
        final TrackPart part = createMockTrackPart();
        // Act
        testee.add(part);
        final boolean result = testee.contains(part);
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void containsNotAfterAddIfOtherObject() {
        final TrackParts testee = new TrackParts();
        final TrackPart part = createMockTrackPart();
        final TrackPart other = createMockTrackPart();
        // Act
        testee.add(part);
        final boolean result = testee.contains(other);
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void paintAllObjects() {
        final TrackPart part = createMockTrackPart();
        final TrackParts testee = new TrackParts();
        final Graphics2D g = mock(Graphics2D.class);
        testee.add(part);
        // Act
        testee.paintAll(g);
        // Assert
        verify(part).paint(g);
    }

    @Test
    public void getNextConnectionCallsGetNextConnectionOnPart() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part2.isNear(Mockito.any(Point.class))).thenReturn(true);
        final Point resPoint = new Point(6, 6);
        when(part2.getNextConnectionpoint(Mockito.any(Point.class))).thenReturn(resPoint);
        testee.add(part1);
        testee.add(part2);
        final Point p = new Point(3, 3);
        // Act
        testee.getNextConnectionPoint(p);
        // Assert
        verify(part1).isNear(p);
        verify(part2).isNear(p);
    }

    @Test
    public void getNextConnectionHasTheOneWithTrue() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part2.isNear(Mockito.any(Point.class))).thenReturn(true);
        final Point resPoint = new Point(6, 6);
        when(part2.getNextConnectionpoint(Mockito.any(Point.class))).thenReturn(resPoint);
        testee.add(part1);
        testee.add(part2);
        final Point p = new Point(3, 3);
        // Act
        final Point result = testee.getNextConnectionPoint(p);
        // Assert
        assertThat(result, is(equalTo(resPoint)));
    }

    @Test
    public void mirrorNextToRotateFound() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part2.isNear(Mockito.any(Point.class))).thenReturn(true);
        when(part2.rotate()).thenReturn(part1);
        testee.add(part1);
        testee.add(part2);
        final Point resPoint = new Point(6, 6);
        // Act
        testee.mirrorNextTo(resPoint);
        // Assert
        verify(part2).rotate();
    }

    @Test
    public void mirrorNextToRotateFoundContainedInTrackParts() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part2.isNear(Mockito.any(Point.class))).thenReturn(true);
        when(part2.rotate()).thenReturn(part1);
        testee.add(part2);
        final Point resPoint = new Point(6, 6);
        // Act
        testee.mirrorNextTo(resPoint);
        // Assert
        assertThat(testee, hasItem(part1));
    }

    @Test
    public void mirrorNextToNothingFound() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        testee.add(part1);
        testee.add(part2);

    }

    @Test
    public void clearCleansAll() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        testee.add(part1);
        // Act
        testee.clear();
        // Assert
        assertThat(testee.contains(part1), is(not(true)));
    }

    @Test
    public void addAllContainsAll() {
        final TrackParts testee = new TrackParts();
        final List<TrackPart> toAdd = Lists.newArrayList(createMockTrackPart(), createMockTrackPart());
        // Act
        testee.addAll(toAdd);
        // Assert
        assertThat(testee.contains(toAdd.get(0)), is(true));
        assertThat(testee.contains(toAdd.get(1)), is(true));
    }

    @Test
    public void toggleNextToToggles() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part2.isNear(Mockito.any(Point.class))).thenReturn(true);
        when(part2.rotate()).thenReturn(part1);
        testee.add(part1);
        testee.add(part2);
        final Point resPoint = new Point(6, 6);
        final SwitchCallback toggler = mock(SwitchCallback.class);
        // Act
        testee.toggleNextTo(resPoint, toggler);
        // Assert
        verify(part2).toggle(toggler);
        verifyZeroInteractions(toggler);
    }

    @Test
    public void deleteNextToRotateFoundContainedInTrackParts() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part1.isNear(Mockito.any(Point.class))).thenReturn(true);
        testee.add(part1);
        testee.add(part2);
        final Point resPoint = new Point(6, 6);
        // Act
        testee.delete(resPoint);
        // Assert
        assertThat(testee, not(hasItem(part1)));
        assertThat(testee, hasItem(part2));
    }

    @Test
    public void deleteNextToNothingFound() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        testee.add(part1);
        testee.add(part2);

        final Point resPoint = new Point(6, 6);
        // act
        testee.delete(resPoint);
        // Assert
        assertThat(testee, hasItem(part1));
        assertThat(testee, hasItem(part2));

    }

    @Test
    public void moveNextToFoundContainedInTrackParts() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        when(part1.moveTo(Mockito.any(Point.class))).thenReturn(part2);
        when(part1.isNear(Mockito.any(Point.class))).thenReturn(true);
        testee.add(part1);
        final Point fromPoint = new Point(6, 6);
        final Point toPoint = new Point(16, 16);
        // act
        testee.move(fromPoint, toPoint);
        // Assert
        assertThat(testee, not(hasItem(part1)));
        assertThat(testee, hasItem(part2));
    }

    @Test
    public void moveNextToNothingFound() {
        final TrackParts testee = new TrackParts();
        final TrackPart part1 = createMockTrackPart();
        final TrackPart part2 = createMockTrackPart();
        testee.add(part1);

        final Point fromPoint = new Point(6, 6);
        final Point toPoint = new Point(16, 16);
        // act
        testee.move(fromPoint, toPoint);
        // Assert
        assertThat(testee, hasItem(part1));
        assertThat(testee, not(hasItem(part2)));

    }

    private TrackPart createMockTrackPart() {
        return mock(TrackPart.class);
    }

}
