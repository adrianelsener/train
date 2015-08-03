package ch.adrianelsener.train.gui.swing.events;

public class UpdateMoveAll {
    private final int distance;

    private UpdateMoveAll(int distance) {
        this.distance = distance;
    }

    public static UpdateMoveAll createMoveToRight() {
        return new UpdateMoveAll(10);
    }

    public int getDistance() {
        return distance;
    }
}
