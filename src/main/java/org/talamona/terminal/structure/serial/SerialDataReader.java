package org.talamona.terminal.structure.serial;

import org.talamona.terminal.structure.Cell;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 11.24
 */
// Commands: V, v, n, t, B
// Fonts:
    /*
    nero_piccolo 	'P'
    rosso_piccolo 	'Q'
    verde_piccolo 	'R'
    blu_piccolo 	'S'
    nero_grande		0x39
    rosso_grande	'A'
    verde_grande	'P'

     */



public interface SerialDataReader {
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException;

}
