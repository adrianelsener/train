package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.ToggleCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;

class InvisiblePart implements TrackPart {
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
    public TrackPart toggle(final ToggleCallback toggler) {
        throw new IllegalStateException("toggle is not available on InvisiblePart");
    }

    @Override
    public TrackPart moveTo(final Point newLocation) {
        throw new IllegalStateException("moveTo is not available on InvisiblePart");
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