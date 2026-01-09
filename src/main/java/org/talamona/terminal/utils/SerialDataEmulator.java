package org.talamona.terminal.utils;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 09/11/17
 * Time: 10.41
 */

import org.talamona.terminal.crc.CRC16CCITT;
import org.talamona.terminal.crc.CRCCalculator;
import org.talamona.terminal.crc.Crc16CcittKermit;
import org.talamona.terminal.structure.serial.MultipleCommandSplitter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 23/03/17
 * Time: 11.38
 */
public class SerialDataEmulator {
    private BlockingQueue<String> serialBuffer;
    private final int waitingTime;

    private final Byte fineStringa = Byte.valueOf("0A", 16).byteValue();





    private SerialDataEmulator(BlockingQueue<String> queue, int waitingTime) {
        this.serialBuffer = queue;
        this.waitingTime = waitingTime;
    }

    public static SerialDataEmulator getNewInstanceBySerialBufferAndWaitingTime(BlockingQueue<String> queue, int waitingTime) {
        return new SerialDataEmulator(queue, waitingTime);
    }
    public BlockingQueue<String> runScenario(String fileName) throws IOException, InterruptedException {
        String realFileName = this.getClass().getClassLoader().getResource(fileName).getFile();
        Scanner linReader = new Scanner(new File(realFileName));
        CRCCalculator crcCalculator = this.selectCalculator();

        String line = null;
        while(linReader.hasNextLine()){
            Thread.sleep(this.waitingTime);

            line = linReader.nextLine();
            line += this.tranformCRCValueToString(crcCalculator.calculateCRCForStringMessage(line));
            this.serialBuffer.addAll(MultipleCommandSplitter.getNewInstance().splitMultipleCommand(line));
            System.out.println(line);
        }
        return this.serialBuffer;

    }
    private CRCCalculator selectCalculator() {
        String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("kermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
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



    public static void main(String[] args) throws Exception {
        SerialDataEmulator sdm = SerialDataEmulator.getNewInstanceBySerialBufferAndWaitingTime(new LinkedBlockingQueue<>(), 1000);
        sdm.runScenario("serialInputs/prova1-fragment1.txt");
    }





}
