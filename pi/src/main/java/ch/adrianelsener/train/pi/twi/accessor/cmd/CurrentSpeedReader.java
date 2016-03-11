package ch.adrianelsener.train.pi.twi.accessor.cmd;

class CurrentSpeedReader extends CmdReader<Integer> {
    @Override
    protected Integer convert(final String line) {
        return Integer.valueOf(line);
    }

    @Override
    protected int getReadPosition() {
        return 0x00;
    }
}
