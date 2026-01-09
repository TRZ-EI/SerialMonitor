package org.talamona.terminal.structure;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 14.53
 */
public class TextMetricCalculator {

    private Text referenceText;
    private static TextMetricCalculator instance;
    public static TextMetricCalculator getInstance() {
        if (instance == null){
            instance = new TextMetricCalculator();
        }
        return instance;
    }
    private TextMetricCalculator() {
        this.referenceText = new Text();
    }
    public int calculateWidth(String text, Font font){
        this.setStringAndFontOnReferenceText(text, font);
        return Double.valueOf(this.referenceText.getLayoutBounds().getWidth()).intValue();
        //return 0;
    }
    public int calculateHeight(String text, Font font){
        this.setStringAndFontOnReferenceText(text, font);
        return Double.valueOf(this.referenceText.getLayoutBounds().getHeight()).intValue();
    }
    public int calculateWidthOfSpace(Font font){
        this.setStringAndFontOnReferenceText(" ", font);
        return Double.valueOf(this.referenceText.getLayoutBounds().getWidth()).intValue();
    }
    private void setStringAndFontOnReferenceText(String text, Font font) {
        this.referenceText.setText(text);
        this.referenceText.setFont(font);
    }

}
