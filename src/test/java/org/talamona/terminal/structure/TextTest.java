package org.talamona.terminal.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.talamona.terminal.utils.ConfigurationHolder;
import org.talamona.terminal.utils.FontAndColorSelector;

import static org.testng.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/06/17
 * Time: 9.50
 */
public class TextTest {
    private Text sut;
    private Font testFont;
    private Color testColor = Color.BLACK;
    private final String TEST_STRING = "TEST_STRING";

    @BeforeMethod
    public void init(){
        ConfigurationHolder.getInstance();
        testFont = FontAndColorSelector.getNewInstance().getSmallFont();
        this.sut = Text.getNewInstanceByFontAndColor(testFont, testColor);
        this.sut.setValue(TEST_STRING);
    }
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
    @DataProvider
    private Object[][] createHashedIds(){
        // f(a, b) = s(a+b) + a, where s(n) = n*(n+1)/2

        return new Object[][]{
                {13, 24, this.hashCode("716")},
                {24, 13, this.hashCode("727")},
                {67, 2, this.hashCode("2482")},
                {2, 67, this.hashCode("2417")},
                {1,2, this.hashCode("7")},
                {2,1,this.hashCode("8")},
                {0,1,this.hashCode("1")},
                {1,0,this.hashCode("2")}

        };
    }

    @DataProvider
    private Object[][] createTextObjectsToVerifyPixelPos(){
        int width = FontAndColorSelector.getNewInstance().getWidthForSmallFont("W");
        int height = FontAndColorSelector.getNewInstance().getHeightForSmallFont("W");
        return new Object[][]{
                {Text.getNewInstanceByFontAndColor(testFont, testColor).setxPos(10).setyPos(10), width * 10, height * 10 + height},
                {Text.getNewInstanceByFontAndColor(testFont, testColor).setxPos(1).setyPos(20), width * 1, height * 20 + height}
        };
    }

    @Test(dataProvider = "createTextObjectsToVerifyPixelPos")
    public void testToVerifyXAndYPixelPos(Cell expectedValue, int pixelXPos, int pixelYPos){
        assertEquals(expectedValue.getPixelScreenXPos(), pixelXPos);
        assertEquals(expectedValue.getPixelScreenYPos(), pixelYPos);
    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test
    public void testGetNewInstanceByFontAndColor() throws Exception {
        assertEquals(testFont, this.sut.getFont());
        assertEquals(testColor, this.sut.getColor());
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals(this.sut.getValue(), TEST_STRING);

    }

    @Test
    public void testUpdateData() throws Exception {
        String aNewTest = "A new Test";
        Text textForUpdate = Text.getNewInstanceByFontAndColor(testFont, testColor);
        textForUpdate.setValue(aNewTest);
        this.sut.setValue(aNewTest);
        this.sut.updateData(textForUpdate);
        assertEquals(textForUpdate, sut);
    }
    @Test(dataProvider = "createHashedIds")
    public void verifyUniqueIdHashed(int x, int y, int expectedValue){
        this.sut.setxPos(x);
        this.sut.setyPos(y);
        assertEquals(this.sut.getId(), expectedValue);

    }



    @Test(dataProvider = "createIds")
    public void verifyUniqueIdCalculation(int x, int y, int expectedValue){
        this.sut.setxPos(x);
        this.sut.setyPos(y);
        assertEquals(this.sut.calculateIdUsingIniectiveFunctionForXandY(x,y), expectedValue);

    }
    @Test
    public void testEquals() throws Exception {
        int x = 10, y = 10;
        Text textForUpdate = Text.getNewInstance();
        textForUpdate.setxPos(x).setyPos(y).setValue(TEST_STRING);
        this.sut.setxPos(x).setyPos(y);
        assertTrue(this.sut.equals(textForUpdate));
    }
    @Test
    public void testNotEquals() throws Exception {
        String aNewTest = "A new Test";
        Text textForUpdate = Text.getNewInstanceByFontAndColor(testFont, testColor);
        textForUpdate.setValue(aNewTest);
        this.sut.setValue(aNewTest).setyPos(10).setyPos(10);
        assertFalse(this.sut.equals(textForUpdate));
    }
    @Test
    public void testGetUpperLeftCoordinatesOfContainerRectangle(){
        TextMetricCalculator calculator = TextMetricCalculator.getInstance();

        int x = 10, y = 10, pixelScreenYPos = 250;
        int expectedValueForX = x * calculator.calculateWidth("W", this.testFont);
        int expectedValueForY = pixelScreenYPos - calculator.calculateHeight("W", this.testFont);

        this.sut.setPixelScreenYPos(250);


        int actualValueForX = this.sut.setxPos(x).setyPos(y).getPixelScreenXPos();
        int actualValueForY = this.sut.getPixelScreenYPosUpper();
        assertEquals(actualValueForX, expectedValueForX);
        assertEquals(actualValueForY, expectedValueForY);
    }
    @Test
    public void testGetWidthAndHeightOfContainerRectangle(){
        TextMetricCalculator calculator = TextMetricCalculator.getInstance();
        int expectedWidth = calculator.calculateWidth(this.sut.getValue(), this.testFont);
        int expectedHeight = calculator.calculateHeight(this.sut.getValue(), this.testFont);
        int actualWidth = this.sut.getWidth();
        int actualHeight = this.sut.getHeight();
        assertEquals(actualWidth, expectedWidth);
        assertEquals(actualHeight, expectedHeight);
    }

    private int hashCode(String message) {
        int retValue = 0;
        int len = message.length();
        if (len > 0) {
            int off = 0;
            char val[] = message.toCharArray();
            for (int i = 0; i < len; i++) {
                retValue = 31*retValue + val[off++];
            }
        }
        return retValue;
    }




}