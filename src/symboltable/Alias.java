package symboltable;

public class Alias extends Type {

    private Type type;

    public Alias(String identifier, Type type) {
        super(identifier);
        this.type = type;
        this.setSize(type.getSize());
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
