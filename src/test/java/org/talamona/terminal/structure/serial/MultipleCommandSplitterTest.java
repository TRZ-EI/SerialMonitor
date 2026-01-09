package org.talamona.terminal.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 17/08/17
 * Time: 14.32
 */
public class MultipleCommandSplitterTest {
    private MultipleCommandSplitter sut;
    // ^V       --> sync
    // 2 digits --> variable ID
    // 1 digit  --> font and color
    // 1 digit  --> int part
    // 1 digit  --> decimal part
    // 2 digit  --> row (y)
    // 2 digit  --> col (x)
    // 4 digit  --> CRC (hex) NB: only at the end of multiple command
    // *************************************************
    // ^v       --> sync
    // 2 digits --> variable ID
    // 8 digits --> value
    // 4 digit  --> CRC (hex) NB: only at the end of multiple command
    // *************************************************

    @DataProvider
    private Object[][] dataForTest(){
        return new Object[][]{
                {"^V01441002302463021203463022A03463022A8953",
                        new String[] {"^V014410023", "^V024630212", "^V03463022A", "^V03463022A"}},
                {"^v0100000031020000000003000000000300000000A188",
                        new String[] {"^v0100000031", "^v0200000000", "^v0300000000","^v0300000000"}},
                {"^v01000000000200000000030000000005000000005905",
                        new String[] {"^v0100000000", "^v0200000000", "^v0300000000","^v0500000000"}},
                {"^t10014Sfiato               secC08C",
                        new String[] {"^t10014Sfiato               secC08C"}}
        };
    }
    @DataProvider
    private Object[][] dataNotPermitted(){
        return new Object[][]{
                {"^t10014Sfiato               secC08C"},
                {"^t10014Sfiato               secC08C"},
                {"^n160002100000001F5A0"},
                {"^n160002100000001F5A0"}
        };
    }
    @DataProvider
    private Object[][] dataPermitted(){
        return new Object[][]{
                {"^V01441002302463021203463022A03463022A8953"},
                {"^v0100000031020000000003000000000300000000A188"},
                {"^v01000000000200000000030000000005000000005905"}
        };
    }
    @BeforeMethod
    private void setup(){
        this.sut = MultipleCommandSplitter.getNewInstance();
    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataForTest")
    public void testSplitMultipleCommand(String multipleCommand, String[] expectedValues){
        List<String> actualValues = this.sut.splitMultipleCommand(multipleCommand);
        assertEquals(actualValues.toArray(), expectedValues);
    }


    @Test(dataProvider = "dataNotPermitted")
    public void testIsPermittedCommandWithWrongData(String command) throws Exception {
        assertFalse(this.sut.isPermittedCommand(command));
    }
    @Test(dataProvider = "dataPermitted")
    public void testIsPermittedCommandWithRightData(String command) throws Exception {
        assertTrue(this.sut.isPermittedCommand(command));
    }









}