package ch.adrianelsener.train.pi.twi.accessor.cmd;

class DirectionSetter extends CmdSetter {
    private final int avrDirectionValue;

    public DirectionSetter(final int avrDirectionValue) {

        this.avrDirectionValue = avrDirectionValue;
    }

    @Override
    protected int dataPosition() {
        return 0x03;
    }

    @Override
    protected int dataToSet() {
        return avrDirectionValue;
    }
}
