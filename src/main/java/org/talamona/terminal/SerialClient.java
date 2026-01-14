package org.talamona.terminal;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Inner class to set up a serial connection.  This requires JavaComm 2.
 */
class SerialClient {

    SerialPort port = null;

    /**
     * Construct a serial comms client
     *
     * @param wantedPortName Desired serial port name, e.g. "COM1"
     * @param baudRate       Desired baud rate.
     */
    public SerialClient(String wantedPortName, int baudRate) {

        // Get an enumeration of all ports known to JavaComm
        SerialPort[] ports = SerialPort.getCommPorts();

//            Enumeration portIdentifiers =
//                    CommPortIdentifier.getPortIdentifiers();

//            CommPortIdentifier portId = null;

        for (SerialPort port : ports) {
            port.getSystemPortName();
        }
        this.port = SerialPort.getCommPort(wantedPortName);
        this.port.setBaudRate(baudRate);
        this.port.openPort();
        // If there's a serial port with the correct name, grab it.
/*
        while (portIdentifiers.hasMoreElements()) {
            CommPortIdentifier pid =
                    (CommPortIdentifier) portIdentifiers.nextElement();
            if (pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
                    pid.getName().equals(wantedPortName)) {
                portId = pid;
                break;
            }
        }
        if (portId == null) {
            JOptionPane.showMessageDialog(null,
                    "Could not find serial port " + wantedPortName,
                    "Error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            port = (SerialPort) portId.open("CrapTerminal", 10000);
        } catch (PortInUseException e) {
            JOptionPane.showMessageDialog(null, "Port " + wantedPortName +
                    " is in use by another application.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        try {
            port.setSerialPortParams(baudRate, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException ex) {
            JOptionPane.showMessageDialog(null,
                    "Could not configure port to required parameters (e.g. baud" + " rate)", "Error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    // Done setting up, now we wait for something to grab the IOStreams.
*/
    }

    /**
     * Get the OutputStream attached to the serial port.
     *
     * @return The OutputStream.
     */
    public OutputStream getOutputStream() {
        OutputStream os = null;
        try {
            os = port.getOutputStream();
        } catch (Exception e) {
        }
        return os;
    }

    /**
     * Get the InputStream attached to the serial port.
     *
     * @return The InputStream.
     */
    public InputStream getInputStream() {
        InputStream is = null;
        try {
            is = port.getInputStream();
        } catch (Exception e) {
        }
        return is;
    }

    /**
     * Close the port, so other things can use it.
     */
    public void close() {
        port.closePort();
    }
}// End Inner class
