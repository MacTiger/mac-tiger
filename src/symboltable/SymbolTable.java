package symboltable;
import java.util.ArrayList;

public class SymbolTable{

    private SymbolTable parent;
    private ArrayList<SymbolTable> children;
    private ArrayList<SymbolTableField> fields;

    public SymbolTable(SymbolTable parent, ArrayList<SymbolTable> children) {
        super();
        this.parent = parent;
        this.children = children;
        this.fields = new ArrayList<>();
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

    public void addField(SymbolTableField field){
        fields.add(field);
    }

    public ArrayList<SymbolTableField> getFields(){
        return fields;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }
}
