package symboltable;

public class Array extends Type {

    private Type type;

    public Array(String identifier, Type type, int length) {
        super(identifier, 4);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
