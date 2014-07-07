package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.SwingSwitch;
import ch.adrianelsener.train.gui.swing.model.Track;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Optional;

public abstract class DraftPartCreationAction extends CreationAction {
    public DraftPartCreationAction(Point point) {
        super(point);
    }

    public static DraftPartCreationAction createTrack(Point point) {
        return new DraftTrackCreationAction(point);
    }

    public static DraftPartCreationAction createSwitchTrack(Point point) {
        return new DraftSwitchTrackCreationAction(point);
    }


    public static DraftPartCreationAction createSwitch(Point point) {
        return new DraftSwitchCreationAction(point);
    }

    public static DraftPartCreationAction createDummySwitch(Point point) {
        return new DraftDummySwitchCreationAction(point);
    }

    private static class DraftSwitchTrackCreationAction extends DraftPartCreationAction {
        public DraftSwitchTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            return Track.createSwitchTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
        }
    }

    private static class DraftTrackCreationAction extends DraftPartCreationAction {
        public DraftTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            return Track.createSimpleTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
        }
    }

    private static class DraftSwitchCreationAction extends DraftPartCreationAction {
        private static final Logger logger = LoggerFactory.getLogger(DraftSwitchCreationAction.class);

        public DraftSwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final SwingSwitch draftSwitch = SwingSwitch.create(pointCalculator.calculatePoint(getEndPoint()));
            logger.debug("Create Switch {}", draftSwitch);
            return draftSwitch;
        }
    }

    private static class DraftDummySwitchCreationAction extends DraftPartCreationAction {
        public DraftDummySwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            return SwingSwitch.createDummy(pointCalculator.calculatePoint(getEndPoint()));
        }
    }
}
