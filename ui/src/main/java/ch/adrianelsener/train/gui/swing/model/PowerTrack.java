package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.driver.SpeedBoardDriver;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.TrackView;
import ch.adrianelsener.train.gui.swing.common.PointMover;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * BoardId will be the microcontroller and SwitchId the PWM
 */
public class PowerTrack extends Track implements Powerable {
    private final TrackState trackState;
    private final TrackView trackView;
    private final BoardId boardId;
    @Inject
    private Config config;

    PowerTrack(final Point startPoint, final Point endPoint, BoardId boardId, TrackState trackState, final TrackView trackView){
        super(startPoint, endPoint);
        this.boardId = boardId;
        this.trackState = trackState;
        this.trackView = trackView;
//        Config config = other by boardId P+BoardId
    }

    PowerTrack(Point startPoint, Point endPoint) {
        this(startPoint, endPoint, BoardId.createDummy(), TrackState.Off, TrackView.Default);
    }

    @Nonnull
    @Override
    public TrackPart toggle(@Nonnull SwitchCallback toggler) {
        return new PowerTrack(startPoint, endPoint, boardId, trackState.other(), trackView);
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        // NOOP
    }

    @Override
    public TrackPart moveTo(Point newLocation) {
        return this;
    }

    @Override
    public TrackPart move(Point direction) {
        Point newStart = PointMover.use(startPoint).moveTo(direction);
        Point newEnd = PointMover.use(endPoint).moveTo(direction);
        return new PowerTrack(newStart, newEnd);
    }

    @Override
    public Collection<SwitchId> getId() {
        return Lists.newArrayList();
    }

    @Override
    public PowerTrack setId(final String newId) {
        return new PowerTrack(startPoint, endPoint, boardId, trackState, trackView);
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(boardId);
    }

    @Override
    public TrackPart setBoardId(String boardId) {
        return new PowerTrack(startPoint, endPoint, BoardId.fromValue(boardId), trackState, trackView);
    }

    @Override
    public TrackPart invertView(boolean inverted) {
        final TrackView selected;
        if (inverted) {
            selected = TrackView.Inverted;
        } else {
            selected = TrackView.Default;
        }
        return new PowerTrack(startPoint, endPoint, boardId, trackState, selected);
    }

    @Override
    public boolean isInverted() {
        return TrackView.Inverted == trackView;
    }

    @Override
    protected void paintLable(Graphics2D g) {
        int centerX = Math.max(getStart().x, getEnd().x) - (Math.abs(getStart().x - getEnd().x) / 2) + 5;
        int centerY = Math.max(getStart().y, getEnd().y) - (Math.abs(getStart().y - getEnd().y) / 2) + 5;
        g.setColor(Color.black);
        g.drawString(boardId.toUiString(), centerX - 10, centerY - 10);
    }

    @Override
    protected Color getLineColor() {
        if ((TrackState.On == trackState && TrackView.Default == trackView) || TrackState.Off == trackState && TrackView.Inverted == trackView) {
            return Color.red;
        } else {
            return Color.blue;
        }
    }

    @Override
    protected Collection<Object> individualStorageProperties() {
        final List<Object> ownProps = Lists.newArrayList();
        ownProps.add(boardId.toSerializable());
        ownProps.add(trackState);
        ownProps.add(trackView);
        return ownProps;
    }

    @Override
    protected String getCsvIdentifier() {
        return "PT";
    }

    @Override
    public void setCurrentPower(int powerlevel) {

    }

    private SpeedBoardDriver createSpeedBoardDriver(final Config config) {
        final SpeedBoardDriver speedBoard;
        final String drvClassName = config.getChild("DRV");
        try {
            speedBoard = SpeedBoardDriver.class.cast(Class.forName(drvClassName).getConstructor(Config.class).newInstance(config));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
        return speedBoard;
    }
}
