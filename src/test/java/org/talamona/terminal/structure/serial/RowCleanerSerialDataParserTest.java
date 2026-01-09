package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.RowCleaner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 13/11/17
 * Time: 20.32
 */
public class RowCleanerSerialDataParserTest {

    private RowCleanerSerialDataParser sut;

    @DataProvider(name = "dataToParse")
    private Object[][] createDataForTest(){
        return new Object[][]{
                {"^K01C22C", 1},
                {"^K0AB1AB", 10},
                {"^K1AA873", 26}
        };
    }
    @BeforeMethod
    public void setup(){
        this.sut = RowCleanerSerialDataParser.getNewInstance();
    }

    @Test
    public void testInstance(){
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataToParse")
    public void testReadByteArray(String message, int expectedRowIndex) throws Exception {
        Cell actualValue = this.sut.readByteArray(message.getBytes());
        assertTrue(actualValue instanceof RowCleaner);
        assertEquals(actualValue.getId(), expectedRowIndex);
    }

}