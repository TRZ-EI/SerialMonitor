package org.talamona.terminal.crc;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 21/03/17
 * Time: 13.42
 */
/******************************************************************************
 *
 *  Reads in a sequence of bytes and prints out its 16 bit
 *  Cylcic Redundancy Check (CRC-CCIIT 0xFFFF).
 *
 *  1 + x + x^5 + x^12 + x^16 is irreducible polynomial.
 *
 *  polynomial of CRC_CCITT (X16 + X12 + X5 + 1) to generate CRC code.
 *
 *  CRC16-CCITT 123456789
 *  CRC16-CCITT = 29b1
 *
 ******************************************************************************/

public class CRC16CCITT implements CRCCalculator{

    private final int polynomial = 0x1021; // 0001 0000 0010 0001  (0, 5, 12)

    public static CRC16CCITT getNewInstance() {
        return new CRC16CCITT();
    }


    public long calculateCRCForStringMessage(String message){
        return this.calculateCRCforByteArrayMessage(message.getBytes());
    }
    public long calculateCRCforByteArrayMessage(byte[] message){
        int crc = 0xFFFF;          // initial value
        for (byte b : message) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= this.polynomial;
            }
        }
        
        return crc &= 0xffff;
    }
    public static void main(String[] args){
        CRC16CCITT calculator = CRC16CCITT.getNewInstance();
        String message = "^V07S310146";
        String referenceValue = Long.toHexString(calculator.calculateCRCForStringMessage(message));
        System.out.println(referenceValue);
    }


}