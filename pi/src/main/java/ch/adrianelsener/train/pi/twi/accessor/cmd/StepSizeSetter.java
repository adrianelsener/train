package ch.adrianelsener.train.pi.twi.accessor.cmd;

class StepSizeSetter extends CmdSetter {
    private final int stepsize;

    public StepSizeSetter(final int stepsize) {
        this.stepsize = stepsize;
    }

    @Override
    protected int dataPosition() {
        return 0x00;
    }

    @Override
    protected int dataToSet() {
        return stepsize;
    }
}
