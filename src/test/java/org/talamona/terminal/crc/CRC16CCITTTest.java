package org.talamona.terminal.crc;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 21/03/17
 * Time: 13.48
 */
public class CRC16CCITTTest {

    private CRC16CCITT sut;

    @DataProvider
    private Object[][] createDataForTest(){
        return new Object[][]{
                {"123456789",0x29b1},
                {"message" ,0x9CDF},
                {"This is a long message" ,0x3E29},
                {"^v0700000003", 0x0af1},
                {"Per accedere all'area privata puoi semplicemente crearti un'utenza nella sezione \"register\"." ,0xCED5},
                {"^v070000000f", 0x00a1},
                {"^v0800000019", 0x00de}
        };
    }


    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = CRC16CCITT.getNewInstance();
    }

    @Test(dataProvider = "createDataForTest")
    public void testCalculateCRCForStringMessage(String message, long expectedValue) throws Exception {
        long actual = this.sut.calculateCRCForStringMessage(message);
        assertEquals(expectedValue, actual);
    }
    @Test(dataProvider = "createDataForTest")
    public void testCalculateCRCforByteArrayMessage(String message, long expectedValue) throws Exception {
        long actual = this.sut.calculateCRCforByteArrayMessage(message.getBytes());
       assertEquals(expectedValue, actual);
    }

}