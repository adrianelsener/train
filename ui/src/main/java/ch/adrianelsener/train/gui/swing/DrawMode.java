package ch.adrianelsener.train.gui.swing;

enum DrawMode {
    Switch(true), //
    Track(true), //
    NoOp, //
    Rotate, //
    Toggle, //
    Move(true), //
    Delete, //
    Detail, //
    DummySwitch(true),//
    SwitchTrack(true),//
    TripleSwitch(true); ;

    private boolean isDraft;

    private DrawMode() {

    }

    private DrawMode(final boolean asDraft) {
        isDraft = asDraft;
    }

    public boolean isDraft() {
        return isDraft;
    }

    @Override
    public String toString() {
        return name();
    }
}