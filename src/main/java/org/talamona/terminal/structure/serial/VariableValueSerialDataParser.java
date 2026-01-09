package org.talamona.terminal.structure.serial;

import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.Variable;
import org.talamona.terminal.utils.DataTypesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 10.57
 */
public class VariableValueSerialDataParser implements SerialDataReader{
    private final int idVariablePos = 2;
    private final int idVariableLenght = 2;

    private final int valuePos = 4;
    private final int valueLenght = 8;

    private final int crcPos = 12;
    private final int crcLenght = 4;

    private final int eolPos = 16;
    private final int eolLenght = 1;

    private DataTypesConverter converter;

    public static SerialDataReader getNewInstance() {
        return new VariableValueSerialDataParser();
    }
    private VariableValueSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();
    }

    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {
        Cell retValue = null;
        if (this.controlDataLenght(data)) {

            byte[] varId = Arrays.copyOfRange(data, idVariablePos, idVariablePos + idVariableLenght);
            int id = this.converter.bytesToInt(varId);

            byte[] varValue = Arrays.copyOfRange(data, valuePos, valuePos + valueLenght);
            long value = this.converter.bytesToLong(varValue);

            byte[] crcValue = Arrays.copyOfRange(data, crcPos, crcPos + crcLenght);
            int crc = this.converter.notAsciiBytesToInt(crcValue);
            retValue = Variable.getInstance().setAConfiguration(false).setId(id).setValue(Long.toString(value));
        }
        return retValue;
    }

    private boolean controlDataLenght(byte[] data) {
        return (data.length == 12);
    }

    public static VariableValueSerialDataParser getInstance() {
        return new VariableValueSerialDataParser();
    }

}
