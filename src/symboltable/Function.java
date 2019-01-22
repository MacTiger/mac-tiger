package symboltable;

public class Function extends Symbol {

	private Type type;
	private Namespace namespace;

	public Function() {
		this.type = null;
		this.namespace = new Namespace();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public Namespace getNamespace() {
		return this.namespace;
	}

}
