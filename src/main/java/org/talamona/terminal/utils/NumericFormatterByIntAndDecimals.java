package org.talamona.terminal.utils;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/11/17
 * Time: 14.57
 */
public class NumericFormatterByIntAndDecimals {
    private int integerPart, decimalPart;
    private long value;

    private NumericFormatterByIntAndDecimals(int integerPart, int decimalPart, long value){
        this.decimalPart = decimalPart;
        this.integerPart = integerPart;
        this.value = value;
    }

    public static NumericFormatterByIntAndDecimals getInstanceByIntDecimalsAndValue(int integerPart, int decimalPart, long value){
        return new NumericFormatterByIntAndDecimals(integerPart, decimalPart, value);
    }
    
    public String invoke(){
        boolean negative = false;
        long tempValue = this.value;
        if (this.value < 0){
            negative = true;
            tempValue *= -1;
        }
        int divisor = 1;
        for (int i = 0; i < this.decimalPart; i ++){
            divisor *= 10;
        }
        double calculatedValue = (double)tempValue / divisor;
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern(this.createFormat());



        String formattedValue = df.format(calculatedValue);
        /*
        int integerPlaces = formattedValue.indexOf('.');
        if (integerPlaces >= 0 && integerPlaces < this.integerPart){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.integerPart - integerPlaces; i ++){
                sb.append("0");
            }
            formattedValue = sb.toString() + formattedValue;
        }
        */
        if (negative){
            formattedValue = "-" + formattedValue;
        }
        return formattedValue;
        
    }
    private String createFormat(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.integerPart; i++){
            builder.append("#");
        }
        if (this.decimalPart > 0) {
            builder.append(".");
            for (int i = 0; i < this.decimalPart; i++) {
                builder.append("0");
            }
        }
        return builder.toString();
    }



}
