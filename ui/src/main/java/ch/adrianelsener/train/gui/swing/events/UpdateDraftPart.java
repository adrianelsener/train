package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateDraftPart implements UpdateToken {
    private final TrackPart draftPart;

    private UpdateDraftPart(final TrackPart draftPart) {
        this.draftPart = draftPart;
    }

    public static UpdateDraftPart create(TrackPart draftPart) {
        return new UpdateDraftPart(draftPart);
    }

    @Override
    public TrackPart getDraftPart() {
        return draftPart;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
