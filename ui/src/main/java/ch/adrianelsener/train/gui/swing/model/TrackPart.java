package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;
import com.google.common.collect.ImmutableCollection;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.awt.Point;
import java.util.Collection;

public interface TrackPart extends Nearby, Paintable, Datacontainer {

    TrackPart createMirror();

    TrackPart toggle(ToggleCallback toggler);

    TrackPart moveTo(Point newLocation);

    /**
     * Returns all Point that are in connections to other parts
     * @return
     */
    ImmutableCollection<Point> getInConnectors();

    /**
     * Returns all Point that are out connections to other parts
     * @return
     */
    ImmutableCollection<Point> getOutConnectors();

    boolean isPipe();

    SwitchId getId();

    TrackPart setId(String newId);

    BoardId getBoardId();

    TrackPart setBoardId(String boardId);

    TrackPart invertView(boolean inverted);

    boolean isInverted();
}
