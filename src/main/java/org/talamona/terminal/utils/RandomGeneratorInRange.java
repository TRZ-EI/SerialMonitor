package org.talamona.terminal.utils;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 16/03/17
 * Time: 13.13
 */
public class RandomGeneratorInRange {

    private double max;
    private double min;
    private double actual;

    public RandomGeneratorInRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static RandomGeneratorInRange getInstanceByRange(double min, double max){
        return new RandomGeneratorInRange(min, max);
    }
    public double getRandomValue(){
        return this.min + (Math.random() * ((this.max - this.min) + 1));
    }
}
