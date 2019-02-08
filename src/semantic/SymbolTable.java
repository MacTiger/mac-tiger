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
		SymbolTable.nilPseudoType = nilPseudoType;
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
			case REC: return this.fillWithREC(tree, notifier);
			case CALL: return this.fillWithCALL(tree, notifier);
			case ITEM: return this.fillWithITEM(tree, notifier);
			case FIELD: return this.fillWithFIELD(tree, notifier);
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
			this.checkType(tree.getChild(i + 1), notifier, expectedArgs.get(i).getType());
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

			if (expectedFields.get(identifier) == null) {
				notifier.semanticError(tree, "unkown identifier: %s", identifier);
				return null;
			}
		}

		return record;
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
		this.checkType(tree.getChild(1), notifier, SymbolTable.intType);

		// Test sémantique (3)
		this.checkType(tree.getChild(2), notifier, expectedType);

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
		this.checkType(tree.getChild(1),notifier,lType);

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
			notifier.semanticError(tree, "variable %s is not defined", tree.toString());
		} else{
			return variable.getType();
		}
		return null;
	}

	private Type fillWithITEM(Tree tree, Notifier notifier){	//TODO !
		//->^(ITEM $valueExp exp)
		// Par exemple A[4][2]

		this.checkType(tree.getChild(1),notifier,SymbolTable.intType);

		//on regarde si un tableau
		Type type=fillWith(tree.getChild(0),notifier);
		if(!(type instanceof Array)){

		}

		return null;
	}

	private Type fillWithFIELD(Tree tree, Notifier notifier){	//TODO !
		//^(FIELD $valueExp ID))
		//On regarde si le membre de gauche est bien une structure
		Type typeExpr;
		typeExpr=fillWith(tree.getChild(0),notifier);
		if(! (typeExpr instanceof Record)){
			notifier.semanticError(tree,"The variable must be a record");
		}
		else {
			//on sait que typeExpr est de type record
			Namespace<Variable> nms=((Record) typeExpr).getNamespace();
			//on regarde si dans l'espace de noms figure id
			if(nms.get(tree.getChild(1).toString()) == null) {
				notifier.semanticError(tree,"The field doesn't appear in the Record");
			}
			//Sinon c'est OK
			else{
				return nms.get(tree.getChild(1).toString()).getType();
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
		Type expType = this.fillWith(tree.getChild(0), notifier);
		if ((expType != SymbolTable.intType) && (expType != SymbolTable.stringType)){
			notifier.semanticError(tree.getChild(0), "les types non primitifs (ie : autre que 'int' et 'string') ne sont pas acceptés pour cet opérateur : '" + tree.getText() + "'");
		}
		Type secondType = this.checkType(tree.getChild(1), notifier, expType);
		if ((secondType != SymbolTable.intType) && (secondType != SymbolTable.stringType)){
			notifier.semanticError(tree.getChild(1), "les types non primitifs (ie : autre que 'int' et 'string') ne sont pas acceptés pour cet opérateur : '" + tree.getText() + "'");
		}
		return SymbolTable.intType;
	}

	private Type fillWithIntOperator(Tree tree, Notifier notifier) {
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			this.checkType(tree.getChild(i), notifier, SymbolTable.intType);
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
            notifier.semanticError(tree, tree.getText() +" is different from expected type");
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
		SymbolTable table = new SymbolTable(this);				this.children.add(table);
		Variable iterator = new Variable();
		iterator.setType(SymbolTable.intType);
		table.functionsAndVariables.set(tree.getChild(0).toString(), iterator); // Ajout de la variable de boucle for dans sa table de symbole
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
						if (table.types.has(name)) {
							notifier.semanticError(symbol.getChild(0), "redeclaration of type %s", name);
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
								for (int k = 0, lk = shape.getChildCount(); k < lk; k += 2) {
									String fieldName = shape.getChild(k).toString();
									Variable field = new Variable();
									Type fieldType = table.findType(shape.getChild(k + 1).toString());
									if (fieldType == null) {
										notifier.semanticError(shape.getChild(k + 1), "type %s is not defined", shape.getChild(k + 1).toString());
									} else {
										field.setType(fieldType);
									}
									if (namespace.has(fieldName)) {
										notifier.semanticError(shape.getChild(k), "redeclaration of field %s", fieldName);
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
								notifier.semanticError(callType.getChild(k + 1), "type %s is not defined", callType.getChild(k + 1).toString());
							} else {
								argument.setType(argumentType);
							}
							if (subTable.functionsAndVariables.has(argumentName)) {
								notifier.semanticError(callType.getChild(k), "redeclaration of formal parameter %s", argumentName);
							}
							subTable.functionsAndVariables.set(argumentName, argument);
						}
						if (type != null) {
							Type returnType = table.findType(type.toString());
							if (returnType == null) {
								notifier.semanticError(type, "type %s is not defined", type.toString());
							} else {
								function.setType(returnType);
							}
						}
						if (table.functionsAndVariables.has(name)) {
							notifier.semanticError(symbol.getChild(0), "redeclaration of function %s", name);
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
					if (table == this || table.functionsAndVariables.get(name) != null) {
						table = new SymbolTable(supTable);
						supTable.children.add(table);
						supTable = table;
					}
					// Création de la variable déclarée et spécification de son type :
					Variable variable = new Variable();
					variable.setType(returnType);
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

}
