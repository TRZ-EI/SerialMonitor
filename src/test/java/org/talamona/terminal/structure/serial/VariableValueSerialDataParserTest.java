package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.structure.Variable;

import static org.testng.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 25/04/17
 * Time: 15.39
 */
public class VariableValueSerialDataParserTest {

    private VariableValueSerialDataParser sut;

    @BeforeMethod
    public void setUp() throws Exception {
        //ConfigurationHolder.createSingleInstanceByConfigUri("application.properties");
        this.sut = VariableValueSerialDataParser.getInstance();
    }




    @DataProvider
    private Object[][] dataForTest(){
        return new Object[][]{
                {"^v02FFFFFF79", "-13.5",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(2).setValue("-135")},
                {"^v05FFFFFFD8","-4.0",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("-40")},
                {"^v06FFFFFFF4","-1.2",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("-12")},
                {"^v0500000003",".3",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("3")},
                {"^v0600000004",".4",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("4")},
                {"^v0500000005",".5",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("5")},
                {"^v060000000A","1.0",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("10")},
                {"^v05FFFFFFF9","-.7",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("-7")},
                {"^v06FFFFFFFF","-.1",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("-1")}
        };
    }
    @DataProvider
    private Object[][] wrongDataForTest(){
        return new Object[][]{
                {"^v0A0001"},
                {"^v020001"},
                {"^v0A000002"},
                {"^v020000002"},
                {"^v0A00003"},
                {"^v020003"},
                {"^v0A0000004"}
        };
    }

    @Test
    public void testNewInstance(){
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataForTest")
    public void testReadByteArray(String command, String expectedFormattedValue, Variable expectedValue) throws Exception {
        Variable actualValue = (Variable) this.sut.readByteArray(command.getBytes());
        actualValue.setIntegerLenght(4).setDecimalLenght(1);
        assertEquals(actualValue, expectedValue);
        assertEquals(actualValue.getValue(), expectedValue.getValue());
        assertEquals(actualValue.prepareFormattedValue(), expectedFormattedValue);
    }
    @Test(dataProvider = "wrongDataForTest")
    public void testReadByteArrayWithWrongDataLenght(String command) throws Exception {
        assertNull(this.sut.readByteArray(command.getBytes()));
    }

}