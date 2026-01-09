package org.talamona.terminal.comunication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.talamona.terminal.gui.DrawingText;

import java.util.List;

public class TRZSerialPortListener implements SerialPortDataListener {

    private SerialDataManager serialDataManager;
    private DrawingText main;

    public TRZSerialPortListener(SerialDataManager serialDataManager){
        this.serialDataManager = serialDataManager;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event)    {
        int data = 0;
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
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
            if (message.toString().startsWith("^")) {

                // ONLY ONE MESSAGE
                boolean isValid = this.serialDataManager.calculateCRC(new String[]{message.toString()});
                if (isValid) {

                    // TO MANAGE MULTIPLE COMMANDS IN A SINGLE ROW
                    List<String> commands = this.serialDataManager.getMultipleCommandSplitter().splitMultipleCommand(message.toString());

                    this.serialDataManager.getSerialBuffer().addAll(commands);
//                    this.main.runAndWaitMyRunnable();

                    this.serialDataManager.getSerialPort().getOutputStream().write(new String("OK").getBytes());
                    this.serialDataManager.getSerialPort().getOutputStream().flush();
                }
                else{
                    //serialPort.getOutputStream().write(new String("KO: " + message.toString() + NEW_LINE).getBytes());
                    //serialPort.getOutputStream().flush();
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }    }
}
