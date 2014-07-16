package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.SwitchCallback;
import com.google.common.collect.ImmutableCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;

public class InvisiblePart implements TrackPart {
    private final static Logger logger = LoggerFactory.getLogger(InvisiblePart.class);
    private static final InvisiblePart INSTANCE = new InvisiblePart();

    private InvisiblePart() {
        super();
    }

    public static InvisiblePart create() {
        return INSTANCE;
    }

    @Override
    public boolean isNear(final Point point) {
        throw new IllegalStateException("isNear is not available on InvisiblePart");
    }

    @Override
    public Point getNextConnectionpoint(final Point origin) {
        throw new IllegalStateException("getNextConnectionpoint is not available on InvisiblePart");
    }

    @Override
    public void paint(final Graphics2D g) {

    }

    @Override
    public TrackPart createMirror() {
        throw new IllegalStateException("createMirror is not available on InvisiblePart");
    }

    @Override
    public Collection<Object> getDataToPersist() {
        throw new IllegalStateException("asCsvString is not available on InvisiblePart");
    }

    @Override
    public TrackPart toggle(final SwitchCallback toggler) {
        logger.warn("toggle on '{}' has no impact!", this);
        return this;
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        logger.info("applyState on '{}' is not available", this);
    }

    @Override
    public TrackPart moveTo(final Point newLocation) {
        throw new IllegalStateException("moveTo is not available on InvisiblePart");
    }

    @Override
    public ImmutableCollection<Point> getInConnectors() {
        throw new IllegalStateException("Invisible part can not be connected");
    }

    @Override
    public ImmutableCollection<Point> getOutConnectors() {
        throw new IllegalStateException("Invisible part can not be connected");
    }

    @Override
    public boolean isPipe() {
        return true;
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
        logger.info("setId does NoOp while InvisiblePart");
        return this;
    }

    @Override
    public TrackPart setBoardId(final String boardId) {
        logger.info("setBoard does NoOp while InvisiblePart");
        return this;
    }

    @Override
    public InvisiblePart invertView(final boolean inverted) {
        logger.info("invertView does NoOp while {}", this.getClass().getSimpleName());
        return this;
    }

    @Override
    public boolean isInverted() {
        return false;
    }

}