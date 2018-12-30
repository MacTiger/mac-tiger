package symboltable;
import java.util.ArrayList;

public class SymbolTable extends ArrayList<SymbolTableField> {

    private SymbolTable parent;
    private ArrayList<SymbolTable> children;

    public SymbolTable(SymbolTable parent, ArrayList<SymbolTable> children) {
        super();
        this.parent = parent;
        this.children = children;
    }

    public ArrayList<SymbolTable> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SymbolTable> children) {
        this.children = children;
    }

    public void addChild(SymbolTable child){
        children.add(child);
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }
}
