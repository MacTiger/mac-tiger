package symboltable;

public class Primitive extends Type {

    private int size;

    public Primitive(String identifier, int size) {
        super(identifier);
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

}
