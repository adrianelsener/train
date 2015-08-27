package ch.adrianelsener.train.gui.swing.menu;

import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.odb.api.OdbFunction;
import ch.adrianelsener.odb.api.OdbPredicate;
import ch.adrianelsener.train.gui.swing.events.ShowDrawingCrossEvent;
import ch.adrianelsener.train.gui.swing.events.UpdateAllSwitches;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;

public class ToolsMenu extends JMenu {
    private final Odb<TrackPart> db;
    private final EventBus bus;

    public ToolsMenu(final Odb<TrackPart> db, final EventBus bus) {
        super("Tools");
        this.db = db;
        this.bus = bus;
        add(createMoveAllRight());
        add(createMoveAllLeft());
        add(createMoveAllUp());
        add(createMoveAllDown());
        add(createResendSwitchStates());
        add(createShowCrossWhileDrawing());
    }

    private JMenuItem createMoveAllRight() {
        return createMoveAllToDirection("10 >", new Point(10, 0));
    }

    private JMenuItem createMoveAllUp() {
        return createMoveAllToDirection("10 ^", new Point(0, -10));
    }

    private JMenuItem createMoveAllLeft() {
        return createMoveAllToDirection("10 <", new Point(-10, 0));
    }

    private JMenuItem createMoveAllDown() {
        return createMoveAllToDirection("10 ", new Point(0, 10));
    }

    private JMenuItem createMoveAllToDirection(final String directionMenuName, final Point direction) {
        final JMenuItem moveAllToRight = new JMenuItem(directionMenuName);
        final OdbPredicate<TrackPart> predicate = part -> true;
        OdbFunction<TrackPart> updat = part -> part.move(direction);
        moveAllToRight.addActionListener(
                e -> {
                    db.replace(predicate, updat);
                    repaint();
                }
        );
        return moveAllToRight;
    }

    private JMenuItem createShowCrossWhileDrawing() {
        final JCheckBoxMenuItem showCross = new JCheckBoxMenuItem("Show drawing cross", true);
        showCross.addActionListener(e -> {
            if (showCross.isSelected()) {
                bus.post(ShowDrawingCrossEvent.SHOW);
            } else {
                bus.post(ShowDrawingCrossEvent.HIDE);
            }
        });
        return showCross;
    }

    private JMenuItem createResendSwitchStates() {
        final JMenuItem resendSwitchStates = new JMenuItem("Resend switch states");
        resendSwitchStates.addActionListener(e -> bus.post(UpdateAllSwitches.create()));
        return resendSwitchStates;
    }
}
