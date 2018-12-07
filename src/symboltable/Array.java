package symboltable;

public class Array extends Type {

    private int length;
    private Type type;

    public Array(String identifier, Type type, int length) {
        super(identifier, type.getSize() * length);
        this.length = length;
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
