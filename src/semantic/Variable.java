package semantic;

import java.util.ArrayList;
import java.util.Arrays;

import static semantic.SymbolTable.makeAdresse;
import static semantic.SymbolTable.makeLink;

public class Variable extends FunctionOrVariable {

	private Type type;
	private boolean writable;
	private boolean alreadyTranslated;  // Indique durant le passage de TigerTranslator si cette variable a déjà été traduite, pour permettre la redéclaration de variable dans le même bloc
	private int offset;

	public Variable() {
		this.type = null;
		this.writable = true;
		this.offset = 0;
		this.alreadyTranslated = false;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return this.offset;
	}

	public void configure(boolean writable) {
		this.writable = writable;
	}

	public boolean isWritable() {
		return this.writable;
	}

	public boolean isAlreadyTranslated() {
		return alreadyTranslated;
	}

	public void setAlreadyTranslated(boolean alreadyTranslated) {
		this.alreadyTranslated = alreadyTranslated;
	}

	public ArrayList<String> makeCellGraphviz(String nameOfVar, String nameOfThisTDS, String numOfCell){
		ArrayList<String> typeGraphs = type.makeCellGraphviz(nameOfThisTDS, numOfCell);

		String partOfGraph = "var|"+ nameOfVar + "|" + offset + "|";
		partOfGraph += typeGraphs.get(0);
		String graphLinks = typeGraphs.get(1);
		return new ArrayList<>(Arrays.asList(partOfGraph, graphLinks));
	}
}
