package symboltable;

public class Array extends Type {

    private int length;

    public Array(String identifier, int size, int length) {
        super(identifier, size);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void getLength(int length) {
        this.length = length;
    }
}
