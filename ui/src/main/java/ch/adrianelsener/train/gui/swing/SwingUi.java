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
import ch.adrianelsener.train.denkovi.DenkoviWrapper.IpAddress;
import ch.adrianelsener.train.driver.SwitchBoardV1;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.DummyToggler;
import ch.adrianelsener.train.gui.SwitchBoardToggler;
import ch.adrianelsener.train.gui.ToggleCallback;
import ch.adrianelsener.train.gui.swing.events.*;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.xml.soap.Detail;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class SwingUi extends JComponent {
    private static final long serialVersionUID = 1L;
    private final static Logger logger = LoggerFactory.getLogger(SwingUi.class);
    private final ModeKeyListener keyListener;
    private final TrainMouseAdapter mouseListener;
    private final MouseAdapter popUpListener = new MainPopupMenu.PopupClickListener();
    private final DrawModeState currentDrawMode = new DrawModeState();
    private final int rasterSize = 5;
    private final boolean detailsDefaultVisible = true;

    @Inject
    private EventBus bus;
    @Inject
    private Odb<TrackPart> db;
    private final ObjectFactory<TrackPart> objectFactory;
    private TrackPart draftPart = InvisiblePart.create();
    private ToggleCallback toggler;
    private boolean rasterEnabled = true;

    private JFrame frame;
    private DetailWindow details;
    private CheckSwitch switchChecker = new CheckSwitch();
    private Optional<File> currentShowing = Optional.empty();
    private final DbUpdateHandler dbUpdater;
    private final Injector injector;

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
                bind(new TypeLiteral<Odb<TrackPart>>(){}).toInstance(theDb);
            }
        };
        injector = Guice.createInjector(busModule, dbModule);
        keyListener = new ModeKeyListener();
        mouseListener = new TrainMouseAdapter(keyListener);
        objectFactory = input -> {
            final Iterator<String> iterator = input.iterator();
            final String type = iterator.next();
            switch (type) {
                case "T":
                    return SimpleTrack.createSimpleTrack(iterator);
                case "TS":
                    return SwitchTrack.createSwitchTrack(iterator);
                case "S":
                    return Switch.createSwitch(iterator);
                default:
                    throw new IllegalArgumentException("Could not estimate what kind of TrackPart should be created\n" + input);
            }
        };
        EventBus theBus = injector.getInstance(EventBus.class);
        injector.injectMembers(this);
        injector.injectMembers(mouseListener);
        injector.injectMembers(popUpListener);
        injector.injectMembers(keyListener);
        dbUpdater = DbUpdateHandler.create(db);
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
                logger.debug("Create new Board for adress {}", networkAddress);
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
        final DetailWindow.ApplyActionListener applyListener = text -> {};
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
        return menuBar;
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
                final CsvReader<TrackPart> reader = new CsvReader<TrackPart>(selectedFile, objectFactory);
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
    public void updateDraftPart(UpdateDraftPart newDraftPart) {
        logger.debug("Event received with {}", newDraftPart);
        draftPart = newDraftPart.getDraftPart();
    }

    @Subscribe
    public void updateUi(UpdateMainUi updateMainUi) {
        repaint();
    }


    private class TrainMouseAdapter extends MouseAdapter {
        private final Logger logger = LoggerFactory.getLogger(TrainMouseAdapter.class);
        private final ModeKeyListener keyListener;
        private Optional<Point> startPoint = Optional.empty();
        @Inject
        private EventBus bus;
        @Inject
        private Odb<TrackPart> db;

        public TrainMouseAdapter(final ModeKeyListener keyListener) {
            this.keyListener = keyListener;
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                return;
            }
            final Point pressedPoint = calculateRasterPoint(e);
            switch (currentDrawMode.getDrawMode()) {
                case Track:
                case SwitchTrack: {
                    startPoint = Optional.of(db.filterUnique(part -> part.isNear(pressedPoint)).map(part -> part.getNextConnectionpoint(pressedPoint)).orElse(e.getPoint()));
                }
                break;
                case Move:
                    logger.debug("Move Part next to {}", pressedPoint);
                    startPoint = Optional.of(pressedPoint);
                    final TrackPart moveDraft = db.filterUnique(part -> part.isNear(pressedPoint)).orElse(null);
                    logger.debug("Part '{}' found", moveDraft);
                    bus.post(UpdateDraftPart.create(moveDraft));
                    break;
                case Switch:
                    final Switch draftSwitch = Switch.create(pressedPoint);
                    bus.post(UpdateDraftPart.create(draftSwitch));
                    break;
                case Detail:
                    final Optional<TrackPart> nextTo = db.filterUnique(part -> part.isNear(pressedPoint));
                    if (nextTo.isPresent()) {
                        final TrackPart trackPart = nextTo.get();
                        bus.post(UpdateDraftPart.create(trackPart));
                        bus.post(UpdateApplyListener.create(trackPart, bus));
                    }
                    break;
                case DummySwitch:
                case Rotate:
                case Toggle:
                case Delete:
                case NoOp:
                    break;
            }
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                return;
            }
            final Point pressedPoint = calculateRasterPoint(e);
            switch (currentDrawMode.getDrawMode()) {
                case Switch:
                    final Switch draftSwitch = Switch.create(pressedPoint);
                    bus.post(UpdateDraftPart.create(draftSwitch));
                    break;
                case Track: {
                    final Point endPoint = db.filterUnique(part -> part.isNear(pressedPoint)).map(part -> part.getNextConnectionpoint(pressedPoint)).orElse(pressedPoint);
                    logger.debug("Draw line from {}:{} to {}:{}", startPoint.get().x, startPoint.get().y, endPoint.x, endPoint.y);
                    final TrackPart draftSimpleTrack = Track.createSimpleTrack(startPoint.get(), endPoint);
                    bus.post(UpdateDraftPart.create(draftSimpleTrack));
                }
                break;
                case SwitchTrack: {
                    Optional<TrackPart> parts = db.filterUnique(part -> part.isNear(pressedPoint));
                    logger.debug("Found Part to move : {}",parts );
                    final Point endPoint = parts.map(part -> part.getNextConnectionpoint(pressedPoint)).orElse(pressedPoint);
                    logger.debug("Draw SwitchTrack from {}:{} to {}:{}", startPoint.get().x, startPoint.get().y, endPoint.x, endPoint.y);
                    final TrackPart draftSwitchTrack = Track.createSwitchTrack(startPoint.get(), endPoint);
                    bus.post(UpdateDraftPart.create(draftSwitchTrack));
                }
                break;
                case Move:
                    final TrackPart moveDraft = SwingUi.this.draftPart.moveTo(pressedPoint);
                    bus.post(UpdateDraftPart.create(moveDraft));
                    break;
                case DummySwitch:
                case Rotate:
                case Toggle:
                case Delete:
                case Detail:
                case NoOp:
                    break;
            }
            if (currentDrawMode.getDrawMode().isDraft()) {
                bus.post(UpdateMainUi.create());
            }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                return;
            }
            final InvisiblePart invisibleDraftPart = InvisiblePart.create();
            bus.post(UpdateDraftPart.create(invisibleDraftPart));
            final Point pressedPoint = e.getPoint();
            final Point mousePoint = calculateRasterPoint(e);

            switch (currentDrawMode.getDrawMode()) {
                case Track: {
                    final Point endPoint = db.filterUnique(part -> part.isNear(mousePoint)).map(part -> part.getNextConnectionpoint(mousePoint)).orElse(pressedPoint);
                    logger.debug("Draw line from {}:{} to {}:{}", startPoint.get().x, startPoint.get().y, endPoint.x, endPoint.y);
                    db.add(Track.createSimpleTrack(startPoint.get(), endPoint));
                }
                break;
                case SwitchTrack: {
                    final Point endPoint = db.filterUnique(part -> part.isNear(mousePoint)).map(part -> part.getNextConnectionpoint(mousePoint)).orElse(pressedPoint);
                    logger.debug("Draw line from {}:{} to {}:{}", startPoint.get().x, startPoint.get().y, endPoint.x, endPoint.y);
                    db.add(Track.createSwitchTrack(startPoint.get(), endPoint));
                }
                break;
                case Switch: {
                    final Switch newSwitch = Switch.create(mousePoint);
                    logger.debug("Neue switch {}", newSwitch);
                    db.add(newSwitch);
                }
                break;
                case DummySwitch: {
                    final Switch newSwitch = Switch.createDummy(mousePoint);
                    logger.debug("Neue switch {}", newSwitch);
                    db.add(newSwitch);
                }
                break;
                case Rotate:
                    logger.debug("Mirror Object next to {}", mousePoint);
                    db.replace(part -> part.isNear(mousePoint), part -> part.createMirror());
                    break;
                case Delete:
                    logger.debug("Delete part near to {}", mousePoint);
                    db.delete(part -> part.isNear(mousePoint));
                    break;
                case Move:
                    logger.debug("Postion of part near to {} to position {}", startPoint.get(), mousePoint);
                    db.replace(part -> part.isNear(startPoint.get()), part -> part.moveTo(mousePoint));
                    break;
                case NoOp:
                case Toggle:
                    logger.debug("Toggle switch next to {}", mousePoint);
                    db.replace(part -> part.isNear(mousePoint), part -> part.toggle(toggler));
                    break;
                case Detail:
                    break;
            }
            bus.post(UpdateMainUi.create());
            bus.post(DrawMode.NoOp);
        }

        private Point calculateRasterPoint(final MouseEvent e) {
            final Point mousePoint;
            if (rasterEnabled) {
                final Point originalPoint = e.getPoint();
                final int x = originalPoint.x - originalPoint.x % rasterSize;
                final int y = originalPoint.y - originalPoint.y % rasterSize;
                mousePoint = new Point(x, y);
            } else {
                mousePoint = e.getPoint();
            }
            return mousePoint;
        }
    }

}
