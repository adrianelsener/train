package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.DbUpdater;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.odb.api.OdbFunction;
import ch.adrianelsener.odb.api.OdbPredicate;
import ch.adrianelsener.train.gui.swing.TrackPart;

/**
 * Created by els on 5/20/14.
 */
public class DbReplaceToken implements DbUpdater<TrackPart> {
    private final OdbPredicate<TrackPart> predicate;
    private final OdbFunction<TrackPart> replacement;

    private DbReplaceToken(OdbPredicate<TrackPart> predicate, OdbFunction<TrackPart> replacement) {
        this.predicate = predicate;
        this.replacement = replacement;
    }

    public static DbReplaceToken create(OdbPredicate<TrackPart> predicate, OdbFunction<TrackPart> replacement) {
        return new DbReplaceToken(predicate, replacement);
    }

    @Override
    public void apply(Odb<TrackPart> db) {
        db.replace(predicate, replacement);
    }
}
