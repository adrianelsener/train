package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;
import ch.adrianelsener.train.gui.swing.TrackView;
import ch.adrianelsener.train.gui.swing.common.InRangeCalculator;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SwingSwitch implements TrackPart {
    private final static Logger logger = LoggerFactory.getLogger(SwingSwitch.class);
    private final Point center;
    private final SwitchId switchId;
    private final TrackView switchView;
    private final BoardId boardId;
    private final boolean on;
    private final double angle;
    private final Point leftPoint;
    private final Point topRightPoint;
    private final Point bottomRightPoint;
    private final SwitchMode switchMode;
    private final InRangeCalculator inRangeCalc = InRangeCalculator.create();

    private SwingSwitch(final Point point, final SwitchMode switchMode) {
        this(point, 0, SwitchId.createDummy(), BoardId.createDummy(), false, switchMode, TrackView.Default);
    }

    SwingSwitch(final Point center, final double angle, final SwitchId id, final BoardId boardId, final boolean state, final SwitchMode switchMode, TrackView switchView) {
        this.center = center;
        switchId = id;
        on = state;
        this.boardId = boardId;
        this.angle = angle;
        this.switchMode = switchMode;
        this.switchView = switchView;

        final double sin = Math.sin(Math.toRadians(angle));
        final double cos = Math.cos(Math.toRadians(angle));

        final double leftX = center.x - 10;
        final double leftY = center.y;

        leftPoint = new Point(calcX(sin, cos, leftX, leftY), calcY(sin, cos, leftX, leftY));

        final double topRightX = center.x + 15;
        final double topRightY = center.y + 10;

        topRightPoint = new Point(calcX(sin, cos, topRightX, topRightY), calcY(sin, cos, topRightX, topRightY));

        final double bottomRightX = center.x + 15;
        final double bottomRightY = center.y - 10;

        bottomRightPoint = new Point(calcX(sin, cos, bottomRightX, bottomRightY), calcY(sin, cos, bottomRightX, bottomRightY));

    }

    public static SwingSwitch createSwitch(final Iterator<String> iterator) {
        final Point center = new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next()));
        final Double drawAngle = Double.valueOf(iterator.next());
        final SwitchId readSwitchId = SwitchId.fromValue(iterator.next());
        final BoardId readBoardId = BoardId.fromValue(iterator.next());
        final boolean state = Boolean.parseBoolean(iterator.next());
        final SwitchMode switchMode = SwitchMode.valueOf(iterator.next());
        final TrackView switchView = TrackView.valueOf(iterator.next());
        return new SwingSwitch(center, drawAngle, readSwitchId, readBoardId, state, switchMode, switchView);

    }

    public static SwingSwitch create(final Point point) {
        return new RealSwitch(point);
    }

    public static SwingSwitch createDummy(final Point point) {
        return new DummySwitch(point);
    }

    @Override
    public void paint(final Graphics2D g) {
        drawLable(g);
        drawSwitch(g);
        drawDirection(g);

    }

    private void drawSwitch(final Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.blue);
        g.drawLine(leftPoint.x, leftPoint.y, center.x, center.y);
        g.drawLine(center.x, center.y, topRightPoint.x, topRightPoint.y);
        g.drawLine(center.x, center.y, bottomRightPoint.x, bottomRightPoint.y);
    }

    private void drawDirection(final Graphics2D g) {
        if (SwitchMode.Real.equals(switchMode)) {
            g.setColor(Color.red);
            if ((on && (TrackView.Default == switchView)) || ((!on) && (TrackView.Inverted == switchView))) {
                g.drawLine(center.x, center.y, topRightPoint.x, topRightPoint.y);
            } else {
                g.drawLine(center.x, center.y, bottomRightPoint.x, bottomRightPoint.y);
            }
        }
    }

    private void drawLable(final Graphics2D g) {
        if (SwitchMode.Real.equals(switchMode)) {
            g.setColor(Color.blue);
            g.drawString(boardId.toUiString() + "/" + switchId.toUiString(), center.x, center.y - 10);
        }
    }

    private int calcY(final double sin, final double cos, final double leftX, final double leftY) {
        return Double.valueOf((-(leftX - center.x) * sin + (leftY - center.y) * cos) + center.y).intValue();
    }

    private int calcX(final double sin, final double cos, final double leftX, final double leftY) {
        return Double.valueOf(((leftX - center.x) * cos + (leftY - center.y) * sin) + center.x).intValue();
    }

    @Override
    public boolean isNear(final Point point) {
        return onXCoordinates(point) && onYCoordinates(point);
    }

    private boolean onYCoordinates(final Point point) {
        return point.y >= (center.y - 10) && point.y <= (center.y + 10);
    }

    private boolean onXCoordinates(final Point point) {
        return point.x >= (center.x - 10) && point.x <= (center.x + 15);
    }

    @Override
    public Point getNextConnectionpoint(final Point origin) {
        if (inRangeCalc.isInRange(origin, leftPoint, 10)) {
            return leftPoint;
        } else if (inRangeCalc.isInRange(origin, topRightPoint, 15)) {
            return topRightPoint;
        } else if (inRangeCalc.isInRange(origin, bottomRightPoint, 15)) {
            return bottomRightPoint;
        } else {
            throw new IllegalArgumentException("getNextConnectionpoint soll nur aufgerufen werden wenn auch etwas in der naehe ist");
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public SwingSwitch createMirror() {
        return new SwingSwitch(center, angle + 45, switchId, boardId, on, switchMode, switchView);
    }

    @Override
    public Collection<Object> getDataToPersist() {
        final List<Object> objects = Lists.newArrayList();
        objects.add("S,");
        objects.add(center.x);
        objects.add(center.y);
        objects.add(Double.toString(angle));
        objects.add(switchId.toSerializable());
        objects.add(boardId.toSerializable());
        objects.add(on);
        objects.add(switchMode);
        objects.add(switchView);
        return objects;
    }

    @Override
    public SwingSwitch toggle(final ToggleCallback toggler) {
        toggler.toggleSwitch(switchId, boardId, on);
        return invertState();
    }

    private SwingSwitch invertState() {
        return new SwingSwitch(center, angle, switchId, boardId, !on, switchMode, switchView);
    }

    @Override
    public SwingSwitch moveTo(final Point newLocation) {
        return new SwingSwitch(newLocation, angle, switchId, boardId, on, switchMode, switchView);
    }

    @Override
    public SwitchId getId() {
        return switchId;
    }

    @Override
    public SwingSwitch setId(final String newId) {
        return new SwingSwitch(center, angle, SwitchId.fromValue(newId), boardId, on, switchMode, switchView);
    }

    @Override
    public BoardId getBoardId() {
        return boardId;
    }

    @Override
    public SwingSwitch setBoardId(final String boardId) {
        return new SwingSwitch(center, angle, switchId, BoardId.create(boardId), on, switchMode, switchView);
    }

    @Override
    public SwingSwitch invertView(final boolean inverted) {
        final TrackView selected;
        if (inverted) {
            selected = TrackView.Inverted;
        } else {
            selected = TrackView.Default;
        }
        return new SwingSwitch(center, angle, switchId, boardId, on, switchMode, selected);
    }

    @Override
    public boolean isInverted() {
        return TrackView.Inverted == switchView;
    }


    enum SwitchMode {
        Real, Dummy
    }

    static class RealSwitch extends SwingSwitch {

        public RealSwitch(final Point point) {
            super(point, SwitchMode.Real);
        }

        RealSwitch(final Point center, final double angle, final SwitchId id, final BoardId boardId, final boolean state, final SwitchMode switchMode, final TrackView switchView) {
            super(center, angle, id, boardId, state, switchMode, switchView);
        }
    }

    static class DummySwitch extends SwingSwitch {
        public DummySwitch(final Point point) {
            super(point, SwitchMode.Dummy);
        }

        DummySwitch(final Point center, final double angle, final SwitchId id, final BoardId boardId, final boolean state, final SwitchMode switchMode, final TrackView switchView) {
            super(center, angle, id, boardId, state, switchMode, switchView);
        }
    }
}