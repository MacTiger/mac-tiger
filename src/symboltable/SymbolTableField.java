package symboltable;

public abstract class SymbolTableField {

    protected String identifier;

    public SymbolTableField(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
