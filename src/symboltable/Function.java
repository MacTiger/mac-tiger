package symboltable;

public class Function extends Symbol {

	private Type type;
	private SymbolTable symbolTable;

	public Function() {
		this.type = null;
		this.symbolTable = null;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public SymbolTable getSymbolTable() {
		return this.symbolTable;
	}

}
