package ch.adrianelsener.train.gui.swing.common;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Simple builder to create MenuItems.
 */
public class MenuItemBuilder {
    private final String text;
    private ActionListener actionListener;

    private MenuItemBuilder(String text) {
        this.text = text;
    }

    public static MenuItemBuilder create(final String text) {
        return new MenuItemBuilder(text);
    }

    public MenuItemBuilder addActionListener(ActionListener listener) {
        actionListener = listener;
        return this;
    }

    public JMenuItem build() {
        final JMenuItem item = new JMenuItem(text);
        item.addActionListener(actionListener);
        return item;
    }
}
