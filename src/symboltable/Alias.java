package symboltable;

public class Alias extends Type {

	private Type type;

	public Alias() {
		this.type = null;
	}

	public int getSize() {
		return this.type != null ? this.type.getSize() : 0;
	}

	public void setType(Type type) {
		while (type instanceof Alias) {
			type = ((Alias) type).getType();
		}
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

}
