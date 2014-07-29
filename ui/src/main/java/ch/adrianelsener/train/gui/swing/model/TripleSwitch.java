package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.common.RotationCalculator;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.DoubleStream;

public class TripleSwitch implements TrackPart {
    private final static Logger logger = LoggerFactory.getLogger(TripleSwitch.class);
    private final Point center;
    private final double angle;
    private final transient RotationCalculator rotCalc;
    private final Point topRight;
    private final Point bottomRight;
    private final Point middleRight;
    private final Point middleLeft;
    private final SwitchState state;
    private final Pair<BoardId, SwitchId> upperPart;
    private final Pair<BoardId, SwitchId> lowerPart;

    private TripleSwitch(TripleSwitch orig, SwitchState next) {
        this(orig.center, orig.angle, next);
    }

    private TripleSwitch(Builder builder) {
        rotCalc = new RotationCalculator(builder.center, builder.angle);
        this.center = builder.center;
        this.angle = builder.angle;
        this.state = builder.state;

        final int rightX = center.x + 15;
        final int leftX = center.x - 10;
        final int upperY = center.y + 10;
        final int lowerY = center.y - 10;

        this.topRight = new Point(rightX, upperY);
        this.middleRight = new Point(rightX, center.y);
        this.bottomRight = new Point(rightX, lowerY);
        this.middleLeft = new Point(leftX, center.y);
        upperPart = builder.upperPart;
        lowerPart = builder.lowerPart;
    }

    public static TripleSwitch create(Point center, double angle) {
        return new TripleSwitch(center, angle);
    }

    TripleSwitch(Point center, double angle, final SwitchState state) {
        rotCalc = new RotationCalculator(center, angle);
        this.center = center;
        this.angle = angle;
        this.state = state;

        final int rightX = center.x + 15;
        final int leftX = center.x - 10;
        final int upperY = center.y + 10;
        final int lowerY = center.y - 10;

        this.topRight = new Point(rightX, upperY);
        this.middleRight = new Point(rightX, center.y);
        this.bottomRight = new Point(rightX, lowerY);
        this.middleLeft = new Point(leftX, center.y);
        upperPart = Pair.of(BoardId.createDummy(), SwitchId.createDummy());
        lowerPart = Pair.of(BoardId.createDummy(), SwitchId.createDummy());
    }

    TripleSwitch(Point center, double angle) {
        this(center, angle, SwitchState.Middle);
    }

    TripleSwitch(Point middle) {
        this(middle, 0);
    }

    @Override
    public TripleSwitch rotate() {
        throw new IllegalStateException("Not yet implemented");
    }

    @Nonnull
    @Override
    public TripleSwitch toggle(@Nonnull SwitchCallback toggler) {
        logger.debug("start toggle with '{}'", this);
        switch (state) {
            case Middle:
                toggler.toggleSwitch(upperPart.getRight(), upperPart.getLeft(), true);
                toggler.toggleSwitch(lowerPart.getRight(), lowerPart.getLeft(), true);
                break;
            case Upper:
                toggler.toggleSwitch(upperPart.getRight(), upperPart.getLeft(), true);
                toggler.toggleSwitch(lowerPart.getRight(), lowerPart.getLeft(), false);
                break;
            case Lower:
                toggler.toggleSwitch(upperPart.getRight(), upperPart.getLeft(), false);
                toggler.toggleSwitch(lowerPart.getRight(), lowerPart.getLeft(), true);
                break;
        }

        return Builder.create(this).setState(state.next()).build();
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public TripleSwitch moveTo(Point newLocation) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public ImmutableCollection<Point> getInConnectors() {
        throw new IllegalStateException("Not yet implemented"); 
    }

    @Override
    public ImmutableCollection<Point> getOutConnectors() {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public boolean isPipe() {
        return false;
    }

    @Override
    public Collection<SwitchId> getId() {
        return Lists.newArrayList(upperPart.getRight(), lowerPart.getRight());
    }

    @Override
    public TripleSwitch setId(String newId) {
        return Builder.create(this).setId(newId).build();
    }

    public static Builder create(Iterator<String> data) {
        return new Builder(data);
    }

    public static class Builder {
        private Point center;
        private double angle;
        private SwitchState state;
        private Pair<BoardId, SwitchId> upperPart;
        private Pair<BoardId, SwitchId> lowerPart;

        private Builder(TripleSwitch original) {
            center = original.center;
            angle = original.angle;
            state = original.state;
            upperPart = original.upperPart;
            lowerPart = original.lowerPart;
        }

        private Builder(Iterator<String> iterator) {
            center = new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next()));
            angle = Double.parseDouble(iterator.next());
            state = SwitchState.valueOf(iterator.next());
            upperPart = Pair.of(BoardId.create(iterator.next()), SwitchId.fromValue(iterator.next()));
            lowerPart = Pair.of(BoardId.create(iterator.next()), SwitchId.fromValue(iterator.next()));
        }

        public static Builder create(TripleSwitch original) {
            return new Builder(original);
        }

        public Builder setId(String newId) {
            final String[] split = newId.split("/");
            final SwitchId upperSwitch = SwitchId.fromValue(split[0]);
            final SwitchId lowerSwitch = SwitchId.fromValue(split[1]);
            upperPart = Pair.of(upperPart.getLeft(), upperSwitch);
            lowerPart = Pair.of(lowerPart.getLeft(), lowerSwitch);
            return this;
        }

        public TripleSwitch build() {
            return new TripleSwitch(this);
        }

        public Builder setBoardId(String boardId) {
            final String[] split = boardId.split("/");
            upperPart = Pair.of(BoardId.fromValue(split[0]), upperPart.getRight());
            lowerPart = Pair.of(BoardId.fromValue(split[1]), lowerPart.getRight());
            return this;
        }

        public Builder setState(SwitchState state) {
            this.state = state;
            return this;
        }
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(upperPart.getLeft(), lowerPart.getLeft());
    }

    @Override
    public TripleSwitch setBoardId(String boardId) {
        return Builder.create(this).setBoardId(boardId).build();
    }

    @Override
    public TripleSwitch invertView(boolean inverted) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public boolean isInverted() {
        return false;
    }

    @Override
    public Collection<Object> getDataToPersist() {
        final List<Object> objects = Lists.newArrayList();
        objects.add(getClass().getSimpleName());
        objects.add(center.x);
        objects.add(center.y);
        objects.add(angle);
        objects.add(state.name());
        objects.add(upperPart.getLeft().toSerializable());
        objects.add(upperPart.getRight().toSerializable());
        objects.add(lowerPart.getLeft().toSerializable());
        objects.add(lowerPart.getRight().toSerializable());
        return objects;
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
    public Point getNextConnectionpoint(Point origin) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public void paint(Graphics2D g) {
        drawMainSwitch(g);
        drawDirection(g);
    }

    private void drawDirection(Graphics2D g) {
        state.drawRed(g, this);
    }

    private void drawMainSwitch(Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.blue);
        g.drawLine(middleLeft.x, middleLeft.y, center.x, center.y);
        g.drawLine(center.x, center.y, topRight.x, topRight.y);
        g.drawLine(center.x, center.y, middleRight.x, middleRight.y);
        g.drawLine(center.x, center.y, bottomRight.x, bottomRight.y);
    }


    private enum SwitchState {
        Middle {
            @Override
            public void drawRedIndividual(Graphics2D g, TripleSwitch switc) {
                g.drawLine(switc.center.x, switc.center.y, switc.topRight.x, switc.topRight.y);
                g.drawLine(switc.center.x, switc.center.y, switc.bottomRight.x, switc.bottomRight.y);
            }

            @Override
            public SwitchState next() {
                return Upper;
            }
        },
        Upper {
            @Override
            public void drawRedIndividual(Graphics2D g, TripleSwitch switc) {
                g.drawLine(switc.center.x, switc.center.y, switc.middleRight.x, switc.middleRight.y);
                g.drawLine(switc.center.x, switc.center.y, switc.bottomRight.x, switc.bottomRight.y);
            }

            @Override
            public SwitchState next() {
                return Lower;
            }
        },
        Lower {
            @Override
            protected void drawRedIndividual(Graphics2D g, TripleSwitch switc) {
                g.drawLine(switc.center.x, switc.center.y, switc.topRight.x, switc.topRight.y);
                g.drawLine(switc.center.x, switc.center.y, switc.middleRight.x, switc.middleRight.y);
            }

            @Override
            public SwitchState next() {
                return Middle;
            }
        };

        protected abstract void drawRedIndividual(Graphics2D g, TripleSwitch switc);

        public void drawRed(Graphics2D g, TripleSwitch switc) {
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.red);
            drawRedIndividual(g, switc);
        }

        public abstract SwitchState next();

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }
}
