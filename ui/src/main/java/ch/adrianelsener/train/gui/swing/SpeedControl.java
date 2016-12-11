package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.driver.SpeedBoardDriver;
import ch.adrianelsener.train.driver.StatefulDummySpeedBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SpeedControl extends JFrame {
    private final static Logger logger = LoggerFactory.getLogger(SpeedControl.class);
    private static final long serialVersionUID = 1L;
    private boolean forward = true;
    private int currentSpeedValue = 0;

    private final SpeedBoardDriver speedBoardDriver;

    SpeedControl(Config config) {
        super("Speedy");
//        speedBoardDriver = new StatefulDummySpeedBoard(null);
        speedBoardDriver = createSpeedBoardDriver(config);
        setSize(50, 400);
        final LayoutManager layout = new GridLayout(4, 1);
        setLayout(layout);
        final JSlider slider = new JSlider(0, 250);
        final JTextField currentSpeed = new JTextField("0");
        add(currentSpeed);
        currentSpeed.setEditable(true);
        slider.setValue(currentSpeedValue);
        slider.setSize(20, 600);
        slider.setOrientation(SwingConstants.VERTICAL);
        slider.addMouseListener(new FireOnMouseRelease(this, slider));
        slider.addChangeListener(e -> currentSpeed.setText(String.valueOf(slider.getValue())));
        add(slider);
        JToggleButton forwardBackward = new JToggleButton();
        forwardBackward.setSelected(forward);
        forwardBackward.addActionListener(e -> forward = forwardBackward.isSelected());
        add(forwardBackward);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

    private static class FireOnMouseRelease implements MouseListener {
        private final SpeedControl speedControl;
        private final JSlider slider;

        FireOnMouseRelease(final SpeedControl speedControl, JSlider slider) {
            this.speedControl = speedControl;
            this.slider = slider;
        }

        @Override
        public void mouseClicked(final MouseEvent e) {

        }

        @Override
        public void mousePressed(final MouseEvent e) {

        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            speedControl.currentSpeedValue = slider.getValue();
            logger.debug("mouse Released value is: '{}'", speedControl.currentSpeedValue);
            int speedWithDirection = speedControl.currentSpeedValue;
            if (speedControl.forward) {
                speedWithDirection *= -1;
            }
            speedControl.speedBoardDriver.setSpeed(speedWithDirection);
        }

        @Override
        public void mouseEntered(final MouseEvent e) {

        }

        @Override
        public void mouseExited(final MouseEvent e) {

        }
    }
}
