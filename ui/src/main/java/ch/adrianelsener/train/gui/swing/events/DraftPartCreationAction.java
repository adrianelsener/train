package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Optional;

public abstract class DraftPartCreationAction extends CreationAction {
    protected TrackFactory trackFactory = TrackFactory.instance();
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

    public static DraftPartCreationAction createTripleSwitch(Point point) {
        return new DraftTripleSwitchCreateionAction(point);
    }

    public static DraftPartCreationAction createPowerTrack(Point point) {
        return new DraftPowerTrackCreationAction(point);
    }

    private static class DraftSwitchTrackCreationAction extends DraftPartCreationAction {
        private final static Logger logger = LoggerFactory.getLogger(DraftSwitchTrackCreationAction.class);
        public DraftSwitchTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            logger.debug("point for draft SwitchTrach is: '{}'", point);
            return trackFactory.createSwitchTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
        }
    }

    private static class DraftPowerTrackCreationAction extends DraftPartCreationAction {
        private final static Logger logger = LoggerFactory.getLogger(DraftPowerTrackCreationAction.class);

        public DraftPowerTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            logger.debug("point for draft PowerTrack is: '{}'", point);
            return trackFactory.createPowerTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
        }
    }

    private static class DraftTrackCreationAction extends DraftPartCreationAction {
        public DraftTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            return trackFactory.createSimpleTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
        }
    }

    private static class DraftSwitchCreationAction extends DraftPartCreationAction {
        private static final Logger logger = LoggerFactory.getLogger(DraftSwitchCreationAction.class);

        public DraftSwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final SwingSwitch draftSwitch =  trackFactory.create(pointCalculator.calculatePoint(getEndPoint()));
            logger.trace("Create Switch {}", draftSwitch);
            return draftSwitch;
        }
    }

    private static class DraftDummySwitchCreationAction extends DraftPartCreationAction {
        public DraftDummySwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            return trackFactory.createDummySwitch(pointCalculator.calculatePoint(getEndPoint()));
        }
    }

    private static class DraftTripleSwitchCreateionAction extends DraftPartCreationAction {
        private final static Logger logger = LoggerFactory.getLogger(DraftTripleSwitchCreateionAction.class);

        public DraftTripleSwitchCreateionAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final TrackPart tripleSwitch = TripleSwitch.Builder.create(getEndPoint()).build();
            logger.debug("Create {}", tripleSwitch);
            return tripleSwitch;
        }
    }
}
