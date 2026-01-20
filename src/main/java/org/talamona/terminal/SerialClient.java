package org.talamona.terminal;

import com.fazecast.jSerialComm.SerialPort;
import org.talamona.terminal.comunication.GeneralPurposeSerialPortListener;
import org.talamona.terminal.comunication.SerialDataManager;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.io.InputStream;
import java.io.OutputStream;

public class SerialClient implements Runnable{

    SerialPort port = null;
    private SerialPort serialPort;
    private String portName;
    private String baudRate;


    public SerialClient() {
        this.portName = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.PORT);
        this.baudRate = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.BAUD_RATE);
    }
    @Override
    public void run() {
        this.configureAndOpenSerialPort();

    }
    private void configureAndOpenSerialPort() {
        int baudRate = Integer.parseInt(this.baudRate, 10);
        this.serialPort = SerialPort.getCommPort(this.portName);
        this.serialPort.setBaudRate(baudRate);
        this.serialPort.setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        this.serialPort.openPort();
        this.serialPort.addDataListener(new GeneralPurposeSerialPortListener(this.serialPort.getInputStream()));
        if (this.serialPort != null){
            System.out.println("Connected to " + this.serialPort.getSystemPortName() + " and baud = " + this.serialPort.getBaudRate());
        }
    }


    /**
     * Close the port, so other things can use it.
     */
    public void close() {
        port.closePort();
    }
    public static void main(String[] args){
        SerialClient client = new SerialClient();
        Thread runner = new Thread(client);
        runner.start();

        System.out.println("********* SERIAL CLIENT STARTED *****************");
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String input = "WRITE  'STOP' TO STOP SERIAL CLIENT ...";
        System.out.println(input);
        try {
            while (!(input = reader.readLine()).isEmpty()) {
                if (input.equalsIgnoreCase("stop")) {
                    runner.suspend();
                    System.out.println("Comunicator interrupted");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }



}// End Inner class
