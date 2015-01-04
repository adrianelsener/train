package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DraftPartCreationActionTest {
    private Point endPoint;
    private Optional<Point> startPoint;
    private PointCalculator dummyCalc;

    @BeforeMethod
    public void init() {
        endPoint = new Point(7, 14);
        startPoint = Optional.of(new Point(12, 20));
        dummyCalc = mock(PointCalculator.class);
        when(dummyCalc.calculatePoint(endPoint)).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    public void createTrack() throws Exception {
        final DraftPartCreationAction testee = DraftPartCreationAction.createTrack(endPoint);
        // act
        TrackPart result = testee.createDraftPart(startPoint, dummyCalc);
        // assert
        assertThat(result, instanceOf(Track.class));
        assertThat((Track) result, CombinableMatcher.both(startPoint(equalTo(startPoint.get()))).and(endPoint(equalTo(endPoint))));
    }

    @Test
    public void createSwitchTrack() throws Exception {
        final DraftPartCreationAction testee = DraftPartCreationAction.createSwitchTrack(endPoint);
        // act
        final TrackPart result = testee.createDraftPart(startPoint, dummyCalc);
        // assert
        assertThat(result, instanceOf(SwitchTrack.class));
        assertThat((SwitchTrack) result, CombinableMatcher.both(startPoint(equalTo(startPoint.get()))).and(endPoint(equalTo(endPoint))));
    }

    @Test
    public void createSwitch() throws Exception {
        final DraftPartCreationAction testee = DraftPartCreationAction.createSwitch(endPoint);
        // act
        final TrackPart result = testee.createDraftPart(startPoint, dummyCalc);
        // assert
        assertThat(result, instanceOf(RealSwitch.class));
    }

    @Test
    public void createDummySwitch() throws Exception {
        final DraftPartCreationAction testee = DraftPartCreationAction.createDummySwitch(endPoint);
        // act
        final TrackPart result = testee.createDraftPart(startPoint, dummyCalc);
        // assert
        assertThat(result, instanceOf(DummySwitch.class));
    }

    @Test
    public void testCreateTripleSwitch() throws Exception {
        final DraftPartCreationAction testee = DraftPartCreationAction.createTripleSwitch(endPoint);
        // act
        final TrackPart result = testee.createDraftPart(startPoint, dummyCalc);
        // assert
        assertThat(result, instanceOf(TripleSwitch.class));
    }

    private Matcher<? super Track> endPoint(Matcher<Point> matcher) {
        return new FeatureMatcher<Track, Point>(matcher, "getEndPoint", "getEndPoint") {

            @Override
            protected Point featureValueOf(Track actual) {
                return actual.getEnd();
            }
        };
    }

    private Matcher<? super Track> startPoint(Matcher<Point> matcher) {
        return new FeatureMatcher<Track, Point>(matcher, "getStartPoint", "getStartPoint") {

            @Override
            protected Point featureValueOf(Track actual) {
                return actual.getStart();
            }
        };
    }

}