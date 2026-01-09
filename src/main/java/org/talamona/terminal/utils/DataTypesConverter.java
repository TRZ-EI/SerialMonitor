package org.talamona.terminal.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class DataTypesConverter {
    private DataTypesConverter() {
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) throws UnsupportedEncodingException {
        return (long)this.bytesToInt(bytes);
    }

    public byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }
    public byte[] charToByte(char x) {
        ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES);
        buffer.putChar(x);
        return buffer.array();
    }
    public char byteToChar(byte b) {
        return (char)b;
    }

    public String bytesToString(byte[] bytes) throws UnsupportedEncodingException {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < bytes.length; i ++){
            buffer.append(this.byteToChar(bytes[i]));
        }
        return buffer.toString();
    }

    public int bytesToInt(byte[] bytes) throws UnsupportedEncodingException {
        String toTransform = this.bytesToString(bytes);
        return Integer.parseUnsignedInt(toTransform, 16);
    }
    public int notAsciiBytesToInt(byte[] bytes) throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    public static DataTypesConverter getNewInstance() {
        return new DataTypesConverter();
    }

    public byte[] longToAsciiChars(long testValue) {
        String value = Long.toHexString(testValue);
        return value.getBytes();
    }
}