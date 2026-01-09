package org.talamona.terminal.gui;

import org.talamona.terminal.structure.Cell;
import org.talamona.terminal.structure.serial.SerialDataFacade;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 30/08/17
 * Time: 9.04
 */
public class GraphicDesignerRunnable implements Runnable {
    private String[] messages;
    private SerialDataFacade serialDataFacade;
    private GraphicDesigner graphicDesigner;


    private GraphicDesignerRunnable(SerialDataFacade serialDataFacade, GraphicDesigner graphicDesigner) {
        this.serialDataFacade = serialDataFacade;
        this.graphicDesigner = graphicDesigner;
    }

    public static GraphicDesignerRunnable createNewInstanceBySerialDataFacadeAndGraphicDesigner(SerialDataFacade serialDataFacade, GraphicDesigner graphicDesigner){
        return new GraphicDesignerRunnable(serialDataFacade, graphicDesigner);
    }


    @Override
    public void run() {
        try {
            for (String m: messages) {
                Cell c = serialDataFacade.onSerialDataParser(m.getBytes());
                //CellsRow aRow = serialDataFacade.onSerialDataInput(m.getBytes());
                if (c != null){


                }

                //if (aRow != null) {
                //    graphicDesigner.setDataDisplayManager(serialDataFacade.getDisplayManager());
                //    graphicDesigner.drawASingleRowOnCanvas(aRow);
                //}
            }
            //serialDataManager.setIsDataAvalaible(false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }
}
