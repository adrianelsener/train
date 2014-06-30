package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.swing.common.MenuItemBuilder;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPopupMenu extends JPopupMenu {
    private EventBus bus;

    public MainPopupMenu() {
        for (DrawMode drawMode : DrawMode.values()) {
            add(MenuItemBuilder.create(drawMode.name()).addActionListener(e -> bus.post(drawMode)).build());
        }
    }

    public void setBus(EventBus bus) {
        this.bus = bus;
    }


    public static class PopupClickListener extends MouseAdapter {
        @Inject
        private EventBus bus;
        final MainPopupMenu menu = new MainPopupMenu();

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            menu.setBus(bus);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

}
