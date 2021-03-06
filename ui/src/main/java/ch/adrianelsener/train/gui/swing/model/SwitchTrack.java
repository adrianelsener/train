package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.TrackView;
import ch.adrianelsener.train.gui.swing.common.PointMover;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SwitchTrack extends Track {
    private final SwitchId trackId;
    private final BoardId boardId;
    private final TrackState trackState;
    private final TrackView trackView;

    SwitchTrack(final Point startPoint, final Point endPoint) {
        this(startPoint, endPoint, SwitchId.createDummy(), BoardId.createDummy(), TrackState.Off, TrackView.Default);
    }

    SwitchTrack(final Point startPoint, final Point endPoint, SwitchId id, BoardId boardId, TrackState trackState, final TrackView trackView) {
        super(startPoint, endPoint);
        trackId = id;
        this.boardId = boardId;
        this.trackState = trackState;
        this.trackView = trackView;
    }

    @VisibleForTesting TrackState getTrackState() {
        return trackState;
    }

    @Override
    public TrackPart moveTo(final Point newLocation) {
        return this;
    }

    @Override
    public SwitchTrack move(Point direction) {
        final Point newStartPoint = PointMover.use(startPoint).moveTo(direction);
        final Point newEndPoint = PointMover.use(endPoint).moveTo(direction);
        return new SwitchTrack(newStartPoint, newEndPoint, trackId, boardId, trackState, trackView);
    }

    @Override
    public Collection<SwitchId> getId() {
        return Lists.newArrayList(trackId);
    }

    @Override
    public SwitchTrack setId(final String newId) {
        return new SwitchTrack(startPoint, endPoint, SwitchId.fromValue(newId), boardId, trackState, trackView);
    }

    @Override
    public Collection<BoardId> getBoardId() {
        return Lists.newArrayList(boardId);
    }

    @Override
    public SwitchTrack setBoardId(final String boardId) {
        return new SwitchTrack(startPoint, endPoint, trackId, BoardId.fromValue(boardId), trackState, trackView);
    }

    @Nonnull
    @Override
    public SwitchTrack toggle(@Nonnull final SwitchCallback toggler) {
        toggler.toggleSwitch(trackId, boardId, trackState.isOn());
        return new SwitchTrack(startPoint, endPoint, trackId, boardId, trackState.other(), trackView);
    }

    @Override
    public void applyState(@Nonnull SwitchCallback callback) {
        callback.toggleSwitch(trackId, boardId, !getTrackState().isOn());
    }

    @Override
    protected String getCsvIdentifier() {
        return "TS";
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
    protected void paintLable(final Graphics2D g) {
        int centerX = Math.max(getStart().x, getEnd().x) - (Math.abs(getStart().x - getEnd().x) / 2) + 5;
        int centerY = Math.max(getStart().y, getEnd().y) - (Math.abs(getStart().y - getEnd().y) / 2) + 5;

        g.setColor(Color.black);
        g.drawString(boardId.toUiString() + "/" + trackId.toUiString(), centerX - 10, centerY - 10);
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
    public SwitchTrack invertView(final boolean inverted) {
        final TrackView selected;
        if (inverted) {
            selected = TrackView.Inverted;
        } else {
            selected = TrackView.Default;
        }
        return new SwitchTrack(startPoint, endPoint, trackId, boardId, trackState, selected);
    }

    @Override
    public boolean isInverted() {
        return TrackView.Inverted == trackView;
    }
}
