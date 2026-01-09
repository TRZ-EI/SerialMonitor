package org.talamona.terminal.structure;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/10/17
 * Time: 16.36
 */
public class RowCleaner extends Cell {

    public static RowCleaner getInstanceByRowId(int rowId){
        return new RowCleaner(rowId);
    }
    private RowCleaner(int id){
        this.setId(id);
    }

    @Override
    public Runnable accept(StructureVisitor visitor) {
        return visitor.visit(this);
    }
}
