package ch.adrianelsener.train.gui.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class ModeKeyListener implements KeyListener {
    private final static Logger logger = LoggerFactory.getLogger(ModeKeyListener.class);
    private DrawMode mode = DrawMode.NoOp;

    @Override
    public void keyTyped(final KeyEvent e) {
        final char keyChar = e.getKeyChar();
        switch (keyChar) {
        case 'g':
            logger.debug("Detected: keyfeature new track");
            mode = DrawMode.Track;
            break;
        case 'G':
            logger.debug("Detected: keyfeature new switch track");
            mode = DrawMode.SwitchTrack;
            break;
        case 'w':
            logger.debug("Detected: keyfeature new switch");
            mode = DrawMode.Switch;
            break;
        case 'v':
            logger.debug("Detected: keyfeature new dummySwitch");
            mode = DrawMode.DummySwitch;
            break;
        case 'r':
            logger.debug("Detected: keyfeature rotate switch");
            mode = DrawMode.Rotate;
            break;
        case 't':
            logger.debug("Detected: keyfeature toggle");
            mode = DrawMode.Toggle;
            break;
        case 'd':
            logger.debug("Detected: keyfeature delete");
            mode = DrawMode.Delete;
            break;
        case 'm':
            logger.debug("Detected: keyfeature move");
            mode = DrawMode.Move;
            break;
        case 's':
            logger.debug("Detected: keyfeature show details");
            mode = DrawMode.Detail;
            break;
        default:
            logger.debug("Detected keyboard interaction but no mapping");
            break;
        }
    }

    public DrawMode getDrawMode() {
        return mode;
    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }

    @Override
    public void keyPressed(final KeyEvent e) {
    }

    public void resetDrawMode() {
        mode = DrawMode.NoOp;
    }

}
