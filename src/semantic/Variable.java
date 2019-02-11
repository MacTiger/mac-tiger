package semantic;

public class Variable extends FunctionOrVariable {

	private Type type;
	private boolean writable;
	private int shift;

	public Variable() {
		this.type = null;
		this.writable = true;
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

	public void configure(boolean writable) {
		this.writable = writable;
	}

	public boolean isWritable() {
		return this.writable;
	}

}
