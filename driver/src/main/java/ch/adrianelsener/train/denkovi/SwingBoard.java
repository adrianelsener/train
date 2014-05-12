package ch.adrianelsener.train.denkovi;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import ch.adrianelsener.train.denkovi.DenkoviWrapper.Jp;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.Pin;
import ch.adrianelsener.train.denkovi.DenkoviWrapper.State;

public class SwingBoard implements Board {
    private final Map<Pin, State> currentState = new HashMap<>();
    private final List<JCheckBox> checkboxes = new ArrayList<>();

    private final Map<PinKey, Integer> mapping = new HashMap<>();

    public static void main(String[] args) {
        SwingBoard swingBoard = new SwingBoard();
    }

    public SwingBoard() {
        initMapping();
        initUi();
        initStates();
        new Thread(new ConsoleControle(this)).start();
    }

    private void initMapping() {
        mapping.put(new PinKey(Pin._09.on(), Pin._10.on(), Pin._11.on(), Pin._12.on()), 1);
        mapping.put(new PinKey(Pin._09.off(), Pin._10.on(), Pin._11.on(), Pin._12.on()), 2);
        mapping.put(new PinKey(Pin._09.on(), Pin._10.off(), Pin._11.on(), Pin._12.on()), 3);
        mapping.put(new PinKey(Pin._09.off(), Pin._10.off(), Pin._11.on(), Pin._12.on()), 4);
        mapping.put(new PinKey(Pin._09.on(), Pin._10.off(), Pin._11.off(), Pin._12.on()), 5);
        mapping.put(new PinKey(Pin._09.off(), Pin._10.off(), Pin._11.off(), Pin._12.on()), 6);
        mapping.put(new PinKey(Pin._09.on(), Pin._10.on(), Pin._11.on(), Pin._12.off()), 7);
        mapping.put(new PinKey(Pin._09.off(), Pin._10.on(), Pin._11.on(), Pin._12.off()), 8);
    }

    private void initStates() {
        currentState.put(Pin._09, State.Off);
        currentState.put(Pin._10, State.Off);
        currentState.put(Pin._11, State.Off);
        currentState.put(Pin._12, State.Off);
    }

    private void initUi() {
        JFrame frame = new JFrame("Swing Board");
        frame.setBounds(300, 50, 670, 320);
        JButton btnExit = createExitButton();
        frame.add(btnExit);
        GridLayout layout = new GridLayout(4, 10);
        frame.setLayout(layout);
        for (int i = 1; i < 15; i++) {
            checkboxes.add(new JCheckBox("R" + i));
        }
        checkboxes.add(new JCheckBox("EnableR"));
        for (JCheckBox checkBox : checkboxes) {
            frame.add(checkBox);
        }
        frame.setVisible(true);
    }

    private JButton createExitButton() {
        JButton btnExit = new JButton("exit");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return btnExit;
    }

    @Override
    public void close() {

    }

    @Override
    public int SNMP_SET(int Port, Jp OID, byte dataType, String SetValue, String Community) {
        return 0;
    }

    @Override
    public String SNMP_GET(int Port, Jp OID, String Community) {
        return null;
    }

    @Override
    public void set(PinState pinState) {
        currentState.put(pinState.pin, pinState.state);
        if (Pin._13.off().equals(pinState)) {
            applyStates(currentState);
        }
    }

    private void applyStates(Map<Pin, State> currentState) {

        PinKey key = new PinKey(new PinState(Pin._09, currentState.get(Pin._09))//
                , new PinState(Pin._10, currentState.get(Pin._10))//
                , new PinState(Pin._11, currentState.get(Pin._11))//
                , new PinState(Pin._12, currentState.get(Pin._12)));

        Integer toEnable = mapping.get(key);
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setSelected(false);
        }
        checkboxes.get(toEnable - 1).setSelected(true);

    }

    private static class PinKey {
        final PinState r1;
        final PinState r2;
        final PinState r3;
        final PinState r4;

        public PinKey(PinState r1, PinState r2, PinState r3, PinState r4) {
            this.r1 = r1;
            this.r2 = r2;
            this.r3 = r3;
            this.r4 = r4;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((r1 == null) ? 0 : r1.hashCode());
            result = prime * result + ((r2 == null) ? 0 : r2.hashCode());
            result = prime * result + ((r3 == null) ? 0 : r3.hashCode());
            result = prime * result + ((r4 == null) ? 0 : r4.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            PinKey other = (PinKey) obj;
            if (r1 == null) {
                if (other.r1 != null) {
                    return false;
                }
            } else if (!r1.equals(other.r1)) {
                return false;
            }
            if (r2 == null) {
                if (other.r2 != null) {
                    return false;
                }
            } else if (!r2.equals(other.r2)) {
                return false;
            }
            if (r3 == null) {
                if (other.r3 != null) {
                    return false;
                }
            } else if (!r3.equals(other.r3)) {
                return false;
            }
            if (r4 == null) {
                if (other.r4 != null) {
                    return false;
                }
            } else if (!r4.equals(other.r4)) {
                return false;
            }
            return true;
        }

    }

}
