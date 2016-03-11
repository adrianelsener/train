package ch.adrianelsener.train.pi.twi.accessor.cmd;

class SpeedSetter extends CmdSetter {
    private final int speed;

    public SpeedSetter(final int speed) {
        this.speed = speed;
    }

    @Override
    protected int dataPosition() {
        return 0x02;
    }

    @Override
    protected int dataToSet() {
        return speed;
    }
}
