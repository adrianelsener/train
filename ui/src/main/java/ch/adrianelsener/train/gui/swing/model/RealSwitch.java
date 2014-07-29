package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.TrackView;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RealSwitch extends SwingSwitch {

    private final SwitchId switchId;
    private final TrackView switchView;
    private final BoardId boardId;
    private final boolean on;

    public static ch.adrianelsener.train.gui.swing.model.RealSwitch create(final Iterator<String> iterator) {
        final Point center = new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next()));
        final Double drawAngle = Double.valueOf(iterator.next());
        final SwitchId readSwitchId = SwitchId.fromValue(iterator.next());
        final BoardId readBoardId = BoardId.fromValue(iterator.next());
        final boolean state = Boolean.parseBoolean(iterator.next());
        final SwitchMode mode = SwitchMode.valueOf(iterator.next()); // Switchmode no more needed
        final TrackView switchView = TrackView.valueOf(iterator.next());
        switch (mode) {
            case Real:
                return new ch.adrianelsener.train.gui.swing.model.RealSwitch(center, drawAngle, readSwitchId, readBoardId, state, switchView);
            default:
                throw new IllegalArgumentException("Could not determine switch");
        }
    }


    public RealSwitch(final Point point) {
        this(point, 0, SwitchId.createDummy(), BoardId.createDummy(), false, TrackView.Default);
    }

    RealSwitch(final Point center, final double angle, final SwitchId id, final BoardId boardId, final boolean state, final TrackView switchView) {
        super(center, angle);
        switchId = id;
        on = state;
        this.boardId = boardId;
        this.switchView = switchView;

    }

    @Override
    protected RealSwitch createNew(Point center, double angle) {
        return new RealSwitch(center, angle, switchId, boardId, on, switchView);
    }

    @Override
    public RealSwitch toggle(final SwitchCallback toggler) {
        toggler.toggleSwitch(switchId, boardId, on);
        return new RealSwitch(super.center, super.angle, switchId, boardId, !on, switchView);
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        callback.toggleSwitch(switchId, boardId, on);
    }

    @Override
    protected void drawDirection(final Graphics2D g) {
        g.setColor(Color.red);
        if ((on && (TrackView.Default == switchView)) || ((!on) && (TrackView.Inverted == switchView))) {
            g.drawLine(super.center.x, super.center.y, super.topRightPoint.x, super.topRightPoint.y);
        } else {
            g.drawLine(super.center.x, super.center.y, super.bottomRightPoint.x, super.bottomRightPoint.y);
        }
    }

    protected void drawLable(final Graphics2D g) {
        g.setColor(Color.blue);
        g.drawString(boardId.toUiString() + "/" + switchId.toUiString(), super.center.x, super.center.y - 10);
    }

    @Override
    public Collection<Object> getDataToPersist() {
        final List<Object> objects = Lists.newArrayList();
        objects.add("S");
        objects.add(super.center.x);
        objects.add(super.center.y);
        objects.add(Double.toString(super.angle));
        objects.add(switchId.toSerializable());
        objects.add(boardId.toSerializable());
        objects.add(on);
        objects.add(SwitchMode.Real);
        objects.add(switchView);
        return objects;
    }

    @Override
    public Collection<SwitchId> getId() {
        return Lists.newArrayList(switchId);
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(boardId);
    }

    @Override
    public ch.adrianelsener.train.gui.swing.model.RealSwitch invertView(final boolean inverted) {
        final TrackView selected;
        if (inverted) {
            selected = TrackView.Inverted;
        } else {
            selected = TrackView.Default;
        }
        return new ch.adrianelsener.train.gui.swing.model.RealSwitch(super.center, super.angle, switchId, boardId, on, selected);
    }

    @Override
    public SwingSwitch setId(final String newId) {
        return new ch.adrianelsener.train.gui.swing.model.RealSwitch(super.center, super.angle, SwitchId.fromValue(newId), boardId, on, switchView);
    }

    @Override
    public SwingSwitch setBoardId(final String boardId) {
        return new ch.adrianelsener.train.gui.swing.model.RealSwitch(super.center, super.angle, switchId, BoardId.create(boardId), on, switchView);
    }


    @Override
    public boolean isInverted() {
        return TrackView.Inverted == switchView;
    }

}
