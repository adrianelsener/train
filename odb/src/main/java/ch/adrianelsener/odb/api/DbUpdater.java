package ch.adrianelsener.odb.api;

import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.swing.TrackPart;

/**
 * Created by els on 5/20/14.
 */
public interface DbUpdater {
    void apply(Odb<TrackPart> db);
}
