package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.common.net.NetAddress;
import ch.adrianelsener.train.driver.PiTwiSpeedBoardV1;
import ch.adrianelsener.train.driver.SpeedBoardDriver;
import ch.adrianelsener.train.pi.client.TcpGsonClient;
import ch.adrianelsener.train.pi.tcp.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SpeedControl extends JFrame {
    private final static Logger logger = LoggerFactory.getLogger(SpeedControl.class);
    private static final long serialVersionUID = 1L;
    private final JSlider slider;

    private int currentSpeedValue = 0;
    private final SpeedBoardDriver speedBoardDriver;

    public SpeedControl() {
        super("Speedy");
        TcpClient client = new TcpGsonClient(NetAddress.create("172.16.100.120", 2323));
        speedBoardDriver = new PiTwiSpeedBoardV1(client);
        setSize(50, 400);
        final LayoutManager layout = new GridLayout(4, 1);
        setLayout(layout);
        JTextField currentSpeed = new JTextField("0");
        add(currentSpeed);
        currentSpeed.setEditable(false);
        slider = new JSlider(0, 250);
        slider.setValue(currentSpeedValue);
        slider.setSize(20, 300);
        slider.setOrientation(SwingConstants.VERTICAL);
        final ChangeListener sliderMoveListener = e -> {
            currentSpeedValue = slider.getValue();
        };
        slider.addMouseListener(new FireOnMouseRelease(this));
        slider.addChangeListener(sliderMoveListener);
        add(slider);
        JToggleButton forwardBackward = new JToggleButton();
        add(forwardBackward);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final KeyListener keyListener = new SpeedKeyListener(this);
        slider.addKeyListener(keyListener);
    }

    private static class FireOnMouseRelease implements MouseListener {
        private final SpeedControl speedControl;

        public FireOnMouseRelease(final SpeedControl speedControl) {
            this.speedControl = speedControl;
        }

        @Override
        public void mouseClicked(final MouseEvent e) {

        }

        @Override
        public void mousePressed(final MouseEvent e) {

        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            logger.debug("mouse Released value is: '{}'", speedControl.currentSpeedValue);
            speedControl.speedBoardDriver.setSpeed(speedControl.currentSpeedValue);
        }

        @Override
        public void mouseEntered(final MouseEvent e) {

        }

        @Override
        public void mouseExited(final MouseEvent e) {

        }
    }

    private static class SpeedKeyListener implements KeyListener {

        private final SpeedControl speedControl;

        public SpeedKeyListener(final SpeedControl speedControl) {
            this.speedControl = speedControl;
            speedControl.speedBoardDriver.setSpeed(speedControl.currentSpeedValue);
        }

        @Override
        public void keyTyped(final KeyEvent e) {

        }

        @Override
        public void keyPressed(final KeyEvent e) {

        }

        @Override
        public void keyReleased(final KeyEvent e) {
            logger.debug("key release value is: '{}'", speedControl.currentSpeedValue);
        }
    }

}
