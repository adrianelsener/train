package ch.adrianelsener.train.driver;

import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.util.stream.Collector;
import java.util.stream.StreamSupport;

public class CompoundSpeedBoardDriver implements SpeedBoardDriver {
    private final ImmutableList<SpeedBoardDriver> speedBoardDrivers;

    public CompoundSpeedBoardDriver(Config cfg) {
        String partsAsString = cfg.getChild("PARTS");
        Iterable<String> stringParts = Splitter.on(";").split(partsAsString);
         speedBoardDrivers = StreamSupport.stream(stringParts.spliterator(), false)//
                .map(key -> createSpeedBoardDriver(cfg.getAllChilds(key)))//
                .collect(GuavaCollectors.immutableList());
    }
    private SpeedBoardDriver createSpeedBoardDriver(final Config config) {
        final SpeedBoardDriver speedBoard;
        final String drvClassName = config.getChild("DRV");
        try {
            speedBoard = SpeedBoardDriver.class.cast(Class.forName(drvClassName).getConstructor(Config.class).newInstance(config));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
        return speedBoard;
    }

    @Override
    public void faster() {

    }

    @Override
    public void slower() {

    }

    @Override
    public void setSpeed(int estimated) {
        speedBoardDrivers.forEach(sbd -> sbd.setSpeed(estimated));
    }

    @Override
    public int getCurrentSpeed() {
        return 0;
    }

    private static class GuavaCollectors {
        public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> immutableList() {
            return Collector.of(ImmutableList.Builder::new, ImmutableList.Builder::add,
                    (l, r) -> l.addAll(r.build()), ImmutableList.Builder<T>::build);
        }
    }
}
