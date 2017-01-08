package ch.adrianelsener.train.gui.swing.model;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.swing.TrackView;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.awt.*;
import java.util.Iterator;

public class TrackFactory {
    private final Config config;
    private final Injector injector;

    TrackFactory(Config config) {
        this.config = config;
        injector = Guice.createInjector(createConfigModuel(config));
    }

    public SimpleTrack createSimpleTrack(final Point startPoint, final Point endPoint) {
        return new SimpleTrack(startPoint, endPoint);
    }

    public SwitchTrack createSwitchTrack(final Point point, final Point endPoint) {
        return new SwitchTrack(point, endPoint);
    }

    public PowerTrack createPowerTrack(final Point point, final Point endPoint) {
        final PowerTrack powerTrack = new PowerTrack(point, endPoint);
        injector.injectMembers(powerTrack);
        return powerTrack;
    }

    public Track fromStringIterable(final Iterable<String> iter) {
        return fromIterable(iter);
    }

    public TripleSwitch.Builder createTripleSwitch(Iterator<String> data) {
        return new TripleSwitch.Builder(data);
    }


    private Track fromIterable(final Iterable<String> split) {
        final Iterator<String> iterator = split.iterator();
        final String identifier = iterator.next();
        if ("T".equals(identifier)) {
            return createSimpleTrack(iterator);
        } else {
            return createSwitchTrack(iterator);
        }
    }

    public Track createSwitchTrack(final Iterator<String> iterator) {
        return new SwitchTrack(new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())), new Point(
                Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())), SwitchId.fromValue(iterator.next()), BoardId.fromValue(iterator.next()), TrackState.valueOf(iterator.next()), TrackView.valueOf(iterator.next()));
    }


    public SimpleTrack createSimpleTrack(final Iterator<String> iterator) {
        return createSimpleTrack(new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())),
                new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())));
    }

    public static Track createPowerTrack(final Iterator<String> iterator) {
        return new PowerTrack(new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())), new Point(
                Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next())), BoardId.fromValue(iterator.next()), TrackState.valueOf(iterator.next()), TrackView.valueOf(iterator.next()));
    }

    public static RealSwitch create(final Iterator<String> iterator) {
        final Point center = new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next()));
        final Double drawAngle = Double.valueOf(iterator.next());
        final SwitchId readSwitchId = SwitchId.fromValue(iterator.next());
        final BoardId readBoardId = BoardId.fromValue(iterator.next());
        final boolean state = Boolean.parseBoolean(iterator.next());
        final SwingSwitch.SwitchMode mode = SwingSwitch.SwitchMode.valueOf(iterator.next()); // Switchmode no more needed
        final TrackView switchView = TrackView.valueOf(iterator.next());
        switch (mode) {
            case Real:
                return new RealSwitch(center, drawAngle, readSwitchId, readBoardId, state, switchView);
            default:
                throw new IllegalArgumentException("Could not determine switch");
        }
    }

    public RealSwitch create(final Point point) {
        return new RealSwitch(point);
    }

    public DummySwitch createDummySwitch(Point point){
        return new DummySwitch(point);
    }

    public static DummySwitch createDummy(Iterator<String> iterator) {
        final Point center = new Point(Integer.parseInt(iterator.next()), Integer.parseInt(iterator.next()));
        final Double drawAngle = Double.valueOf(iterator.next());
        return new ch.adrianelsener.train.gui.swing.model.DummySwitch(center, drawAngle);
    }


    public static TrackFactory instance() {
        if (instance == null) {
            Config cfg = null;
            try {
                cfg = (Config) new InitialContext().lookup("els:config");
            } catch (NamingException e) {
                throw new IllegalStateException(e);
            }
            instance = new TrackFactory(cfg);
        }
        return instance;
    }

    private static TrackFactory instance = null;

    private static Module createConfigModuel(Config config) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(Config.class).toInstance(config);
            }
        };
    }
}
