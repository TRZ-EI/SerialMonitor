package org.talamona.terminal.gui;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.talamona.terminal.utils.ConfigurationHolder;
import org.talamona.terminal.utils.FontAndColorSelector;


public class GraphicDesigner {
    private  String debug;
    private Canvas canvas, canvasForGrid;
    private Group group;
    private FontAndColorSelector fcs;
    private boolean gridIsDrawn;

    public GraphicDesigner() {
        this.fcs = FontAndColorSelector.getNewInstance();
        this.debug = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.DEBUG);
    }


    public static GraphicDesigner createNewInstanceByGroupAndCanvasAndDebugParam(Group group, Canvas canvas, String debug){
        return new GraphicDesigner(group, canvas, debug);
    }
    public static GraphicDesigner createNewInstance(){
        return new GraphicDesigner();

    }

    // TODO Canvas canvas is to remove (not yet useful)

    private GraphicDesigner(Group group, Canvas canvas, String debug) {
        this.group = group;
        this.canvas = canvas;
        this.debug = debug;
        this.fcs = FontAndColorSelector.getNewInstance();
        this.gridIsDrawn = false;
    }





    public void drawGridForGraphicHelp(Canvas gridCanvas){
        if (this.debug != null && this.debug.equalsIgnoreCase("debug")) {
            this.drawHorizontalLinesOnCanvas(gridCanvas);
            this.drawVerticalLinesOnCanvas(gridCanvas);
        }
    }

    private void drawHorizontalLinesOnCanvas(Canvas gridCanvas) {
        int canvasHeight = Double.valueOf(gridCanvas.getHeight()).intValue();
        int canvasWidth =  Double.valueOf(gridCanvas.getWidth()).intValue();
        int rowHeight = this.fcs.getHeightForSmallFont("W");
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(0.1d);
        int rows = canvasHeight / rowHeight;
        for (int i = 0; i < rows; i ++){
            gc.strokeLine(0, rowHeight * i, canvasWidth, rowHeight * i);
        }
    }
    private void drawVerticalLinesOnCanvas(Canvas gridCanvas) {
        int canvasHeight = Double.valueOf(gridCanvas.getHeight()).intValue();
        int canvasWidth =  Double.valueOf(gridCanvas.getWidth()).intValue();
        int fontWidth = this.fcs.getWidthForSmallFont("W");
        int verticalLines = canvasWidth / fontWidth;
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(0.1d);
        for (int i = 1; i < verticalLines; i++){
            gc.strokeLine(fontWidth * i, 0, fontWidth * i, canvasHeight);
        }
    }



    public void setCanvasForGrid(Canvas canvasForGrid) {
        this.canvasForGrid = canvasForGrid;
    }

}