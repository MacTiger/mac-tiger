package symboltable;
import java.util.ArrayList;

public class Function extends SymbolTableField {

    private Type type;
    private SymbolTable symbolTable;

    public Function(String identifier, SymbolTable parent) {
        super(identifier);
        this.type = null;
        this.symbolTable = new SymbolTable(parent);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

}
