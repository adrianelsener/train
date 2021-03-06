package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class DummySwitch extends SwingSwitch {
    private final static Logger logger = LoggerFactory.getLogger(ch.adrianelsener.train.gui.swing.model.DummySwitch.class);

    DummySwitch(final Point point) {
        super(point);
    }

    DummySwitch(final Point center, final double angle) {
        super(center, angle);
    }

    @Override
    public TrackPart invertView(boolean inverted) {
        return this;
    }

    @Override
    protected ch.adrianelsener.train.gui.swing.model.DummySwitch createNew(Point center, double angle) {
        return new ch.adrianelsener.train.gui.swing.model.DummySwitch(center, angle);
    }

    @Nonnull
    @Override
    public TrackPart toggle(@Nonnull SwitchCallback toggler) {
        logger.trace("NoOp for toggle on DummySwitch");
        return this;
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        logger.trace("NoOp for applyState on DummySwitch");
    }

    protected void drawDirection(final Graphics2D g) {
        logger.trace("drawDirection nothing to do as DummySwitch");
    }

    @Override
    protected void drawLable(Graphics2D g) {
        logger.trace("drawLable nothing to do as DummySwitch");
    }

    @Override
    public Collection<Object> getDataToPersist() {
        final java.util.List<Object> objects = Lists.newArrayList();
        objects.add("DS");
        objects.add(super.center.x);
        objects.add(super.center.y);
        objects.add(Double.toString(super.angle));
        return objects;
    }

    @Override
    public Collection<SwitchId> getId() {
        return Lists.newArrayList(SwitchId.createDummy());
    }


    @Override
    public SwingSwitch setId(final String newId) {
        return this;
    }

    @Override
    public SwingSwitch setBoardId(final String boardId) {
        return this;
    }


    @Override
    public boolean isInverted() {
        return false;
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(BoardId.createDummy());
    }

}
