package org.talamona.terminal.utils;

import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.Number;
import org.talamona.terminal.structure.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/01/18
 * Time: 10.27
 */
public class RightTextAligner {

    private static RightTextAligner instance;

    private final FontAndColorSelector fontAndColorSelector;

    public static RightTextAligner getSingleInstance(){
        if(instance == null){
            instance = new RightTextAligner();
        }
        return instance;
    }
    private RightTextAligner(){
        this.fontAndColorSelector = FontAndColorSelector.getNewInstance();
    }


    // TODO: why method overload?
    public int calculatePrintingPositionByCell(Cell cell){

        int retValue = cell.getPixelScreenXPos();
        if (cell instanceof Number) {
            retValue = this.calculatePositionByCell((Number) cell);
        }else if(cell instanceof Variable){
            retValue = this.calculatePositionByCell((Variable) cell);
        }
        return retValue;
    }
    private int calculatePositionByCell(Variable cell){
        String value = cell.printFormattedValue();
        int fontWidth = this.fontAndColorSelector.getWidthForFont(cell.getFont(), "W");
        int deltaPos = value.length() * fontWidth;
        return cell.getPixelScreenXPos() - deltaPos;
    }
    private int calculatePositionByCell(Number cell){
        String value = cell.printFormattedValue();
        int fontWidth = this.fontAndColorSelector.getWidthForFont(cell.getFont(), "W");
        int deltaPos = value.length() * fontWidth;
        return cell.getPixelScreenXPos() - deltaPos;
    }


}
