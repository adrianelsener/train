package ch.adrianelsener.train.pi.twi.accessor.cmd;

class StepSizeSetter extends CmdSetter {
    private final int stepsize;

    public StepSizeSetter(final int stepsize) {
        this.stepsize = stepsize;
    }

    @Override
    protected int dataPosition() {
        return 0x01;
    }

    @Override
    protected int dataToSet() {
        return stepsize;
    }
}
