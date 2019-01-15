package symboltable;

public class Variable extends SymbolTableField {

    private Type type;
    private int shift;

    public Variable(String identifier) {
        super(identifier);
        this.type = null;
        this.shift = 0;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setShift() {
        this.shift = shift;
    }

    public int getShift() {
        return this.shift;
    }

}
