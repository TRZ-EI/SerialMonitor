package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.utils.ConfigurationHolder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/05/17
 * Time: 16.49
 */
public class TextSerialDataParserTest {

    private TextSerialDataParser sut;

    @DataProvider
    private Object[][] createDataForTest(){
        return new Object[][]{
                {"^tQ0100This is a text fo test1234", "This is a text fo test"},
                {"^t10000   ATTESA START BLOCCO  0966", "   ATTESA START BLOCCO  "},
                {"^t10000    Chiusura     pinza A55C","    Chiusura     pinza "},
                {"^t10000      Chiusura       tappi 1571","      Chiusura       tappi "},
                {"^t40000   PROVA N 1           11C7","   PROVA N 1           "},
                {"^t10200   Press. P1=        Bar   Press. P2=        BarE559","   Press. P1=        Bar   Press. P2=        Bar"},
                {"^tE0400   DP1=           mBar6163","   DP1=           mBar"},
                {"^tE0900   DP2=           mBar673D","   DP2=           mBar"},
                {"^t10606S2 =         mBar    S1 =         mBarF047","S2 =         mBar    S1 =         mBar"},
                {"^t10B06S4 =         mBar    S3 =         mBar6B55","S4 =         mBar    S3 =         mBar"},

                {"^tQ0100This is a text fo test1234\n", "This is a text fo test"},
                {"^t10000   ATTESA START BLOCCO  0966\n", "   ATTESA START BLOCCO  "},
                {"^t10000    Chiusura     pinza A55C\n","    Chiusura     pinza "},
                {"^t10000      Chiusura       tappi 1571\n","      Chiusura       tappi "},
                {"^t40000   PROVA N 1           11C7\n","   PROVA N 1           "},
                {"^t10200   Press. P1=        Bar   Press. P2=        BarE559\n","   Press. P1=        Bar   Press. P2=        Bar"},
                {"^tE0400   DP1=           mBar6163\n","   DP1=           mBar"},
                {"^tE0900   DP2=           mBar673D\n","   DP2=           mBar"},
                {"^t10606S2 =         mBar    S1 =         mBarF047\n","S2 =         mBar    S1 =         mBar"},
                {"^t10B06S4 =         mBar    S3 =         mBar6B55\n","S4 =         mBar    S3 =         mBar"}

        };
    }


    @BeforeTest
    private void setup(){
        ConfigurationHolder.getInstance();
        this.sut = TextSerialDataParser.getNewInstance();
    }
    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test(dataProvider = "createDataForTest")
    public void testReadByteArray(String command, String expectedValue) throws Exception {
        Cell actualValue = this.sut.readByteArray(command.getBytes());
        assertEquals(actualValue.getValue(), expectedValue);
    }
}