package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.odb.api.OdbFunction;
import ch.adrianelsener.odb.api.OdbPredicate;
import ch.adrianelsener.train.gui.swing.DetailWindow;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import com.google.common.eventbus.EventBus;

public class UpdateApplyListener implements UpdateToken {
    private final TrackPart trackPart;
    private final DetailWindow.ApplyActionListener applyListener;

    private UpdateApplyListener(TrackPart trackPart, EventBus bus) {

        this.trackPart = trackPart;

        applyListener = text -> {
            OdbPredicate<TrackPart> predicate = part -> trackPart == part;
            OdbFunction<TrackPart> replacement = part -> part.setId(text.getId()).setBoardId(text.getBoardId()).invertView(text.isInverted());
            bus.post(DbReplaceToken.create(predicate, replacement));
            bus.post(UpdateMainUi.create());
        };

    }

    public static UpdateApplyListener create(TrackPart trackPart, EventBus bus) {
        return new UpdateApplyListener(trackPart, bus);
    }

    @Override
    public TrackPart getDraftPart() {
        return trackPart;
    }

    public DetailWindow.ApplyActionListener getListener() {
        return applyListener;
    }
}
