package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.*;
import org.talamona.terminal.utils.ConfigurationHolder;
import org.talamona.terminal.utils.FontAndColorSelector;

import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 04/05/17
 * Time: 15.57
 */
public class SerialDataFacadeTest {
    private SerialDataFacade sut;
    private FontAndColorSelector fontAndColorSelector;

    @DataProvider
    private Object[][] testDataForConfigurationVariable(){
        return new Object[][]{
                {"^V07A310509", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setIntegerLenght(3).setDecimalLenght(1).setId(7).setxPos(9).setyPos(5)},
                {"^V03R220101", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('R'), fontAndColorSelector.selectColor('R'))
                        .setIntegerLenght(2).setDecimalLenght(2).setId(3).setxPos(1).setyPos(1)},
                {"^V07A310A0B", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setIntegerLenght(3).setDecimalLenght(1).setId(7).setxPos(11).setyPos(10)},
                {"^V0CA310A0B", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setIntegerLenght(3).setDecimalLenght(1).setId(12).setxPos(11).setyPos(10)}
        };
    }
    @DataProvider
    private Object[][] testDataForVariable(){
        return new Object[][]{
                {"^v0C0000000A", Variable.getInstance().setAConfiguration(false).setId(12).setValue("10")},
                {"^v0C0000000B", Variable.getInstance().setAConfiguration(false).setId(12).setValue("11")},
                {"^v0C0000000C", Variable.getInstance().setAConfiguration(false).setId(12).setValue("12")},
                {"^v0C0000000D", Variable.getInstance().setAConfiguration(false).setId(12).setValue("13")},
                {"^v0C0000000E", Variable.getInstance().setAConfiguration(false).setId(12).setValue("14")},
                {"^v0C0000000F", Variable.getInstance().setAConfiguration(false).setId(12).setValue("15")}
        };
    }
    @DataProvider
    private Object[][] testDataForText(){
        return new Object[][]{
                {"^tA0509TestoProva1xxxx\n", Text.getNewInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setxPos(9).setyPos(5).setValue("TestoProva1")},
                {"^tA0A0BTestoProva2xxxx\n", Text.getNewInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setxPos(11).setyPos(10).setValue("TestoProva2")},
                {"^tR0F0CQuesto essere un testo di prova lungoxxxx\n", Text.getNewInstanceByFontAndColor(fontAndColorSelector.selectFont('R'), fontAndColorSelector.selectColor('R'))
                        .setxPos(12).setyPos(15).setValue("Questo essere un testo di prova lungo")},
        };
    }
    @DataProvider
    private Object[][] testDataForNumber(){
        return new Object[][]{
                {"^nP20001C00000001", Variable.getInstance().setIntegerLenght(2).
                        setDecimalLenght(0).setyPos(0).setxPos(28).setId(String.valueOf(434).hashCode()).setValue("1")},    // ID = 434
                {"^nQ410611FFFFFFD8", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(6).setxPos(17).setId(String.valueOf(293).hashCode()).setValue("-40")},  // ID = 293
                {"^nR41062500000005", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(6).setxPos(37).setId(String.valueOf(983).hashCode()).setValue("5")},  // ID = 983
                {"^nS410B11FFFFFFD8", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(11).setxPos(17).setId(String.valueOf(423).hashCode()).setValue("-40")},  // ID = 423
                {"^nS410B250000000A", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(11).setxPos(37).setId(String.valueOf(1213).hashCode()).setValue("10")},  // ID = 1213
        };
    }
    @DataProvider
    private Object[][] testDataForBar(){
        return new Object[][]{
            {"^B05FFFFFFD80000000507", Bar.getInstance().setMinValue(-40).setMaxValue(5).setId(5).setyPos(7).setxPos(10)},
            {"^B06FFFFFFD80000000A0C", Bar.getInstance().setMinValue(-40).setMaxValue(10).setId(6).setyPos(12).setxPos(10)}
        };
    }
    @DataProvider
    private Object[][] testDataForClear(){
        return new Object[][]{
                {"^C", new Clear()}
        };
    }
    @DataProvider
    private Object[][] testDataForRowCleaner(){
        return new Object[][]{
            {"^K097C2F", RowCleaner.getInstanceByRowId(9)},
            {"^K0A0ACE", RowCleaner.getInstanceByRowId(10)},
            {"^K0B755E", RowCleaner.getInstanceByRowId(11)},
            {"^K0C755E", RowCleaner.getInstanceByRowId(12)}
        };
    }

    @BeforeTest
    private void setup(){
        String configurationFile = this.getClass().getClassLoader().getResource("application.properties").getFile();
        ConfigurationHolder.getInstance();
        this.sut = SerialDataFacade.createNewInstance();
        this.fontAndColorSelector = FontAndColorSelector.getNewInstance();
    }
    @Test
    public void testCreateNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test(dataProvider = "testDataForConfigurationVariable")
    public void testOnSerialDataParserForConfiguration(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        assertEquals(actualValue, expectedValue);
    }
    @Test(dataProvider = "testDataForVariable")
    public void testOnSerialDataParserForVariables(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        String actualVariableValue = actualValue.getValue();
        String expectedVariableValue = expectedValue.getValue();
        assertEquals(actualValue, expectedValue);
        assertEquals(actualVariableValue, expectedVariableValue);
    }
    @Test(dataProvider = "testDataForText")
    public void testOnSerialDataParserForText(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        String actualVariableValue = actualValue.getValue();
        String expectedVariableValue = expectedValue.getValue();
        assertEquals(actualValue, expectedValue);
        assertEquals(actualVariableValue, expectedVariableValue);
    }
    @Test(dataProvider = "testDataForNumber")
    public void testOnSerialDataParserForNumber(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        String actualVariableValue = actualValue.getValue();
        String expectedVariableValue = expectedValue.getValue();
        assertEquals(actualValue, expectedValue);
        assertEquals(actualVariableValue, expectedVariableValue);
    }
    @Test(dataProvider = "testDataForClear")
    public void testOnSerialDataParserForClear(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        assertEquals(this.sut.onSerialDataParser(dataToParse.getBytes()), expectedValue);
    }
    @Test(dataProvider = "testDataForRowCleaner")
    public void testOnSerialDataParserForRowCleaner(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        assertEquals(this.sut.onSerialDataParser(dataToParse.getBytes()), expectedValue);
    }
    @Test(dataProvider = "testDataForBar")
    public void testOnSerialDataParserForBar(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        assertEquals(this.sut.onSerialDataParser(dataToParse.getBytes()), expectedValue);
    }
}