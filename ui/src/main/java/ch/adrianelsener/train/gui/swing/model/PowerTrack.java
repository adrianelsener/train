package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.TrackView;
import ch.adrianelsener.train.gui.swing.common.PointMover;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * BoardId will be the microcontroller and SwitchId the PWM
 */
public class PowerTrack extends Track {
    private final TrackState trackState;
    private final TrackView trackView;
    private final BoardId boardId;
    private final SwitchId trackId;

    PowerTrack(final Point startPoint, final Point endPoint, SwitchId id, BoardId boardId, TrackState trackState, final TrackView trackView){
        super(startPoint, endPoint);
        trackId = id;
        this.boardId = boardId;
        this.trackState = trackState;
        this.trackView = trackView;
    }

    PowerTrack(Point startPoint, Point endPoint) {
        this(startPoint, endPoint, SwitchId.createDummy(), BoardId.createDummy(), TrackState.Off, TrackView.Default);
    }

    public static Track createPowerTrack(final Iterator<String> iterator) {
        return new PowerTrack(new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())), new Point(
                Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())), SwitchId.fromValue(iterator.next()), BoardId.fromValue(iterator.next()), TrackState.valueOf(iterator.next()), TrackView.valueOf(iterator.next()));
    }

    @Nonnull
    @Override
    public TrackPart toggle(@Nonnull SwitchCallback toggler) {
        return new PowerTrack(startPoint, endPoint, trackId, boardId, trackState.other(), trackView);
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
        return Lists.newArrayList(trackId);
    }

    @Override
    public PowerTrack setId(final String newId) {
        return new PowerTrack(startPoint, endPoint, SwitchId.fromValue(newId), boardId, trackState, trackView);
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(boardId);
    }

    @Override
    public TrackPart setBoardId(String boardId) {
        return new PowerTrack(startPoint, endPoint, trackId, BoardId.fromValue(boardId), trackState, trackView);
    }

    @Override
    public TrackPart invertView(boolean inverted) {
        final TrackView selected;
        if (inverted) {
            selected = TrackView.Inverted;
        } else {
            selected = TrackView.Default;
        }
        return new PowerTrack(startPoint, endPoint, trackId, boardId, trackState, selected);
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
        g.drawString(boardId.toUiString() + "/" + trackId.toUiString(), centerX - 10, centerY - 10);
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
        ownProps.add(trackId.toSerializable());
        ownProps.add(boardId.toSerializable());
        ownProps.add(trackState);
        ownProps.add(trackView);
        return ownProps;
    }

    @Override
    protected String getCsvIdentifier() {
        return "PT";
    }
}
