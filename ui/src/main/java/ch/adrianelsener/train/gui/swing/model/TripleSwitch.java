package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.common.RotationCalculator;
import com.google.common.collect.ImmutableCollection;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;

public class TripleSwitch implements TrackPart {
    private final Point center;
    private final double angle;
    private final RotationCalculator rotCalc;
    private final Point topRight;
    private final Point bottomRight;
    private final Point middleRight;
    private final Point middleLeft;

    public static TripleSwitch create(Point center, double angle) {
        return new TripleSwitch(center, angle);
    }

    TripleSwitch(Point center, double angle) {
        rotCalc = new RotationCalculator(center, angle);
        this.center = center;
        this.angle = angle;

        final int rightX = center.x + 15;
        final int leftX = center.x - 10;
        final int upperY = center.y + 10;
        final int lowerY = center.y - 10;

        this.topRight = new Point(rightX, upperY);
        this.middleRight = new Point(rightX, center.y);
        this.bottomRight = new Point(rightX, lowerY);
        this.middleLeft = new Point(leftX, center.y);

    }

    TripleSwitch(Point middle) {
        this(middle, 0);
    }

    @Override
    public TrackPart createMirror() {
        return null;
    }

    @Nonnull
    @Override
    public TrackPart toggle(@Nonnull SwitchCallback toggler) {
        return null;
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {

    }

    @Override
    public TrackPart moveTo(Point newLocation) {
        return null;
    }

    @Override
    public ImmutableCollection<Point> getInConnectors() {
        return null;
    }

    @Override
    public ImmutableCollection<Point> getOutConnectors() {
        return null;
    }

    @Override
    public boolean isPipe() {
        return false;
    }

    @Override
    public SwitchId getId() {
        return null;
    }

    @Override
    public TrackPart setId(String newId) {
        return null;
    }

    @Override
    public BoardId getBoardId() {
        return null;
    }

    @Override
    public TrackPart setBoardId(String boardId) {
        return null;
    }

    @Override
    public TrackPart invertView(boolean inverted) {
        return null;
    }

    @Override
    public boolean isInverted() {
        return false;
    }

    @Override
    public Collection<Object> getDataToPersist() {
        return null;
    }

    @Override
    public boolean isNear(Point point) {
        return false;
    }

    @Override
    public Point getNextConnectionpoint(Point origin) {
        return null;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.blue);
        g.drawLine(middleLeft.x, middleLeft.y, center.x, center.y);
        g.drawLine(center.x, center.y, topRight.x, topRight.y);
        g.drawLine(center.x, center.y, middleRight.x, middleRight.y);
        g.drawLine(center.x, center.y, bottomRight.x, bottomRight.y);
    }
}
