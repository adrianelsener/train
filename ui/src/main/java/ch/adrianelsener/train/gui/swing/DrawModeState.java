package ch.adrianelsener.train.gui.swing;

import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
