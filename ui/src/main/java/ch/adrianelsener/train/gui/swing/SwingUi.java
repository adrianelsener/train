package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.csvdb.CsvOdb;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.config.ConfigPropertyReader;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.DenkoviWrapper;
import ch.adrianelsener.train.denkovi.IpAddress;
import ch.adrianelsener.train.driver.SwitchBoardV1;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchBoardToggler;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.swing.events.DbUpdateHandler;
import ch.adrianelsener.train.gui.swing.events.DraftPartCreationAction;
import ch.adrianelsener.train.gui.swing.events.MousePositionEvent;
import ch.adrianelsener.train.gui.swing.events.PartCreationAction;
import ch.adrianelsener.train.gui.swing.events.PointCalculator;
import ch.adrianelsener.train.gui.swing.events.RasterEnabledEvent;
import ch.adrianelsener.train.gui.swing.events.ShowDrawingCrossEvent;
import ch.adrianelsener.train.gui.swing.events.UpdateAllSwitches;
import ch.adrianelsener.train.gui.swing.events.UpdateApplyListener;
import ch.adrianelsener.train.gui.swing.events.UpdateDetails;
import ch.adrianelsener.train.gui.swing.events.UpdateDraftPart;
import ch.adrianelsener.train.gui.swing.events.UpdateMainUi;
import ch.adrianelsener.train.gui.swing.events.UpdateMoveDraftPart;
import ch.adrianelsener.train.gui.swing.events.UpdatePart;
import ch.adrianelsener.train.gui.swing.events.UpdatePoint;
import ch.adrianelsener.train.gui.swing.events.UpdateStates;
import ch.adrianelsener.train.gui.swing.menu.FileMenu;
import ch.adrianelsener.train.gui.swing.menu.SettingsMenu;
import ch.adrianelsener.train.gui.swing.menu.ToolsMenu;
import ch.adrianelsener.train.gui.swing.menu.ViewMenu;
import ch.adrianelsener.train.gui.swing.model.InvisiblePart;
import ch.adrianelsener.train.gui.swing.model.TrackFactory;
import ch.adrianelsener.train.gui.swing.model.TrackPart;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static ch.adrianelsener.train.gui.swing.events.PointCalculator.RasterEnabled;

public class SwingUi extends JComponent {
    private static final long serialVersionUID = 1L;
    private final static Logger logger = LoggerFactory.getLogger(SwingUi.class);
    private final ModeKeyListener keyListener;
    private final TrainMouseAdapter mouseListener;
    private final MouseAdapter popUpListener = new MainPopupMenu.PopupClickListener();
    private final DrawModeState currentDrawMode = new DrawModeState();
    private final int rasterSize = 5;
    private final PointCalculator pointCalc;

    @Inject
    private EventBus bus;
    @Inject
    private Odb<TrackPart> db;
    @Inject
    private TrackFactory trackFactory;
    private TrackPart draftPart = InvisiblePart.create();
    private SwitchCallback toggler;
    private boolean rasterEnabled = true;

    private JFrame frame;
    private DetailWindow details;
    private CheckSwitch switchChecker = new CheckSwitch();
    private ShowDrawingCrossEvent showDrawingCross = ShowDrawingCrossEvent.SHOW;
    private Point currentMousePosition;

    SwingUi() {
        Odb<TrackPart> theDb = CsvOdb.create(TrackPart.class).build();
        try {
            new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        final Module busModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(EventBus.class).asEagerSingleton();
            }
        };
        final Module dbModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<Odb<TrackPart>>() {
                }).toInstance(theDb);
            }
        };
        final Module trackCreatorModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(TrackFactory.class).toInstance(TrackFactory.instance());
            }
        };
        final Injector injector = Guice.createInjector(busModule, dbModule, trackCreatorModule);
        keyListener = new ModeKeyListener();
        mouseListener = new TrainMouseAdapter(currentDrawMode);
        if (rasterEnabled) {
            pointCalc = new RasterEnabled(rasterSize, theDb);
        } else {
            pointCalc = new PointCalculator.RasterDisabled(theDb);
        }
        EventBus theBus = injector.getInstance(EventBus.class);
        injector.injectMembers(this);
        injector.injectMembers(mouseListener);
        injector.injectMembers(popUpListener);
        injector.injectMembers(keyListener);
        final DbUpdateHandler dbUpdater = DbUpdateHandler.create(db);
        theBus.register(this);
        theBus.register(dbUpdater);
        theBus.register(currentDrawMode);
    }

    public static void main(final String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> new SwingUi().init());
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2d = (Graphics2D) g;
        paintOptionalRaster(g2d);
        paintParts(g2d);
        paintOptionalMousePart(g2d);
        paintDrawingCross(g2d);
    }

    private void paintDrawingCross(Graphics2D g2d) {
        showDrawingCross.draw(g2d, frame.getSize(), currentMousePosition);
    }

    private void paintOptionalMousePart(final Graphics2D g2d) {
        draftPart.paint(g2d);
    }

    private void paintOptionalRaster(final Graphics2D g2d) {
        if (rasterEnabled) {
            paintRaster(g2d);
        }
    }

    private void paintRaster(final Graphics2D g2d) {
        final Dimension dimension = frame.getSize();
        g2d.setColor(Color.GRAY);
        for (int y = rasterSize; y < dimension.height; y += rasterSize) {
            for (int x = rasterSize; x < dimension.width; x += rasterSize) {
                g2d.drawLine(x, y, x, y);
            }
        }
    }

    private void paintParts(final Graphics2D g2d) {
        for (final TrackPart part : db.getAll()) {
            part.paint(g2d);
        }
    }

    public void init() {
        final Config config = readConfig();
        final ConfKey rbKey = ConfKey.create("RB");
        final String all = config.get(rbKey);
        final Map<String, Board> boards = Maps.newHashMap();
        toggler = SwitchBoardToggler.create();
        for (final String boardId : all.split(";")) {
            final String networkAddress = config.get(rbKey.createSubKey(boardId).createSubKey("IP"));
            final Board board;
            if (boards.containsKey(networkAddress)) {
                logger.debug("Getting caches board for address {}", networkAddress);
                board = boards.get(networkAddress);
            } else {
                logger.debug("Create new Board for address {}", networkAddress);
                final IpAddress address = IpAddress.fromValue(networkAddress);
                board = new DenkoviWrapper(address);
            }

            final int boardNumber = Integer.parseInt(boardId);
            final SwitchBoardV1 weichenBoard = new SwitchBoardV1(board, config, boardNumber);
            toggler.addBoard(BoardId.fromValue(boardNumber), weichenBoard);
        }
        frame = new JFrame("Swing Board");
        frame.setSize(new Dimension(1000, 800));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        final JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        frame.add(p);
        p.add(this, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        details = new DetailWindow();
        bus.register(details);
        bus.register(this);
        final DetailWindow.ApplyActionListener applyListener = text -> {
        };
        details.setApplyListener(applyListener);

        switchChecker = new CheckSwitch();
        switchChecker.setVisible(false);

        SpeedControl speedControlFrame = new SpeedControl(config.getAll(ConfKey.create("SB").createSubKey("C0")));
        speedControlFrame.setLocation(frame.getLocation().x - 100, frame.getLocation().y);
        speedControlFrame.setVisible(true);
        frame.addKeyListener(keyListener);
        p.addMouseListener(mouseListener);
        p.addMouseListener(popUpListener);
        p.addMouseMotionListener(mouseListener);
    }

    private Config readConfig() {
        final InputStream configfis = SwingUi.class.getResourceAsStream("sample.conf");
        final Config config = new ConfigPropertyReader(configfis).getConfig();
        IOUtils.closeQuietly(configfis);
        return config;
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new FileMenu(db));
        menuBar.add(new ViewMenu(details, switchChecker, bus));
        menuBar.add(new SettingsMenu(bus));
        menuBar.add(new ToolsMenu(db, bus));
        return menuBar;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void setToggler(SwitchCallback toggler) {
        this.toggler = toggler;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void createDraftPart(final DraftPartCreationAction action) {
        logger.debug("use creationStartPoint '{}' and pointCalc '{}'", creationStartPoint, pointCalc);
        this.draftPart = action.createDraftPart(creationStartPoint, pointCalc);
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void setCreationStartPoint(final UpdatePoint.CreationStartPoint creationStartPoint) {
        logger.debug("creation start point set to {}", creationStartPoint);
        Point pressedPoint = pointCalc.calculatePoint(creationStartPoint.getPoint());
        logger.debug("basic calculation of start point '{}'", pressedPoint);
        this.creationStartPoint = Optional.of(db.filterUnique(part -> part.isNear(pressedPoint)).map(part -> part.getNextConnectionpoint(pressedPoint)).orElse(creationStartPoint.getPoint()));
        draftPart = db.filterUnique(part -> part.isNear(pressedPoint)).orElse(InvisiblePart.create());
        logger.debug("calculated creationStartPoint is '{}'", creationStartPoint);
        logger.debug("use draftPart '{}' to update", draftPart);
    }

    private Optional<Point> creationStartPoint = Optional.empty();

    @Subscribe
    @AllowConcurrentEvents
    public void showDrawingCross(ShowDrawingCrossEvent showCrossEvent) {
        showDrawingCross = showCrossEvent;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updateAllParts(UpdateAllSwitches updateAll) {
        db.getAll().forEach(part -> {
            logger.debug("Update state for {}", part);
            part.applyState(toggler);
        });
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updateDraftPart(UpdateDraftPart newDraftPart) {
        logger.debug("Event received with {}", newDraftPart);
        draftPart = newDraftPart.getDraftPart();
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void createPart(
            PartCreationAction newPart) {
        db.add(newPart.createDraftPart(creationStartPoint, pointCalc));
        draftPart = InvisiblePart.create();
        bus.post(DrawMode.NoOp);
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updatePart(UpdatePart action) {
        action.doTransformation(db, pointCalc, creationStartPoint);
        currentDrawMode.setMode(DrawMode.NoOp);
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void moveDraftPart(UpdateMoveDraftPart destination) {
        final Point currentPoint = pointCalc.calculatePoint(destination.getDestination());
        bus.post(UpdateDraftPart.create(draftPart.moveTo(currentPoint)));
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updateUi(UpdateMainUi updateMainUi) {
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updateDetailsUi(UpdatePoint.DetailCoordinatesPoint detailsPoint) {
        final Optional<TrackPart> nextTo = db.filterUnique(part -> part.isNear(detailsPoint.getPoint()));
        if (nextTo.isPresent()) {
            final TrackPart trackPart = nextTo.get();
            bus.post(UpdateDetails.create(trackPart));
            bus.post(UpdateApplyListener.create(trackPart, bus));
        }
        currentDrawMode.setMode(DrawMode.NoOp);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void toggle(UpdatePart.TogglePart toggleAction) {
        if (toggler.isReady()) {
            toggleAction.toggle(toggler, db);
            repaint();
        } else {
            logger.debug("should notify status bar");
            // TODO notify statusbar...
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updateSwitches(UpdateStates updateStates) {
        db.getAll().forEach(part -> {
//            part.toggle()
        });
    }

    @Subscribe
    @AllowConcurrentEvents
    public void updateMousePosition(MousePositionEvent positionEvent) {
        currentMousePosition = positionEvent.getPosition();
        repaint();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void enableRaster(RasterEnabledEvent rasterEvent) {
        rasterEnabled = rasterEvent.isEnbaled();
    }
}