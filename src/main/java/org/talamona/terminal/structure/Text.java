package org.talamona.terminal.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.talamona.terminal.utils.IdGeneratorByPosition;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/03/17
 * Time: 15.37
 */
public class Text extends Cell{


    private int calculatedId;

    public static Text getNewInstance(){
        return new Text();
    }
    private Text(){
        super();
    }

    public static Text getNewInstanceByFontAndColor(Font font, Color color){
        return new Text(font, color);
    }
    private Text(Font font, Color color) {
        super(font, color);
    }

    @Override
    public Cell setValue(String value){
        super.setValue(value);
        return this;
    }

    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public Runnable accept(StructureVisitor visitor) {
        return visitor.visit(this);
    }

    public Text updateData(Text dataParsed) {
        if (this.equals(dataParsed)) {
            this.setValue(dataParsed.getValue());
        }
        return this;
    }
    @Override
    public int getId(){
        if(this.calculatedId == 0) {
            this.calculatedId = this.calculateHashedId(this.getxPos(), this.getyPos());
        }
        return this.calculatedId;
    }

    private int calculateHashedId(int x, int y) {
        return String.valueOf((this.calculateIdUsingIniectiveFunctionForXandY(x, y))).hashCode();
    }

    public int calculateIdUsingIniectiveFunctionForXandY(int x, int y) {
        return IdGeneratorByPosition.getNewInstanceByXAndY(x, y).invoke();


    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Text){
            Text other = (Text) o;
            result = this.getxPos() == other.getxPos();
            result &= this.getyPos() == other.getyPos();
        }
        return result;
    }

}
