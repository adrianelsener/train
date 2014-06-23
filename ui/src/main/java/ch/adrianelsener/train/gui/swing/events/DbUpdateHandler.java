package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.DbUpdater;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.swing.TrackPart;
import com.google.common.eventbus.Subscribe;

/**
 * Created by els on 5/20/14.
 */
public class DbUpdateHandler {
    private final Odb<TrackPart> db;

    private DbUpdateHandler(Odb<TrackPart> db) {
        this.db = db;
    }

    public static DbUpdateHandler create(Odb<TrackPart> db) {
        return new DbUpdateHandler(db);
    }

    @Subscribe
    public void replace(DbUpdater<TrackPart> updater) {
        updater.apply(db);
    }
}
