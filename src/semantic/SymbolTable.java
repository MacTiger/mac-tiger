package semantic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

import misc.Constants;
import misc.Notifier;

import static syntactic.TigerParser.ARRTYPE;
import static syntactic.TigerParser.RECTYPE;
import static syntactic.TigerParser.SEQ;
import static syntactic.TigerParser.ARR;
import static syntactic.TigerParser.REC;
import static syntactic.TigerParser.CALL;
import static syntactic.TigerParser.ITEM;
import static syntactic.TigerParser.FIELD;
import static syntactic.TigerParser.ID;
import static syntactic.TigerParser.STR;
import static syntactic.TigerParser.INT;

public class SymbolTable {

	private static Type nilPseudoType;
	private static Type intType;
	private static Type stringType;
	private static SymbolTable root;

	static {
		Type nilPseudoType = new Record();
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
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.functionsAndVariables;
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.functionsAndVariables.set("printi", function);
		}
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
		SymbolTable.nilPseudoType = nilPseudoType;
		SymbolTable.intType = intType;
		SymbolTable.stringType = stringType;
		SymbolTable.root = root;
	}

	private SymbolTable parent;
	private List<SymbolTable> children;
	private Namespace<Type> types;
	private Namespace<FunctionOrVariable> functionsAndVariables;

	public SymbolTable getParent() {
		return parent;
	}

	public List<SymbolTable> getChildren() {
		return children;
	}

	public Namespace<Type> getTypes() {
		return types;
	}

	public Namespace<FunctionOrVariable> getFunctionsAndVariables() {
		return functionsAndVariables;
	}

	public SymbolTable() {
		this(SymbolTable.root);
	}

	private SymbolTable(SymbolTable parent) {
		this.parent = parent;
		this.children = new ArrayList<SymbolTable>();
		this.types = new Namespace<Type>();
		this.functionsAndVariables = new Namespace<FunctionOrVariable>();
	}

	private Type findType(String name) {
		// recherche le type indiqué dans les tables des symboles supérieures et retourne celui-ci si trouvé ou 'null' sinon
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
		// recherche la fonction indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou 'null' sinon
		FunctionOrVariable functionOrVariable = this.functionsAndVariables.get(name);
		if (functionOrVariable != null) {	// Si la fonction est trouvé dans cette table des symboles
			if (functionOrVariable instanceof Function) {
				return (Function) functionOrVariable;
			} else {
				return null;
			}
		} else if (this.parent != null) {	// Si la fonction n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return this.parent.findFunction(name);
		} else {
			return null;
		}
	}

	private Variable findVariable(String name) {
		// recherche la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou 'null' sinon
		FunctionOrVariable functionOrVariable = this.functionsAndVariables.get(name);
		if (functionOrVariable != null) {	// Si la variable est trouvé dans cette table des symboles
			if (functionOrVariable instanceof Variable) {
				return (Variable) functionOrVariable;
			} else {
				return null;
			}
		} else if (this.parent != null) {	// Si la variable n'est pas trouvé ici, on cherche dans la table parent, si elle existe
			return this.parent.findVariable(name);
		} else {
			return null;
		}
	}

	public Type fillWith(Tree tree, Notifier notifier) {
		switch (tree.getType ()) {
			case SEQ: return this.fillWithSEQ(tree, notifier);
			case ARR: return this.fillWithARR(tree, notifier);
			case REC: return this.fillWithREC(tree, notifier);
			case CALL: return this.fillWithCALL(tree, notifier);
			case ITEM: return this.fillWithITEM(tree, notifier);
			case FIELD: return this.fillWithFIELD(tree, notifier);
			case ID: return this.fillWithID(tree, notifier);
			case STR: return this.fillWithSTR(tree, notifier);
			case INT: return this.fillWithINT(tree, notifier);
		}
		switch (tree.toString()) {
			case ":=": return this.fillWithAssignment(tree, notifier);
			case "=":
			case "<>": return this.fillWithEqualOrNot(tree, notifier);
			case ">":
			case "<":
			case ">=":
			case "<=": return this.fillWithComparator(tree, notifier);
			// "|", "&", "+", "-", "*" et "/" ont tous le même comportement pour les types de leurs opérandes :
			case "|":
			case "&":
			case "+":
			case "-":
			case "*":
			case "/": return this.fillWithIntOperator(tree, notifier);
			case "if": return this.fillWithIf(tree, notifier);
			case "while": return this.fillWithWhile(tree, notifier);
			case "for": return this.fillWithFor(tree, notifier);
			case "let": return this.fillWithLet(tree, notifier);
			case "nil": return this.fillWithNil(tree, notifier);
			case "break": return this.fillWithBreak(tree, notifier);
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.fillWith(tree.getChild(i), notifier);
				}
				return null;
			}
		}
	}

	private Type fillWithCALL(Tree tree, Notifier notifier) {
		/* Appel d'une fonction
		 * (1) Vérification de l'existence de la fonction
		 * (2) Vérification du nombre d'arguments
		 * (3) Vérification des types d'arguments
		 */
 		String name = tree.getChild(0).toString();
		Function function = this.findFunction(name);
		Type returnType = null;
		int i = 1;
		int l = tree.getChildCount();
		if (function == null) { // Test sémantique (1)
			notifier.semanticError(tree, "function %s is not defined", name);
		} else {
			Namespace<FunctionOrVariable> namespace = function.getSymbolTable().functionsAndVariables;
			for (Map.Entry<String, FunctionOrVariable> entry: namespace) {
				if (i >= l) { // Test sémantique (2)
					notifier.semanticError(tree, "%s requires more arguments", name);
					break;
				}
				this.checkType(tree.getChild(i), notifier, ((Variable) entry.getValue()).getType()); // Test sémantique (3)
				++i;
			}
			if (i < l) { // Test sémantique (2)
				notifier.semanticError(tree, "%s requires fewer arguments", name);
			}
			returnType = function.getType();
		}
		for (; i < l; ++i) {
			this.fillWith(tree.getChild(i), notifier);
		}
		return returnType;
	}

	private Type fillWithREC(Tree tree, Notifier notifier) {
		/* Déclaration d'une structure
		 * (1) Vérification de l'existence du type de structure
		 * (2) Vérification du nombre de champs
		 * (3) Vérification des noms des champs
		 * (4) Vérification des types des champs
		 */
		String name = tree.getChild(0).toString();
		Type returnType = this.findType(name);
		int i = 1;
		int l = tree.getChildCount();
		if (returnType == null) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Record)) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not a record type", name);
			returnType = null;
		} else {
			Record record = (Record) returnType;
			Namespace<Variable> namespace = record.getNamespace();
			for (Map.Entry<String, Variable> entry: namespace) {
				if (i >= l) { // Test sémantique (2)
					notifier.semanticError(tree, "%s requires more fields", name);
					break;
				}
				if (!tree.getChild(i).toString().equals(entry.getKey())) { // Test sémantique (3)
					notifier.semanticError(tree, "field name %s was expected but field name %s was found", entry.getKey(), tree.getChild(i).toString());
				}
				this.checkType(tree.getChild(i + 1), notifier, entry.getValue().getType()); // Test sémantique (4)
				i += 2;
			}
			if (i < l) { // Test sémantique (2)
				notifier.semanticError(tree, "%s requires fewer fields", name);
			}
		}
		for (; i < l; i += 2) {
			this.fillWith(tree.getChild(i + 1), notifier);
		}
		return returnType;
	}

	private Type fillWithARR(Tree tree, Notifier notifier) {
		/* Déclaration d'un tableau
		 * (1) Vérification de l'existence du type de tableau
		 * (2) Vérification du type de la taille du tableau
		 * (3) Vérification du type des éléments du tableau
		 */
		String name = tree.getChild(0).toString();
		Type returnType = this.findType(name);
		if (returnType == null) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Array)) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not an array type", name);
			returnType = null;
		}
		this.checkType(tree.getChild(1), notifier, SymbolTable.intType); // Test sémantique (2)
		if (returnType != null) {
			Array array = (Array) returnType;
			this.checkType(tree.getChild(2), notifier, array.getType()); // Test sémantique (3)
		}
		return returnType;
	}

	private Type fillWithAssignment(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		switch (exp.getType()) { // vérification que l'expression à gauche du ":=" est un lValue (cherche si le token de 'exp' est bien parmis les tokens imaginaires ANTLR correspondant à un lValue)
			case ID:
			case ITEM:
			case FIELD: {
				if (exp.getType() == ID) {
					Variable variable = this.findVariable(exp.toString());
					if (variable != null && !variable.isWritable()) {
						notifier.semanticError(exp, "loop index %s cannot be assigned", exp.toString());
						this.fillWith(tree.getChild(1), notifier);
						break;
					}
				}
				this.checkType(tree.getChild(1), notifier, this.fillWith(exp, notifier)); // vérification de cohérence de type entre l'expression gauche et droite
				break;
			}
			default: {
				notifier.semanticError(exp, "%s is not a valid target for an assignment", exp.toString());
				this.fillWith(tree.getChild(1), notifier);
			}
		}
		return null;
	}

	private Type fillWithSEQ(Tree tree, Notifier notifier) {
		Type returnType = null;
		for (int i = 0, l = tree.getChildCount(); i < l; ++i) {
			returnType = this.fillWith(tree.getChild(i), notifier);
		}
		return returnType;
	}

	private Type fillWithSTR(Tree tree, Notifier notifier) {
		return SymbolTable.stringType;
	}

	private Type fillWithID(Tree tree, Notifier notifier) {
		Variable variable = this.findVariable(tree.toString());
		if (variable == null) {
			notifier.semanticError(tree, "variable %s is not defined", tree.toString());
		} else{
			return variable.getType();
		}
		return null;
	}

	private Type fillWithITEM(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		Type returnType = null;
		if (!(expType instanceof Array)) { // on regarde si le fils gauche est bien un tableau
			notifier.semanticError(exp, "%s is not an array", exp.toString());
		} else { // on sait qu'on a bien un tableau
			Array array = (Array) expType;
			returnType = array.getType(); // on retourne le type des éléments stockés dans le tableau
		}
		this.checkType(tree.getChild(1), notifier, SymbolTable.intType); // on regarde si le fils droit est bien un entier
		return returnType;
	}

	private Type fillWithFIELD(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		if(!(expType instanceof Record)) { // on regarde si le fils gauche est bien une structure
			notifier.semanticError(exp, "%s is not a record", exp.toString());
		} else { // on sait qu'on a bien une structure
			Record record = (Record) expType;
			Namespace<Variable> fields = record.getNamespace();
			if (fields.get(tree.getChild(1).toString()) == null) { // on regarde si le champ existe
				notifier.semanticError(tree, "field %s is not defined", tree.getChild(1).toString());
			} else { // sinon c'est bon
				return fields.get(tree.getChild(1).toString()).getType();
			}
		}
		return null;
	}

	private Type fillWithINT(Tree tree, Notifier notifier) {
		return SymbolTable.intType;
	}

	private Type fillWithEqualOrNot(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		if (this.checkType(tree.getChild(1), notifier, expType) == SymbolTable.nilPseudoType) {
			notifier.semanticError(exp, "the type of %s cannot be inferred", exp.toString());
		}
		return SymbolTable.intType;
	}

	private Type fillWithComparator(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		if ((expType != SymbolTable.intType) && (expType != SymbolTable.stringType)) {
			notifier.semanticError(exp, "the type of %s is not a primitive type (%s or %s)");
			exp = tree.getChild(1);
			expType = this.fillWith(exp, notifier);
			if ((expType != SymbolTable.intType) && (expType != SymbolTable.stringType)) {
				notifier.semanticError(exp, "%s is not a primitive value (either an integer or a string)");
			}
		} else {
			exp = tree.getChild(1);
			expType = this.checkType(exp, notifier, expType);
		}
		return SymbolTable.intType;
	}

	private Type fillWithIntOperator(Tree tree, Notifier notifier) {
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			this.checkType(tree.getChild(i), notifier, SymbolTable.intType);
		}
		return SymbolTable.intType;
	}

    private Type checkType(Tree tree, Notifier notifier, Type type) {
		// Vérifie que le type de l'expression de 'tree' est bien 'type'
		// Si le type de 'tree' n'est pas 'type', alors `null` est renvoyé
		// Sinon le type de 'tree' est renvoyé
        Type expType = this.fillWith(tree, notifier);
        if (expType == type || expType == SymbolTable.nilPseudoType && type instanceof Record) {
            return type;
        } else if (type == SymbolTable.nilPseudoType && expType instanceof Record) {
            return expType;
        } else {
            notifier.semanticError(tree, "type of %s does not match", tree.toString());
	        return null;
        }
    }

	private Type fillWithIf(Tree tree, Notifier notifier) {
		this.checkType(tree.getChild(0), notifier, SymbolTable.intType);
		if (tree.getChildCount() > 2) {
			return this.checkType(tree.getChild(2), notifier, this.fillWith(tree.getChild(1), notifier));
		} else {
			return this.checkType(tree.getChild(1), notifier, null);
		}
	}

	private Type fillWithWhile(Tree tree, Notifier notifier) {
		this.checkType(tree.getChild(0), notifier, SymbolTable.intType);
		this.checkType(tree.getChild(1), notifier, null);
		return null;
	}

	private Type fillWithFor(Tree tree, Notifier notifier) {
		SymbolTable table = new SymbolTable(this);
		this.children.add(table);
		Variable index = new Variable();
		index.configure(false);
		index.setType(SymbolTable.intType);
		table.functionsAndVariables.set(tree.getChild(0).toString(), index); // Ajout de la variable de boucle for dans sa table de symbole
		this.checkType(tree.getChild(1), notifier, SymbolTable.intType);	// Rempli la table des symboles pour la borne inférieure du for
		this.checkType(tree.getChild(2), notifier, SymbolTable.intType);	// Rempli la table des symboles pour la borne supérieure du for
		table.checkType(tree.getChild(3), notifier, null);	// Remplissage de la table des symboles de la boucle for
		return null;
	}

	private Type fillWithLet(Tree tree, Notifier notifier) {
		SymbolTable supTable = this;
		SymbolTable table = this;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		int variableOffset = 0;
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": {
					table = new SymbolTable(supTable);
					supTable.children.add(table);
					supTable = table;
					variableOffset = 0;
					int lj = i;
					int remainingAliases = 0;
					boolean remainsAliases = false;
					boolean remainsArraysAndRecords = false;
					do {
						Type type = null;
						String name = symbol.getChild(0).toString();
						if (table.types.has(name)) {
							notifier.semanticError(symbol.getChild(0), "redeclaration of type %s", name);
						}
						Tree shape = symbol.getChild(1);
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
								type = table.findType(shape.toString());
								if (type != null) {
									table.types.set(name, type);
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
					if (!remainsAliases || remainsArraysAndRecords) {
						for (int j = i; j < lj; ++j) {
							symbol = dec.getChild(j);
							String name = symbol.getChild(0).toString();
							Tree shape = symbol.getChild(1);
							Type type = table.types.get(name);
							if (type instanceof Array) {
								Array array = (Array) type;
								Type itemType = table.findType(shape.getChild(0).toString());
								if (itemType == null) {
									notifier.semanticError(shape.getChild(0), "type %s is not defined", shape.getChild(0).toString());
								} else {
									array.setType(itemType);
								}
							} else if (type instanceof Record) {
								Record record = (Record) type;
								Namespace<Variable> namespace = record.getNamespace();
								int fieldOffset = 0;
								for (int k = 0, lk = shape.getChildCount(); k < lk; k += 2) {
									Variable field = new Variable();
									String fieldName = shape.getChild(k).toString();
									if (namespace.has(fieldName)) {
										notifier.semanticError(shape.getChild(k), "redeclaration of field %s", fieldName);
									}
									Type fieldType = table.findType(shape.getChild(k + 1).toString());
									if (fieldType == null) {
										notifier.semanticError(shape.getChild(k + 1), "type %s is not defined", shape.getChild(k + 1).toString());
									} else {
										field.setType(fieldType);
									}
									field.setOffset(fieldOffset);
									if (fieldType != null) {
										fieldOffset += fieldType.getSize();
									}
									namespace.set(fieldName, field);
								}
							} else if (type == null) {
								notifier.semanticError(symbol.getChild(0), "circular definition of type %s", name);
							}
						}
					}
					i = lj - 1;
					break;
				}
				case "function": {
					table = new SymbolTable(supTable);
					supTable.children.add(table);
					supTable = table;
					variableOffset = 0;
					int lj = i;
					do {
						SymbolTable subTable = new SymbolTable(table);
						table.children.add(subTable);
						Function function = new Function();
						function.setSymbolTable(subTable);
						String name = symbol.getChild(0).toString();
						if (table.functionsAndVariables.has(name)) {
							notifier.semanticError(symbol.getChild(0), "redeclaration of function %s", name);
						}
						Tree callType = symbol.getChild(1);
						int argumentOffset = 0;
						for (int k = 0, lk = callType.getChildCount(); k < lk; k += 2) {
							Variable argument = new Variable();
							String argumentName = callType.getChild(k).toString();
							if (subTable.functionsAndVariables.has(argumentName)) {
								notifier.semanticError(callType.getChild(k), "redeclaration of parameter %s", argumentName);
							}
							Type argumentType = table.findType(callType.getChild(k + 1).toString());
							if (argumentType == null) {
								notifier.semanticError(callType.getChild(k + 1), "type %s is not defined", callType.getChild(k + 1).toString());
							} else {
								argument.setType(argumentType);
							}
							argument.setOffset(argumentOffset);
							if (argumentType != null) {
								argumentOffset += argumentType.getSize();
							}
							subTable.functionsAndVariables.set(argumentName, argument);
						}
						for (Map.Entry<String, FunctionOrVariable> entry: subTable.functionsAndVariables) {
							Variable argument = (Variable) entry.getValue();
							argument.setOffset(argument.getOffset() - argumentOffset);
						}
						Tree type = symbol.getChildCount() > 3 ? symbol.getChild(3) : null;
						if (type != null) {
							Type returnType = table.findType(type.toString());
							if (returnType == null) {
								notifier.semanticError(type, "type %s is not defined", type.toString());
							} else {
								function.setType(returnType);
							}
						}
						table.functionsAndVariables.set(name, function);
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));
					for (int j = i; j < lj; ++j) {
						symbol = dec.getChild(j);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = (Function) table.functionsAndVariables.get(name);
						SymbolTable subTable = function.getSymbolTable();
						subTable.fillWith(body, notifier);
					}
					i = lj - 1;
					break;
				}
				case "var": {
					Variable variable = new Variable(); // Création de la variable déclarée
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					Tree type = symbol.getChildCount() > 2 ? symbol.getChild(2) : null;
					Type returnType;
					// Détermination du type de la variable : il est soit renseigné dans la déclaration, soit par inférence
					if (type != null) {	// Cas où le type de la variable est renseigné dans la déclaration
						returnType = table.findType(type.toString());
						if (returnType == null) {
							notifier.semanticError(type, "type %s is not defined", type.toString());
							returnType = table.fillWith(exp, notifier);
						} else {
							table.checkType(exp, notifier, returnType);
						}
					} else {	// Cas où le type de la variable doit être déduit par inférence sur le type de l'expression à droite du ':='
						returnType = table.fillWith(exp, notifier);
						if (returnType == null || returnType == SymbolTable.nilPseudoType) {
							notifier.semanticError(exp, "the type of %s cannot be inferred", name);
						}
					}
					variable.setType(returnType); // Spécification du type de la variable
					if (table == this || table.functionsAndVariables.get(name) != null) {
						table = new SymbolTable(supTable);
						supTable.children.add(table);
						supTable = table;
						variableOffset = 0;
					}
					variable.setOffset(variableOffset);
					if (returnType != null) {
						variableOffset += returnType.getSize();
					}
					table.functionsAndVariables.set(name, variable);
					break;
				}
			}
		}
		return table.fillWith(seq, notifier);
	}

	private Type fillWithNil(Tree tree, Notifier notifier) {
		return SymbolTable.nilPseudoType;
	}

	private Type fillWithBreak(Tree tree, Notifier notifier) {
		Tree parent = tree;
		Tree child = tree;
		while ((parent = parent.getParent()) != null && !parent.toString().equals("function")) {
			if (parent.toString().equals("while") || parent.toString().equals("for") && child == parent.getChild(3)) {
				return null;
			} else {
				child = parent;
			}
		}
		notifier.semanticError(tree, "%s must be inside loop", tree.toString());
		return null;
	}

	public String toGraphVizFirst(){
		// Fonction créant le String qui permettra de générer l'illustration de la TDS
		// Elle appelle toGraphViz
		String res = "digraph structs {\nrankdir=LR;\n";

		ArrayList<String> resArray = this.toGraphViz(0,0);
		res += resArray.get(0) + resArray.get(1) + resArray.get(2);

		res += "\n}";
		return res;
	}

	private String nameOfTDS(int depth, int num_TDS){   //TODO : la profondeur et le numéro ne suffisent pas à identifier de manière unique des TDS !
		return "TDS_" + depth + "_" + num_TDS;
	}

	public ArrayList<String> toGraphViz(int depth, int num_TDS){
		// Renvoit : [graph, graphChildren, graphLinks]

		String graph =  nameOfTDS(depth,num_TDS)+ "[shape=record, label=\""; // Résultat à afficher
		String linksOfGraph = "";   // Partie de graph qui servira à la liaison avec les autres TDS
		String typesGraph = "";          // Partie de graph qui contiendra les types
		String varFuncGraph = "";        // Partie de graph qui contiendra les fonctions et variables

		String graphChildren = "";  // String des graphiques des TDS filles de cette TDS
		String graphLinks = ""; // String des liaisons entre TDS


		int i = 0;

		linksOfGraph += "{<parent>}";   // Prévoit le point d'ancrage du lient vers son père

		for (SymbolTable symbolTable : this.children){ // Parcours des TDS filles de cette TDS  //TODO : Relier les TDS des fonctions aux fonctions de cette TDS
			ArrayList symbolTableGraph = symbolTable.toGraphViz(depth+1, i);
			// Récupère les graphes créés par la fille symbolTable :
			graphLinks += "\n" + symbolTableGraph.get(2) ;  // Ajout des liaisons créées par symbolTable
			graphChildren += "\n" + symbolTableGraph.get(0) + "\n" + symbolTableGraph.get(1);   // Ajout du graph de symbolTable et de ces enfants

			// Ajout des liens entre cette TDS et sa fille symbolTable
			linksOfGraph += " | {<"+ nameOfTDS(depth+1,i) + ">} ";
			graphLinks+= "\"" + nameOfTDS(depth+1,i) + "\":" + "parent" + " -> \"" + nameOfTDS(depth,num_TDS) + "\":"+nameOfTDS(depth+1,i) + ";";
			i++;
		}

		Variable var = null;
		String partOfGraph =""; // String auxiliaire pour la création des divers cellules de la TDS
		i =0;

		for (Map.Entry<String, FunctionOrVariable> stringSymbolEntry : this.functionsAndVariables){ // Parcours des fonctions et variables déclarées dans cette TDS
			partOfGraph = "";
			if (i > 0){
				varFuncGraph+=" | ";
			}
			if (stringSymbolEntry.getValue() instanceof Variable){
				var = (Variable) stringSymbolEntry.getValue();
				partOfGraph += stringSymbolEntry.getKey() + "|" + var.getType().toString() + "|" + var.getOffset(); //TODO : liaison avec les Types
			}
			//TODO : Traiter le cas des Function
			varFuncGraph += "{" + partOfGraph + "}";
			i++;
		}

		for (Map.Entry<String, Type> stringTypeEntry : this.types){ // Parcours des types déclarées dans cette TDS  //TODO : cellule des Types

		}

		// Gestion des séparation entres les différentes parties de graph
		if( !(typesGraph.equals("")) ){
			linksOfGraph += " | ";
		}
		if ( !(varFuncGraph.equals("")) ){
			typesGraph += " | ";
		}
		graph += linksOfGraph + typesGraph + varFuncGraph;  // Assemblage des parties de graph
		graph += "\"]";
		return new ArrayList<>(Arrays.asList(graph, graphChildren, graphLinks));
	}

}
