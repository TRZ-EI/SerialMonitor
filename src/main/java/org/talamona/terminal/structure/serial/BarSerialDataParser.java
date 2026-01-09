package org.talamona.terminal.structure.serial;

import javafx.scene.paint.Color;
import org.talamona.terminal.structure.Bar;
import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.utils.DataTypesConverter;
import org.talamona.terminal.utils.FontAndColorSelector;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 28/05/17
 * Time: 16.12
 */
public class BarSerialDataParser implements SerialDataReader {
    private final int COLUMN = 10;

    private final int idVariablePos = 2;
    private final int idVariableLenght = 2;

    private final int valueMinPos = 4;
    private final int valueMinLenght = 8;

    private final int valueMaxPos = 12;
    private final int valueMaxLenght = 8;

    private final int rowPos = 20;
    private final int rowLenght = 2;

    private final int crcPos = 22;
    private final int crcLenght = 4;

    private final int eolPos = 26;
    private final int eolLenght = 1;

    private DataTypesConverter converter;

    public static BarSerialDataParser getNewInstance(){
        return new BarSerialDataParser();
    }
    private BarSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();
    }


    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {
        byte[] varId = Arrays.copyOfRange(data, idVariablePos, idVariablePos + idVariableLenght);
        int id = this.converter.bytesToInt(varId);

        byte [] minVarValue = Arrays.copyOfRange(data, valueMinPos, valueMinPos + valueMinLenght);
        long minValue = this.converter.bytesToLong(minVarValue);

        byte [] maxVarValue = Arrays.copyOfRange(data, valueMaxPos, valueMaxPos + valueMaxLenght);
        long maxValue = this.converter.bytesToLong(maxVarValue);

        byte[] rp = Arrays.copyOfRange(data, rowPos, rowPos + rowLenght);
        int row = this.converter.bytesToInt(rp);

        byte[] crcValue = Arrays.copyOfRange(data, crcPos, crcPos + crcLenght);
        int crc = this.converter.notAsciiBytesToInt(crcValue);

        Cell retValue = Bar.getInstance().setMinValue(minValue).setMaxValue(maxValue).setId(id).setyPos(row).setxPos(COLUMN);
        retValue.setFont(FontAndColorSelector.getNewInstance().getSmallFont());
        retValue.setColor(Color.BLACK);
        return retValue;
    }
}
