package ch.adrianelsener.train.gui.swing;


import ch.adrianelsener.csvdb.CsvOdb;
import ch.adrianelsener.csvdb.CsvReader;
import ch.adrianelsener.odb.api.ObjectFactory;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.config.ConfKey;
import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.config.ConfigPropertyReader;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.DenkoviWrapper;
import ch.adrianelsener.train.denkovi.IpAddress;
import ch.adrianelsener.train.driver.SwitchBoardV1;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.DummyToggler;
import ch.adrianelsener.train.gui.SwitchBoardToggler;
import ch.adrianelsener.train.gui.SwitchCallback;
import ch.adrianelsener.train.gui.swing.events.*;
import ch.adrianelsener.train.gui.swing.model.*;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
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
    private final boolean detailsDefaultVisible = true;
    private PointCalculator pointCalc;

    @Inject
    private EventBus bus;
    @Inject
    private Odb<TrackPart> db;
    private final ObjectFactory<TrackPart> objectFactory;
    private TrackPart draftPart = InvisiblePart.create();
    private SwitchCallback toggler;
    private boolean rasterEnabled = true;

    private JFrame frame;
    private DetailWindow details;
    private CheckSwitch switchChecker = new CheckSwitch();
    private Optional<File> currentShowing = Optional.empty();

    SwingUi() {
        Odb<TrackPart> theDb = CsvOdb.create(TrackPart.class).build();

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
        final Injector injector = Guice.createInjector(busModule, dbModule);
        keyListener = new ModeKeyListener();
        mouseListener = new TrainMouseAdapter(currentDrawMode);
        objectFactory = input -> {
            final Iterator<String> iterator = input.iterator();
            final String type = iterator.next();
            switch (type) {
                case "T":
                    return SimpleTrack.createSimpleTrack(iterator);
                case "TS":
                    return SwitchTrack.createSwitchTrack(iterator);
                case "S":
                    return SwingSwitch.createSwitch(iterator);
                case "DS":
                    return DummySwitch.create(iterator);
                default:
                    throw new IllegalArgumentException("Could not estimate what kind of TrackPart should be created\n" + input);
            }
        };
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
        final InputStream configfis = SwingUi.class.getResourceAsStream("sample.conf");
        final Config config = new ConfigPropertyReader(configfis).getConfig();
        IOUtils.closeQuietly(configfis);
        final ConfKey rbKey = ConfKey.create("RB");
        final String all = config.get(rbKey);
        final Map<String, Board> boards = Maps.newHashMap();
        toggler = SwitchBoardToggler.create();
        for (final String boardId : all.split(",")) {
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
        details.setVisible(detailsDefaultVisible);

        switchChecker = new CheckSwitch();
        switchChecker.setVisible(false);

        SpeedControl speedControlFrame = new SpeedControl();
        speedControlFrame.setLocation(frame.getLocation().x - 100, frame.getLocation().y);
        speedControlFrame.setVisible(true);
        frame.addKeyListener(keyListener);
        p.addMouseListener(mouseListener);
        p.addMouseListener(popUpListener);
        p.addMouseMotionListener(mouseListener);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menu = createMenuFile();
        menuBar.add(menu);
        final JMenu view = createMenuView();
        menuBar.add(view);
        final JMenu settings = createMenuSettings();
        menuBar.add(settings);
        final JMenu tools = createMenuTools();
        menuBar.add(tools);
        return menuBar;
    }

    private JMenu createMenuTools() {
        final JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.add(createResendSwitchStates());
        return toolsMenu;
    }

    private JMenuItem createResendSwitchStates() {
        final JMenuItem resendSwitchStates = 

    }

    private JMenu createMenuSettings() {
        final JMenu settings = new JMenu("Settings");
        settings.add(createSetDummyBoard());
        return settings;
    }

    private JMenuItem createSetDummyBoard() {
        final JCheckBoxMenuItem setDummyBoard = new JCheckBoxMenuItem("Use Dummyboard", false);
        setDummyBoard.addActionListener(e -> {
            if (setDummyBoard.isSelected()) {
                toggler = DummyToggler.create();
            } else {
                toggler = SwitchBoardToggler.create();
            }
        });
        return setDummyBoard;
    }

    private JMenu createMenuView() {
        final JMenu view = new JMenu("View");
        final JMenuItem showDetail = createShowDetailMenuItem();
        final JMenuItem showSwitchChecker = createShowSwitchCheckerMenuItem();
        final JMenuItem showRaster = createShowRasterMenuItem();
        view.add(showDetail);
        view.add(showSwitchChecker);
        view.add(showRaster);
        return view;
    }

    private JMenuItem createShowRasterMenuItem() {
        JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show Raster", rasterEnabled);
        show.addActionListener(e -> rasterEnabled = show.isSelected());
        return show;
    }

    private JMenuItem createShowSwitchCheckerMenuItem() {
        final JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show Switch Checker", switchChecker.isVisible());
        show.addActionListener(e -> switchChecker.setVisible(show.isSelected()));
        switchChecker.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                show.setSelected(detailsDefaultVisible);
            }
        });
        return show;
    }

    private JMenu createMenuFile() {
        final JMenu menu = new JMenu("File");
        final JMenuItem exit = createExitMenuItem();
        final JMenuItem save = createSaveMenuItem();
        final JMenuItem saveAs = createSaveAsMenuItem();
        final JMenuItem open = createOpenMenuItem();
        menu.add(open);
        menu.add(save);
        menu.add(saveAs);
        menu.add(exit);
        return menu;
    }

    private JMenuItem createShowDetailMenuItem() {
        final JCheckBoxMenuItem show = new JCheckBoxMenuItem("Show Details");
        show.addActionListener(e -> details.setVisible(show.isSelected()));
        return show;
    }

    private JMenuItem createOpenMenuItem() {
        final JMenuItem open = new JMenuItem("Open");
        open.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                final File selectedFile;
                selectedFile = fileChooser.getSelectedFile();
                currentShowing = Optional.of(selectedFile);
                final CsvReader<TrackPart> reader = new CsvReader<>(selectedFile, objectFactory);
                db.setStorage(reader).init();
                repaint();
            }
        });
        return open;
    }


    private JMenuItem createSaveMenuItem() {
        final JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> {
            final Optional<File> selectedFile;
            if (currentShowing.isPresent()) {
                selectedFile = currentShowing;
            } else {
                final JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = Optional.of(fileChooser.getSelectedFile());
                } else {
                    selectedFile = Optional.empty();
                }
            }
            if (selectedFile.isPresent()) {
                db.flush();
            } else {
                logger.warn("Nothing done");
            }

        });
        return save;
    }

    private JMenuItem createSaveAsMenuItem() {
        final JMenuItem save = new JMenuItem("Save As");
        save.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();
                db.setStorage(new CsvReader<>(selectedFile, objectFactory)).flush();
            }
        });
        return save;
    }

    private JMenuItem createExitMenuItem() {
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        return exit;
    }

    @Subscribe
    public void createDraftPart(final DraftPartCreationAction action) {
        this.draftPart = action.createDraftPart(creationStartPoint, pointCalc);
        repaint();
    }

    @Subscribe
    public void setCreationStartPoint(final UpdatePoint.CreationStartPoint creationStartPoint) {
        logger.debug("creation start point set to {}", creationStartPoint);
        Point pressedPoint = pointCalc.calculatePoint(creationStartPoint.getPoint());
        logger.debug("basic calculation of start point '{}'", pressedPoint);
        this.creationStartPoint = Optional.of(db.filterUnique(part -> part.isNear(pressedPoint)).map(part -> part.getNextConnectionpoint(pressedPoint)).orElse(creationStartPoint.getPoint()));
        draftPart = db.filterUnique(part -> part.isNear(pressedPoint)).orElse(InvisiblePart.create());
    }

    private Optional<Point> creationStartPoint = Optional.empty();

    @Subscribe
    public void updateDraftPart(UpdateDraftPart newDraftPart) {
        logger.debug("Event received with {}", newDraftPart);
        draftPart = newDraftPart.getDraftPart();
        repaint();
    }

    @Subscribe
    public void createPart(PartCreationAction newPart) {
        db.add(newPart.createDraftPart(creationStartPoint, pointCalc));
        draftPart = InvisiblePart.create();
        bus.post(DrawMode.NoOp);
        repaint();
    }

    @Subscribe
    public void updatePart(UpdatePart action) {
        action.doTransformation(db, pointCalc, creationStartPoint);
        currentDrawMode.setMode(DrawMode.NoOp);
        repaint();
    }

    @Subscribe
    public void moveDraftPart(UpdateMoveDraftPart destination) {
        final Point currentPoint = pointCalc.calculatePoint(destination.getDestination());
        bus.post(UpdateDraftPart.create(draftPart.moveTo(currentPoint)));
        repaint();
    }

    @Subscribe
    public void updateUi(UpdateMainUi updateMainUi) {
        repaint();
    }

    @Subscribe
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
    public void toggle(UpdatePart.TogglePart toggleAction) {
        toggleAction.toggle(toggler, db);
        repaint();
    }

    @Subscribe
    public void updateSwitches(UpdateStates updateStates) {
        db.getAll().forEach(part -> {
//            part.toggle()
        });
    }
}