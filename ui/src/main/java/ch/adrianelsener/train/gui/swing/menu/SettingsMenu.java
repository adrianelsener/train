package ch.adrianelsener.train.gui.swing.menu;

import ch.adrianelsener.train.gui.DummyToggler;
import ch.adrianelsener.train.gui.SwitchBoardToggler;
import com.google.common.eventbus.EventBus;

import javax.swing.*;

public class SettingsMenu extends JMenu {
    private final EventBus bus;

    public SettingsMenu(final EventBus bus) {
        super("Settings");
        this.bus = bus;
        add(createSetDummyBoard());
    }

    private JMenuItem createSetDummyBoard() {
        final JCheckBoxMenuItem setDummyBoard = new JCheckBoxMenuItem("Use Dummyboard", false);
        setDummyBoard.addActionListener(e -> {
            if (setDummyBoard.isSelected()) {
                bus.post(DummyToggler.create());
            } else {
                bus.post(SwitchBoardToggler.create());
            }
        });
        return setDummyBoard;
    }
}
