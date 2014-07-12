package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchId;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class GraphSwitchTest {

    @Test
    public void afterAppendChild_childHasParent() {
        final GraphSwitch other = new GraphSwitch(SwitchId.create(1));
        final GraphTrackPart testee = new GraphSwitch(SwitchId.create(2));
        // Act
        testee.appendChild(other);
        // Assert
        Optional<GraphTrackPart> parent = other.getParent();
        // Assert
        assertThat(parent, has(sameInstance(testee)));
    }


    @Test
    public void afterAppendChild_parentHasChild() {
        final GraphSwitch other = new GraphSwitch(SwitchId.create(1));
        final GraphTrackPart testee = new GraphSwitch(SwitchId.create(2));
        // Act
        testee.appendChild(other);
        // Assert
        Collection<GraphTrackPart> child = testee.getChilds();
        // Assert
        assertThat(child, contains(other));
    }

    private Matcher<? super Optional<GraphTrackPart>> has(Matcher<GraphTrackPart> graphTrackPartMatcher) {
        return new FeatureMatcher<Optional<GraphTrackPart>, GraphTrackPart>(graphTrackPartMatcher, "get", "get") {
            @Override
            protected GraphTrackPart featureValueOf(Optional<GraphTrackPart> actual) {
                return actual.get();
            }
        };
    }
}
