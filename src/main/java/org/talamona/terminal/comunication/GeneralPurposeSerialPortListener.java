package org.talamona.terminal.comunication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.io.InputStream;

public class GeneralPurposeSerialPortListener implements SerialPortDataListener {


    private final InputStream serialPortInputStream;

    public GeneralPurposeSerialPortListener(InputStream serialPortInputStream){
        this.serialPortInputStream = serialPortInputStream;
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

            while ( ( data = this.serialPortInputStream.read()) > -1 ){
                if ( data == this.readEndLineFromConfiguration() ) {
                    break;
                }
                message.append((char) data);
            }
            this.printMessageToConsole(message);
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    private void printMessageToConsole(StringBuilder message) {
        System.out.println(message);
    }
    private char readEndLineFromConfiguration(){
        String value = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.END_OF_LINE);
        return (char)Integer.parseInt(value, 16);
    }

}
