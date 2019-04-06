package semantic;

import java.util.ArrayList;
import java.util.Arrays;

public class Variable extends FunctionOrVariable {

	private Type type;
	private boolean writable;
	private int offset;

	public Variable() {
		this.type = null;
		this.writable = true;
		this.offset = 0;
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

	public ArrayList<String> makeCellGraphviz(String nameOfVar, String nameOfThisTDS, String numOfCell){
		ArrayList<String> typeGraphs = type.makeCellGraphviz(nameOfThisTDS, numOfCell);

		String partOfGraph = "var|"+ nameOfVar + "|" + offset + "|";
		partOfGraph += typeGraphs.get(0);
		String graphLinks = typeGraphs.get(1);
		return new ArrayList<>(Arrays.asList(partOfGraph, graphLinks));
	}

}
