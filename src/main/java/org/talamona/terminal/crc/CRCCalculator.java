package org.talamona.terminal.crc;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 10.07
 */
public interface CRCCalculator {
    public long calculateCRCForStringMessage(String message);
    public long calculateCRCforByteArrayMessage(byte[] message);
}