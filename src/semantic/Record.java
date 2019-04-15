package semantic;

public class Record extends Type {

	private Namespace<Variable> namespace;

	public Record() {
		this.namespace = new Namespace<Variable>();
	}

	public Namespace<Variable> getNamespace() {
		return this.namespace;
	}

}
