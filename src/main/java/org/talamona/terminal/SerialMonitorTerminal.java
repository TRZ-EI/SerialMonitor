/*
 * CrapTerminal
 * Version 3.0, 1 October 2010
 * Copyright 2010 Ian Renton
 * For more information, see this application's web page at:
 * http://www.onlydreaming.net/software/crapterminal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.talamona.terminal;

import com.fazecast.jSerialComm.SerialPort;
import org.talamona.terminal.comunication.SerialDataManager;
import org.talamona.terminal.crc.CRC16CCITT;
import org.talamona.terminal.crc.CRCCalculator;
import org.talamona.terminal.crc.Crc16CcittKermit;
import org.talamona.terminal.utils.ConfigurationHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Scanner;

/**
 * A simple serial console, much like HyperTerminal but infinitely less
 * annoying.
 * @author Ian Renton
 */
public class SerialMonitorTerminal extends javax.swing.JFrame {

    private OutputStream writer = null;
    int lastDirection = 0;
    private javax.swing.JComboBox baudRateComboBox;
    private javax.swing.JComboBox cRcComboBox;
    private javax.swing.JComboBox serialPortPopupMenu;

    private javax.swing.JLabel baudRateLabel;
    private javax.swing.JLabel crcLabel;

    private javax.swing.JPanel bottomLeftPanel;
    private javax.swing.JTextArea incomingTextArea;
    private javax.swing.JScrollPane incomingScrollPane;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel bgPanel;
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JLabel aboutText;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel sendPanel;
    private javax.swing.JLabel serialPortLabel;
    private javax.swing.JPanel topLeftPanel;

    private javax.swing.JButton sendFileButton;
    private javax.swing.JButton sendDashboardButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton clearButton;

    private SerialDataManager serialDataManager;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                SerialMonitorTerminal app = new SerialMonitorTerminal();
            }
        });
    }

    public SerialMonitorTerminal() {
        super("Serial Monitor Terminal");
        this.setLookAndFeel();
        this.initComponents();
        this.fillPopupMenuWithSerialPorts();
        this.setVisible(true);
    }

    private void fillPopupMenuWithSerialPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        this.serialPortPopupMenu.removeAllItems();
        this.serialPortPopupMenu.addItem("");
        for (SerialPort port: ports){
            this.serialPortPopupMenu.addItem(port.getSystemPortName());
        }
        if (this.serialPortPopupMenu.getItemCount() == 2) {
            this.serialPortPopupMenu.setSelectedIndex(1);
        }
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting LAF: " + e);
        }
    }

    private void initComponents() {

        this.leftPanel = new javax.swing.JPanel();
        this.topLeftPanel = new javax.swing.JPanel();
        this.serialPortLabel = new javax.swing.JLabel("Serial Port");
        this.serialPortPopupMenu = new javax.swing.JComboBox();
        this.baudRateLabel = new javax.swing.JLabel("Baud Rate");
        this.crcLabel = new javax.swing.JLabel("CRC method");
        this.baudRateComboBox = new javax.swing.JComboBox();
        this.cRcComboBox = new javax.swing.JComboBox();
        this.bottomLeftPanel = new javax.swing.JPanel();
        this.rightPanel = new javax.swing.JPanel();
        this.bgPanel = new javax.swing.JPanel();
        this.mainPanel = new javax.swing.JPanel();
        this.aboutPanel = new javax.swing.JPanel();
        this.aboutText = new javax.swing.JLabel("TRZ Serial monitor terminal - developed by L.Talamona");
        this.jSplitPane1 = new javax.swing.JSplitPane();
        this.incomingScrollPane = new javax.swing.JScrollPane();
        this.incomingTextArea = new javax.swing.JTextArea(""); // AREA TO PRINT SERIAL DATA
        this.sendPanel = new javax.swing.JPanel();

        this.exitButton = new javax.swing.JButton("Exit");

        this.sendFileButton = new javax.swing.JButton("Send File");
        this.sendFileButton.setToolTipText("Send a block of text or binary data.");

        this.sendDashboardButton = new javax.swing.JButton("Send Dashboard");
        this.sendDashboardButton.setToolTipText("Send dashboard content to serial");

        this.saveButton = new javax.swing.JButton("Save");
        this.saveButton.setToolTipText("Save dashboard content to a file");
        this.clearButton = new javax.swing.JButton("Clear");
        this.clearButton.setToolTipText("Clear the dashboard");

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setBackground(SystemColor.control);
        this.setBounds(new Rectangle(50, 50, 0, 0));
        this.getContentPane().setLayout(new BorderLayout(0, 0));
        this.bgPanel.setLayout(new BorderLayout(0, 0));
        this.mainPanel.setLayout(new BorderLayout(5, 5));
        this.leftPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 0));
        this.leftPanel.setLayout(new BorderLayout(20, 20));
        this.topLeftPanel.setLayout(new GridLayout(0, 1, 5, 2));
        this.topLeftPanel.add(serialPortLabel);
        this.serialPortPopupMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialPortActionPerformed(evt);
            }
        });
        this.topLeftPanel.add(serialPortPopupMenu);
        this.topLeftPanel.add(baudRateLabel);

        this.baudRateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"1200", "2400", "4800", "9600", "19200", "28800",
                "38400", "57600", "115200"}));
        this.baudRateComboBox.setSelectedIndex(8);
        this.baudRateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baudRateActionPerformed(evt);
            }
        });
        this.cRcComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"CRC16CCITT", "CRC16CCITTKermit"}));
        this.cRcComboBox.setSelectedIndex(1);


        this.topLeftPanel.add(this.baudRateComboBox);
        this.topLeftPanel.add(this.crcLabel);
        this.topLeftPanel.add(this.cRcComboBox);
        this.leftPanel.add(topLeftPanel, BorderLayout.NORTH);
        this.bottomLeftPanel.setLayout(new GridLayout(0, 1, 5, 2));
        this.exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        this.bottomLeftPanel.add(exitButton);
        this.leftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        this.mainPanel.add(leftPanel, BorderLayout.WEST);
        this.rightPanel.setLayout(new BorderLayout(5, 5));
        this.jSplitPane1.setDividerLocation(500);
        this.jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        this.jSplitPane1.setResizeWeight(0.9);
        this.incomingScrollPane.setAutoscrolls(true);
        this.incomingScrollPane.setPreferredSize(new Dimension(583, 500));
        this.incomingTextArea.setColumns(80);
        this.incomingTextArea.setEditable(true);


        this.incomingTextArea.setFont(new Font("Monospaced", 0, 12)); // NOI18N
        this.incomingTextArea.setBackground(Color.BLACK);
        this.incomingTextArea.setForeground(Color.GREEN);
        this.incomingTextArea.setCaretColor(Color.WHITE);

        this.incomingTextArea.setLineWrap(true);
        this.incomingTextArea.setWrapStyleWord(true);
        this.incomingScrollPane.setViewportView(incomingTextArea);
        this.jSplitPane1.setLeftComponent(incomingScrollPane);
        this.sendPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.sendFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFile(evt);
            }
        });
        this.saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDataToFile();
                // byte[] dataToSave =
                // TODO: saveFileMethod;
            }
        });
        this.clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDashboard();
            }
        });
        this.sendDashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendDashboard();
            }
        });

        //
        this.sendPanel.add(this.sendFileButton);
        this.sendPanel.add(this.saveButton);
        this.sendPanel.add(this.clearButton);
        this.sendPanel.add(this.sendDashboardButton);

        this.jSplitPane1.setRightComponent(sendPanel);
        this.rightPanel.add(jSplitPane1, BorderLayout.CENTER);
        this.mainPanel.add(rightPanel, BorderLayout.CENTER);
        this.bgPanel.add(mainPanel, BorderLayout.CENTER);
        this.aboutPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.aboutPanel.add(aboutText);
        this.bgPanel.add(aboutPanel, BorderLayout.SOUTH);
        this.getContentPane().add(bgPanel, BorderLayout.CENTER);
        this.pack();

    }


    private void sendDashboard() {
        StringReader stringReader = new StringReader(this.incomingTextArea.getText());
        Scanner textScanner = new Scanner(stringReader);
        String line = null;
        while (textScanner.hasNextLine()){
            line = textScanner.nextLine();
            this.sendTextRowToSerial(line);
        }
    }

    private void clearDashboard() {
        this.incomingTextArea.setText("");
        this.incomingTextArea.repaint();
    }


    private void saveDataToFile() {
        byte[] data = this.incomingTextArea.getText().getBytes();
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        if (fc.getSelectedFile() != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fc.getSelectedFile());
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void serialPortActionPerformed(java.awt.event.ActionEvent evt) {
        String serialPortName = serialPortPopupMenu.getSelectedItem().toString();
        String baudRateName = baudRateComboBox.getSelectedItem().toString();
        if (serialPortName.length() > 0 && baudRateName != null) {

            if (this.serialDataManager != null && this.serialDataManager.getSerialPort().isOpen()) {
                this.serialDataManager.reconnectToSerialPort(serialPortName, baudRateName);
            } else {
                this.serialDataManager = SerialDataManager.createNewInstance();
                this.serialDataManager.setUiControlToWrite(incomingTextArea);
                this.serialDataManager.connectToSerialPort(serialPortName, baudRateName);
                incomingTextArea.setText(null);
            }
        }
    }

    /**
     * Actions performed on a new baud rate being selected.  Chains into the
     * "serial port changed" function, as it needs to do the same thing anyway.
     * @param evt
     */
    private void baudRateActionPerformed(java.awt.event.ActionEvent evt) {
        this.serialPortActionPerformed(null);
    }

    /**
     * Sends the contents of a file as a binary blob.
     * @param evt
     */
    private void sendFile(java.awt.event.ActionEvent evt) {
        this.incomingTextArea.selectAll();
        this.incomingTextArea.replaceSelection("");
        this.incomingTextArea.setText("SENDING FILE TO SERIAL PORT\n");
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        if (fc.getSelectedFile() != null) {
            Scanner lineReader = null;
            try {
                lineReader = new Scanner(fc.getSelectedFile());
                String line = null;
                while (lineReader.hasNextLine()) {
                    line = lineReader.nextLine();
                    this.sendTextRowToSerial(line);
                    this.updateDashBoardArea(line);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private void sendTextRowToSerial(String textRow){
        CRCCalculator crcCalculator = this.selectCalculator();
        try {
            Thread.sleep(500);
            textRow += this.tranformCRCValueToString(crcCalculator.calculateCRCForStringMessage(textRow));
            textRow += this.readNewLineValueFromConfiguration();
            if (this.serialDataManager != null) {
                this.serialDataManager.writeToSerialPort(textRow.getBytes());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    private void updateDashBoardArea(String line){
        this.incomingTextArea.append(line + '\n');
        // Scroll to the end, so latest data is visible.
        this.incomingTextArea.setCaretPosition(this.incomingTextArea.getDocument().getLength());
        this.incomingTextArea.requestFocusInWindow();
        this.incomingTextArea.repaint();
    }

    private CRCCalculator selectCalculator() {
        String crc = this.cRcComboBox.getSelectedItem().toString();
        //String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("CRC16CCITTKermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
    }
    private String tranformCRCValueToString(long crc){
        String crcHex = Long.toHexString(crc);
        if (crcHex.length() == 3){
            crcHex = "0" + crcHex;
        }else if (crcHex.length() == 2){
            crcHex = "00" + crcHex;
        }else if (crcHex.length() == 1){
            crcHex = "000" + crcHex;
        }
        return crcHex;
    }
    private char readNewLineValueFromConfiguration() {
        String value = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.END_OF_LINE);
        return (char)Integer.parseInt(value, 16);

    }
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }


}
