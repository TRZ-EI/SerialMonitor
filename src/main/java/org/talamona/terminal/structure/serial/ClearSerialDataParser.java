package org.talamona.terminal.structure.serial;

import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.Clear;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 17/11/17
 * Time: 13.49
 */
public class ClearSerialDataParser implements SerialDataReader{

    public static ClearSerialDataParser getNewInstance(){
        return new ClearSerialDataParser();
    }
    private ClearSerialDataParser(){

    }
    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {
        return new Clear();
    }
}
