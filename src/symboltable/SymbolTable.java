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
			// case ":=":
			// case "|":
			// case "&":
			// case "=":
			// case "<>":
			// case ">":
			// case "<":
			// case ">=":
			// case "<=":
			// case "+":
			// case "-":
			// case "*":
			// case "/":
			// case "SEQ"
			// case "ARR"
			// case "REC"
			// case "CALL"
			// case "ITEM"
			// case "FIELD"
			// case "if":
			// case "while":
			case "for": {
				SymbolTable table = new SymbolTable(this);				this.children.add(table);
				Variable iterator = new Variable();
				iterator.setType(null);	// TODO : remplacer "null" par le type primitif "int" déclaré dans la TDS d'ordre 0, on le cherche avec un findType appliqué sur la TDS d'ordre 0
				table.functionsAndVariables.set(tree.getChild(0).toString(), iterator); // Ajout de la variable de boucle for dans sa table de symbole

				this.fillWith(tree.getChild(1));	// Rempli la table des symboles pour la borne inférieure du for
				this.fillWith(tree.getChild(2));	// Rempli la table des symboles pour la borne supérieure du for

				table.fillWith(tree.getChild(3));	// Remplissage de la table des symboles de la boucle for
				break;
			}
			case "let": {
				SymbolTable table = new SymbolTable(this);
				this.children.add(table);
				Tree dec = tree.getChild(0);
				Tree seq = tree.getChild(1);
				for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
					Tree symbol = dec.getChild(i);
					switch (symbol.toString()) {
						case "type": {
							int lj = i;
							int remainingAliases = 0;
							boolean remainsAliases = false;
							boolean remainsArraysAndRecords = false;
							do {
								String name = symbol.getChild(0).toString();
								Tree shape = symbol.getChild(1);
								Type type = null;
								switch (shape.toString()) {
									case "ARRTYPE": {
										type = new Array();
										if (!remainsArraysAndRecords) {
											remainsArraysAndRecords = true;
										}
										break;
									}
									case "RECTYPE": {
										type = new Record();
										if (!remainsArraysAndRecords) {
											remainsArraysAndRecords = true;
										}
										break;
									}
									default: {
										remainingAliases++;
										if (!remainsAliases) {
											remainsAliases = true;
										}
									}
								}
								if (table.types.get(name) != null) {
									/* TODO */
								} else {
									table.types.set(name, type);
								}
							} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
							aliases: while (remainsAliases) {
								remainsAliases = false;
								for (int j = i; j < lj; ++j) {
									symbol = dec.getChild(j);
									String name = symbol.getChild(0).toString();
									Tree shape = symbol.getChild(1);
									Type type = (Type) table.types.get(name);
									if (type == null) {
										type = (Type) this.findType(shape.toString());
										if (type == null) {
											/* TODO */
										} else {
											this.types.set(name, type);
											if (remainingAliases == 1) {
												break aliases;
											} else {
												remainingAliases--;
												if (!remainsAliases) {
													remainsAliases = true;
												}
											}
										}
									}
								}
							}
							if (remainsArraysAndRecords) {
								for (int j = i; j < lj; ++j) {
									symbol = dec.getChild(j);
									String name = symbol.getChild(0).toString();
									Tree shape = symbol.getChild(1);
									Type type = (Type) table.types.get(name);
									if (type instanceof Array) {
										Array array = (Array) type;
										Type itemType = (Type) this.findType(shape.getChild(0).toString());
										if (itemType == null) {
											/* TODO */
										} else {
											array.setType(itemType);
										}
									} else if (type instanceof Record) {
										Record record = (Record) type;
										Namespace namespace = record.getNamespace();
										for (int k = 0, lk = shape.getChildCount(); k < lk; k += 2) {
											String fieldName = shape.getChild(k).toString();
											Variable field = new Variable();
											Type fieldType = (Type) table.findType(shape.getChild(k + 1).toString());
											if (fieldType == null) {
												/* TODO */
											} else {
												field.setType(fieldType);
											}
											if (namespace.get(fieldName) != null) {
												/* TODO */
											} else {
												namespace.set(fieldName, field);
											}
										}
									}
								}
							}
							i = lj - 1;
							break;
						}
						case "function": {
							int lj = i;
							do {
								SymbolTable subTable = new SymbolTable(table);
								table.children.add(subTable);
								String name = symbol.getChild(0).toString();
								Tree callType = symbol.getChild(1);
								Tree type = symbol.getChildCount() > 3 ? symbol.getChild(3) : null;
								Function function = new Function();
								function.setSymbolTable(subTable);
								for (int k = 0, lk = callType.getChildCount(); k < lk; k += 2) {
									String argumentName = callType.getChild(k).toString();
									Variable argument = new Variable();
									Type argumentType = (Type) this.findType(callType.getChild(k + 1).toString());
									if (argumentType == null) {
										/* TODO */
									} else {
										argument.setType(argumentType);
									}
									if (subTable.functionsAndVariables.get(argumentName) != null) {
										/* TODO */
									} else {
										subTable.functionsAndVariables.set(argumentName, argument);
									}
								}
								if (type != null) {
									Type returnType = (Type) this.findType(type.toString());
									if (returnType == null) {
										/* TODO */
									} else {
										function.setType(returnType);
									}
								}
								if (table.functionsAndVariables.get(name) != null) {
									/* TODO */
								} else {
	  								table.functionsAndVariables.set(name, function);
								}
							} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));
							for (int j = i; j < lj; ++j) {
								symbol = dec.getChild(j);
								String name = symbol.getChild(0).toString();
								Tree body = symbol.getChild(2);
								Function function = (Function) table.functionsAndVariables.get(name);
								SymbolTable subTable = function.getSymbolTable();
								subTable.fillWith(body);
							}
							i = lj - 1;
							break;
						}
						case "var": {
							String name = symbol.getChild(0).toString();
							Tree exp = symbol.getChild(1);
							this.fillWith(exp);
							this.functionsAndVariables.set(name, new Variable());
							break;
						}
					}
				}
				this.fillWith(seq);
				break;
			}
			// case "nil":
			// case "break":
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.fillWith(tree.getChild(i));
				}
			}
		}
	}

	private Symbol findType(String name) {
		// recherche le type indiqué dans les tables des symboles supérieures et retourne celui-ci si trouvé ou `null` sinon

		Symbol type = types.get(name);
		if(type != null){	// Si le type est trouvé dans cette table des symboles
			return type;
		}
		else if (parent == null){	// Si le type n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return null;
		}
		else {
			return parent.findType(name);
		}
	}

	private Symbol findFunctionOrVariable(String name) {
		// recherche la fonction ou la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou `null` sinon

		Symbol functionOrVariable = functionsAndVariables.get(name);
		if(functionOrVariable != null){	// Si la fonction ou variable est trouvé dans cette table des symboles
			return functionOrVariable;
		}
		else if (parent == null){	// Si la fonction ou variable n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return null;
		}
		else {
			return parent.findFunctionOrVariable(name);
		}
	}

}
