package ch.adrianelsener.train.gui.swing;

import ch.adrianelsener.train.gui.swing.events.UpdateDetails;
import com.google.common.eventbus.Subscribe;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.GridLayout;

public class DetailWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField id = new JTextField();
    private final JTextField board = new JTextField();
    private final JButton apply = new JButton("apply");
    private final JCheckBox inverted = new JCheckBox();
    private ApplyActionListener applyListener = (e) -> {};

    public DetailWindow() {
        super("Details");
        setSize(130, 80);
        final GridLayout layout = new GridLayout(4, 1);
        setLayout(layout);
        add(createIdPanel());
        add(createBoardPanel());
        add(createInverted());
        add(apply);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        apply.addActionListener(e -> {
            final Details details = new Details(id.getText(), board.getText(), inverted.isSelected());
            this.applyListener.applyId(details);
        });

    }

    private Component createInverted() {
        return createPanel("Inverted", this.inverted);
    }

    private JPanel createBoardPanel() {
        return createPanel("Board", this.board);
    }

    private JPanel createIdPanel() {
        return createPanel("ID", this.id);
    }

    private JPanel createPanel(final String label, final Component textField) {
        final JPanel board = new JPanel();
        board.setLayout(new GridLayout(1, 2));
        board.add(new JLabel(label));
        board.add(textField);
        return board;
    }


    public void setApplyListener(final ApplyActionListener applyListener) {
        this.applyListener = applyListener;
    }

    @Subscribe
    public void updateDetails(UpdateDetails detailWithPart) {
        setDetails(detailWithPart.getDraftPart());
    }

    public void setDetails(final TrackPart details) {
        id.setText(details.getId().toUiString());
        board.setText(details.getBoardId().toUiString());
        inverted.setSelected(details.isInverted());
    }

    public interface ApplyActionListener {
        void applyId(DetailWindow.Details text);
    }

    public static class Details {

        private final String id;
        private final String board;
        private final boolean inverted;

        private Details(final String id, final String board, final boolean inverted) {
            this.id = id;
            this.board = board;
            this.inverted = inverted;
        }

        public String getId() {
            return id;
        }

        public String getBoardId() {
            return board;
        }

        public boolean isInverted() {
            return inverted;
        }
    }
}