package org.talamona.terminal.structure;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.talamona.terminal.gui.DrawingText;
import org.talamona.terminal.gui.TRZBar;
import org.talamona.terminal.utils.FontAndColorSelector;
import org.talamona.terminal.utils.RightTextAligner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/11/17
 * Time: 16.54
 */
public class StructureVisitor {

    private String value;
    private DrawingText mainWindow;
    private Multimap<Integer, Node> multipleItems = ArrayListMultimap.create();
    private Map<Integer, Variable> configurations = new HashMap<>();
    private RightTextAligner rightTextAligner;

    public StructureVisitor(DrawingText drawingText) {
        this.mainWindow = drawingText;
        this.rightTextAligner = RightTextAligner.getSingleInstance();
    }
    public Runnable visit(Text cell){
        value = cell.getValue();
        return this.findTextAndFillWithData(cell);
    }
    public Runnable visit(Number number) {
        this.value = number.printFormattedValue();
        return this.findTextAndFillWithData(number);
    }

    public Runnable visit(Variable cell){
        Runnable retValue = null;
        if (cell.isAConfiguration()){
            this.configurations.put(cell.getId(), cell);
        }else {
            cell = this.mergeWithConfiguration(cell);
            if (cell != null) {
                this.value = cell.printFormattedValue();
                retValue = this.findTextAndFillWithData(cell);
            }
        }
        return retValue;
    }

    private Variable mergeWithConfiguration(Variable cell) {
        Variable retValue = null;
        Variable configuration = this.configurations.get(cell.getId());
        if (configuration != null){
            retValue = (Variable) cell.setDecimalLenght(configuration.getDecimalLenght())
                    .setIntegerLenght(configuration.getIntegerLenght()).setFont(configuration.getFont())
                    .setColor(configuration.getColor()).setxPos(configuration.getxPos()).setyPos(configuration.getyPos());
        }else{
            retValue = cell;
        }
        return retValue;
    }

    public Runnable visit(Clear cell){
        this.mainWindow.getRows().clear();
        this.multipleItems.clear();
        return new JfxCleaner(this.mainWindow);
    }
    public Runnable visit(RowCleaner cell){
        Collection<Node> contents = this.mainWindow.getRows().get(cell.getId());

        return new JfxRowCleaner(this.mainWindow, contents);
    }
    public Runnable visit(Bar cell){
        return this.findBarAndFillWithData(cell);
    }

    private Runnable findBarAndFillWithData(Bar variable){
        Runnable returnValue = null;
        String id = String.valueOf(variable.getId());
        Node bar = this.mainWindow.getRoot().getScene().lookup("#" + id);
        if (bar == null){
            bar = this.configureBar(variable);
            returnValue = this.addBarToJfxTree(variable, bar);
        }
        return returnValue;
    }
    private Runnable addBarToJfxTree(Bar variable, Node bar) {
        this.mainWindow.getRows().put(variable.getyPos(), bar);
        this.multipleItems.put(variable.getId(), bar);
        return new JfxTreeEnhancerForTRZBar(bar, this.mainWindow);
    }

    private Node configureBar(Bar variable) {
        TRZBar retValue = new TRZBar();
        // TODO: EXPERIMENTS WITH BARS
        double height = 30d;
        double width = 800d;
        int pixelScreenYPos = variable.getPixelScreenYPos();
        retValue.setMinValue(variable.getMinValue());
        retValue.setMaxValue(variable.getMaxValue());
        retValue.setHeight(height);
        retValue.setWidth(width);
        retValue.setX(0d);
        retValue.setY(pixelScreenYPos);
        retValue.setFill(null);


        return retValue;
    }


    private Runnable findTextAndFillWithData(Cell variable){
        Runnable retValue = null;
        String id = String.valueOf(variable.getId());

        // TO TEST: REMOVE SEARCH
        javafx.scene.text.Text myText = (javafx.scene.text.Text)this.mainWindow.getRoot().getScene().lookup("#" + id);
        if (myText == null){
            myText = createTextNode(variable);
            retValue = this.addNodeToJfxTree(variable, myText);
        }else {
            retValue = this.updateValue(variable);
        }
        return retValue;
    }

/*
    // WRITE FOR TEXT
    private void findTextAndFillWithData(Cell variable){
        // TO TEST: REMOVE SEARCH
        javafx.scene.text.Text myText = this.createTextNode(variable);
        this.addNodeToJfxTree(variable, myText);
        this.updateValue(variable);
    }
*/

    private Runnable updateValue(Cell cell) {
        JfxCompositeRunnable retValue = new JfxCompositeRunnable();
        for (Node n: this.multipleItems.get(cell.getId())){
            if (n instanceof javafx.scene.text.Text){
               retValue.add(this.update(cell, (javafx.scene.text.Text)n));
            }else{
               retValue.add(this.update((TRZBar)n, cell.getValue()));
            }
        }
        return retValue;
    }
    private RunnableFragment update(Cell cell, javafx.scene.text.Text t){
        int rightPos = this.rightTextAligner.calculatePrintingPositionByCell(cell);
        return new JfxTextUpdaterFragment(t, value, rightPos, cell.getPixelScreenYPos());
    }

    private RunnableFragment update(TRZBar t, String rawValue){
        Color color = FontAndColorSelector.getNewInstance().getColorForBar();
        double finalValue = Double.parseDouble(rawValue);
        return new JfxGaugeBarUpdaterFragment(color, finalValue, t);
    }


    private Runnable addNodeToJfxTree(Cell variable, Node myText) {
        this.mainWindow.getRows().put(variable.getyPos(), myText);
        this.multipleItems.put(variable.getId(), myText);
        return new JfxTreeEnhancer(myText, this.mainWindow);
    }

    private javafx.scene.text.Text createTextNode(Cell variable) {
        String id = String.valueOf(variable.getId());
        javafx.scene.text.Text retValue = new javafx.scene.text.Text();
        this.updateStartingPositionToWriteText(variable, retValue);
        retValue.setId(id);
        retValue.setFill(variable.getColor());
        retValue.setFont(variable.getFont());
        retValue.setText(this.value);
        return retValue;
    }

    private void updateStartingPositionToWriteText(Cell variable, javafx.scene.text.Text retValue) {
        int rightPos = this.rightTextAligner.calculatePrintingPositionByCell(variable);
        retValue.setX(rightPos);
        retValue.setY(variable.getPixelScreenYPos());
    }

}
