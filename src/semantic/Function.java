package semantic;

public class Function extends FunctionOrVariable {

	private Type type;
	private SymbolTable table;
	private boolean written;

	public Function() {
		this.type = null;
		this.table = null;
		this.written = false;
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

	public void setWritten() {
		this.written = true;
	}

	public boolean isWritten() {
		return written;
	}

	public boolean isNative() {
		return this.table.getDepth() == 1; // Cette fonction est une fonction de la bibliothèque standard si elle est déclarée dans la TDS globale, donc que sa TDS est de profondeur 1
	}

}
