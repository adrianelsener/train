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
        drawMode.getDrawMode().doWhileInitialButtonIsPressed(bus, e);
        bus.post(MousePositionEvent.create(e.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        bus.post(MousePositionEvent.create(e.getPoint()));
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        drawMode.getDrawMode().doWhileMouseIsDragged(bus, e);
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
        drawMode.getDrawMode().doWhileMouseReleased(bus, e);
    }
}