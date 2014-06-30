package ch.adrianelsener.train.gui.swing;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class ModeKeyListener implements KeyListener {
    private final static Logger logger = LoggerFactory.getLogger(ModeKeyListener.class);
    @Inject
    private EventBus bus;

    @Override
    public void keyTyped(final KeyEvent e) {
        final char keyChar = e.getKeyChar();
        switch (keyChar) {
        case 'g':
            logger.debug("Detected: keyfeature new track");
            bus.post(DrawMode.Track);
            break;
        case 'G':
            logger.debug("Detected: keyfeature new switch track");
            bus.post( DrawMode.SwitchTrack);
            break;
        case 'w':
            logger.debug("Detected: keyfeature new switch");
            bus.post( DrawMode.Switch);
            break;
        case 'v':
            logger.debug("Detected: keyfeature new dummySwitch");
            bus.post( DrawMode.DummySwitch);
            break;
        case 'r':
            logger.debug("Detected: keyfeature rotate switch");
            bus.post( DrawMode.Rotate);
            break;
        case 't':
            logger.debug("Detected: keyfeature toggle");
            bus.post( DrawMode.Toggle);
            break;
        case 'd':
            logger.debug("Detected: keyfeature delete");
            bus.post(DrawMode.Delete);
            break;
        case 'm':
            logger.debug("Detected: keyfeature move");
            bus.post(DrawMode.Move);
            break;
        case 's':
            logger.debug("Detected: keyfeature show details");
            bus.post(DrawMode.Detail);
            break;
        default:
            logger.debug("Detected keyboard interaction but no mapping");
            break;
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }

    @Override
    public void keyPressed(final KeyEvent e) {
    }
}
