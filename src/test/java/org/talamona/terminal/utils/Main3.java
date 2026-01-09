package org.talamona.terminal.utils;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/11/17
 * Time: 11.56
 */
public class Main3 {
    public static void main(String[] args) {
        Integer min = Integer.MIN_VALUE;
        //String minHex = Integer.toHexString(Integer.MIN_VALUE);
        String minHex = "FFFFFFD8";
        //System.out.println(min + " " + minHex);
        long convertedValue = Integer.parseUnsignedInt(minHex, 16);
        long l = new BigInteger(minHex, 16).intValue();
        System.out.println((int)Long.parseLong(minHex, 16));
    }
}