package symboltable;

abstract public class Type extends SymbolTableField {

    public Type(String identifier) {
        super(identifier);
    }

    abstract public int getSize();

}
