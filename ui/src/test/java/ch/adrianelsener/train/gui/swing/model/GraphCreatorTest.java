package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchId;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

public class GraphCreatorTest {
    private final GraphCreator testee = new GraphCreator();

    @Test
    public void switchWithSameEndpointsDefineA_graph() {
        final Point sharedPoint = mock(Point.class);
        final SwingSwitch swingSwitchParent = mock(SwingSwitch.class);
        final SwingSwitch swingSwitchChild = mock(SwingSwitch.class);
        when(swingSwitchParent.getId()).thenReturn(Lists.newArrayList(SwitchId.create(7)));
        when(swingSwitchChild.getId()).thenReturn(Lists.newArrayList(SwitchId.create(8)));
        when(swingSwitchParent.getOutConnectors()).thenReturn(ImmutableList.of(sharedPoint));
        when(swingSwitchParent.getInConnectors()).thenReturn(ImmutableList.of());
        when(swingSwitchChild.getInConnectors()).thenReturn(ImmutableList.of(sharedPoint));
        when(swingSwitchChild.getOutConnectors()).thenReturn(ImmutableList.of());
        final ImmutableCollection<TrackPart> parts = ImmutableList.of(swingSwitchChild, swingSwitchParent);
        // Act
        final ImmutableCollection<GraphTrackPart> result = testee.graphify(parts);
        // Assert
        assertThat(result, containsInAnyOrder(parentContainingChild(contains(withId(equalTo(swingSwitchChild.getId().iterator().next())))), childWithParent(withParent(equalTo(swingSwitchParent.getId().iterator().next())))));
    }

    @Test
    public void switchWithSameEndpointsDefineA_graph_multpileChild() {
        final Point sharedPointA = mock(Point.class);
        final Point sharedPointB = mock(Point.class);
        final SwingSwitch swingSwitchParent = mock(SwingSwitch.class);
        final SwingSwitch swingSwitchChildA = mock(SwingSwitch.class);
        final SwingSwitch swingSwitchChildB = mock(SwingSwitch.class);
        when(swingSwitchParent.getId()).thenReturn(Lists.newArrayList(SwitchId.create(7)));
        when(swingSwitchChildA.getId()).thenReturn(Lists.newArrayList(SwitchId.create(8)));
        when(swingSwitchChildB.getId()).thenReturn(Lists.newArrayList(SwitchId.create(9)));
        when(swingSwitchParent.getOutConnectors()).thenReturn(ImmutableList.of(sharedPointA, sharedPointB));
        when(swingSwitchParent.getInConnectors()).thenReturn(ImmutableList.of());
        when(swingSwitchChildA.getInConnectors()).thenReturn(ImmutableList.of(sharedPointA));
        when(swingSwitchChildA.getOutConnectors()).thenReturn(ImmutableList.of());
        when(swingSwitchChildB.getInConnectors()).thenReturn(ImmutableList.of(sharedPointB));
        when(swingSwitchChildB.getOutConnectors()).thenReturn(ImmutableList.of());
        final ImmutableCollection<TrackPart> parts = ImmutableList.of(swingSwitchChildA, swingSwitchChildB, swingSwitchParent);
        // Act
        final ImmutableCollection<GraphTrackPart> result = testee.graphify(parts);
        // Assert
        assertThat(result, containsInAnyOrder(parentContainingChild(containsInAnyOrder(withId(equalTo(SwitchId.create(8))), withId(equalTo(SwitchId.create(9))))), childWithParent(withParent(equalTo(SwitchId.create(7)))), childWithParent(withParent(equalTo(SwitchId.create(7))))));
    }

    @Test
    public void switchWithLineDefinesA_graph() {
        final Point sharedPointA = mock(Point.class, "PointA");
        final Point sharedPointB = mock(Point.class, "PointB");
        final SwingSwitch swingSwitchParent = mock(SwingSwitch.class, "Parent");
        final SwingSwitch swingSwitchChild = mock(SwingSwitch.class, "Child");
        final Track track = mock(Track.class, "Pipetrack");
        when(swingSwitchParent.getId()).thenReturn(Lists.newArrayList(SwitchId.create(7)));
        when(swingSwitchChild.getId()).thenReturn(Lists.newArrayList(SwitchId.create(8)));
        when(swingSwitchParent.getOutConnectors()).thenReturn(ImmutableList.of(sharedPointA));
        when(swingSwitchParent.getInConnectors()).thenReturn(ImmutableList.of());
        when(track.getInConnectors()).thenReturn(ImmutableList.of(sharedPointA, sharedPointB));
        when(track.getOutConnectors()).thenReturn(ImmutableList.of(sharedPointB, sharedPointA));
        when(track.isPipe()).thenReturn(true);
        when(swingSwitchChild.getInConnectors()).thenReturn(ImmutableList.of(sharedPointB));
        when(swingSwitchChild.getOutConnectors()).thenReturn(ImmutableList.of());
        final ImmutableCollection<TrackPart> parts = ImmutableList.of(swingSwitchChild, swingSwitchParent, track);
        // Act
        final ImmutableCollection<GraphTrackPart> result = testee.graphify(parts);
        // Assert
        assertThat(result, containsInAnyOrder(parentContainingChild(contains(withId(equalTo(SwitchId.create(8))))), childWithParent(withParent(equalTo(SwitchId.create(7))))));
    }

    @Test
    public void switchWithTwoLinesDefinesOneGraph() {
        final Point sharedPointA = mock(Point.class, "PointA");
        final Point sharedPointB = mock(Point.class, "PointB");
        final Point sharedPointC = mock(Point.class, "PointC");
        final SwingSwitch swingSwitchParent = mock(SwingSwitch.class, "Parent");
        final SwingSwitch swingSwitchChild = mock(SwingSwitch.class, "Child");
        final Track trackA = mock(Track.class, "PipetrackA");
        final Track trackB = mock(Track.class, "PipetrackB");
        when(swingSwitchParent.getId()).thenReturn(Lists.newArrayList(SwitchId.create(7)));
        when(swingSwitchChild.getId()).thenReturn(Lists.newArrayList(SwitchId.create(8)));
        when(swingSwitchParent.getOutConnectors()).thenReturn(ImmutableList.of(sharedPointA));
        when(swingSwitchParent.getInConnectors()).thenReturn(ImmutableList.of());
        when(trackA.getInConnectors()).thenReturn(ImmutableList.of(sharedPointA, sharedPointB));
        when(trackA.getOutConnectors()).thenReturn(ImmutableList.of(sharedPointB, sharedPointA));
        when(trackA.isPipe()).thenReturn(true);
        when(trackB.getInConnectors()).thenReturn(ImmutableList.of(sharedPointC, sharedPointB));
        when(trackB.getOutConnectors()).thenReturn(ImmutableList.of(sharedPointB, sharedPointC));
        when(trackB.isPipe()).thenReturn(true);
        when(swingSwitchChild.getInConnectors()).thenReturn(ImmutableList.of(sharedPointC));
        when(swingSwitchChild.getOutConnectors()).thenReturn(ImmutableList.of());
        final ImmutableCollection<TrackPart> parts = ImmutableList.of(swingSwitchChild, swingSwitchParent, trackA, trackB);
        // Act
        final ImmutableCollection<GraphTrackPart> result = testee.graphify(parts);
        // Assert
        assertThat(result, containsInAnyOrder(parentContainingChild(contains(withId(equalTo(SwitchId.create(8))))), childWithParent(withParent(equalTo(SwitchId.create(7))))));

    }

    @Test(enabled = false)
    public void multiSwitch() {
        fail("create test");
    }

    private static Matcher<? super GraphTrackPart> withId(Matcher<SwitchId> matcher) {
        return new FeatureMatcher<GraphTrackPart, SwitchId>(matcher, "getId", "getId") {
            @Override
            protected SwitchId featureValueOf(GraphTrackPart actual) {
                return actual.getId();
            }
        };
    }

    private static Matcher<GraphTrackPart> childWithParent(Matcher<? super Optional<GraphTrackPart>> hasParent) {
        return new FeatureMatcher<GraphTrackPart, Optional<GraphTrackPart>>(hasParent, "evaluate there is a parent", "evaluate there is a parent") {
            @Override
            protected Optional<GraphTrackPart> featureValueOf(GraphTrackPart actual) {
                return actual.getParent();
            }
        };
    }

    private static Matcher<GraphTrackPart> parentContainingChild(Matcher<Iterable<? extends GraphTrackPart>> iterableMatcher) {
        return new FeatureMatcher<GraphTrackPart, Collection<GraphTrackPart>>(iterableMatcher, "evaluate there is a child", "evaluate there is a child") {
            @Override
            protected Collection<GraphTrackPart> featureValueOf(GraphTrackPart actual) {
                return actual.getChilds();
            }
        };
    }


    private static Matcher<? super Optional<GraphTrackPart>> withParent(Matcher<SwitchId> switchIdMatcher) {
        return new FeatureMatcher<Optional<GraphTrackPart>, SwitchId>(switchIdMatcher, "getIdFromOptional", "getIdFromOptiona") {
            @Override
            protected SwitchId featureValueOf(Optional<GraphTrackPart> actual) {
                return actual.get().getId();
            }
        };
    }

}