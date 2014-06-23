package ch.adrianelsener.train.gui.swing;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPopupMenu extends JPopupMenu {
    private final JMenuItem item;
    public MainPopupMenu() {
        item = new JMenuItem("fooo");
        add(item);
    }

    public static class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseReleased(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e){
            final MainPopupMenu menu = new MainPopupMenu();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
