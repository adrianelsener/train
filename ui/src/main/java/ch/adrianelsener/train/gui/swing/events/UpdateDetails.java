package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.TrackPart;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by els on 5/19/14.
 */
public class UpdateDetails implements UpdateToken {
    private final TrackPart draftPart;

    private UpdateDetails(final TrackPart draftPart) {
        this.draftPart = draftPart;
    }

    public static UpdateDetails create(TrackPart draftPart) {
        return new UpdateDetails(draftPart);
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
