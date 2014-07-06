package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateDetails {
    private final TrackPart draftPart;

    private UpdateDetails(final TrackPart draftPart) {
        this.draftPart = draftPart;
    }

    public static UpdateDetails create(TrackPart draftPart) {
        return new UpdateDetails(draftPart);
    }

    public TrackPart getDraftPart() {
        return draftPart;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
