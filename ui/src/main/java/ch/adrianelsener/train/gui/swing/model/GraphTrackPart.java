package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.SwitchId;

import java.util.Collection;
import java.util.Optional;

public interface GraphTrackPart {
    void appendChild(GraphTrackPart other);

    Optional<GraphTrackPart> getParent();

    Collection<GraphTrackPart> getChilds();

    void setParent(GraphTrackPart parent);

    SwitchId getId();
}
