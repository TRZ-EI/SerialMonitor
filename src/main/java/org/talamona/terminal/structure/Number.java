package org.talamona.terminal.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.talamona.terminal.utils.NumericFormatterByIntAndDecimals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/03/17
 * Time: 11.41
 */
public class Number extends Cell {

    private long value;
    private int integerLenght;
    private int decimalLenght;
    private String valueToPrint;


    public static Number getInstance(){
        Number retValue = new Number();
        retValue.setxPos(-1).setyPos(-1);
        return retValue;
    }


    public static Number getInstanceByFontAndColor(Font font, Color color){
        return new Number(font, color);

    }

    private Number() {
        super();
    }
    private Number(Font font, Color color) {
        super(font, color);
    }

    public String getValue(){
        return Long.toString(this.value);
    }

    @Override
    public Runnable accept(StructureVisitor visitor) {
        return visitor.visit(this);
    }

    public int getWidth() {

        return TextMetricCalculator.getInstance().calculateWidth(this.printFormattedValue(), this.getFont());
    }
    public String printFormattedValue(){
        return this.valueToPrint;
    }

    public String prepareFormattedValue() {
        return NumericFormatterByIntAndDecimals.
                getInstanceByIntDecimalsAndValue(this.integerLenght, this.decimalLenght, this.value)
                .invoke();
    }

    public Number setValue(String value) {
        if (value != null){
            long tempValue = Long.valueOf(value, 10);
            this.value = tempValue;
            this.valueToPrint = this.prepareFormattedValue();
        }
        return this;
    }

    public Number setDecimalLenght(int decimalLenght) {
        this.decimalLenght = decimalLenght;
        this.valueToPrint = this.prepareFormattedValue();
        return this;
    }
    public Number setIntegerLenght(int integerLenght) {
        this.integerLenght = integerLenght;
        this.valueToPrint = this.prepareFormattedValue();
        return this;
    }


    public void updateData(Number d) {
        this.value = d.value;
        this.decimalLenght = d.decimalLenght;
        this.integerLenght = d.integerLenght;
        this.valueToPrint = this.prepareFormattedValue();
    }

    public int getIntegerLenght() {
        return integerLenght;
    }

    public int getDecimalLenght() {
        return decimalLenght;
    }

}
