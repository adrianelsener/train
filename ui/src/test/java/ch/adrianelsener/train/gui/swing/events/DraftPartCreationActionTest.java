package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.Track;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DraftPartCreationActionTest {

    @Test
    public void createTrack() throws Exception {
        final Point endPoint = new Point(7, 14);
        final Optional<Point> startPoint = Optional.of(new Point(12, 20));
        final PointCalculator dummyCalc = mock(PointCalculator.class);
        when(dummyCalc.calculatePoint(endPoint)).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        final DraftPartCreationAction testee = DraftPartCreationAction.createTrack(endPoint);
        // act
        TrackPart result = testee.createDraftPart(startPoint, dummyCalc);
        // assert
        assertThat(result, instanceOf(Track.class));
        assertThat((Track) result, CombinableMatcher.both(startPoint(equalTo(startPoint.get()))).and(endPoint(equalTo(endPoint))));
    }

    @Test
    public void createSwitchTrack() throws Exception {

    }

    @Test
    public void testCreateSwitch() throws Exception {

    }

    @Test
    public void testCreateDummySwitch() throws Exception {

    }

    @Test
    public void testCreateTripleSwitch() throws Exception {

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