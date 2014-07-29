package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

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
    @Mock
    private SwitchCallback switchCallback;
    @Captor
    private ArgumentCaptor<SwitchId> switchIdCaptor;
    @Captor
    private ArgumentCaptor<BoardId> boardIdCaptor;
    @Captor
    private ArgumentCaptor<Boolean> stateCaptor;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void regularDrawing() {
        final TripleSwitch testee = new TripleSwitch(middle);
        testee.paint(g);
        // Assert
        InOrder inOrder = inOrder(g);
        inOrder.verify(g).setColor(Color.BLUE);
        inOrder.verify(g, times(4)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());
        inOrder.verify(g).setColor(Color.RED);
        inOrder.verify(g, times(2)).drawLine(startXCaptor.capture(), startYCaptor.capture(), endXCaptor.capture(), endYCaptor.capture());

        final Set<Integer> startXSet = Sets.newHashSet(startXCaptor.getAllValues());
        assertThat(startXSet, containsInAnyOrder(middle.x - 10, middle.x));

        final Set<Integer> endXSet = Sets.newHashSet(endXCaptor.getAllValues());
        assertThat(endXSet, containsInAnyOrder(middle.x, middle.x + 15));

        final Set<Integer> startYSet = Sets.newHashSet(startYCaptor.getAllValues());
        assertThat(startYSet, contains(middle.y));

        final Set<Integer> endYSet = Sets.newHashSet(endYCaptor.getAllValues());
        assertThat(endYSet, containsInAnyOrder(middle.y, middle.y - 10, middle.y + 10));
    }

    @Test
    public void twoWaysAreRed() {
        final TripleSwitch testee = new TripleSwitch(middle);
        testee.paint(g);
        // Assert
        InOrder inOrder = inOrder(g);
        inOrder.verify(g).setColor(Color.BLUE);
        inOrder.verify(g, times(4)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
        inOrder.verify(g).setColor(Color.RED);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y + 10);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y - 10);
    }

    @Test
    public void blueDirectionIsChangedAfterOneSwitching() {
        final TripleSwitch testee = new TripleSwitch(middle);
        // Act
        testee.toggle(switchCallback).paint(g);
        // Assert
        InOrder inOrder = inOrder(g);
        inOrder.verify(g).setColor(Color.BLUE);
        inOrder.verify(g, times(4)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
        inOrder.verify(g).setColor(Color.RED);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y - 10);
    }

    @Test
    public void blueDirectionIsChangedAfterTwoSwitching() {
        final TripleSwitch testee = new TripleSwitch(middle);
        // Act
        testee.toggle(switchCallback).toggle(switchCallback).paint(g);
        // Assert
        InOrder inOrder = inOrder(g);
        inOrder.verify(g).setColor(Color.BLUE);
        inOrder.verify(g, times(4)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
        inOrder.verify(g).setColor(Color.RED);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y + 10);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y);
    }

    @Test
    public void blueDirectionIsChangedAfterThreeSwitching() {
        final TripleSwitch testee = new TripleSwitch(middle);
        // Act
        testee.toggle(switchCallback).toggle(switchCallback).toggle(switchCallback).paint(g);
        // Assert
        InOrder inOrder = inOrder(g);
        inOrder.verify(g).setColor(Color.BLUE);
        inOrder.verify(g, times(4)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
        inOrder.verify(g).setColor(Color.RED);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y + 10);
        inOrder.verify(g).drawLine(middle.x, middle.y, middle.x + 15, middle.y - 10);
    }

    @Test
    public void setId() {
        final TripleSwitch testee = new TripleSwitch(middle);
        // Act
        final TripleSwitch result = testee.setId("13/14");
        // Assert
        assertThat(result.getId(), containsInAnyOrder(SwitchId.create(13), SwitchId.create(14)));
    }

    @Test
    public void setBoardId() {
        final TripleSwitch testee = new TripleSwitch(middle);
        // Act
        final TripleSwitch result = testee.setBoardId("13/14");
        // Assert
        assertThat(result.getBoardId(), containsInAnyOrder(BoardId.create(13), BoardId.create(14)));
    }

    @Test
    public void firstToggle() {
        final TripleSwitch testee = new TripleSwitch(middle).setBoardId("24/25").setId("11/12");
        // Act
        testee.toggle(switchCallback);
        // Assert
        verify(switchCallback, times(2)).toggleSwitch(switchIdCaptor.capture(), boardIdCaptor.capture(), stateCaptor.capture());
        assertThat(boardIdCaptor.getAllValues(), contains(BoardId.create(24), BoardId.create(25)));
        assertThat(switchIdCaptor.getAllValues(), contains(SwitchId.create(11), SwitchId.create(12)));
        assertThat(stateCaptor.getAllValues(), contains(true, true));
    }

    @Test
    public void secondToggle() {
        SwitchCallback tmpCallback = mock(SwitchCallback.class);
        final TripleSwitch testee = new TripleSwitch(middle).setBoardId("24/25").setId("11/12").toggle(tmpCallback);
        // Act
        testee.toggle(switchCallback);
        // Assert
        verify(switchCallback, times(2)).toggleSwitch(switchIdCaptor.capture(), boardIdCaptor.capture(), stateCaptor.capture());
        assertThat(boardIdCaptor.getAllValues(), contains(BoardId.create(24), BoardId.create(25)));
        assertThat(switchIdCaptor.getAllValues(), contains(SwitchId.create(11), SwitchId.create(12)));
        assertThat(stateCaptor.getAllValues(), contains(true, false));
    }

    @Test
    public void thirdToggle() {
        SwitchCallback tmpCallback = mock(SwitchCallback.class);
        final TripleSwitch testee = new TripleSwitch(middle).setBoardId("24/25").setId("11/12").toggle(tmpCallback).toggle(tmpCallback);
        // Act
        testee.toggle(switchCallback);
        // Assert
        verify(switchCallback, times(2)).toggleSwitch(switchIdCaptor.capture(), boardIdCaptor.capture(), stateCaptor.capture());
        assertThat(boardIdCaptor.getAllValues(), contains(BoardId.create(24), BoardId.create(25)));
        assertThat(switchIdCaptor.getAllValues(), contains(SwitchId.create(11), SwitchId.create(12)));
        assertThat(stateCaptor.getAllValues(), contains(false, true));
    }

    @Test
    public void fourthToggle() {
        SwitchCallback tmpCallback = mock(SwitchCallback.class);
        final TripleSwitch testee = new TripleSwitch(middle).setBoardId("24/25").setId("11/12").toggle(tmpCallback).toggle(tmpCallback).toggle(tmpCallback);
        // Act
        testee.toggle(switchCallback);
        // Assert
        verify(switchCallback, times(2)).toggleSwitch(switchIdCaptor.capture(), boardIdCaptor.capture(), stateCaptor.capture());
        assertThat(boardIdCaptor.getAllValues(), contains(BoardId.create(24), BoardId.create(25)));
        assertThat(switchIdCaptor.getAllValues(), contains(SwitchId.create(11), SwitchId.create(12)));
        assertThat(stateCaptor.getAllValues(), contains(true, true));
    }

    @Test
    public void getDataToPersist() {
        final TripleSwitch testee = new TripleSwitch(middle).setBoardId("24/25").setId("11/12");
        // Act
        final Collection<Object> data = testee.getDataToPersist();
        final Collection<String> strings = Collections2.transform(data, input -> input.toString());
        // Assert
        final Iterator<String> iterator = strings.iterator();
        iterator.next();
        assertThat(TripleSwitch.create(iterator).build(), is(equalTo(testee)));
    }
}