package ch.adrianelsener.train.gui.swing;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawModeState {
    private static final Logger logger = LoggerFactory.getLogger(DrawModeState.class);
    private DrawMode mode = DrawMode.NoOp;

    @Subscribe
    public void setMode(DrawMode newMode) {
        logger.debug("Received new Mode {}", newMode);
        mode = newMode;
    }

    public DrawMode getDrawMode() {
        return mode;
    }
}
