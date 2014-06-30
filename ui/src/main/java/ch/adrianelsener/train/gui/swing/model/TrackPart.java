package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;

import java.awt.Point;

public interface TrackPart extends Nearby, Paintable, Datacontainer {

    TrackPart createMirror();

    TrackPart toggle(ToggleCallback toggler);

    TrackPart moveTo(Point newLocation);

    SwitchId getId();

    TrackPart setId(String newId);

    BoardId getBoardId();

    TrackPart setBoardId(String boardId);

    TrackPart invertView(boolean inverted);

    boolean isInverted();
}
