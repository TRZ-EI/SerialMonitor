package org.talamona.terminal.crc;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 03/06/17
 * Time: 17.26
 */

import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 9.12
 */
public class CRC32Calculator implements CRCCalculator {

    private static CRC32Calculator instance;
    private CRC32Calculator(){

    }
    public static CRC32Calculator getInstance(){
        if (instance == null){
            instance = new CRC32Calculator();
        }
        return instance;
    }
    public long calculateCRC(String message) {
        return this.calculateCRCForStringMessage(message);
    }
    @Override
    public long calculateCRCForStringMessage(String message) {
        return (int)this.calculateCRC32(message);
    }
    @Override
    public long calculateCRCforByteArrayMessage(byte[] message) {
        String m = Arrays.toString(message);
        return this.calculateCRCForStringMessage(m);
    }
    private long calculateCRC32(String message){
        byte bytes[] = message.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();
    }
}