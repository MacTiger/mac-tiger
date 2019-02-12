package semantic;

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

}
