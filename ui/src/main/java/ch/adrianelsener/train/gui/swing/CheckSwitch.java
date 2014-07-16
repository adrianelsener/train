package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.config.ConfigPropertyReader;
import ch.adrianelsener.train.denkovi.Board;
import ch.adrianelsener.train.denkovi.DenkoviWrapper;
import ch.adrianelsener.train.denkovi.IpAddress;
import ch.adrianelsener.train.driver.SwitchBoardV1;
import ch.adrianelsener.train.gui.BoardId;
import ch.adrianelsener.train.gui.SwitchBoardToggler;
import ch.adrianelsener.train.gui.SwitchId;
import ch.adrianelsener.train.gui.SwitchCallback;
import org.apache.commons.io.IOUtils;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

/**
 * This will be a simple UI to check a relay card
 */
public class CheckSwitch extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private final JTextField board = new JTextField();
    private final JTextField ip = new JTextField("172.16.100.2");
    private final JTextField confFile = new JTextField("ch/adrianelsener/train/gui/swing/sample.conf");
    private final JCheckBox checkbox;
    private boolean state;

    public CheckSwitch() {
        super("Switch checker");
        setSize(130, 80);
        final GridLayout layout = new GridLayout(4, 1);
        setLayout(layout);
        final JPanel panelIp = new JPanel();
        panelIp.setLayout(new GridLayout(1, 2));
        panelIp.add(new JLabel("IP"));
        panelIp.add(ip);
        add(panelIp);
        final JPanel panelConfFile = new JPanel();
        panelConfFile.setLayout(new GridLayout(1, 2));
        panelConfFile.add(new JLabel("ConfFile"));
        JTextField id = new JTextField();
        panelConfFile.add(id);
        add(panelConfFile);
        final JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 2));
        p1.add(new JLabel("ID"));
        p1.add(id);
        add(p1);
        final JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1, 2));
        p2.add(new JLabel("Board"));
        p2.add(board);
        add(p2);
        final JPanel p3 = new JPanel();
        p3.setLayout(new GridLayout(1, 2));
        checkbox = new JCheckBox();
        checkbox.setEnabled(false);
        checkbox.setSelected(state);
        p3.add(checkbox);
        p3.add(new JLabel("State"));
        add(p3);
        JButton apply = new JButton("toggle");
        add(apply);

        apply.addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final BoardId boardId = BoardId.create(board.getText());
        final SwitchId switchId = SwitchId.create(Integer.parseInt(board.getText()));

        final IpAddress address = IpAddress.fromValue(ip.getText());
        final Board board = new DenkoviWrapper(address);
        final InputStream configfis = SwingUi.class.getResourceAsStream(confFile.getText());
        final Config config = new ConfigPropertyReader(configfis).getConfig();
        IOUtils.closeQuietly(configfis);
        final SwitchBoardV1 weichenBoard = new SwitchBoardV1(board, config, 0);
        final SwitchCallback toggler = SwitchBoardToggler.create();
        toggler.addBoard(BoardId.fromValue(0), weichenBoard);

        state = !state;
        toggler.toggleSwitch(switchId, boardId, state);
        checkbox.setSelected(state);
    }
}
