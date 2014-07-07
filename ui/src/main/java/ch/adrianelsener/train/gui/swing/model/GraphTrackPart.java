package ch.adrianelsener.train.gui.swing.model;

import java.util.Collection;
import java.util.Optional;

public interface GraphTrackPart {
    void appendChild(GraphTrackPart other);

    Optional<GraphTrackPart> getParent();

    Collection<GraphTrackPart> getChilds();

    void setParent(GraphTrackPart parent);
}
