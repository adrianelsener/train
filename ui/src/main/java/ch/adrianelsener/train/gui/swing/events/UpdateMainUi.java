package ch.adrianelsener.train.gui.swing.events;

import com.sun.deploy.net.UpdateTracker;

/**
 * Created by els on 5/20/14.
 */
public class UpdateMainUi {
    private UpdateMainUi() {
        super();
    }

    public static UpdateMainUi create() {
        return new UpdateMainUi();
    }
}
