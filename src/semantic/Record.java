package semantic;

import misc.Constants;

public class Record extends Type {

	private Namespace<Variable> namespace;

	public Record() {
		this.namespace = new Namespace<Variable>();
	}

	public int getSize() {
		return Constants.pointerSize;
	}

	public Namespace<Variable> getNamespace() {
		return this.namespace;
	}

}
