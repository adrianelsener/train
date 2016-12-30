package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import com.google.common.collect.Collections2;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.core.CombinableMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Collection;

import static ch.adrianelsener.train.gui.swing.common.PointMatchers.hasX;
import static ch.adrianelsener.train.gui.swing.common.PointMatchers.hasY;
import static ch.adrianelsener.train.gui.swing.model.TrackMatchers.hasEnd;
import static ch.adrianelsener.train.gui.swing.model.TrackMatchers.hasStart;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SwitchTrackTest {
    private final Point startPoint = new Point(30, 30);
    private final Point endPoint = new Point(100, 30);
    @Mock
    private SwitchCallback toggler;
    @Mock
    private  Graphics2D g;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void pointIsNearby() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 30));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnTop() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 19));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnTop() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 25));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnBottom() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 41));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnBottom() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(60, 35));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnRight() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(106, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnRight() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(99, 30));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void pointIsOutsideOnLeft() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(24, 30));
        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void pointIsInsideOnLeft() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final boolean result = testee.isNear(new Point(31, 30));
        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void getNextConnectionPointLeft() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(40, 30));
        // Assert
        assertThat(result, is(equalTo(startPoint)));
    }

    @Test
    public void getNextConnectionPointRight() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final Point result = testee.getNextConnectionpoint(new Point(96, 30));
        // Assert
        assertThat(result, is(equalTo(endPoint)));
    }

    @Test
    public void drawCorrectDisabled() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        testee.paint(g);
        // Assert
        verify(g).drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        verify(g).setColor(Color.blue);
    }

    @Test
    public void drawCorrectEnabled() {
        final Graphics2D g = mock(Graphics2D.class);

        final Track testee = new SwitchTrack(startPoint, endPoint).toggle(toggler);
        // Act
        testee.paint(g);
        // Assert
        verify(g).drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        verify(g).setColor(Color.red);
    }

    @Test
    public void createMirrorReturnsItself() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final Track result = testee.rotate();
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvString() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final Track result = SwitchTrack.fromStringIterable(Collections2.transform(testee.getDataToPersist(), Object::toString));
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvStringToggled() {
        final Track testee = new SwitchTrack(startPoint, endPoint).setBoardId("7").setId("4").toggle(toggler).invertView(true);
        // Act
        final Track result = SwitchTrack.fromStringIterable(Collections2.transform(testee.getDataToPersist(), Object::toString));
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void setSwitchIdReturnsTrackWithSwitchId() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint);
        final SwitchTrack result = testee.setId("1");
        assertThat(result, hasId(SwitchId.fromValue("1")));
    }

    @Test
    public void setBoardIdReturnsTrackWithBoardId() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint);
        final SwitchTrack result = testee.setBoardId("1");
        assertThat(result, hasId(BoardId.fromValue("1")));
    }

    @Test
    public void invertView() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint).invertView(true);
        testee.paint(g);
        // Assert
        verify(g).drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        verify(g).setColor(Color.red);
        verify(g).setColor(Color.black);

    }

    @Test
    public void toggle_invokesCallbackCorrectly() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint).setBoardId("24").setId("42");
        // act
        testee.toggle(toggler);
        // assert
        verify(toggler).toggleSwitch(SwitchId.create(42), BoardId.create(24), false);
    }

    @Test
    public void toggle_returnsInvertedState() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint).setBoardId("24").setId("42");
        // act
        final SwitchTrack result = testee.toggle(toggler);
        // assert
        assertThat(result, CombinableMatcher.both(hasId(BoardId.create(24))).and(hasId(SwitchId.create(42))).and(hasState(TrackState.On)));
    }

    @Test
    public void applyState_invokesCallbackCorrectly() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint).setBoardId("42").setId("24");
        // Act
        testee.applyState(toggler);
        // assert
        verify(toggler).toggleSwitch(SwitchId.create(24), BoardId.create(42), true);
    }

    @Test
    public void move_xAndY_startAndEndpointMoved() {
        final SwitchTrack testee = new SwitchTrack(startPoint, endPoint);
        final Point direction = new Point(7, 9);
        // act
        final SwitchTrack result = testee.move(direction);
        // assert
        assertThat(result,
                CombinableMatcher.<SwitchTrack>both(hasStart(CombinableMatcher.both(hasX(equalTo(startPoint.x + 7))).and(hasY(equalTo(startPoint.y + 9)))))
                        .and(hasEnd(CombinableMatcher.both(hasX(equalTo(endPoint.x + 7))).and(hasY(equalTo(endPoint.y + 9))))));
    }

    private FeatureMatcher<SwitchTrack, TrackState> hasState(TrackState s) {
        return new FeatureMatcher<SwitchTrack, TrackState>(equalTo(s), "trackState", "trackState") {
            @Override
            protected TrackState featureValueOf(SwitchTrack o) {
                return o.getTrackState();
            }
        };
    }
    private FeatureMatcher<SwitchTrack, Collection<SwitchId>> hasId(SwitchId s) {
        return new FeatureMatcher<SwitchTrack, Collection<SwitchId>>(containsInAnyOrder(s), "getId", "getId") {
            @Override
            protected Collection<SwitchId> featureValueOf(SwitchTrack o) {
                return o.getId();
            }
        };
    }

    private FeatureMatcher<SwitchTrack, Collection<BoardId>> hasId(BoardId s) {
        return new FeatureMatcher<SwitchTrack, Collection<BoardId>>(contains(s), "getBoardId", "getBoardId") {
            @Override
            protected Collection<BoardId> featureValueOf(SwitchTrack o) {
                return o.getBoardId();
            }
        };
    }

}
