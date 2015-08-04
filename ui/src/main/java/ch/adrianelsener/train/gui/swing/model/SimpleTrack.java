package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.swing.common.PointMover;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class SimpleTrack extends Track {
    private final static Logger logger = LoggerFactory.getLogger(SimpleTrack.class);
    private static final Collection<Object> emptyIndividualProps = Lists.newArrayList();

    SimpleTrack(final Point startPoint, final Point endPoint) {
        super(startPoint, endPoint);
    }

    public static SimpleTrack createSimpleTrack(final Iterator<String> iterator) {
        return createSimpleTrack(new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())),
                new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())));
    }

    @Override
    public SimpleTrack moveTo(final Point newLocation) {
        return this;
    }

    @Override
    public TrackPart move(Point direction) {
        Point newStart = PointMover.use(startPoint).moveTo(direction);
        Point newEnd = PointMover.use(endPoint).moveTo(direction);
        return new SimpleTrack(newStart, newEnd);
    }

    @Override
    public Collection<SwitchId> getId() {
        return Lists.newArrayList(SwitchId.createDummy());
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(BoardId.createDummy());
    }

    @Override
    public SimpleTrack setId(final String newId) {
        logger.info("setId NoOp while Track");
        return this;
    }

    @Override
    public SimpleTrack setBoardId(final String boardId) {
        logger.info("setBoardId NoOp while Track");
        return this;
    }

    @Override
    protected Color getLineColor() {
        return Color.blue;
    }

    @Override
    protected Collection<Object> individualStorageProperties() {
        return emptyIndividualProps;
    }

    @Override
    public SimpleTrack toggle(final SwitchCallback toggler) {
        logger.debug("No toggle on simple track");
        return this;
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        logger.debug("No op on applyStatement since it is a simple track");
    }

    @Override
    protected String getCsvIdentifier() {
        return "T";
    }

    @Override
    protected void paintLable(final Graphics2D g) {

    }

    @Override
    public boolean isInverted() {
        return false;
    }

}