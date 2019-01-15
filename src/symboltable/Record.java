package symboltable;

import misc.Constants;

public class Record extends Type {

    private SymbolTable symbolTable;

    public Record(String identifier, SymbolTable parent) {
        super(identifier);
        this.symbolTable = new SymbolTable(parent);
    }

    public int getSize() {
        return Constants.pointerSize;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

}
