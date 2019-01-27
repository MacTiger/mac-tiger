package symboltable;

public class Function extends FunctionOrVariable {

	private Type type;
	private SymbolTable table;

	public Function() {
		this.type = null;
		this.table = null;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void setSymbolTable(SymbolTable table) {
		this.table = table;
	}

	public SymbolTable getSymbolTable() {
		return this.table;
	}

}
