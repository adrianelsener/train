package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.SwitchCallback;
import com.beust.jcommander.internal.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class SimpleTrack extends Track {
    private final static Logger logger = LoggerFactory.getLogger(SimpleTrack.class);
    private static final Collection<Object> emptyIndividualProps = Lists.newArrayList();

    SimpleTrack(final Point startPoint, final Point endPoint) {
        super(startPoint, endPoint);
    }

    public static Track createSimpleTrack(final Iterator<String> iterator) {
        return createSimpleTrack(new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())),
                new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())));
    }

    @Override
    public SimpleTrack moveTo(final Point newLocation) {
        return this;
    }

    @Override
    public SwitchId getId() {
        return SwitchId.createDummy();
    }

    @Override
    public BoardId getBoardId() {
        return BoardId.createDummy();
    }

    @Override
    public TrackPart setId(final String newId) {
        logger.info("setId NoOp while Track");
        return this;
    }

    @Override
    public TrackPart setBoardId(final String boardId) {
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
    public TrackPart toggle(final SwitchCallback toggler) {
        logger.debug("No toggle on simple track");
        return this;
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