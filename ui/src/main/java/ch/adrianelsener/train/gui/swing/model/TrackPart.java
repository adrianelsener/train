package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.odb.api.Datacontainer;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.SwitchCallback;
import com.google.common.collect.ImmutableCollection;

import javax.annotation.Nonnull;
import java.awt.Point;

public interface TrackPart extends Nearby, Paintable, Datacontainer {

    TrackPart createMirror();

    /**
     * Toggles the part. This means, the drawn part will change its state and the physical has to change the state.
     * To change the physical state, the ToggleCallback is used to do that.
     *
     * @param toggler
     * @return the new part with changed state
     */
    @Nonnull TrackPart toggle(@Nonnull SwitchCallback toggler);

    /**
     * Applies the current state to SwitchCallback
     * @param callback
     */
    void applyState(@Nonnull SwitchCallback callback);

    /**
     * Moves a part to the new location
     * @param newLocation
     * @return the moved part
     */
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
