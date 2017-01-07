package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.common.InRangeCalculator;
import ch.adrianelsener.train.gui.swing.common.PointMover;
import ch.adrianelsener.train.gui.swing.common.RotationCalculator;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TripleSwitch extends BaseSwingSwitch implements TrackPart {
    private final static Logger logger = LoggerFactory.getLogger(TripleSwitch.class);
    private final transient RotationCalculator rotCalc;
    private final Point topRight;
    private final Point bottomRight;
    private final Point middleRight;
    private final Point middleLeft;
    private final SwitchState state;
    private final Pair<BoardId, SwitchId> upperPart;
    private final Pair<BoardId, SwitchId> lowerPart;

    private TripleSwitch(Builder builder) {
        super(builder.center, builder.angle);
        rotCalc = new RotationCalculator(builder.center, builder.angle);
        this.state = builder.state;

        final int rightX = center.x + 15;
        final int leftX = center.x - 10;
        final int upperY = center.y + 10;
        final int lowerY = center.y - 10;

        this.topRight = rotCalc.calc(rightX, upperY);
        this.middleRight = rotCalc.calc(rightX, center.y);
        this.bottomRight = rotCalc.calc(rightX, lowerY);
        this.middleLeft = rotCalc.calc(leftX, center.y);
        upperPart = builder.upperPart;
        lowerPart = builder.lowerPart;
    }

    public static TripleSwitch create(Point center, double angle) {
        return new Builder().setAngle(angle).setCenter(center).build();
    }

    TripleSwitch(Point center, double angle, final SwitchState state) {
        super(center, angle);
        rotCalc = new RotationCalculator(center, angle);
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
        return Builder.create(this).setAngle(angle + 45).build();
    }

    @Nonnull
    @Override
    public TripleSwitch toggle(@Nonnull SwitchCallback toggler) {
        final TripleSwitch tripleSwitch = Builder.create(this).setState(state.next()).build();
        tripleSwitch.applyState(toggler);
        return tripleSwitch;
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        logger.debug("start toggle with '{}'", this);
        switch (state) {
            case Upper:
                callback.toggleSwitch(upperPart.getRight(), upperPart.getLeft(), true);
                callback.toggleSwitch(lowerPart.getRight(), lowerPart.getLeft(), true);
                break;
            case Lower:
                callback.toggleSwitch(upperPart.getRight(), upperPart.getLeft(), true);
                callback.toggleSwitch(lowerPart.getRight(), lowerPart.getLeft(), false);
                break;
            case Middle:
                callback.toggleSwitch(upperPart.getRight(), upperPart.getLeft(), false);
                callback.toggleSwitch(lowerPart.getRight(), lowerPart.getLeft(), true);
                break;
        }
    }

    @Override
    public TripleSwitch moveTo(Point newLocation) {
        return Builder.create(this).setCenter(newLocation).build();
    }

    @Override
    public TripleSwitch move(Point direction) {
        return Builder.create(this).setCenter(PointMover.use(center).moveTo(direction)).build();
    }

    @Override
    public ImmutableCollection<Point> getInConnectors() {
        return ImmutableSet.of(middleLeft);
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

        public Builder(Iterator<String> iterator) {
            center = new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next()));
            angle = Double.parseDouble(iterator.next());
            state = SwitchState.valueOf(iterator.next());
            upperPart = Pair.of(BoardId.create(iterator.next()), SwitchId.fromValue(iterator.next()));
            lowerPart = Pair.of(BoardId.create(iterator.next()), SwitchId.fromValue(iterator.next()));
        }

        public Builder() {
            upperPart = Pair.of(BoardId.createDummy(), SwitchId.createDummy());
            lowerPart = Pair.of(BoardId.createDummy(), SwitchId.createDummy());
            state = SwitchState.Middle;
        }

        public static Builder create(Point center) {
            return new Builder().setCenter(center);
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

        public Builder setCenter(Point newLocation) {
            center = newLocation;
            return this;
        }

        public Builder setAngle(double angle) {
            this.angle = angle;
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
        final InRangeCalculator inRangeCalc = InRangeCalculator.create();
        if (inRangeCalc.isInRange(origin, middleLeft, 10)) {
            return middleLeft;
        } else if (inRangeCalc.isInRange(origin, topRight, 5)) {
            return topRight;
        } else if (inRangeCalc.isInRange(origin, bottomRight, 5)) {
            return bottomRight;
        } else if (inRangeCalc.isInRange(origin, middleRight, 5)) {
            return middleRight;
        } else {
            throw new IllegalArgumentException("getNextConnectionpoint soll nur aufgerufen werden wenn auch etwas in der naehe ist");
        }
    }

    @Override
    public void paint(Graphics2D g) {
        drawLable(g);
        drawMainSwitch(g);
        drawDirection(g);
    }

    private void drawLable(Graphics2D g) {
        g.setColor(Color.blue);
        g.drawString(upperPart.getLeft().toUiString() + "/" + upperPart.getRight().toUiString() + " : " + lowerPart.getLeft().toUiString() + "/" + lowerPart.getRight().toUiString(), center.x, center.y - 10);
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

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }
}
