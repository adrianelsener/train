package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchCallback;
import com.google.common.collect.Sets;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TripleSwitchTest {
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
    public void regularDrawing() {
        final TripleSwitch testee = new TripleSwitch(middle);
        testee.paint(g);
        // Assert
        verify(g, times(4)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());

        final Set<Integer> startXSet = Sets.newHashSet(startXCaptor.getAllValues());
        assertThat(startXSet, containsInAnyOrder(middle.x - 10, middle.x));

        final Set<Integer> endXSet = Sets.newHashSet(endXCaptor.getAllValues());
        assertThat(endXSet, containsInAnyOrder(middle.x, middle.x + 15));

        final Set<Integer> startYSet = Sets.newHashSet(startYCaptor.getAllValues());
        assertThat(startYSet, contains(middle.y));

        final Set<Integer> endYSet = Sets.newHashSet(endYCaptor.getAllValues());
        assertThat(endYSet, containsInAnyOrder(middle.y, middle.y - 10, middle.y + 10));
    }
}