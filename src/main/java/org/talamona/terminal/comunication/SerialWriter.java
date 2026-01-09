package org.talamona.terminal.comunication;


import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter implements Runnable {
    private OutputStream output;
    private String message;
    private boolean running;

    public SerialWriter ( OutputStream out ){
        this.output = out;
        this.running = true;
    }

    public void run (){
        while(true){
            try{
                if (this.message != null && this.message.length() > 0){
                    this.output.write(this.message.getBytes());
                    this.output.flush();
                    this.message = null;
                }
            }
            catch (Exception e ){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
    public void writeMessage(String message){
        this.message = message;
    }
    public void terminate() throws IOException {
        this.running = false;
        this.output.close();
    }
}