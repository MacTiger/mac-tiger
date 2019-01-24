package symboltable;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

public class SymbolTable {

	private SymbolTable parent;
	private ArrayList<SymbolTable> children;
	private Namespace types;
	private Namespace functionsAndVariables;

	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
		this.children = new ArrayList<SymbolTable>();
		this.types = new Namespace();
		this.functionsAndVariables = new Namespace();
	}

	public void fillWith(Tree tree) {
		switch (tree.toString()) {
			case "for": {
				SymbolTable table = new SymbolTable(this);
				this.children.add(table);
				break;
			}
			case "let": {
				SymbolTable table = new SymbolTable(this);
				this.children.add(table);
				Tree dec = tree.getChild(0);
				Tree seq = tree.getChild(1);
				for (int i = 0, li = dec.getChildCount(); i < li; i++) {
					Tree symbol = dec.getChild(i);
					Tree key = symbol.getChild(0);
					switch (symbol.toString()) {
						case "type": {
							Tree value = symbol.getChild(1);
							Type type = null;
							if (value.getChildCount() > 0) {
								switch (value.toString()) {
									case "ARRTYPE": {
										type = new Array();
										break;
									}
									case "RECTYPE": {
										type = new Record();
										Namespace namespace = ((Record) type).getNamespace();
										for (int k = 1, lk = value.getChildCount(); k < lk; k += 2) {
											namespace.set(value.getChild(i).toString(), null);
										}
										break;
									}
								}
							}
							this.types.set(key.toString(), type);
							int j = i;
							while (i++ < li && (symbol = dec.getChild(i)).toString().equals("type")) {
								key = symbol.getChild(0);
								value = symbol.getChild(1);
								type = null;
								if (value.getChildCount() > 0) {
									switch (value.toString()) {
										case "ARRTYPE": {
											type = new Array();
											break;
										}
										case "RECTYPE": {
											type = new Record();
											Namespace namespace = ((Record) type).getNamespace();
											for (int k = 1, lk = value.getChildCount(); k < lk; k += 2) {
												namespace.set(value.getChild(i).toString(), null);
											}
											break;
										}
									}
								}
								this.types.set(key.toString(), type);
							}
							for (; j < i; j++) {
								this.fillWith(dec.getChild(j));
							}
							break;
						}
						case "function": {
							this.functionsAndVariables.set(key.toString(), new Function());
							int j = i;
							while (i++ < li && (symbol = dec.getChild(i)).toString().equals("function")) {
								this.functionsAndVariables.set(symbol.getChild(0).toString(), new Function());
							}
							for (; j < i; j++) {
								this.fillWith(dec.getChild(j));
							}
							break;
						}
						case "var": {
							this.fillWith(symbol);
							this.functionsAndVariables.set(key.toString(), new Variable(0));
							break;
						}
					}
				}
				this.fillWith(seq);
				break;
			}
			case "type": {
				Tree alias = tree.getChild(0);
				Tree shape = tree.getChild(1);
				Type type = (Type) this.types.get(alias.toString());
				if (type == null) {
					type = (Type) this.findType(shape.getChild(0).toString());
					if (type == null) {
						/* TODO */
					} else {
						this.types.set(alias.toString(), type);
					}
				} else if (type instanceof Array) {
					Array array = (Array) type;
					Type itemType = array.getType();
					if (itemType == null) {
						itemType = (Type) this.findType(shape.getChild(0).toString());
						if (itemType == null) {
							/* TODO */
						} else {
							array.setType(itemType);
						}
					}
				} else if (type instanceof Record) {
					Record record = (Record) type;
					Namespace namespace = record.getNamespace();
					for (int i = 1, l = shape.getChildCount(); i < l; i += 2) {
						Tree key = shape.getChild(i);
						Tree value = shape.getChild(i + 1);
						Variable field = (Variable) namespace.get(key.toString());
						Type fieldType = field.getType();
						if (fieldType == null) {
							fieldType = (Type) this.findType(value.toString());
							if (fieldType == null) {
								/* TODO */
							} else {
								field.setType(fieldType);
							}
						}
					}
				}
				break;
			}
			case "function": {
				SymbolTable table = new SymbolTable(this);
				this.children.add(table);
				Tree name = tree.getChild(0);
				Tree callType = tree.getChild(1);
				Tree body = tree.getChild(2);
				Tree returnType = tree.getChildCount() > 3 ? tree.getChild(3) : null;
				Function function = (Function) this.functionsAndVariables.get(name.toString()); /* TODO */
				table.functionsAndVariables = function.getNamespace();
				for (int i = 0, l = callType.getChildCount(); i < l; i += 2) {
					Tree key = callType.getChild(i);
					// Tree value = callType.getChild(i + 1);
					Variable argument = new Variable(0);
					// argument.setType();
					table.functionsAndVariables.set(key.toString(), argument);
				}
				if (returnType != null) {
					// function.setType(returnType);
				}
				/* TODO */
				this.fillWith(body);
				break;
			}
			case "var": {
				break;
			}
			default: {
				for (int i = 0, l = tree.getChildCount(); i < l; i++) {
					this.fillWith(tree.getChild(i));
				}
			}
		}
	}

	private Symbol findType(String name) {
		// recherche le type indiqué dans les tables des symboles supérieures et retourne celui-ci si trouvé ou `null` sinon
		return null;
	}

	private Symbol findFunctionOrVariable(String name) {
		// recherche la fonction ou la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou `null` sinon
		return null;
	}

}
