package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.Switch;
import ch.adrianelsener.train.gui.swing.model.Track;
import ch.adrianelsener.train.gui.swing.model.TrackPart;

import java.awt.*;
import java.util.Optional;

/**
 * Created by els on 7/1/14.
 */
public abstract class PartCreationAction extends CreationAction{
    public PartCreationAction(Point point) {
        super( point);
    }

    public static PartCreationAction createTrack(Point point) {
        return new TrackCreationAction(point);
    }

    public static PartCreationAction createSwitchTrack(Point point) {
        return new SwitchTrackCreationAction(point);
    }


    public static PartCreationAction createSwitch(Point point) {
        return new SwitchCreationAction(point);
    }

    public static PartCreationAction createDummySwitch(Point point) {
        return new DummySwitchCreationAction(point);
    }

    private static class SwitchTrackCreationAction extends PartCreationAction {
        public SwitchTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final TrackPart draftSwitchTrack = Track.createSwitchTrack(point.get(), getEndPoint());
            return draftSwitchTrack;
        }
    }

    private static class TrackCreationAction extends PartCreationAction {
        public TrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final TrackPart draftSimpleTrack = Track.createSimpleTrack(point.get(), getEndPoint());
            return draftSimpleTrack;
        }
    }

    private static class SwitchCreationAction extends PartCreationAction{
        public SwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final Switch draftSwitch = Switch.create(getEndPoint());
            return draftSwitch;
        }
    }

    private static class DummySwitchCreationAction extends PartCreationAction {
        public DummySwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            return Switch.createDummy(getEndPoint());
        }
    }
}
