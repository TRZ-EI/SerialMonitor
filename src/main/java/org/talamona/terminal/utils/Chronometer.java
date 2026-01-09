package org.talamona.terminal.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Chronometer{

    private long begin, end;

    public static Chronometer getInstance(){
        return new Chronometer();
    }

    public void start(){
        begin = System.currentTimeMillis();
    }

    public void stop(){
        end = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return end-begin;
    }
    public double getActualTimeInSeconds(){
        Double d = Double.valueOf(System.currentTimeMillis()) ;
        d = d - begin;
        Double now = d.doubleValue() / 1000;
        return BigDecimal.valueOf(now).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }


    public double getElapsedTimeInSeconds() {
        return (end - begin) / 1000.0;
    }

    public double getElapsedTimeMinutes() {
        return (end - begin) / 60000.0;
    }

    public double getElapsedTimeHours() {
        return (end - begin) / 3600000.0;
    }

}
