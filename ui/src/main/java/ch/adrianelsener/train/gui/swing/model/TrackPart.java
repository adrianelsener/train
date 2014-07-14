package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;
import com.google.common.collect.ImmutableCollection;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.Nonnull;
import java.awt.Point;
import java.util.Collection;

public interface TrackPart extends Nearby, Paintable, Datacontainer {

    TrackPart createMirror();

    /**
     * Toggles the part. This means, the drawn part will change its state and the physical has to change the state.
     * To change the physical state, the ToggleCallback is used to do that.
     *
     * @param toggler
     * @return
     */
    @Nonnull TrackPart toggle(@Nonnull ToggleCallback toggler);

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
