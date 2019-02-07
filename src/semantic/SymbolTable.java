package semantic;

import java.util.*;

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

	public SymbolTable getParent() {
		return parent;
	}

	public ArrayList<SymbolTable> getChildren() {
		return children;
	}

	private Namespace<Type> getTypes() {
		return types;
	}

	private Namespace<FunctionOrVariable> getFunctionsAndVariables() {
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
			// case REC: return this.fillWithREC(tree, notifier);
			case CALL: return this.fillWithCALL(tree, notifier);
			// case ITEM: return this.fillWithITEM(tree, notifier);
			// case FIELD: return this.fillWithFIELD(tree, notifier);
			case ID: return this.fillWithID(tree, notifier);
			case STR: return this.fillWithSTR(tree, notifier);
			case INT: return this.fillWithINT(tree, notifier);
		}

		switch (tree.toString()) {

			 case ":=":	return this.fillWithAssignment(tree, notifier);
             case "=":
             case "<>": return fillWithEqualOrNot(tree, notifier);
             case ">":
             case "<":
             case ">=":
             case "<=": return this.fillWithComparator(tree, notifier);

            // "+", "-", "*", "/", "&" et "|" ont tous le même comportement pour les types de leurs opérandes :
			case "+":
            case "-":
            case "*":
            case "/":
            case "|":
            case "&": return this.fillWithIntOperator(tree, notifier);

			// case "if":
			// case "while":
			case "for": return this.fillWithFor(tree, notifier);
			case "let": return this.fillWithLet(tree, notifier);
			// case "nil":
			case "break": return this.fillWithBreak(tree, notifier);
			default: { /* TODO: à retirer lorsque tous les contrôles sémantiques seront faits */
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.fillWith(tree.getChild(i), notifier);
				}
				return null;
			}
		}
	}

	private Type fillWithCALL(Tree tree, Notifier notifier) {
		/* Appel d'une fonction.
		 * (1) Vérification de l'existence de la fonction appelée
		 * (2) Vérification du nombre et des types d'arguments
		 */
		String functionName = tree.getChild(0).toString();
		Function function = this.findFunction(functionName);

		// Test sémantique (1)
		if (function == null) {
			notifier.semanticError(tree, "undeclared function: %s", functionName);
			return null;
		}

		SymbolTable functionSymbolTable = function.getSymbolTable();
		Namespace<FunctionOrVariable> argsNamespace = functionSymbolTable.getFunctionsAndVariables();
		ArrayList<Variable> expectedArgs = new ArrayList<Variable>();

		for (Map.Entry <String, FunctionOrVariable> entry : argsNamespace) {
			expectedArgs.add((Variable) entry.getValue());
		}

		// Test sémantique (2)
		if (expectedArgs.size() != tree.getChildCount() - 1) {
			notifier.semanticError(tree, "wrong arguments type or count");
			return null;
		}

		// Test sémantique (3)
		for (int i = 0; i < expectedArgs.size(); i++) {
			this.checkType(tree.getChild(i + 1), notifier, expectedArgs.get(i).getType(), false);
		}

		return function.getType();
	}

	private Type fillWithREC(Tree tree, Notifier notifier) {
		/* Déclaration d'un record
		 * (1) Vérification de l'existence du record
		 * (2) On ne peut pas mettre deux fois le même champ
		 * (3) Tous les champs doivent être renseignés
		 * (4) Tous les champs doivent être de bon type
		 * (5) On ne peut pas utiliser un champ qui n'existe pas
		 */

		String recordIdentifier = tree.getChild(0).toString();
		Record record = (Record) this.findType(recordIdentifier);

		// Test sémantique (1)
		if (record == null) {
			notifier.semanticError(tree, "undeclared record: %s", recordIdentifier);
			return null;
		}

		Namespace<Variable> expectedFields = record.getNamespace();
		HashMap<String, Type> givenFields = new HashMap<>();

		// Test sémantique (2)
		for (int i = 1; i < tree.getChildCount(); i = i + 2) {
			String keyName = tree.getChild(i).toString();

			if (givenFields.containsKey(keyName)) {
				notifier.semanticError(tree.getChild(i), "key %s given twice", keyName);
				return null;
			}

			givenFields.put(tree.getChild(i).toString(), fillWith(tree.getChild(i+1), notifier));
		}

		for (Map.Entry <String, Variable> entry : expectedFields) {
			String identifier = entry.getKey();
			Variable variable = entry.getValue();
			Type type = variable.getType();

			// Test sémantique (3)
			if (!givenFields.containsKey(identifier)) {
				notifier.semanticError(tree, "missing field %s", identifier);
				return null;
			}

			// Test sémantique (4)
			if (givenFields.get(identifier) != type) {
				notifier.semanticError(tree, "incorrect type for field %s", identifier);
				return null;
			}
		}

		for (Map.Entry<String, Type> entry : givenFields.entrySet()) {
			String identifier = entry.getKey();

			if (!expectedFields.getSymbols().containsKey(identifier)) {
				notifier.semanticError(tree, "unkown identifier: %s", identifier);
				return null;
			}
		}

		return null;
	}

	private Type fillWithARR(Tree tree, Notifier notifier) {
		/* Déclaration d'un tableau (partie à droite du :=)
		 * (1) Vérification de l'existence du type des éléments du tableau
		 * (2) Vérification du type du nombre d'éléments dans le tableau (obligatoirement int)
		 * (3) Vérification du type de l'élément pour remplir
		 */

		String expectedTypeIdentifier = tree.getChild(0).toString();
		Type expectedType = this.findType(expectedTypeIdentifier);

		// Test sémantique (1)
		if (expectedType == null) {
			notifier.semanticError(tree, "type %s doesn't exists");
			return null;
		}

		// Test sémantique (2)
		checkType(tree.getChild(1), notifier, root.intType, false);

		// Test sémantique (3)
		checkType(tree.getChild(2), notifier, expectedType, false);

		Array arr = new Array();
		arr.setType(expectedType);
		return arr;
	}

	private Type fillWithAssignment(Tree tree, Notifier notifier){

		Type lType = null;
		Tree lValue = tree.getChild(0);
		switch (lValue.getType()) { //Vérification que l'expression à gauche du ":=" est un lValue (cherche si le token de 'lValue' est bien parmis les tokens imaginaires ANTLR correspondant à un lValue)
			case ID:
			case ITEM:
			case FIELD: {
				lType = this.fillWith(lValue, notifier);
				break;
			}
			default: {
				notifier.semanticError(lValue, "the expression on the left of the operator %s isn't a lValue", tree.toString());
			}
		}

		// Vérification de cohérence de type entre l'expression gauche et droite
		if (checkType(tree.getChild(1),notifier,lType,true)==null){
			notifier.semanticError(tree, "types doesn't match between the two sides of the operator ':='");
		}

		return null;
	}

	private Type fillWithSEQ(Tree tree, Notifier notifier) {
		Type expType = null;
		for (int i = 0; i < tree.getChildCount(); i++) {
			expType = this.fillWith(tree.getChild(i), notifier);
		}
		return expType;
	}

	private Type fillWithSTR(Tree tree, Notifier notifier) {
		return SymbolTable.stringType;
	}

	private Type fillWithID(Tree tree, Notifier notifier) {
		Variable variable = this.findVariable(tree.getText());
		if (variable == null){
			notifier.semanticError(tree, "the variable isn't defined");
		} else{
			return variable.getType();
		}
		return null;
	}

	private Type fillWithITEM(Tree tree, Notifier notifier){	//TODO !
		return null;
	}

	private Type fillWithFIELD(Tree tree, Notifier notifier){	//TODO !
		return null;
	}

	private Type fillWithINT(Tree tree, Notifier notifier) {
		return SymbolTable.intType;
	}

	private Type fillWithEqualOrNot(Tree tree, Notifier notifier) {
		Type expType = fillWith(tree.getChild(0), notifier);
		checkType(tree.getChild(1), notifier, expType, false);
		return SymbolTable.intType;
	}

	private Type fillWithComparator(Tree tree, Notifier notifier) {
		Type expType = fillWith(tree.getChild(0), notifier);
		if ((expType != SymbolTable.intType) && (expType != SymbolTable.stringType)){
			notifier.semanticError(tree.getChild(0), "les types non primitifs (ie : autre que 'int' et 'string') ne sont pas acceptés pour cet opérateur : '" + tree.getText() + "'");
		}
		Type secondType = checkType(tree.getChild(1), notifier, expType, true);
		if ((secondType != SymbolTable.intType) && (secondType != SymbolTable.stringType)){
			notifier.semanticError(tree.getChild(1), "les types non primitifs (ie : autre que 'int' et 'string') ne sont pas acceptés pour cet opérateur : '" + tree.getText() + "'");
		}
		return SymbolTable.intType;
	}

	private Type fillWithIntOperator(Tree tree, Notifier notifier) {
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			this.checkType(tree.getChild(i), notifier, SymbolTable.intType, false);
		}
		return SymbolTable.intType;
	}

	private Type fillWithNAire(Tree tree, Type operandsType, Notifier notifier) {
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
			operandsType = fillWith(tree.getChild(0), notifier);
			i++;
		}

		Tree operand = null;

		for (int li = tree.getChildCount(); i < li; ++i){
			operand = tree.getChild(i);
			if (! (this.fillWith(operand, notifier) == operandsType)){	// Si le type du ieme opérande n'est pas operandsType
				notifier.semanticError(operand,"types de "+ operand.getText() +" non consistant dans l'opération : " + tree.toString());	// TODO : afficher l'expression de tree ?
				return null;
			}
		}
		return operandsType;
	}

    private Type checkType(Tree tree, Notifier notifier, Type type, boolean returnTreeType) {
		// Vérifie que le type de l'expression de 'tree' est bien 'type'
	    // Si 'type' vaut null, alors le type de 'tree' est renvoyé
	    // Si le type de 'tree' n'est pas 'type' :
	    // - null est renvoyé si 'returnTreeType' est faux
	    // - le type de l'expression de 'tree' est renvoyé sinon

        Type expType = this.fillWith(tree, notifier);
        if (type == null) {
            return expType;
        } else if (type == expType) {
            return type;
        } else {
            notifier.semanticError(tree, tree.getText() + " is different from expected type");
            if (returnTreeType) {
	            return expType;
            }
	        return null;
        }
    }

	private Type fillWithFor(Tree tree, Notifier notifier) {
		SymbolTable table = new SymbolTable(this);				this.children.add(table);
		Variable iterator = new Variable();
		iterator.setType(SymbolTable.intType);
		table.functionsAndVariables.set(tree.getChild(0).toString(), iterator); // Ajout de la variable de boucle for dans sa table de symbole
		this.fillWith(tree.getChild(1), notifier);	// Rempli la table des symboles pour la borne inférieure du for
		this.fillWith(tree.getChild(2), notifier);	// Rempli la table des symboles pour la borne supérieure du for
		table.fillWith(tree.getChild(3), notifier);	// Remplissage de la table des symboles de la boucle for
		return null;
	}

	private Type fillWithLet(Tree tree, Notifier notifier) {
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
							notifier.semanticError(symbol.getChild(0), "redéclaration du type %s", name);
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
									notifier.semanticError(shape.getChild(0), "type %s non déclaré", shape.getChild(0).toString());
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
										notifier.semanticError(shape.getChild(k + 1), "type %s non déclaré", shape.getChild(k + 1).toString());
									} else {
										field.setType(fieldType);
									}
									if (namespace.get(fieldName) != null) {
										notifier.semanticError(shape.getChild(k), "redéclaration du champ %s", fieldName);
									}
									namespace.set(fieldName, field);
								}
							} else if (type == null) {
								notifier.semanticError(symbol.getChild(0), "définition cyclique du type %s", name);
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
								notifier.semanticError(callType.getChild(k + 1), "type %s non déclaré", callType.getChild(k + 1).toString());
							} else {
								argument.setType(argumentType);
							}
							if (subTable.functionsAndVariables.get(argumentName) != null) {
								notifier.semanticError(callType.getChild(k), "redéclaration du paramètre %s", argumentName);
							}
							subTable.functionsAndVariables.set(argumentName, argument);
						}
						if (type != null) {
							Type returnType = table.findType(type.toString());
							if (returnType == null) {
								notifier.semanticError(type, "type %s non déclaré", type.toString());
							} else {
								function.setType(returnType);
							}
						}
						if (table.functionsAndVariables.get(name) != null) {
							notifier.semanticError(symbol.getChild(0), "redéclaration de la fonction %s", name);
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
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					Type expType = table.fillWith(exp, notifier);
					if (table == this || table.functionsAndVariables.get(name) != null) {
						table = new SymbolTable(supTable);
						supTable.children.add(table);
						supTable = table;
					}

					// Détermination du type de la variable : il est soit renseigné dans la déclaration, soit par inférence
					Type typeOfVar;
					if (symbol.getChildCount() == 3){	// Cas où le type de la variable est renseigné dans la déclaration
						Tree typeTree = symbol.getChild(2);
						if ((typeOfVar = table.checkType(typeTree,notifier, null,true)) == null){
							notifier.semanticError(typeTree, "the type in declaration doesn't exist");
						}
						else{
							if (typeOfVar != expType){
								notifier.semanticError(typeTree, "the type of the declaration doesn't match the one of the initizialisation");
							}
						}
					}
					else {	// Cas où le type de la variable doit être déduit par inférence sur le type de l'expression à droite du ':='
						if (expType==null){
							notifier.semanticError(tree, "the type of the declaration cannot be resolved");
						}
						typeOfVar = expType;
					}

					// Création de la variable déclarée et spécification de son type :
					Variable variable = new Variable();
					variable.setType(typeOfVar);

					table.functionsAndVariables.set(name, variable);
					break;
				}
			}
		}
		return table.fillWith(seq, notifier);
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
		notifier.semanticError(tree, "utilisation de %s en dehors d'une boucle", tree.toString());
		return null;
	}
}
