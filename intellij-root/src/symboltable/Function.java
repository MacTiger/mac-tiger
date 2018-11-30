package symboltable;
import java.util.ArrayList;

public class Function extends STObject {
    private Type returnType;
    private ArrayList<Variable> arguments;

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}
