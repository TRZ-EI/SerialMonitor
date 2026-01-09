package org.talamona.terminal.comunication;

import com.fazecast.jSerialComm.SerialPort;
import org.talamona.terminal.crc.CRC16CCITT;
import org.talamona.terminal.crc.CRCCalculator;
import org.talamona.terminal.crc.Crc16CcittKermit;
import org.talamona.terminal.structure.serial.MultipleCommandSplitter;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: Jan 08, 2026
 * Time: 14.20
 */
public class SerialDataManager {
    private char NEW_LINE;

    private SerialPort serialPort;
    private MultipleCommandSplitter multipleCommandSplitter;
    private BlockingQueue<String> serialBuffer;

    public static SerialDataManager createNewInstance(){
        return new SerialDataManager();
    }

    public static SerialDataManager createNewInstanceBySerialBuffer(BlockingQueue<String> serialBuffer){
        return new SerialDataManager(serialBuffer);
    }

    private SerialDataManager(){
        this.serialBuffer = new LinkedBlockingQueue<>();
        this.multipleCommandSplitter = MultipleCommandSplitter.getNewInstance();
        this.NEW_LINE = this.readEndLineFromConfiguration();
    }
    private SerialDataManager(BlockingQueue<String> serialBuffer) {
        this.serialBuffer = serialBuffer;
        this.multipleCommandSplitter = MultipleCommandSplitter.getNewInstance();
        this.NEW_LINE = this.readEndLineFromConfiguration();
    }
    private char readEndLineFromConfiguration(){
        String value = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.END_OF_LINE);
        return (char)Integer.parseInt(value, 16);
    }


    public SerialPort connectToSerialPort(){
        try {
            String serialPortName = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.PORT);
            String baudRateValue = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.BAUD_RATE);
            int baudRate = Integer.parseInt(baudRateValue, 10);

            this.configureAndOpenSerialPort(serialPortName, baudRate);
            if (serialPort != null){
                System.out.println("Connected !!!");
                this.serialPort.addDataListener(new TRZSerialPortListener(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.serialPort;
    }
    private void configureAndOpenSerialPort(String serialPortName, int baudRate) {
        this.serialPort = SerialPort.getCommPort(serialPortName);
        this.serialPort.setBaudRate(baudRate);
        this.serialPort.setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        this.serialPort.openPort();
    }


    protected boolean calculateCRC(String[] messages) {
        int score = 0;
        int crcDigits = 4; // 4 hex digits
        for (String message: messages){
            int size = message.length();
            if (size > crcDigits){
                String hexCrc = message.substring(size - crcDigits);
                String messageToCalculate = message.substring(0, size - crcDigits);
                CRCCalculator calculator = this.selectCalculator();
                long crc = calculator.calculateCRCForStringMessage(messageToCalculate);
                String crcHex = Long.toHexString(crc);
                if (crcHex.length() == 3){
                    crcHex = "0" + crcHex;

                }else if (crcHex.length() == 2){
                    crcHex = "00" + crcHex;
                }else if (crcHex.length() == 1){
                    crcHex = "000" + crcHex;
                }
                score += (hexCrc.equalsIgnoreCase(crcHex))? 1: 0;
            }
        }
        return (score == messages.length);
    }

    private CRCCalculator selectCalculator() {
        String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("kermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
    }

    public synchronized boolean disconnectFromSerialPort(){
        boolean retValue = true;
        if(this.serialPort != null){
            System.out.println("disconnectFromSerialPort()");
            try {
                this.serialPort.getOutputStream().close();
                this.serialPort.getInputStream().close();
                this.serialPort.closePort();
                this.serialPort.removeDataListener();
            } catch (Exception e) {
                retValue = false;
                e.printStackTrace();
            }
        }
        return retValue;
    }


    public SerialPort getSerialPort() {
        return this.serialPort;
    }

    public char getNEW_LINE() {
        return this.NEW_LINE;
    }

    public BlockingQueue<String> getSerialBuffer() {
        return this.serialBuffer;
    }

    public MultipleCommandSplitter getMultipleCommandSplitter() {
        return this.multipleCommandSplitter;
    }
}
