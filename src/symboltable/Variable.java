package symboltable;

public class Variable extends Symbol {

	private Type type;
	private int shift;

	public Variable(int shift) {
		this.type = null;
		this.shift = 0;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public int getShift() {
		return this.shift;
	}

}
