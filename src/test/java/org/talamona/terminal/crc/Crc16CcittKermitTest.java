package org.talamona.terminal.crc;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 29/07/17
 * Time: 12.46
 */
public class Crc16CcittKermitTest {
    private Crc16CcittKermit sut;

    @DataProvider
    private Object[][] createDataForTest(){
        return new Object[][]{
                {"123456789",0x2189},
                {"message" ,0x2D2C},
                {"This is a long message" ,0xBF6B},
                {"Per accedere all'area privata puoi semplicemente crearti un'utenza nella sezione \"register\"." ,0x92F2}
        };
    }
    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = Crc16CcittKermit.getNewInstance();
    }


    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "createDataForTest")
    public void testCalculateCRCForStringMessage(String message, int crcExpected) throws Exception {
        assertEquals(this.sut.calculateCRCForStringMessage(message), crcExpected);
    }

    @Test(dataProvider = "createDataForTest")
    public void testCalculateCRCforByteArrayMessage(String message, int crcExpected) throws Exception {
        assertEquals(this.sut.calculateCRCforByteArrayMessage(message.getBytes()), crcExpected);
    }

}