package org.talamona.terminal.structure.serial;

import org.talamona.terminal.structure.Variable;
import org.talamona.terminal.utils.DataTypesConverter;
import org.talamona.terminal.utils.FontAndColorSelector;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 10.56
 */
public class VariableConfiguratorSerialDataParser implements SerialDataReader {

    // position and lenght for byte[] content (Variable configuration)

    private final int idVariablePos = 2;
    private final int idVariableLenght = 2;

    private final int fontColorPos = 4;
    private final int fontColorLenght = 1;

    private final int integerPartPos = 5;
    private final int integerPartLenght = 1;

    private final int decimalPartPos = 6;
    private final int decimalPartLenght = 1;

    private final int rowPos = 7;
    private final int rowLenght = 2;

    private final int colPos = 9;
    private final int colLenght = 2;

    private final int crcPos = 11;
    private final int crcLenght = 4;

    private final int eolPos = 15;
    private final int eolLenght = 1;


    private DataTypesConverter converter;

    public static VariableConfiguratorSerialDataParser getNewInstance() {
        return new VariableConfiguratorSerialDataParser();
    }

    public Variable readByteArray(byte[] data) throws UnsupportedEncodingException {
        byte[] varId = Arrays.copyOfRange(data, idVariablePos, idVariablePos + idVariableLenght);

        int id = this.converter.bytesToInt(varId);

        byte fontColor = Arrays.copyOfRange(data, fontColorPos, fontColorPos + fontColorLenght)[0];
        char fc = this.converter.byteToChar(fontColor);

        byte[] intPos = Arrays.copyOfRange(data, integerPartPos, integerPartPos + integerPartLenght);
        int integerDigits = this.converter.bytesToInt(intPos);

        byte[] dec = Arrays.copyOfRange(data, decimalPartPos, decimalPartPos + decimalPartLenght);
        int decimalDigits = this.converter.bytesToInt(dec);

        byte[] r = Arrays.copyOfRange(data, rowPos, rowPos + rowLenght);
        int row = this.converter.bytesToInt(r);

        byte[] c = Arrays.copyOfRange(data, colPos, colPos + colLenght);
        int column = this.converter.bytesToInt(c);

        byte[] crc = Arrays.copyOfRange(data, crcPos, crcPos + crcLenght);
        int crcValue = this.converter.notAsciiBytesToInt(crc);

        Variable retValue = this.createVariable(fc);

        retValue.setIntegerLenght(integerDigits).setDecimalLenght(decimalDigits).setxPos(column).setyPos(row).setId(id);
        return retValue;


    }

    public Variable createVariable(char fc) {
        FontAndColorSelector fcs = FontAndColorSelector.getNewInstance();
        return Variable.getInstanceByFontAndColor(fcs.selectFont(fc), fcs.selectColor(fc)).setAConfiguration(true);
    }

    private VariableConfiguratorSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();

    }



}
