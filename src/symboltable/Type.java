package symboltable;

public class Type extends SymbolTableField {
    private int size;

    public Type(String identifier) {
        super(identifier);
        this.size = 1;
    }

    public Type(String identifier, int size) {
        super(identifier);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
