package ch.adrianelsener.train.gui.swing.menu;

import ch.adrianelsener.train.gui.swing.CheckSwitch;
import ch.adrianelsener.train.gui.swing.DetailWindow;
import ch.adrianelsener.train.gui.swing.events.RasterEnabledEvent;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewMenu extends JMenu {
    private final DetailWindow details;
    private final CheckSwitch switchChecker;
    private final boolean detailsDefaultVisible = true;
    private final EventBus bus;

    public ViewMenu(final DetailWindow details, final CheckSwitch switchChecker, final EventBus bus) {
        super("View");
        this.details = details;
        this.switchChecker = switchChecker;
        this.bus = bus;
        final JMenuItem showDetail = createShowDetailMenuItem();
        final JMenuItem showSwitchChecker = createShowSwitchCheckerMenuItem();
        final JMenuItem showRaster = createShowRasterMenuItem();
        add(showDetail);
        add(showSwitchChecker);
        add(showRaster);
    }

    private JMenuItem createShowDetailMenuItem() {
        final JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show Details");
        show.addActionListener(e -> details.setVisible(show.isSelected()));
        return show;
    }

    private JMenuItem createShowSwitchCheckerMenuItem() {
        final JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show Switch Checker", switchChecker.isVisible());
        show.addActionListener(e -> switchChecker.setVisible(show.isSelected()));
        switchChecker.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                show.setSelected(detailsDefaultVisible);
            }
        });
        return show;
    }

    private JMenuItem createShowRasterMenuItem() {
        JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show Raster", true);
        show.addActionListener(e -> bus.post(RasterEnabledEvent.create(show.isSelected())));
        return show;
    }
}
