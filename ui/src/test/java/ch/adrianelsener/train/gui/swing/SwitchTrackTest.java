package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;
import com.google.common.collect.Collections2;
import org.hamcrest.FeatureMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SwitchTrackTest {
    private final Point startPoint = new Point(30, 30);
    private final Point endPoint = new Point(100, 30);
    @Mock
    private  ToggleCallback toggler;
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
        final Track result = testee.createMirror();
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvString() {
        final Track testee = new SwitchTrack(startPoint, endPoint);
        // Act
        final Track result = SwitchTrack.fromStringIterable(Collections2.transform(testee.getDataToPersist(), input -> input.toString()));
        // Assert
        assertThat(result, is(equalTo(testee)));
    }

    @Test
    public void asCsvStringCanBeUsedForFromCsvStringToggled() {
        final Track testee = new SwitchTrack(startPoint, endPoint).setBoardId("7").setId("4").toggle(toggler).invertView(true);
        // Act
        final Track result = SwitchTrack.fromStringIterable(Collections2.transform(testee.getDataToPersist(), input -> input.toString()));
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

    private FeatureMatcher<SwitchTrack, SwitchId> hasId(SwitchId s) {
        return new FeatureMatcher<SwitchTrack, SwitchId>(equalTo(s), "getId", "getId") {
            @Override
            protected SwitchId featureValueOf(SwitchTrack o) {
                return o.getId();
            }
        };
    }

    private FeatureMatcher<SwitchTrack, BoardId> hasId(BoardId s) {
        return new FeatureMatcher<SwitchTrack, BoardId>(equalTo(s), "getBoardId", "getBoardId") {
            @Override
            protected BoardId featureValueOf(SwitchTrack o) {
                return o.getBoardId();
            }
        };
    }

}
