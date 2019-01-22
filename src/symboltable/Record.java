package symboltable;

import misc.Constants;

public class Record extends Type {

	private Namespace namespace;

	public Record() {
		this.namespace = new Namespace();
	}

	public int getSize() {
		return Constants.pointerSize;
	}

	public Namespace getNamespace() {
		return this.namespace;
	}

}
