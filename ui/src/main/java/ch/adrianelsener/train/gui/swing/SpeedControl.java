package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.driver.SpeedBoardDriver;
import ch.adrianelsener.train.driver.SpeedBoardV1;
import com.google.common.base.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpeedControl extends JFrame {
    private final static Logger logger = LoggerFactory.getLogger(SpeedControl.class);
    private static final long serialVersionUID = 1L;
    private final JSlider slider;
    private final SpeedBoardDriver speedDriver;

    public SpeedControl() {
        super("Speedy");
        speedDriver = new SpeedBoardV1(null, null);
        setSize(50, 200);
        final LayoutManager layout = new GridLayout(4, 1);
        setLayout(layout);
        JTextField currentSpeed = new JTextField("0");
        add(currentSpeed);
        currentSpeed.setEditable(false);
        slider = new JSlider(0, 100);
        slider.setValue(0);
        slider.setSize(20, 100);
        slider.setOrientation(SwingConstants.VERTICAL);
        slider.addChangeListener(new SliderMoveListener(currentSpeed, speedDriver));
        add(slider);
        JToggleButton forwardBackward = new JToggleButton();
        add(forwardBackward);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final KeyListener keyListener = new SpeedKeyListener();
        addKeyListener(keyListener);
    }

    private static class SliderMoveListener implements ChangeListener {

        private final JTextField currentSpeed;
        private final SpeedBoardDriver speedDriver;

        private SliderMoveListener(JTextField currentSpeed, SpeedBoardDriver speedDriver) {
            this.currentSpeed = currentSpeed;
            this.speedDriver = speedDriver;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Verify.verify(e.getSource() instanceof JSlider);
            JSlider slider = (JSlider) e.getSource();
            logger.debug("Current is {}", slider.getValue());
            currentSpeed.setText(Integer.toString(slider.getValue()));
            speedDriver.setSpeed(slider.getValue());
        }
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
