package org.talamona.terminal.structure;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 14.55
 */
public class TextMetricCalculatorTest {

    private TextMetricCalculator sut;
    private String referenceText;


    @DataProvider
    private Object[][] prepareDataForTest(){
        return new Object[][]{
                {Font.font("Arial", FontWeight.NORMAL, 16)},
                {Font.font("Arial", FontWeight.NORMAL, 20)},
                {Font.font("Arial", FontWeight.NORMAL, 36)},
                {Font.font("Arial", FontWeight.NORMAL, 40)},
                {Font.font("Serif", FontWeight.NORMAL, 16)},
                {Font.font("Serif", FontWeight.NORMAL, 20)},
                {Font.font("Serif", FontWeight.NORMAL, 36)},
                {Font.font("Serif", FontWeight.NORMAL, 40)},
        };
    }

    @BeforeMethod
    private void prepareSut(){
        this.sut = TextMetricCalculator.getInstance();
        this.referenceText = "REFER_TEXT";
    }
    @Test(dataProvider = "prepareDataForTest")
    public void testCalculateWidth(Object font) throws Exception {
        Font f = (Font)font;
        int expectedValue = this.calculateTextWidthByFont(referenceText, f);
        assertEquals(expectedValue, this.sut.calculateWidth(referenceText, f));
    }
    @Test(dataProvider = "prepareDataForTest")
    public void testCalculateHeight(Object font) throws Exception {
        Font f = (Font)font;
        int expectedValue = this.calculateTextHeightByFont(referenceText, f);
        assertEquals(expectedValue, this.sut.calculateHeight(referenceText, f));
    }
    @Test(dataProvider = "prepareDataForTest")
    public void testCalculateWidthOfSpace(Object font) throws Exception {
        Font f = (Font)font;
        int expectedValue = this.calculateTextWidthByFont(" ", f);
        assertEquals(expectedValue, this.sut.calculateWidthOfSpace(f));
    }
    private int calculateTextWidthByFont(String referenceText, Font font) {
        Text t = getTextByFont(referenceText, font);
        return Double.valueOf(t.getLayoutBounds().getWidth()).intValue();
    }
    private int calculateTextHeightByFont(String referenceText, Font font) {
        Text t = getTextByFont(referenceText, font);
        return Double.valueOf(t.getLayoutBounds().getHeight()).intValue();
    }
    private Text getTextByFont(String referenceText, Font font) {
        Text t = new Text(referenceText);
        t.setFont(font);
        return t;
    }



}