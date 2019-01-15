package symboltable;
import java.util.ArrayList;

public class SymbolTable{

    private SymbolTable parent;
    private ArrayList<SymbolTable> children;
    private ArrayList<SymbolTableField> fields;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.children = new ArrayList<SymbolTable>();
        this.fields = new ArrayList<SymbolTableField>();
    }

    public SymbolTable getParent() {
        return parent;
    }

    public ArrayList<SymbolTable> getChildren() {
        return children;
    }

    public ArrayList<SymbolTableField> getFields() {
        return fields;
    }

}
