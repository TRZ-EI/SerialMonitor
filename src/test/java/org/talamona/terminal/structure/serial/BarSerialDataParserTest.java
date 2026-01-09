package org.talamona.terminal.structure.serial;

import org.talamona.terminal.structure.serial.BarSerialDataParser;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.Bar;
import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.utils.ConfigurationHolder;

import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 28/05/17
 * Time: 16.25
 */
public class BarSerialDataParserTest {

    private BarSerialDataParser sut;

    @BeforeTest
    private void prepareFontsForTest(){
        try {
            this.loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @BeforeTest
    private void setup(){
        this.sut = BarSerialDataParser.getNewInstance();
    }

    @DataProvider
    private Object[][] testDataForBar(){
        return new Object[][]{
                {"^B05FFFFFFD800000005071234", Bar.getInstance().setMinValue(-40).setMaxValue(5).setId(5).setyPos(7).setxPos(10)},
                {"^B06FFFFFFD80000000A0C1234", Bar.getInstance().setMinValue(-40).setMaxValue(10).setId(6).setyPos(12).setxPos(10)}
        };
    }



    private void loadProperties(){
        ConfigurationHolder.getInstance();
    }




    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "testDataForBar")
    public void testReadByteArray(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.readByteArray(dataToParse.getBytes());
        assertEquals(actualValue, expectedValue);
    }

}