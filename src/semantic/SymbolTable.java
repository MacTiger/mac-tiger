package semantic;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

import misc.Constants;
import misc.Helpers;

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
		// recherche la fonction indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou `null` sinon
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
		// recherche la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou `null` sinon
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

	public Type fillWith(Tree tree) {
		switch (tree.getType ()) {
			// case SEQ:
			// case ARR:
			// case REC:
			// case CALL:
			// case ITEM:
			// case FIELD:
			// case ID:
			case STR: return this.fillWithSTR(tree);
			case INT: return this.fillWithINT(tree);
		}
		Type expType;
		switch (tree.toString()) {

			// case ":=":		//TODO : définition de type par inférence de l'expression à droite du =

             case "=":
             case "<>": return fillWithEqualOrNot(tree);

             case ">":
             case "<":
             case ">=":
             case "<=": return this.fillWithComparator(tree);


            // "+", "-", "*", "/", "&" et "|" ont tous le même comportement pour les types de leurs opérandes :
            case "+":
            case "-":
            case "*":
            case "/":
            case "|":
            case "&": return this.fillWithIntOperator(tree);

			// case "if":
			// case "while":
			case "for": return this.fillWithFor(tree);
			case "let": return this.fillWithLet(tree);
			// case "nil":
			case "break": return this.fillWithBreak(tree);
			default: { /* TODO: à retirer lorsque tous les contrôles sémantiques seront faits */
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.fillWith(tree.getChild(i));
				}
				return null;
			}
		}
	}

	private Type fillWithSTR(Tree tree) {
		return SymbolTable.stringType;
	}

	private Type fillWithINT(Tree tree) {
		return SymbolTable.intType;
	}

	private Type fillWithEqualOrNot(Tree tree){
		Type expType = fillWith(tree.getChild(0));
		checkType(tree.getChild(1),expType, false);
		return SymbolTable.intType;
	}

	private Type fillWithComparator(Tree tree){
		Type expType = fillWith(tree.getChild(0));
		if ((expType != SymbolTable.intType) && (expType != SymbolTable.stringType)){
			Helpers.alert(tree.getChild(0), "les types non primitifs (ie : autre que int et string) ne sont pas acceptés pour cet opérateur : "+ tree.getText());
		}
		Type secondType = checkType(tree.getChild(1),expType, true);
		if ((secondType != SymbolTable.intType) && (secondType != SymbolTable.stringType)){
			Helpers.alert(tree.getChild(1), "les types non primitifs (ie : autres que int et string) ne sont pas acceptés pour cet opérateur : "+ tree.getText());
		}
		return SymbolTable.intType;
	}

	private Type fillWithIntOperator(Tree tree){
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			this.checkType(tree.getChild(i), SymbolTable.intType, false);
		}
		return SymbolTable.intType;
	}

	private Type fillWithNAire(Tree tree, Type operandsType){
		/* Complète la TDS pour un opérateur n-aire ayant par défaut les propriétés suivantes :
		* - les types des opérandes doivent être identiques
		* - le type du résultat est toujours du même type que celui des opérandes, sauf en cas d'erreur sémantique : il vaudra null
		* Le paramètre operandsType indique le type accepté pour les opérandes : s'il vaut null, alors on détermine le type des opérandes par inférence.
		* 	C'est à dire que le type du premier opérande déterminera le type que les autres opérandes doivent suivre.
		*
		* Si l'un des opérandes n'est pas du type operandsType, on renvoit le type null
		* */

		int i = 0;
		if (operandsType == null){	// Gère cas où le type est déterminé par inférence
			operandsType = fillWith(tree.getChild(0));
			i++;
		}

		Tree operand = null;

		for (int li = tree.getChildCount(); i < li; ++i){
			operand = tree.getChild(i);
			if (! (this.fillWith(operand) == operandsType)){	// Si le type du ieme opérande n'est pas operandsType
				Helpers.alert(operand,"types de "+ operand.getText() +" non consistant dans l'opération : " + tree.toString());	// TODO : afficher l'expression de tree ?
				return null;
			}
		}
		return operandsType;
	}

    private Type checkType(Tree tree, Type type, boolean returnTreeType) {
		// Vérifie que le type de l'expression de 'tree' est bien 'type'
	    // Si 'type' vaut null, alors le type de 'tree' est renvoyé
	    // Si le type de 'tree' n'est pas 'type' :
	    // - null est renvoyé si 'returnTreeType' est faux
	    // - le type de l'expression de 'tree' est renvoyé sinon

        Type expType = this.fillWith(tree);
        if (type == null) {
            return expType;
        } else if (type == expType) {
            return type;
        } else {
            Helpers.alert(tree, tree.getText() +" est de type différent de celui attendu.");
            if (returnTreeType) {
	            return expType;
            }
	        return null;
        }
    }

	private Type fillWithFor(Tree tree) {
		SymbolTable table = new SymbolTable(this);				this.children.add(table);
		Variable iterator = new Variable();
		iterator.setType(SymbolTable.intType);
		table.functionsAndVariables.set(tree.getChild(0).toString(), iterator); // Ajout de la variable de boucle for dans sa table de symbole
		this.fillWith(tree.getChild(1));	// Rempli la table des symboles pour la borne inférieure du for
		this.fillWith(tree.getChild(2));	// Rempli la table des symboles pour la borne supérieure du for
		table.fillWith(tree.getChild(3));	// Remplissage de la table des symboles de la boucle for
		return null;
	}

	private Type fillWithLet(Tree tree) {
		SymbolTable supTable = this;
		SymbolTable table = this;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": {
					table = new SymbolTable(supTable);
					supTable.children.add(table);
					supTable = table;
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
							Helpers.alert(symbol.getChild(0), "redéclaration du type `" + name + "`");
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
									Helpers.alert(shape.getChild(0), "type `" + shape.getChild(0).toString() + "` non déclaré");
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
										Helpers.alert(shape.getChild(k + 1), "type `" + shape.getChild(k + 1).toString() + "` non déclaré");
									} else {
										field.setType(fieldType);
									}
									if (namespace.get(fieldName) != null) {
										Helpers.alert(shape.getChild(k), "redéclaration du champ `" + fieldName + "`");
									}
									namespace.set(fieldName, field);
								}
							} else if (type == null) {
								Helpers.alert(symbol.getChild(0), "définition cyclique du type `" + name + "`");
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
							Type argumentType = table.findType(callType.getChild(k + 1).toString());
							if (argumentType == null) {
								Helpers.alert(callType.getChild(k + 1), "type `" + callType.getChild(k + 1).toString() + "` non déclaré");
							} else {
								argument.setType(argumentType);
							}
							if (subTable.functionsAndVariables.get(argumentName) != null) {
								Helpers.alert(callType.getChild(k), "redéclaration du paramètre `" + argumentName + "`");
							}
							subTable.functionsAndVariables.set(argumentName, argument);
						}
						if (type != null) {
							Type returnType = table.findType(type.toString());
							if (returnType == null) {
								Helpers.alert(type, "type `" + type.toString() + "` non déclaré");
							} else {
								function.setType(returnType);
							}
						}
						if (table.functionsAndVariables.get(name) != null) {
							Helpers.alert(symbol.getChild(0), "redéclaration de la fonction `" + name + "`");
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
					table.fillWith(exp);
					if (table == this || table.functionsAndVariables.get(name) != null) {
						table = new SymbolTable(supTable);
						supTable.children.add(table);
						supTable = table;
					}
					table.functionsAndVariables.set(name, new Variable());
					break;
				}
			}
		}
		return table.fillWith(seq);
	}

	private Type fillWithBreak(Tree tree) {
		Tree parent = tree;
		Tree child = tree;
		while ((parent = parent.getParent()) != null && !parent.toString().equals("function")) {
			if (parent.toString().equals("while") || parent.toString().equals("for") && child == parent.getChild(3)) {
				return null;
			} else {
				child = parent;
			}
		}
		Helpers.alert(tree, "utilisation de `break` en dehors d'une boucle");
		return null;
	}

}
