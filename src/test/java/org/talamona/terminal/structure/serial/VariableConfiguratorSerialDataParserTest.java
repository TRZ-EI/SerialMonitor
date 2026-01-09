package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.Variable;
import org.talamona.terminal.utils.ConfigurationHolder;
import org.talamona.terminal.utils.FontAndColorSelector;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 11.41
 */
public class VariableConfiguratorSerialDataParserTest {

    private VariableConfiguratorSerialDataParser sut;
    private FontAndColorSelector fontAndColorSelector;

    /*
    NERO_PICCOLO=1
    ROSSO_PICCOLO=2
    VERDE_PICCOLO=3
    BLU_PICCOLO=4
    ROSSO_PICCOLO_GRASSETTO=6
    NERO_GRANDE=D
    ROSSO_GRANDE=E
    VERDE_GRANDE=F
    BLU_GRANDE=G
    
    /*
    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'P';
    private final char BLU_GRANDE = 'C';

     */

    
    

    @DataProvider
    private Object[][] prepareDataForTest(){
        return new Object[][]{
                {"^V05E410416819C", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('E'), fontAndColorSelector.selectColor('E'))
                        .setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(true).setyPos(4).setxPos(22).setId(5)},
                {"^V06E410916F433", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('E'), fontAndColorSelector.selectColor('E'))
                        .setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(true).setyPos(9).setxPos(22).setId(6)},
                {"^V0145100230354", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('4'), fontAndColorSelector.selectColor('4'))
                        .setIntegerLenght(5).setDecimalLenght(1).setAConfiguration(true).setyPos(0).setxPos(35).setId(1)},
                {"^V0A453020F3D2E", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('4'), fontAndColorSelector.selectColor('4'))
                        .setIntegerLenght(5).setDecimalLenght(3).setAConfiguration(true).setyPos(2).setxPos(15).setId(10)},
                {"^V05E410416819C", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('E'), fontAndColorSelector.selectColor('E'))
                        .setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(true).setyPos(4).setxPos(22).setId(5)},
                {"^V0CE410916F433", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('E'), fontAndColorSelector.selectColor('E'))
                        .setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(true).setyPos(9).setxPos(22).setId(12)}


        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        ConfigurationHolder.getInstance();
        this.sut = VariableConfiguratorSerialDataParser.getNewInstance();
        this.fontAndColorSelector = FontAndColorSelector.getNewInstance();
    }
    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(VariableConfiguratorSerialDataParser.getNewInstance());
    }
    @Test(dataProvider = "prepareDataForTest")
    public void testReadByteArray(String message, Variable expectedValue) throws Exception {
        Variable actualValue = this.sut.readByteArray(message.getBytes());
        assertEquals(actualValue, expectedValue);
        assertEquals(actualValue.getIntegerLenght(), expectedValue.getIntegerLenght());
        assertEquals(actualValue.getDecimalLenght(), expectedValue.getDecimalLenght());
        assertEquals(actualValue.getColor(), expectedValue.getColor());
        assertEquals(actualValue.getFont(), expectedValue.getFont());
        assertEquals(actualValue.isAConfiguration(), expectedValue.isAConfiguration());



    }


}