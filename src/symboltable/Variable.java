package symboltable;

public class Variable extends FunctionOrVariable {

	private Type type;
	private int shift;

	public Variable() {
		this.type = null;
		this.shift = 0;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void setShift(int shift) {
		this.shift = shift;
	}

	public int getShift() {
		return this.shift;
	}

}
