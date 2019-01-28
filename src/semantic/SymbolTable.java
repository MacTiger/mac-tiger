package semantic;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import misc.Constants;

import static syntactic.TigerParser.ARRTYPE;
import static syntactic.TigerParser.RECTYPE;
import static syntactic.TigerParser.SEQ;
import static syntactic.TigerParser.ARR;
import static syntactic.TigerParser.REC;
import static syntactic.TigerParser.CALL;
import static syntactic.TigerParser.ITEM;
import static syntactic.TigerParser.FIELD;

public class SymbolTable {

	private static Type intType;
	private static Type stringType;
	private static SymbolTable root;

	static {
		Type intType = new Primitive(Constants.intSize);
		Type stringType = new Primitive(Constants.pointerSize);
		Variable intVariable = new Variable();
		intVariable.setType(intType);
		Variable stringVariable = new Variable();
		stringVariable.setType(stringType);
		SymbolTable root = new SymbolTable();
		root.types.set("int", intType);
		root.types.set("string", stringType);
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("chr", function);
		}
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("s1", stringVariable);
			functionsAndVariables.set("s2", stringVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("concat", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("exit", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("flush", function);
		}
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("getchar", function);
		}
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("not", function);
		}
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("s", stringVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("ord", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("s", stringVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("print", function);
		}
		// {
		// 	Function function = new Function();
		// SymbolTable table = new SymbolTable(root);
		// Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
		// 	functionsAndVariables.set("i", intVariable);
		// function.setSymbolTable(table);
		// 	root.functionsAndVariables.set("printi", function);
		// }
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("s", stringVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("size", function);
		}
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("s", stringVariable);
			functionsAndVariables.set("first", intVariable);
			functionsAndVariables.set("n", intVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("substring", function);
		}
		SymbolTable.intType = intType;
		SymbolTable.stringType = stringType;
		SymbolTable.root = root;
	}

	private SymbolTable parent;
	private ArrayList<SymbolTable> children;
	private Namespace<Type> types;
	private Namespace<FunctionOrVariable> functionsAndVariables;

	public SymbolTable() {
		this(SymbolTable.root);
	}

	private SymbolTable(SymbolTable parent) {
		this.parent = parent;
		this.children = new ArrayList<SymbolTable>();
		this.types = new Namespace<Type>();
		this.functionsAndVariables = new Namespace<FunctionOrVariable>();
	}

	public Type fillWith(Tree tree) {
		switch (tree.getType ()) {
			// case SEQ:
			// case ARR:
			// case REC:
			// case CALL:
			// case ITEM:
			// case FIELD:
		}
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
			// case "if":
			// case "while":
			case "for": {
				SymbolTable table = new SymbolTable(this);				this.children.add(table);
				Variable iterator = new Variable();
				iterator.setType(SymbolTable.intType);
				table.functionsAndVariables.set(tree.getChild(0).toString(), iterator); // Ajout de la variable de boucle for dans sa table de symbole
				this.fillWith(tree.getChild(1));	// Rempli la table des symboles pour la borne inférieure du for
				this.fillWith(tree.getChild(2));	// Rempli la table des symboles pour la borne supérieure du for

				table.fillWith(tree.getChild(3));	// Remplissage de la table des symboles de la boucle for
				return null;
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
								switch (shape.getType()) {
									case ARRTYPE: {
										type = new Array();
										if (!remainsArraysAndRecords) {
											remainsArraysAndRecords = true;
										}
										break;
									}
									case RECTYPE: {
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
								}
								table.types.set(name, type);
							} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
							aliases: while (remainsAliases) {
								remainsAliases = false;
								for (int j = i; j < lj; ++j) {
									symbol = dec.getChild(j);
									String name = symbol.getChild(0).toString();
									Tree shape = symbol.getChild(1);
									Type type = table.types.get(name);
									if (type == null) {
										type = this.findType(shape.toString());
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
									Type type = table.types.get(name);
									if (type instanceof Array) {
										Array array = (Array) type;
										Type itemType = this.findType(shape.getChild(0).toString());
										if (itemType == null) {
											/* TODO */
										} else {
											array.setType(itemType);
										}
									} else if (type instanceof Record) {
										Record record = (Record) type;
										Namespace<Variable> namespace = record.getNamespace();
										for (int k = 0, lk = shape.getChildCount(); k < lk; k += 2) {
											String fieldName = shape.getChild(k).toString();
											Variable field = new Variable();
											Type fieldType = table.findType(shape.getChild(k + 1).toString());
											if (fieldType == null) {
												/* TODO */
											} else {
												field.setType(fieldType);
											}
											if (namespace.get(fieldName) != null) {
												/* TODO */
											}
											namespace.set(fieldName, field);
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
									Type argumentType = this.findType(callType.getChild(k + 1).toString());
									if (argumentType == null) {
										/* TODO */
									} else {
										argument.setType(argumentType);
									}
									if (subTable.functionsAndVariables.get(argumentName) != null) {
										/* TODO */
									}
									subTable.functionsAndVariables.set(argumentName, argument);
								}
								if (type != null) {
									Type returnType = this.findType(type.toString());
									if (returnType == null) {
										/* TODO */
									} else {
										function.setType(returnType);
									}
								}
								if (table.functionsAndVariables.get(name) != null) {
									/* TODO */
								}
  								table.functionsAndVariables.set(name, function);
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
				return this.fillWith(seq);
			}
			// case "nil":
			// case "break":
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.fillWith(tree.getChild(i));
				}
				return null;
			}
		}
	}

	private Type findType(String name) {
		// recherche le type indiqué dans les tables des symboles supérieures et retourne celui-ci si trouvé ou `null` sinon
		Type type = types.get(name);
		if (type != null) {	// Si le type est trouvé dans cette table des symboles
			return type;
		} else if (parent != null) {	// Si le type n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return parent.findType(name);
		} else {
			return null;
		}
	}

	private Function findFunction(String name) {
		// recherche la fonction ou la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou `null` sinon
		FunctionOrVariable functionOrVariable = this.functionsAndVariables.get(name);
		if (functionOrVariable != null) {	// Si la fonction ou variable est trouvé dans cette table des symboles
			if (functionOrVariable instanceof Function) {
				return (Function) functionOrVariable;
			} else {
				return null;
			}
		} else if (this.parent != null) {	// Si la fonction ou variable n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return this.parent.findFunction(name);
		} else {
			return null;
		}
	}

	private Variable findVariable(String name) {
		// recherche la fonction ou la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou `null` sinon
		FunctionOrVariable functionOrVariable = this.functionsAndVariables.get(name);
		if (functionOrVariable != null) {	// Si la fonction ou variable est trouvé dans cette table des symboles
			if (functionOrVariable instanceof Variable) {
				return (Variable) functionOrVariable;
			} else {
				return null;
			}
		} else if (this.parent != null) {	// Si la fonction ou variable n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return this.parent.findVariable(name);
		} else {
			return null;
		}
	}

}