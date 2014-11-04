package ch.adrianelsener.train.gui.swing.events;

import java.awt.*;

public enum ShowDrawingCrossEvent {
    SHOW {
        @Override
        public void draw(Graphics2D g2d, Dimension size, Point position) {
            g2d.setColor(Color.GRAY);
            if (null != position) {
                g2d.drawLine(0, position.y, size.width, position.y);
                g2d.drawLine(position.x, 0, position.x, size.hashCode());
            }
        }
    }, HIDE {
        @Override
        public void draw(Graphics2D g2d, Dimension size, Point position) {

        }
    };

    public abstract void draw(Graphics2D g2d, Dimension size, Point position);
}
