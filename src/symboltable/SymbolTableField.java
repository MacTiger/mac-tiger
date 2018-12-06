package symboltable;

public abstract class SymbolTableField {

    private String identifier;

    public SymbolTableField(String indentifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
