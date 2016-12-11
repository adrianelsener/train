package ch.adrianelsener.train.pi.twi.accessor.cmd;

class AccelerationSetter extends CmdSetter {
    private final int acceleration;

    public AccelerationSetter(final int acceleration) {

        this.acceleration = acceleration;
    }

    @Override
    protected int dataPosition() {
        return 0x00;
    }

    @Override
    protected int dataToSet() {
        return acceleration;
    }
}
