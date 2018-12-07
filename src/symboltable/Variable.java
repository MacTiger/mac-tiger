package symboltable;

public class Variable extends SymbolTableField {

    private Type type;
    private int shift;

    public Variable(String identifier, Type type, int shift) {
        super(identifier);
        this.type = type;
        this.shift = shift;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getShift() {
        return shift;
    }

    public void setShift() {
        this.shift = shift;
    }

}