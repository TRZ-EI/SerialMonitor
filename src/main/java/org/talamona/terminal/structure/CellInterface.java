package org.talamona.terminal.structure;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/03/17
 * Time: 15.33
 */
public interface CellInterface {
    String getValue();
    public Runnable accept(StructureVisitor visitor);
}
