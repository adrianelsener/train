package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.swing.events.DraftPartCreationAction;
import ch.adrianelsener.train.gui.swing.events.MousePositionEvent;
import ch.adrianelsener.train.gui.swing.events.PartCreationAction;
import ch.adrianelsener.train.gui.swing.events.UpdateDraftPart;
import ch.adrianelsener.train.gui.swing.events.UpdateMoveDraftPart;
import ch.adrianelsener.train.gui.swing.events.UpdatePart;
import ch.adrianelsener.train.gui.swing.events.UpdatePoint;
import ch.adrianelsener.train.gui.swing.model.InvisiblePart;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TrainMouseAdapter  extends MouseAdapter {
    private final Logger logger = LoggerFactory.getLogger(TrainMouseAdapter.class);
    private final DrawModeState drawMode;

    @Inject
    private EventBus bus;

    public TrainMouseAdapter(DrawModeState drawMode) {
        this.drawMode = drawMode;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        logger.debug("Mouse pressed while draw mode is set to {}", drawMode);
        if (e.isPopupTrigger()) {
            return;
        }
        final int button = e.getButton();
        logger.debug("button nr {} is pressed", button);
        switch (drawMode.getDrawMode()) {
            case Track:
            case SwitchTrack:
                bus.post(UpdatePoint.createCreationStartPoint(e.getPoint()));
                break;
            case Move:
                final UpdatePoint.CreationStartPoint event = UpdatePoint.createCreationStartPoint(e.getPoint());
                logger.debug("Fire event '{}'", event);
                bus.post(event);
                break;
            case Switch:
                bus.post(DraftPartCreationAction.createSwitch(e.getPoint()));
                break;
            case Detail:
                bus.post(UpdatePoint.createDetailCoordinates(e.getPoint()));
                break;
            case DummySwitch:
                bus.post(DraftPartCreationAction.createDummySwitch(e.getPoint()));
                break;
            case TripleSwitch:
                bus.post(DraftPartCreationAction.createTripleSwitch(e.getPoint()));
                break;
            case NoOp:
            case Toggle:
                bus.post(UpdatePart.createToggle(e.getPoint()));
                break;
            case Rotate:
            case Delete:
                break;
        }
        bus.post(MousePositionEvent.create(e.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        bus.post(MousePositionEvent.create(e.getPoint()));
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        switch (drawMode.getDrawMode()) {
            case Switch:
                bus.post(DraftPartCreationAction.createSwitch(e.getPoint()));
                break;
            case Track:
                bus.post(DraftPartCreationAction.createTrack(e.getPoint()));
                break;
            case SwitchTrack:
                bus.post(DraftPartCreationAction.createSwitchTrack(e.getPoint()));
                break;
            case Move:
                bus.post(UpdateMoveDraftPart.create(e.getPoint()));
                break;
            case DummySwitch:
                bus.post(DraftPartCreationAction.createDummySwitch(e.getPoint()));
                break;
            case TripleSwitch:
                bus.post(DraftPartCreationAction.createTripleSwitch(e.getPoint()));
                break;
            case Rotate:
            case Toggle:
            case Delete:
            case Detail:
            case NoOp:
                break;
        }
        bus.post(MousePositionEvent.create(e.getPoint()));
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        logger.trace("Mouse release with drawMode {}", drawMode);
        if (e.isPopupTrigger()) {
            return;
        }
        logger.debug("mouse button {} was released", e.getButton());
        final InvisiblePart invisibleDraftPart = InvisiblePart.create();
        bus.post(UpdateDraftPart.create(invisibleDraftPart));
        switch (drawMode.getDrawMode()) {
            case Track:
                bus.post(PartCreationAction.createTrack(e.getPoint()));
                break;
            case SwitchTrack:
                bus.post(PartCreationAction.createSwitchTrack(e.getPoint()));
                break;
            case Switch:
                bus.post(PartCreationAction.createSwitch(e.getPoint()));
                break;
            case TripleSwitch:
                bus.post(PartCreationAction.createTripleSwitch(e.getPoint()));
                break;
            case DummySwitch:
                bus.post(PartCreationAction.createDummySwitch(e.getPoint()));
                break;
            case Rotate:
                bus.post(UpdatePart.createMirror(e.getPoint()));
                break;
            case Delete:
                bus.post(UpdatePart.deletePart(e.getPoint()));
                break;
            case Move:
                bus.post(UpdatePart.movePart(e.getPoint()));
                break;
            case NoOp:
            case Toggle:
            case Detail:
                break;
        }
    }
}