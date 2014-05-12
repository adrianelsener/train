package ch.adrianelsener.train.gui.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpeedControl extends JFrame {
    private final static Logger logger = LoggerFactory.getLogger(SpeedControl.class);
    private static final long serialVersionUID = 1L;
    private final JSlider slider;

    private int currentSpeedValue = 0;

    public SpeedControl() {
        super("Speedy");
        setSize(50, 200);
        final LayoutManager layout = new GridLayout(4, 1);
        setLayout(layout);
        JTextField currentSpeed = new JTextField("0");
        add(currentSpeed);
        currentSpeed.setEditable(false);
        slider = new JSlider(0, 100);
        slider.setValue(currentSpeedValue);
        slider.setSize(20, 100);
        slider.setOrientation(SwingConstants.VERTICAL);
        final ChangeListener sliderMoveListener = e -> {
            final int difference = slider.getValue() - currentSpeedValue;
            currentSpeedValue = slider.getValue();
            logger.debug("Current difference ist {}", difference);
        };
        slider.addChangeListener(sliderMoveListener);
        add(slider);
        JToggleButton forwardBackward = new JToggleButton();
        add(forwardBackward);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final KeyListener keyListener = new SpeedKeyListener();
        addKeyListener(keyListener);
    }

    private static class SpeedKeyListener implements  KeyListener {

        @Override
        public void keyTyped(final KeyEvent e) {

        }

        @Override
        public void keyPressed(final KeyEvent e) {

        }

        @Override
        public void keyReleased(final KeyEvent e) {

        }
    }

}
