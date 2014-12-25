package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.swing.common.MenuItemBuilder;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

public class MainPopupMenu extends JPopupMenu {
    private static final Logger logger = LoggerFactory.getLogger(MainPopupMenu.class);
    private EventBus bus;
    private Optional<TrackPart> optPart = Optional.empty();
    private final JTextField txtBoardId = new JTextField();

    public MainPopupMenu() {
        for (DrawMode drawMode : DrawMode.values()) {
            add(MenuItemBuilder.create(drawMode.name()).addActionListener(e -> bus.post(drawMode)).build());
        }
        add(txtBoardId);
        txtBoardId.setVisible(false);
    }

    public void setBus(EventBus bus) {
        this.bus = bus;
    }

    public void setPart(Optional<TrackPart> newPart) {
        logger.debug("set popup menu part to {}", newPart);
        this.optPart = newPart;
        logger.debug("evaluted part '{}' for popupmenu", optPart);
        if (optPart.isPresent()) {
            txtBoardId.setText(optPart.get().getBoardId().toString());
            txtBoardId.setVisible(true);
        } else {
            txtBoardId.setVisible(false);
        }
    }

    public static class PopupClickListener extends MouseAdapter {
        @Inject
        private EventBus bus;
        @Inject
        private Odb<TrackPart> db;

        final MainPopupMenu menu = new MainPopupMenu();

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPop(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPop(e);
            }
        }

        private void doPop(MouseEvent e) {
            menu.setBus(bus);
            Optional<TrackPart> partNearBy = db.filterUnique(part -> part.isNear(e.getPoint()));
            menu.setPart(partNearBy);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private static class JLabelWithTextField extends JComponent {
        private final JLabel lblName;
        private final JTextField txtValue;

        public JLabelWithTextField(JLabel lblName, JTextField txtValue) {
            this.lblName = lblName;
            this.txtValue = txtValue;
        }
    }

}
