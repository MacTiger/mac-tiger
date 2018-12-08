package symboltable;
import misc.Constants;

public class Array extends Type {

    private Type type;

    public Array(String identifier, Type type) {
        super(identifier, Constants.pointerSize);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
