package symboltable;
import java.util.ArrayList;

public class Function extends SymbolTableField {

    private Type returnType;
    private SymbolTable symbolTable;

    public Function(String identifier, Type returnType, SymbolTable symbolTable) {
        super(identifier);
        this.returnType = returnType;
        this.symbolTable = symbolTable;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
}
