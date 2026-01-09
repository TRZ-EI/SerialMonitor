package org.talamona.terminal.structure;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 28/05/17
 * Time: 16.14
 */
public class Bar extends Cell {
    private long minValue;
    private long maxValue;

    public long getMinValue() {
        return minValue;
    }

    public Bar setMinValue(long minValue) {
        if(this.minValue != minValue){
            this.minValue = minValue;
        }
        return this;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public Bar setMaxValue(long maxValue) {
        if(this.maxValue != maxValue){
            this.maxValue = maxValue;
        }
        return this;
    }

    public static Bar getInstance() {
        return new Bar();
    }

    @Override
    public Runnable accept(StructureVisitor visitor) {
        return visitor.visit(this);
    }
}
