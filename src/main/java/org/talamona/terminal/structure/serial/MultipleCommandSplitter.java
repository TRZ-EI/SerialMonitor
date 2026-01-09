package org.talamona.terminal.structure.serial;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 17/08/17
 * Time: 14.31
 */
// ^V/^v    --> sync
// *************************************************
// For ^V   --> CONFIGURATION: 9 chars
// 2 digits --> variable ID
// 1 digit  --> font and color
// 1 digit  --> int part
// 1 digit  --> decimal part
// 2 digit  --> row (y)
// 2 digit  --> col (x)
// *************************************************
// For ^v   --> VALUE: 10 chars
// 2 digits --> variable ID
// 8 digits --> value
// *************************************************
// 4 digit  --> CRC (hex) NB: only at the end of multiple command




public class MultipleCommandSplitter {
    private final int LENGHT_FOR_CONFIG = 9;
    private final int LENGHT_FOR_VALUE = 10;
    private final int LENGHT_FOR_CRC = 4;


    public static MultipleCommandSplitter getNewInstance() {
        return new MultipleCommandSplitter();
    }
    private MultipleCommandSplitter(){

    }
    public List<String> splitMultipleCommand(String multipleCommand) {
        List<String> retValue = new ArrayList<>();
        if (this.isPermittedCommand(multipleCommand)){
            int parsingLength = this.selectParsingLenght(multipleCommand);
            multipleCommand = this.stripSyncAndCRC(multipleCommand);
            retValue = parseMultipleCommand(multipleCommand, parsingLength);
        }else{
            retValue.add(multipleCommand);
        }
        return retValue;
    }
    private List<String> parseMultipleCommand(String multipleCommand, int parsingLength) {
        List<String> retValue = new ArrayList<>();
        for (String substring : Splitter.fixedLength(parsingLength).split(multipleCommand)) {
            retValue.add("^" + this.selectCommand(parsingLength) + substring);
        }
        return retValue;
    }
    private String selectCommand(int parsingLength) {
        return (parsingLength == LENGHT_FOR_CONFIG)? "V": "v";
    }
    private String stripSyncAndCRC(String multipleCommand) {
        int pos = multipleCommand.indexOf("^") + 2;
        String tempValue = multipleCommand.substring(pos);
        return tempValue.substring(0, tempValue.length() - LENGHT_FOR_CRC);
    }
    private int selectParsingLenght(String multipleCommand) {
        return (multipleCommand.indexOf("V") >= 0)? LENGHT_FOR_CONFIG: LENGHT_FOR_VALUE;
    }
    public boolean isPermittedCommand(String multipleCommand) {
        return (multipleCommand.indexOf("^V") >= 0 || multipleCommand.indexOf("^v") >= 0);
    }

}
