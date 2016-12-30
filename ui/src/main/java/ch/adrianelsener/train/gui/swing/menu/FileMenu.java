package ch.adrianelsener.train.gui.swing.menu;

import ch.adrianelsener.csvdb.CsvReader;
import ch.adrianelsener.odb.api.ObjectFactory;
import ch.adrianelsener.odb.api.Odb;
import ch.adrianelsener.train.gui.swing.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Iterator;
import java.util.Optional;

public class FileMenu extends JMenu {
    private static final Logger logger = LoggerFactory.getLogger(FileMenu.class);
    private Optional<File> currentShowing = Optional.empty();
    private final Odb<TrackPart> db;
    private final ObjectFactory<TrackPart> objectFactory;

    public FileMenu(final Odb<TrackPart> db) {
        super("File");
        this.db = db;
        final JMenuItem exit = createExitMenuItem();
        final JMenuItem save = createSaveMenuItem();
        final JMenuItem saveAs = createSaveAsMenuItem();
        final JMenuItem open = createOpenMenuItem();
        add(open);
        add(save);
        add(saveAs);
        add(exit);

        objectFactory = input -> {
            final Iterator<String> iterator = input.iterator();
            final String type = iterator.next();
            switch (type) {
                case "T":
                    return SimpleTrack.createSimpleTrack(iterator);
                case "TS":
                    return SwitchTrack.createSwitchTrack(iterator);
                case "PT":
                    return PowerTrack.createPowerTrack(iterator);
                case "S":
                    return RealSwitch.create(iterator);
                case "DS":
                    return DummySwitch.create(iterator);
                default:
                    throw new IllegalArgumentException("Could not estimate what kind of TrackPart should be created\n" + input);
            }
        };

    }

    private JMenuItem createExitMenuItem() {
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        return exit;
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
                db.setStorage(CsvReader.create(selectedFile, objectFactory)).flush();
            }
        });
        return save;
    }

    private JMenuItem createOpenMenuItem() {
        final JMenuItem open = new JMenuItem("Open");
        open.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                final File selectedFile;
                selectedFile = fileChooser.getSelectedFile();
                currentShowing = Optional.of(selectedFile);
                final CsvReader<TrackPart> reader = CsvReader.create(selectedFile, objectFactory);
                db.setStorage(reader).init();
                repaint();
            }
        });
        return open;
    }
}
