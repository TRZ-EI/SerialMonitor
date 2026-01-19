package org.talamona.terminal.comunication;


import com.fazecast.jSerialComm.SerialPort;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.talamona.terminal.crc.CRC16CCITT;
import org.talamona.terminal.crc.CRC32Calculator;
import org.talamona.terminal.crc.CRCCalculator;
import org.talamona.terminal.crc.Crc16CcittKermit;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.io.*;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/01/17
 * Time: 12.42
 */
public class DataWriterToSerialPort implements SerialCommunicatorInterface {


    private Properties properties;

    //input and output streams for sending and receiving data
    private InputStream input;
    private OutputStream output;


    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    private char END_OF_LINE;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";

    private SerialPort serialPort;
    private StringBuffer buffer;

    private SerialWriter writer;
    private DoubleProperty numericValue = new SimpleDoubleProperty();


    public DataWriterToSerialPort() throws IOException {
        this.init();
    }
    public DataWriterToSerialPort(SerialPort serialPort) throws IOException {
        this.serialPort = serialPort;
        this.init();
        this.initOutputStream();
    }
    private void init() throws IOException {
        this.properties = ConfigurationHolder.getInstance().getProperties();
        this.END_OF_LINE = this.readNewLineValueFromConfiguration();
        this.buffer = new StringBuffer();
    }

    private char readNewLineValueFromConfiguration() {
        String value = this.properties.getProperty(ConfigurationHolder.END_OF_LINE);
        return (char)Integer.parseInt(value, 16);

    }

    public SerialPort connect() {
        this.serialPort = SerialDataManager.createNewInstance().connectToSerialPort();
        this.initOutputStream();
        return this.serialPort;
    }


    //open the input and output streams
    //pre: an open port
    //post: initialized input and output streams for use to communicate data
    public boolean initOutputStream(){
        output = serialPort.getOutputStream();
        this.writer = new SerialWriter(output);
        Thread t = new Thread(this.writer);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        return (output != null);
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads


    private boolean calculateCRC(String message) {
        boolean retValue = false;
        int crcDigits = 4; // 4 hex digits
        int size = message.length();
        if (size > crcDigits){
            String hexCrc = message.substring(size - crcDigits);
            String messageToCalculate = message.substring(0, size - crcDigits);
            String crcHex = calculateCrCForString(messageToCalculate);
            if (crcHex.length() < crcDigits){
                crcHex = "0" + crcHex;
            }
            retValue = hexCrc.equalsIgnoreCase(crcHex);
        }
        return retValue;
    }

    public String calculateCrCForString(String messageToCalculate) {
        long crc = this.selectCalculator().calculateCRCForStringMessage(messageToCalculate);
        return this.fillHexCodeToFourDigitIfNecessary(crc);
    }
    private CRCCalculator selectCalculator() {
        String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("kermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
    }

    private String fillHexCodeToFourDigitIfNecessary(long crc) {
        String hexCode = Long.toHexString(crc);
        int len = hexCode.length();
        int zeros = 4 -len;
        if (zeros > 0){
            for (int i = 0; i < zeros; i ++){
                hexCode = "0" + hexCode;
            }
        }
        return hexCode;
    }


    private void calculateChecksumAndSendResponse(String s) throws IOException {
        String receveid = s.replace('\r', ' ').trim();
        if (receveid.contains("*")) {
            String delimiter = "*";
            String[] parts = receveid.split(Pattern.quote(delimiter));
            String message = parts[0];
            String checksum = parts[1];
            long checksumValue = Long.valueOf(checksum);

            CRC32Calculator calculator = CRC32Calculator.getInstance();


            long calculatedChecksum = calculator.calculateCRC(message);

            if (checksumValue == calculatedChecksum) {
                //this.writer.writeMessage(String.copyValueOf(new char[]{'O', 'K', '\n'}));

                this.output.write(("OK" + this.END_OF_LINE).getBytes());
                this.output.flush();
            }
        }
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(byte[] data) {
        try {
            for (int i = 0; i < data.length; i++) {
                output.write(data[i]);
                output.flush();
            }
        } catch (Exception e) {
            logText = "Failed to write data. (" + e.toString() + ")";
            System.out.println(logText);
        }
    }
}


