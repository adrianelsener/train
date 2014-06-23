package sample;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.adventnet.snmp.snmp2.SnmpAPI;

public class t1 {
    static JTextField IPAddress = new JTextField("172.16.100.2");
    static JLabel Status = new JLabel();

    final static JCheckBox ch1 = new JCheckBox("P3.1");
    final static JCheckBox ch2 = new JCheckBox("P3.2");
    final static JCheckBox ch3 = new JCheckBox("P3.3");
    final static JCheckBox ch4 = new JCheckBox("P3.4");
    final static JCheckBox ch5 = new JCheckBox("P3.5");
    final static JCheckBox ch6 = new JCheckBox("P3.6");
    final static JCheckBox ch7 = new JCheckBox("P3.7");
    final static JCheckBox ch8 = new JCheckBox("P3.8");

    final static JCheckBox ch9 = new JCheckBox("P5.1");
    final static JCheckBox ch10 = new JCheckBox("P5.2");
    final static JCheckBox ch11 = new JCheckBox("P5.3");
    final static JCheckBox ch12 = new JCheckBox("P5.4");
    final static JCheckBox ch13 = new JCheckBox("P5.5");
    final static JCheckBox ch14 = new JCheckBox("P5.6");
    final static JCheckBox ch15 = new JCheckBox("P5.7");
    final static JCheckBox ch16 = new JCheckBox("P5.8");

    static JLabel InL1 = new JLabel();
    static JLabel InL2 = new JLabel();
    static JLabel InL3 = new JLabel();
    static JLabel InL4 = new JLabel();
    static JLabel InL5 = new JLabel();
    static JLabel InL6 = new JLabel();
    static JLabel InL7 = new JLabel();
    static JLabel InL8 = new JLabel();

    private static void doTimesIn2(int times) {
        long sleepduration = 100;
        for (int i = 0; i < times; i++) {
            System.out.println("Durchlauf " + i);
            System.out.println("Auf on Setzen");
            doOn();
            // Status.setText(Status.getText() + "  times " + times);
            // try {
            // Thread.sleep(sleepduration);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            System.out.println("Auf off Setzen");
            doOff();
            // Status.setText(Status.getText() + "  times " + times);
            // try {
            // Thread.sleep(sleepduration);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

        }
    }

    private static void doOff() {
        try {
            final MYSNMP a = new MYSNMP();
            int test = 0;
            test = test & 253;
            a.SNMP_SET(IPAddress.getText(), 161,
                    ".1.3.6.1.4.1.19865.1.2.1.33.0", SnmpAPI.INTEGER,
                    Integer.toString(test), "private");
            Status.setText("P3 changed !");
        } catch (NumberFormatException e1) {
            Status.setText("Error: Unable to connect with the target!");
        }

    }

    private static void doOn() {
        try {
            final MYSNMP a = new MYSNMP();
            int test = 0;
            test = test | 2;
            a.SNMP_SET(IPAddress.getText(), 161,
                    ".1.3.6.1.4.1.19865.1.2.1.33.0", SnmpAPI.INTEGER,
                    Integer.toString(test), "private");
            Status.setText("P3 changed !");
        } catch (NumberFormatException e1) {
            Status.setText("Error: Unable to connect with the target!");
        }

    }

    static void ReadP3() throws NumberFormatException {
        try {
            final MYSNMP a = new MYSNMP();
            int test = Integer.parseInt(a.SNMP_GET(IPAddress.getText(), 161,
                    ".1.3.6.1.4.1.19865.1.2.1.33.0", "private"));
            if ((test & 128) == 0) {
                ch8.setSelected(false);
            } else {
                ch8.setSelected(true);
            }
            if ((test & 64) == 0) {
                ch7.setSelected(false);
            } else {
                ch7.setSelected(true);
            }
            if ((test & 32) == 0) {
                ch6.setSelected(false);
            } else {
                ch6.setSelected(true);
            }
            if ((test & 16) == 0) {
                ch5.setSelected(false);
            } else {
                ch5.setSelected(true);
            }
            if ((test & 8) == 0) {
                ch4.setSelected(false);
            } else {
                ch4.setSelected(true);
            }
            if ((test & 4) == 0) {
                ch3.setSelected(false);
            } else {
                ch3.setSelected(true);
            }
            if ((test & 2) == 0) {
                ch2.setSelected(false);
            } else {
                ch2.setSelected(true);
            }
            if ((test & 1) == 0) {
                ch1.setSelected(false);
            } else {
                ch1.setSelected(true);
            }
            Status.setText("Connection established !");
        } catch (NumberFormatException e1) {
            Status.setText("Error: Unable to connect with the target!");
        }
    }

    static void ReadP5() throws NumberFormatException {
        try {
            final MYSNMP a = new MYSNMP();
            int test = Integer.parseInt(a.SNMP_GET(IPAddress.getText(), 161,
                    ".1.3.6.1.4.1.19865.1.2.2.33.0", "private"));
            if ((test & 128) == 0) {
                ch9.setSelected(false);
            } else {
                ch9.setSelected(true);
            }
            if ((test & 64) == 0) {
                ch10.setSelected(false);
            } else {
                ch10.setSelected(true);
            }
            if ((test & 32) == 0) {
                ch11.setSelected(false);
            } else {
                ch11.setSelected(true);
            }
            if ((test & 16) == 0) {
                ch12.setSelected(false);
            } else {
                ch12.setSelected(true);
            }
            if ((test & 8) == 0) {
                ch13.setSelected(false);
            } else {
                ch13.setSelected(true);
            }
            if ((test & 4) == 0) {
                ch14.setSelected(false);
            } else {
                ch14.setSelected(true);
            }
            if ((test & 2) == 0) {
                ch15.setSelected(false);
            } else {
                ch15.setSelected(true);
            }
            if ((test & 1) == 0) {
                ch16.setSelected(false);
            } else {
                ch16.setSelected(true);
            }
            Status.setText("Connection established !");
        } catch (NumberFormatException e1) {
            Status.setText("Error: Unable to connect with the target!");
        }
    }

    static void SetP3() throws NumberFormatException {
        try {
            final MYSNMP a = new MYSNMP();
            int test = 0;
            if (ch8.isSelected()) {
                test = test | 128;
            } else {
                test = test & 127;
            }
            if (ch7.isSelected()) {
                test = test | 64;
            } else {
                test = test & 191;
            }
            if (ch6.isSelected()) {
                test = test | 32;
            } else {
                test = test & 223;
            }
            if (ch5.isSelected()) {
                test = test | 16;
            } else {
                test = test & 239;
            }
            if (ch4.isSelected()) {
                test = test | 8;
            } else {
                test = test & 247;
            }
            if (ch3.isSelected()) {
                test = test | 4;
            } else {
                test = test & 251;
            }
            if (ch2.isSelected()) {
                test = test | 2;
            } else {
                test = test & 253;
            }
            if (ch1.isSelected()) {
                test = test | 1;
            } else {
                test = test & 254;
            }

            a.SNMP_SET(IPAddress.getText(), 161,
                    ".1.3.6.1.4.1.19865.1.2.1.33.0", SnmpAPI.INTEGER,
                    Integer.toString(test), "private");
            Status.setText("P3 changed !");
        } catch (NumberFormatException e1) {
            Status.setText("Error: Unable to connect with the target!");
        }
    }

    static void SetP5() throws NumberFormatException {
        try {
            final MYSNMP a = new MYSNMP();
            int test = 0;
            if (ch9.isSelected()) {
                test = test | 128;
            } else {
                test = test & 127;
            }
            if (ch10.isSelected()) {
                test = test | 64;
            } else {
                test = test & 191;
            }
            if (ch11.isSelected()) {
                test = test | 32;
            } else {
                test = test & 223;
            }
            if (ch12.isSelected()) {
                test = test | 16;
            } else {
                test = test & 239;
            }
            if (ch13.isSelected()) {
                test = test | 8;
            } else {
                test = test & 247;
            }
            if (ch14.isSelected()) {
                test = test | 4;
            } else {
                test = test & 251;
            }
            if (ch15.isSelected()) {
                test = test | 2;
            } else {
                test = test & 253;
            }
            if (ch16.isSelected()) {
                test = test | 1;
            } else {
                test = test & 254;
            }
            a.SNMP_SET(IPAddress.getText(), 161,
                    ".1.3.6.1.4.1.19865.1.2.2.33.0", SnmpAPI.INTEGER,
                    Integer.toString(test), "private");
            Status.setText("P5 changed !");
        } catch (NumberFormatException e1) {
            Status.setText("Error: Unable to connect with the target!");
        }

    }

    static void ReadADC() throws NumberFormatException {

        final MYSNMP a = new MYSNMP();
        InL1.setText("AI1="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.1.0", "private"));
        InL2.setText("AI2="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.2.0", "private"));
        InL3.setText("AI3="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.3.0", "private"));
        InL4.setText("AI4="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.4.0", "private"));
        InL5.setText("AI5="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.5.0", "private"));
        InL6.setText("AI6="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.6.0", "private"));
        InL7.setText("AI7="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.7.0", "private"));
        InL8.setText("AI8="
                + a.SNMP_GET(IPAddress.getText(), 161,
                        ".1.3.6.1.4.1.19865.1.2.3.8.0", "private"));
        Status.setText("Data received !");

    }

    public static void main(String args[]) {

        JFrame frame = new JFrame("Denkovi Ethernet Module Tester");
        frame.setBounds(300, 50, 670, 320);
        frame.setLayout(null);
        frame.setResizable(false);

        JPanel panel1 = new JPanel();
        panel1.setBorder(BorderFactory.createLineBorder(Color.black));
        panel1.setBounds(10, 10, 200, 160);
        frame.add(panel1);
        panel1.setVisible(true);
        panel1.setLayout(null);

        JLabel Label_1 = new JLabel();
        Label_1.setText("Port JP3 (P3)");
        Label_1.setBounds(60, 10, 200, 20);
        panel1.add(Label_1);
        JLabel Label_11 = new JLabel();
        Label_11.setText("Digital Outputs");
        Label_11.setBounds(56, 25, 200, 20);
        panel1.add(Label_11);

        ch1.setBounds(10, 50, 50, 20);
        panel1.add(ch1);
        ch1.setVisible(true);

        ch2.setBounds(10, 70, 50, 20);
        panel1.add(ch2);
        ch2.setVisible(true);

        ch3.setBounds(10, 90, 50, 20);
        panel1.add(ch3);
        ch3.setVisible(true);

        ch4.setBounds(10, 110, 50, 20);
        panel1.add(ch4);
        ch4.setVisible(true);

        ch5.setBounds(120, 50, 50, 20);
        panel1.add(ch5);
        ch5.setVisible(true);

        ch6.setBounds(120, 70, 50, 20);
        panel1.add(ch6);
        ch6.setVisible(true);

        ch7.setBounds(120, 90, 50, 20);
        panel1.add(ch7);
        ch7.setVisible(true);

        ch8.setBounds(120, 110, 50, 20);
        panel1.add(ch8);
        ch8.setVisible(true);

        JButton SetPort3 = new JButton("Set P3");
        SetPort3.setBounds(10, 135, 180, 20);
        panel1.add(SetPort3);

        JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createLineBorder(Color.black));
        panel2.setBounds(230, 10, 200, 160);
        frame.add(panel2);
        panel2.setVisible(true);
        panel2.setLayout(null);

        JLabel Label_2 = new JLabel();
        Label_2.setText("Port JP4 (P5)");
        Label_2.setBounds(60, 10, 200, 20);
        panel2.add(Label_2);
        JLabel Label_21 = new JLabel();
        Label_21.setText("Digital Outputs");
        Label_21.setBounds(56, 25, 200, 20);
        panel2.add(Label_21);

        ch9.setBounds(10, 50, 50, 20);
        panel2.add(ch9);
        ch9.setVisible(true);

        ch10.setBounds(10, 70, 50, 20);
        panel2.add(ch10);
        ch10.setVisible(true);

        ch11.setBounds(10, 90, 50, 20);
        panel2.add(ch11);
        ch11.setVisible(true);

        ch12.setBounds(10, 110, 50, 20);
        panel2.add(ch12);
        ch12.setVisible(true);

        ch13.setBounds(120, 50, 50, 20);
        panel2.add(ch13);
        ch13.setVisible(true);

        ch14.setBounds(120, 70, 50, 20);
        panel2.add(ch14);
        ch14.setVisible(true);

        ch15.setBounds(120, 90, 50, 20);
        panel2.add(ch15);
        ch15.setVisible(true);

        ch16.setBounds(120, 110, 50, 20);
        panel2.add(ch16);
        ch16.setVisible(true);

        JButton SetPort5 = new JButton("Set P5");
        SetPort5.setBounds(10, 135, 180, 20);
        panel2.add(SetPort5);

        JPanel panel3 = new JPanel();
        panel3.setBorder(BorderFactory.createLineBorder(Color.black));
        panel3.setBounds(450, 10, 200, 160);
        frame.add(panel3);
        panel3.setVisible(true);
        panel3.setLayout(null);

        JLabel Label_3 = new JLabel();
        Label_3.setText("Port JP5 (P6)");
        Label_3.setBounds(60, 10, 200, 20);
        panel3.add(Label_3);
        JLabel Label_31 = new JLabel();
        Label_31.setText("Analog Inputs (max 1023)");
        Label_31.setBounds(30, 25, 200, 20);
        panel3.add(Label_31);

        JButton Read = new JButton("Read");
        Read.setBounds(10, 135, 180, 20);
        panel3.add(Read);
        // final MYSNMP a = new MYSNMP();
        // a.SNMP_GET("172.16.100.2", 161, ".1.3.6.1.4.1.19865.1.2.3.1.0",
        // "private");

        InL1.setText("AI1=0");
        InL1.setBounds(40, 50, 200, 20);
        panel3.add(InL1);

        InL2.setText("AI2=0");
        InL2.setBounds(40, 70, 200, 20);
        panel3.add(InL2);

        InL3.setText("AI3=0");
        InL3.setBounds(40, 90, 200, 20);
        panel3.add(InL3);

        InL4.setText("AI4=0");
        InL4.setBounds(40, 110, 200, 20);
        panel3.add(InL4);

        InL5.setText("AI5=0");
        InL5.setBounds(120, 50, 200, 20);
        panel3.add(InL5);

        InL6.setText("AI6=0");
        InL6.setBounds(120, 70, 200, 20);
        panel3.add(InL6);

        InL7.setText("AI7=0");
        InL7.setBounds(120, 90, 200, 20);
        panel3.add(InL7);

        InL8.setText("AI8=0");
        InL8.setBounds(120, 110, 200, 20);
        panel3.add(InL8);

        // -----------------------------
        JPanel panel4 = new JPanel();
        panel4.setBorder(BorderFactory.createLineBorder(Color.black));
        panel4.setBounds(10, 180, 640, 40);
        frame.add(panel4);
        panel4.setVisible(true);
        panel4.setLayout(null);

        IPAddress.setBounds(30, 10, 140, 20);
        panel4.add(IPAddress);

        JLabel IPlabel = new JLabel("IP");
        IPlabel.setBounds(10, 10, 200, 20);
        panel4.add(IPlabel);

        JButton Connect = new JButton("Test target");
        Connect.setBounds(270, 10, 70, 20);
        panel4.add(Connect);

        JButton _10times = new JButton("Set 10Times");
        _10times.setBounds(350, 10, 50, 20);
        panel4.add(_10times);

        final JTextField txtTimes = new JTextField("10");
        txtTimes.setBounds(430, 10, 30, 20);
        panel4.add(txtTimes);

        _10times.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int times = Integer.parseInt(txtTimes.getText());
                System.out.println(times);
                doTimesIn2(times);
            }

        });

        JPanel panel5 = new JPanel();
        panel5.setBorder(BorderFactory.createLineBorder(Color.black));
        panel5.setBounds(10, 240, 640, 30);
        frame.add(panel5);
        panel5.setVisible(true);
        panel5.setLayout(null);

        Status.setText("...");
        Status.setBounds(10, 5, 400, 20);
        panel5.add(Status);

        frame.setVisible(true);
        // ReadP3();

        Connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReadADC();
                ReadP3();
                ReadP5();

            }
        });

        SetPort3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SetP3();
                ReadP5();
                ReadADC();
                ReadP3();
            }
        });

        SetPort5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SetP5();
                ReadP3();
                ReadADC();
                ReadP5();
            }
        });

        Read.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReadADC();
                ReadP3();
                ReadP5();
            }
        });

    }

}
