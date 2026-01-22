package org.talamona.terminal.comunication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.talamona.terminal.gui.DrawingText;

import java.util.List;

public class TRZSerialPortListener implements SerialPortDataListener {

    private SerialDataManager serialDataManager;

    public TRZSerialPortListener(SerialDataManager serialDataManager){
        this.serialDataManager = serialDataManager;
        this.serialDataManager.getIncomingTextAree().setText("");
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event)    {
        int data = 0;
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
            this.serialDataManager.getIncomingTextAree().setText("");
            return;
        }

        try {
            StringBuilder message = new StringBuilder();

            while ( ( data = this.serialDataManager.getSerialPort().getInputStream().read()) > -1 ){
                if ( data == this.serialDataManager.getNEW_LINE() ) {
                    break;
                }
                message.append((char) data);
            }

            boolean isValid = this.serialDataManager.calculateCRC(new String[]{message.toString()});
            String strippedMessage = stripCrcFromMessage(message.toString());
            this.appendMessageToTextArea(strippedMessage);

                // ONLY ONE MESSAGE
                if (!isValid) {
                    throw new Exception("PROBLEMS WITH CRC");
                }



        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    private String stripCrcFromMessage(String message) {
        int crcLenght = 4;
        return message.substring(0, message.length() - crcLenght);

    }

    private void appendMessageToTextArea(String message) {
        this.serialDataManager.getIncomingTextAree().append(message + '\n');
        this.serialDataManager.getIncomingTextAree().requestFocusInWindow();

/*
        String originalMessage = this.serialDataManager.getIncomingTextAree().getText();
        if (originalMessage != null){
            StringBuilder buffer = new StringBuilder(originalMessage);
            buffer.append('\n');
            buffer.append(message.toString());
            this.serialDataManager.getIncomingTextAree().setText(buffer.toString());

        }
*/

    }
}
