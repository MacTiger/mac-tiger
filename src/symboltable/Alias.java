package symboltable;

public class Alias extends Type {

    private Type type;

    public Alias(String identifier, int size, Type type) {
        super(identifier, size);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
