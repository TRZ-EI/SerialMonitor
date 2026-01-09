package org.talamona.terminal.utils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/11/17
 * Time: 15.10
 */
public class NumericFormatterByIntAndDecimalsTest {

    private NumericFormatterByIntAndDecimals sut;
    @DataProvider
    private Object[][] prepareDataForTest(){
        return new Object[][]{
                {123456, 2, 4, "12.3456"},
                {-123456,4, 2, "-1234.56"},
                {-123456,2, 4, "-12.3456"},
                {1, 1, 4, ".0001"},
                {-1, 1, 4, "-.0001"},
                {123456, 7, 2, "1234.56"},
                {-123456,7, 1, "-12345.6"},
                {5,0, 1, ".5"},
                {100,3, 0, "100"},
                {100,3, 1, "10.0"},
                {1,3, 1, ".1"},
                {342, 3,1,"34.2"}
        };
    }
    @BeforeMethod
    public void setUp() throws Exception {
    }

    @Test
    public void testGetInstanceByIntDecimalsAndValue() throws Exception {
        assertNotNull(NumericFormatterByIntAndDecimals.getInstanceByIntDecimalsAndValue(4, 2, 123456));
    }

    @Test(dataProvider = "prepareDataForTest")
    public void testInvoke(long value, int intPart, int decimalPart, String expectedValue) throws Exception {
        assertEquals(NumericFormatterByIntAndDecimals.getInstanceByIntDecimalsAndValue(intPart, decimalPart, value).invoke(), expectedValue);
    }

}