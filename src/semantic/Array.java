package semantic;

import misc.Constants;

public class Array extends Type {

	private Type type;

	public Array() {
		this.type = null;
	}

	public int getSize() {
		return Constants.pointerSize;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

}