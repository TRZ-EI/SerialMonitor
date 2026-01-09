package org.talamona.terminal.structure;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/10/17
 * Time: 16.36
 */
public class Clear extends Cell {

    @Override
    public Runnable accept(StructureVisitor visitor) {
        return visitor.visit(this);
    }
}
