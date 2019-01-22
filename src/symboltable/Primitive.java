package symboltable;

public class Primitive extends Type {

	private int size;

	public Primitive(int size) {
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

}
