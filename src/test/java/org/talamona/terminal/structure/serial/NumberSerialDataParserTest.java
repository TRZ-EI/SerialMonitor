package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.Number;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/11/17
 * Time: 16.15
 */
public class NumberSerialDataParserTest {
    private NumberSerialDataParser sut;
    
    @DataProvider
    private Object[][] dataForTest(){
        return new Object[][]{
                {"^nP20001C00000001", String.valueOf(434).hashCode(), "1", 2, 0, 0, 28},    // ID = 434
                {"^nQ410611FFFFFFD8", String.valueOf(293).hashCode(), "-40", 4, 1, 6, 17},  // ID = 293
                {"^nR41062500000005", String.valueOf(983).hashCode(), "5", 4, 1, 6, 37},    // ID = 983
                {"^nS410B11FFFFFFD8", String.valueOf(423).hashCode(), "-40", 4, 1, 11, 17},  // ID = 423
                {"^nS410B250000000A", String.valueOf(1213).hashCode(), "10", 4, 1, 11, 37}   // ID = 1213
        };
    }
    
    
    @BeforeMethod
    public void setUp() throws Exception {
        ConfigurationHolder.getInstance();
        this.sut = NumberSerialDataParser.getNewInstance();
    }

    @Test
    public void testGetNewInstance(){
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataForTest")
    public void testReadByteArray(String command, int expectedId, String expectedValue, int intPart, int decPart, int row, int col) throws UnsupportedEncodingException {
        Number actualValue = this.sut.readByteArray(command.getBytes());
        assertNotNull(actualValue);
        assertEquals(actualValue.getId(), expectedId);
        assertEquals(actualValue.getValue(), expectedValue);
        assertEquals(actualValue.getIntegerLenght(), intPart);
        assertEquals(actualValue.getDecimalLenght(), decPart);
        assertEquals(actualValue.getyPos(), row);
        assertEquals(actualValue.getxPos(), col);



    }
    
    
    

}