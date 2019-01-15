package symboltable;

public abstract class SymbolTableField {

    private String identifier;

    public SymbolTableField(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

}
