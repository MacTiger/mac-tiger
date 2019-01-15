package symboltable;

public class Alias extends Type {

    private Type type;

    public Alias(String identifier) {
        super(identifier);
        this.type = null;
    }

    public int getSize() {
        return this.type != null ? this.type.getSize() : 0;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

}
