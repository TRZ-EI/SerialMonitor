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
 *  Cylcic Redundancy Check (CRC-CCIIT Kermit 0x0000).
 *
 *  1 + x + x^5 + x^12 + x^16 is irreducible polynomial.
 *
 *  polynomial of CRC_CCITT (X16 + X12 + X5 + 1) to generate CRC code.
 *
 *  CRC16-CCITT 123456789
 *  CRC16-CCITT = 29b1
 *
 ******************************************************************************/

public class Crc16CcittKermit  implements CRCCalculator {

    private final int polynomial = 0x8408; // 0001 0000 0010 0001  (0, 5, 12)

    public static Crc16CcittKermit getNewInstance() {
        return new Crc16CcittKermit();
    }


    public long calculateCRCForStringMessage(String message){
        return crc16(message.getBytes());
    }
    public long calculateCRCforByteArrayMessage(byte[] message){
        return this.crc16(message);
    }
    private int crc16(byte[] buffer) {
        int length = buffer.length;
        int crc = 0x0;
        int carry = 0;
        for(int index = 0; index < length; index++) {
            crc ^= (buffer[index] & 0xFF);
            for(int i = 0; i < 8; i++) {
                carry = crc & 1;
                crc >>= 1;
                if(carry != 0) {
                    crc ^= this.polynomial;
                }
            }

        }
        return crc;
    }


}
