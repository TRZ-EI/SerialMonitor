package org.talamona.terminal.utils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/11/17
 * Time: 14.49
 */
public class IdGeneratorByPositionTest {
    @DataProvider
    private Object[][] createIds(){
        // f(a, b) = s(a+b) + a, where s(n) = n*(n+1)/2
        return new Object[][]{
                {13, 24, 716},
                {24, 13, 727},
                {67, 2, 2482},
                {2, 67, 2417},
                {1,2, 7},
                {2,1,8},
                {0,1,1},
                {1,0,2}

        };
    }


    @BeforeMethod
    public void setUp() throws Exception {
    }

    @Test(dataProvider = "createIds")
    public void testInvoke(int x, int y, int expectedValue) throws Exception {
        assertEquals(IdGeneratorByPosition.getNewInstanceByXAndY(x, y).invoke(), expectedValue);
    }

}