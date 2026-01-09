package org.talamona.terminal.utils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 14/04/17
 * Time: 12.39
 */
public class DataTypesConverterTest {
    private DataTypesConverter sut;

    @DataProvider
    private Object[][] dataToTestBytesToInt(){
        return new Object[][]{
                {new byte[]{51, 52, 53, 54, 55, 56}, 3430008}, // HEX values
                {new byte[]{57}, 9}, // ASCII VALUES (09)
                {new byte[]{56}, 8}, // ASCII VALUES (08)
                {new byte[]{55}, 7}, // ASCII VALUES (07)
                {new byte[]{54}, 6}, // ASCII VALUES (06)
                {new byte[]{53}, 5}, // ASCII VALUES (05)
                {new byte[]{52}, 4}, // ASCII VALUES (04)
                {new byte[]{51}, 3}, // ASCII VALUES (03)
                {new byte[]{50}, 2}, // ASCII VALUES (02)
                {new byte[]{49}, 1}, // ASCII VALUES (01)
                {new byte[]{48,49}, 1}, // ASCII VALUES (01)
                {new byte[]{49, 0x39}, 25}, // MIXED ASCII/HEX VALUES (19)
                {new byte[]{'0','A'}, 10},
                {new byte[]{'0','B'}, 11},
                {new byte[]{'0','C'}, 12},
                {new byte[]{'0','D'}, 13},
                {new byte[]{'0','E'}, 14},
                {new byte[]{'0','F'}, 15},
                {new byte[]{'1','0'}, 16},
                {new byte[]{'1','1'}, 17},
                {new byte[]{'1','2'}, 18},
                {new byte[]{'1','3'}, 19},
                {new byte[]{'1','4'}, 20},
                {new byte[]{'F','F','F','F','F','F','D','8'}, -40}, // signed HEX values
                {new byte[]{'0','0','0','0','0','0','0','5'}, 5},
                {"00000001".getBytes(), 1},
                {"FFFF".getBytes(), 65535}
        };
    }
    @DataProvider
    private Object[][] dataToTestBytesToLong(){
        return new Object[][]{
                {new byte[]{0x33, 0x34, 0x35, 0x36, 0x37, 0x38}, 3430008L}, // HEX values
                {new byte[]{57}, 9L}, // ASCII VALUES (09)
                {new byte[]{56}, 8L}, // ASCII VALUES (08)
                {new byte[]{55}, 7L}, // ASCII VALUES (07)
                {new byte[]{54}, 6L}, // ASCII VALUES (06)
                {new byte[]{53}, 5L}, // ASCII VALUES (05)
                {new byte[]{52}, 4L}, // ASCII VALUES (04)
                {new byte[]{51}, 3L}, // ASCII VALUES (03)
                {new byte[]{50}, 2L}, // ASCII VALUES (02)
                {new byte[]{49}, 1L}, // ASCII VALUES (01)
                {new byte[]{49,0x37,0x32,57}, 5929L}, // MIXED ASCII/HEX VALUES (1729)
                {new byte[]{49, 0x39}, 25L}, // MIXED ASCII/HEX VALUES (19)
                {new byte[]{'F','F','F','F','F','F','D','8'}, -40}, // signed HEX values
                {new byte[]{'0','0','0','0','0','0','0','5'}, 5},
                {"FFFF".getBytes(), 65535}

        };
    }
    @DataProvider
    private Object[][] dataToTestLongToAsciiChars(){
        return new Object[][]{
                {new byte[]{0x35, 0x34, 0x36, 0x34, 0x65}, 345678L}, // HEX values
                {new byte[]{57}, 9L}, // ASCII VALUES (09)
                {new byte[]{56}, 8L}, // ASCII VALUES (08)
                {new byte[]{55}, 7L}, // ASCII VALUES (07)
                {new byte[]{54}, 6L}, // ASCII VALUES (06)
                {new byte[]{53}, 5L}, // ASCII VALUES (05)
                {new byte[]{52}, 4L}, // ASCII VALUES (04)
                {new byte[]{51}, 3L}, // ASCII VALUES (03)
                {new byte[]{50}, 2L}, // ASCII VALUES (02)
                {new byte[]{49}, 1L}, // ASCII VALUES (01)
                {new byte[]{49,0x37,0x32,57}, 5929L}, // MIXED ASCII/HEX VALUES (1729)
                {new byte[]{49, 0x39}, 25L}, // MIXED ASCII/HEX VALUES (19)
                {new byte[]{0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x65,0x64}, -19L},
                {new byte[]{0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,0x66,57,51,0x66}, -1729L}, // MIXED ASCII/HEX VALUES (-1729)


        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = DataTypesConverter.getNewInstance();
    }
    @Test
    public void testNewInstance(){
        assertNotNull(DataTypesConverter.getNewInstance());
    }


    @Test
    public void testLongToBytes() throws Exception {
        long testValue = 1000l;
        byte[] expectedValue = ByteBuffer.allocate(8).putLong(testValue).array();
        byte[] actualValue = this.sut.longToBytes(testValue);
        assertEquals(actualValue, expectedValue);
    }
    @Test(dataProvider = "dataToTestLongToAsciiChars")
    public void testLongToAsciiChars(byte[] expectedValue, long testValue) throws Exception {
        byte[] actualValue = this.sut.longToAsciiChars(testValue);
        assertEquals(actualValue, expectedValue);
    }



    @Test(dataProvider = "dataToTestBytesToLong")
    public void testBytesToLong(byte[] testValue, long expectedValue) throws Exception {
        assertEquals(this.sut.bytesToLong(testValue), expectedValue);
    }

    @Test
    public void testIntToBytes() throws Exception {
        int testValue = 123456;
        byte[] expectedValue = ByteBuffer.allocate(4).putInt(testValue).array();
        assertEquals(this.sut.intToBytes(testValue), expectedValue);
    }

    @Test(dataProvider = "dataToTestBytesToInt")
    public void testBytesToInt(byte[] testValue, int expectedValue) throws Exception {
        assertEquals(this.sut.bytesToInt(testValue), expectedValue);
    }
    @Test
    public void testByteToChar() throws Exception {
        char expectedValue = 'C';
        byte testValue = 0x43;
        assertEquals(this.sut.byteToChar(testValue), expectedValue);
    }
    @Test
    public void testBytesToString() throws Exception {
        String expectedValue = "12";
        byte[] testValue = {0x31, 0x32};
        assertEquals(this.sut.bytesToString(testValue), expectedValue);
    }

    @Test
    public void testNotAsciiBytesToInt() throws Exception {
        int expectedValue = 2345;
        byte[] testValue = this.sut.intToBytes(expectedValue);
        assertEquals(this.sut.notAsciiBytesToInt(testValue), expectedValue);
    }



}