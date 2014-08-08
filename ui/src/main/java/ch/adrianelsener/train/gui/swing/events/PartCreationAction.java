package ch.adrianelsener.train.gui.swing.events;

import ch.adrianelsener.train.gui.swing.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Optional;

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

    public static PartCreationAction createTripleSwitch(Point point) {
        return new TripleSwitchCreationAction(point);
    }

    private static class TripleSwitchCreationAction extends PartCreationAction {
        private static final Logger logger = LoggerFactory.getLogger(TripleSwitchCreationAction.class);

        public TripleSwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final TripleSwitch tripleSwitch = TripleSwitch.Builder.create(getEndPoint()).build();
            logger.debug("created switch '{}' to send to ui", tripleSwitch);
            return tripleSwitch;
        }
    }

    private static class SwitchTrackCreationAction extends PartCreationAction {
        private static final Logger logger = LoggerFactory.getLogger(SwitchTrackCreationAction.class);
        public SwitchTrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final TrackPart switchTrack = Track.createSwitchTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
            logger.debug("Created final switch track '{}' to send to ui", switchTrack);
            return switchTrack;
        }
    }

    private static class TrackCreationAction extends PartCreationAction {
        private final static Logger logger = LoggerFactory.getLogger(TrackCreationAction.class);
        public TrackCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final TrackPart simpleTrack = Track.createSimpleTrack(point.get(), pointCalculator.calculatePoint(getEndPoint()));
            logger.debug("created simple track '{}' to send to ui", simpleTrack);
            return simpleTrack;
        }
    }

    private static class SwitchCreationAction extends PartCreationAction{
        private final static Logger logger = LoggerFactory.getLogger(SwitchTrackCreationAction.class);
        public SwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final SwingSwitch swingSwitch = new RealSwitch(getEndPoint());
            logger.debug("created switch '{}' to send to ui", swingSwitch);
            return swingSwitch;
        }
    }

    private static class DummySwitchCreationAction extends PartCreationAction {
        private final static Logger logger = LoggerFactory.getLogger(DummySwitchCreationAction.class);
        public DummySwitchCreationAction(Point point) {
            super(point);
        }

        @Override
        public TrackPart createDraftPart(Optional<Point> point, PointCalculator pointCalculator) {
            final SwingSwitch dummySwitch = new DummySwitch(getEndPoint());
            logger.debug("created dummy switch '{}' to send to ui", dummySwitch);
            return dummySwitch;
        }
    }
}
