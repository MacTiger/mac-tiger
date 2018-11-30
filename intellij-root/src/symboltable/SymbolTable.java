package symboltable;
import java.util.ArrayList;

public class SymbolTable extends ArrayList<STObject> {

    private SymbolTable parent;
    private ArrayList<SymbolTable> children;

    public ArrayList<SymbolTable> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SymbolTable> children) {
        this.children = children;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }
}
