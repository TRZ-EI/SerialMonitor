package org.talamona.terminal.utils;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/11/17
 * Time: 14.47
 */
public class IdGeneratorByPosition {
    private int x;
    private int y;
    public static IdGeneratorByPosition getNewInstanceByXAndY(int x, int y) {
        return new IdGeneratorByPosition(x, y);
    }

    private IdGeneratorByPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int invoke() {
        // f(a, b) = s(a+b) + a, where s(n) = n*(n+1)/2
        return ((x+y)*(x+y+1))/2 + x;
    }

}
