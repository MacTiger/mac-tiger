package semantic;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

	private int depth;
	private SymbolTable parent;
	private List<SymbolTable> children;
	private Namespace<Type> types;
	private Namespace<FunctionOrVariable> functionsAndVariables;

	public SymbolTable(SymbolTable parent) {
		this.depth = parent != null
			? parent.depth + 1
			: 0;
		this.parent = parent;
		this.children = new ArrayList<SymbolTable>();
		this.types = new Namespace<Type>();
		this.functionsAndVariables = new Namespace<FunctionOrVariable>();
	}

	public int getDepth() {
		return depth;
	}

	public SymbolTable getParent() {
		return parent;
	}

	public List<SymbolTable> getChildren() {
		return children;
	}

	public Namespace<Type> getTypes() {
		return types;
	}

	public Namespace<FunctionOrVariable> getFunctionsAndVariables() {
		return functionsAndVariables;
	}

}
