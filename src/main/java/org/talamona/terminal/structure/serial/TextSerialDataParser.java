package org.talamona.terminal.structure.serial;

import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.Text;
import org.talamona.terminal.utils.DataTypesConverter;
import org.talamona.terminal.utils.FontAndColorSelector;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 10.57
 */
public class TextSerialDataParser implements SerialDataReader{

    // TO NOTE: text is variable in lenght. So, start at the end of the array
    // to find eol, crc. Start at the begin of the array to find font+color,
    // row pos, col pos. Text string is between these boundaries....

    private final int fontColorPos = 2;
    private final int fontColorLenght = 1;


    private final int rowPos = 3;
    private final int rowLenght = 2;

    private final int colPos = 5;
    private final int colLenght = 2;




    private final int crcLenght = 4;

    private final int eolLenght = 1;
    private byte newLine = 0x0A;

    private DataTypesConverter converter;

    public static TextSerialDataParser getNewInstance(){
        return new TextSerialDataParser();
    }
    private TextSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();
    }


    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {

        byte fc = Arrays.copyOfRange(data, this.fontColorPos, this.fontColorPos + this.fontColorLenght)[0];
        char selectorForFontAndColor = this.converter.byteToChar(fc);

        byte[] rowInBytes = Arrays.copyOfRange(data, this.rowPos, this.rowPos + this.rowLenght);
        int rowIndex = this.converter.bytesToInt(rowInBytes);

        byte[] columnInBytes = Arrays.copyOfRange(data, this.colPos, this.colPos + this.colLenght);
        int columnIndex = this.converter.bytesToInt(columnInBytes);

        int newLineIndex = this.thereIsNewLine(data);

        int lastTextCharIndex = newLineIndex - 4;
        int firstTextCharIndex = 7;

        byte[] textInBytes = Arrays.copyOfRange(data, firstTextCharIndex, lastTextCharIndex);

        String text = this.converter.bytesToString(textInBytes);
        FontAndColorSelector fcs = FontAndColorSelector.getNewInstance();
        Text textCell = Text.getNewInstanceByFontAndColor(fcs.selectFont(selectorForFontAndColor), fcs.selectColor(selectorForFontAndColor));
        textCell.setValue(text);
        textCell.setxPos(columnIndex);
        textCell.setyPos(rowIndex);

        return textCell;
    }

    private int thereIsNewLine(byte[] data) {
        int index = 0;
        for (byte b: data){
            index = (b == newLine)? 1: 0;
        }
        return data.length - index;

    }
}
