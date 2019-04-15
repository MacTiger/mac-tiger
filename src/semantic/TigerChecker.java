package semantic;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

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

public class TigerChecker {

	public static Type nilPseudoType;
	public static Type intType;
	public static Type stringType;
	private static SymbolTable root;

	static {
		Type nilPseudoType = new Record();
		Type intType = new Primitive();
		Type stringType = new Primitive();
		Variable intVariable = new Variable();
		intVariable.setType(intType);
		Variable stringVariable = new Variable();
		stringVariable.setType(stringType);
		SymbolTable root = new SymbolTable(null);
		Namespace<Type> types = root.getTypes();
		types.set("int", intType);
		types.set("string", stringType);
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("chr", function);
		}
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("s1", stringVariable);
			functionsAndVariables.set("s2", stringVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("concat", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("exit", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("flush", function);
		}
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("getchar", function);
		}
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("not", function);
		}
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("s", stringVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("ord", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("s", stringVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("print", function);
		}
		{
			Function function = new Function();
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("i", intVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("printi", function);
		}
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("read", function);
		}
		{
			Function function = new Function();
			function.setType(intType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("s", stringVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("size", function);
		}
		{
			Function function = new Function();
			function.setType(stringType);
			SymbolTable table = new SymbolTable(root);
			Namespace<FunctionOrVariable> functionsAndVariables = table.getFunctionsAndVariables();
			functionsAndVariables.set("s", stringVariable);
			functionsAndVariables.set("first", intVariable);
			functionsAndVariables.set("n", intVariable);
			function.setSymbolTable(table);
			root.getFunctionsAndVariables().set("substring", function);
		}
		TigerChecker.nilPseudoType = nilPseudoType;
		TigerChecker.intType = intType;
		TigerChecker.stringType = stringType;
		TigerChecker.root = root;
	}

	private SymbolTable symbolTable;
	private SymbolTable currentTable;
	private Map<Tree, Type> treeTypes;

	public TigerChecker(Tree tree) {
		this.currentTable = this.symbolTable = new SymbolTable(TigerChecker.root);
		this.treeTypes = new HashMap<Tree, Type>();
		this.check(tree);
	}

	public void reportError(Tree tree, String message, String... names) {
		System.out.println(this.getErrorMessage(tree, message, names));
	}

	public String getTokenErrorDisplay(String name) {
		return "'" + name + "'";
	}

	public String getErrorMessage(Tree tree, String message, String... names) {
		Object[] strings = new String[names.length];
		for (int i = 0, l = names.length; i < l; ++i) {
			strings[i] = this.getTokenErrorDisplay(names[i]);
		}
		return String.format(message, strings);
	}

	private Type findType(String name) {
		// recherche le type indiqué dans les tables des symboles supérieures et retourne celui-ci si trouvé ou null sinon
		SymbolTable table = this.currentTable;
		Type type;
		while (
			(type = table.getTypes().get(name)) == null &&
			(table = table.getParent()) != null
		);
		return type;
	}

	private Function findFunction(String name) {
		// recherche la fonction indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou null sinon
		SymbolTable table = this.currentTable;
		FunctionOrVariable functionOrVariable;
		while (
			(functionOrVariable = table.getFunctionsAndVariables().get(name)) == null &&
			(table = table.getParent()) != null
		);
		return functionOrVariable != null && functionOrVariable instanceof Function
			? (Function) functionOrVariable
			: null;
	}

	private Variable findVariable(String name) {
		// recherche la variable indiquée dans les tables des symboles supérieures et retourne celle-ci si trouvée ou null sinon
		SymbolTable table = this.currentTable;
		FunctionOrVariable functionOrVariable;
		while (
			(functionOrVariable = table.getFunctionsAndVariables().get(name)) == null &&
			(table = table.getParent()) != null
		);
		return functionOrVariable != null && functionOrVariable instanceof Variable
			? (Variable) functionOrVariable
			: null;
	}

	private Type check(Tree tree) {
		switch (tree.getType()) {
			case SEQ: return this.checkSEQ(tree);
			case ARR: return this.checkARR(tree);
			case REC: return this.checkREC(tree);
			case CALL: return this.checkCALL(tree);
			case ITEM: return this.checkITEM(tree);
			case FIELD: return this.checkFIELD(tree);
			case ID: return this.checkID(tree);
			case STR: return this.checkSTR(tree);
			case INT: return this.checkINT(tree);
		}
		switch (tree.toString()) {
			case ":=": return this.checkAssignment(tree);
			case "=":
			case "<>": return this.checkComparison(tree);
			case ">":
			case "<":
			case ">=":
			case "<=": return this.checkOrdering(tree);
			// "|", "&", "+", "-", "*" et "/" ont tous le même comportement pour les types de leurs opérandes :
			case "|":
			case "&":
			case "+":
			case "-":
			case "*":
			case "/": return this.checkOperator(tree);
			case "if": return this.checkIf(tree);
			case "while": return this.checkWhile(tree);
			case "for": return this.checkFor(tree);
			case "let": return this.checkLet(tree);
			case "nil": return this.checkNil(tree);
			case "break": return this.checkBreak(tree);
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.check(tree.getChild(i));
				}
				return null;
			}
		}
	}

	private Type check(Tree tree, Type type) {
		// Vérifie que le type de l'expression de 'tree' est bien 'type'
		// Si le type de 'tree' n'est pas 'type', alors `null` est renvoyé
		// Sinon le type de 'tree' est renvoyé
		Type expType = this.check(tree);
		Type returnType = null;
		if (expType == type || expType == TigerChecker.nilPseudoType && type instanceof Record) {
			returnType = type;
		} else if (type == TigerChecker.nilPseudoType && expType instanceof Record) {
			returnType = expType;
		} else {
			this.reportError(tree, "type of %s does not match", tree.toString());
			returnType = null;
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkSEQ(Tree tree) {
		Type returnType = null;
		for (int i = 0, l = tree.getChildCount(); i < l; ++i) {
			returnType = this.check(tree.getChild(i));
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkARR(Tree tree) {
		/* Déclaration d'un tableau
		 * (1) Vérification de l'existence du type de tableau
		 * (2) Vérification du type de la taille du tableau
		 * (3) Vérification du type des éléments du tableau
		 */
		String name = tree.getChild(0).toString();
		Type returnType = this.findType(name);
		if (returnType == null) { // Test sémantique (1)
			this.reportError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Array)) { // Test sémantique (1)
			this.reportError(tree, "type %s is not an array type", name);
			returnType = null;
		}
		this.check(tree.getChild(1), TigerChecker.intType); // Test sémantique (2)
		if (returnType == null) {
			this.check(tree.getChild(2));
		} else {
			Array array = (Array) returnType;
			this.check(tree.getChild(2), array.getType()); // Test sémantique (3)
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkREC(Tree tree) {
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
			this.reportError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Record)) { // Test sémantique (1)
			this.reportError(tree, "type %s is not a record type", name);
			returnType = null;
		} else {
			Record record = (Record) returnType;
			Namespace<Variable> namespace = record.getNamespace();
			for (Map.Entry<String, Variable> entry: namespace) {
				if (i >= l) { // Test sémantique (2)
					this.reportError(tree, "%s requires more fields", name);
					break;
				}
				if (!tree.getChild(i).toString().equals(entry.getKey())) { // Test sémantique (3)
					this.reportError(tree, "field name %s was expected but field name %s was found", entry.getKey(), tree.getChild(i).toString());
				}
				Type type = ((Variable) entry.getValue()).getType();
				if (type == null) {
					this.check(tree.getChild(i + 1));
				} else {
					this.check(tree.getChild(i + 1), type); // Test sémantique (4)
				}
				i += 2;
			}
			if (i < l) { // Test sémantique (2)
				this.reportError(tree, "%s requires fewer fields", name);
			}
		}
		for (; i < l; i += 2) {
			this.check(tree.getChild(i + 1));
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkCALL(Tree tree) {
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
			this.reportError(tree, "function %s is not defined", name);
		} else {
			Namespace<FunctionOrVariable> namespace = function.getSymbolTable().getFunctionsAndVariables();
			for (Map.Entry<String, FunctionOrVariable> entry: namespace) {
				if (i >= l) { // Test sémantique (2)
					this.reportError(tree, "%s requires more arguments", name);
					break;
				}
				Type type = ((Variable) entry.getValue()).getType();
				if (type == null) {
					this.check(tree.getChild(i));
				} else {
					this.check(tree.getChild(i), type); // Test sémantique (3)
				}
				++i;
			}
			if (i < l) { // Test sémantique (2)
				this.reportError(tree, "%s requires fewer arguments", name);
			}
			returnType = function.getType();
		}
		for (; i < l; ++i) { //On parcourt les arguments supplémentaires
			this.check(tree.getChild(i));
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkITEM(Tree tree) {
		Tree exp = tree.getChild(0);
		Type expType = this.check(exp);
		Type returnType = null;
		if (!(expType instanceof Array)) { // on regarde si le fils gauche est bien un tableau
			this.reportError(exp, "%s is not an array", exp.toString());
		} else { // on sait qu'on a bien un tableau
			Array array = (Array) expType;
			returnType = array.getType(); // on retourne le type des éléments stockés dans le tableau
		}
		this.check(tree.getChild(1), TigerChecker.intType); // on regarde si le fils droit est bien un entier
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkFIELD(Tree tree) {
		Tree exp = tree.getChild(0);
		Type expType = this.check(exp);
		Type returnType = null;
		if (!(expType instanceof Record)) { // on regarde si le fils gauche est bien une structure
			this.reportError(exp, "%s is not a record", exp.toString());
		} else { // on sait qu'on a bien une structure
			Record record = (Record) expType;
			Namespace<Variable> fields = record.getNamespace();
			if (fields.get(tree.getChild(1).toString()) == null) { // on regarde si le champ existe
				this.reportError(tree, "field %s is not defined", tree.getChild(1).toString());
			} else { // sinon c'est bon
				returnType = fields.get(tree.getChild(1).toString()).getType();
			}
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkID(Tree tree) {
		Variable variable = this.findVariable(tree.toString());
		Type returnType = null;
		if (variable == null) {
			this.reportError(tree, "variable %s is not defined", tree.toString());
		} else{
			returnType = variable.getType();
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkSTR(Tree tree) {
		this.treeTypes.put(tree, TigerChecker.stringType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return TigerChecker.stringType;
	}

	private Type checkINT(Tree tree) {
		this.treeTypes.put(tree, TigerChecker.intType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return TigerChecker.intType;
	}

	private Type checkAssignment(Tree tree) {
		Tree exp = tree.getChild(0);
		switch (exp.getType()) { // vérification que l'expression à gauche du ":=" est un lValue (cherche si le token de 'exp' est bien parmis les tokens imaginaires ANTLR correspondant à un lValue)
			case ID:
			case ITEM:
			case FIELD: {
				if (exp.getType() == ID) {
					Variable variable = this.findVariable(exp.toString());
					if (variable != null && !variable.isWritable()) {
						this.reportError(exp, "loop index %s cannot be assigned", exp.toString());
						this.check(tree.getChild(1));
						break;
					}
				}
				this.check(tree.getChild(1), this.check(exp)); // vérification de cohérence de type entre l'expression gauche et droite
				break;
			}
			default: {
				this.reportError(exp, "%s is not a valid target for an assignment", exp.toString());
				this.check(tree.getChild(1));
			}
		}
		this.treeTypes.put(tree, null); // Associe à ce tree son type, pour garder cet information à la génération de code
		return null;
	}

	private Type checkComparison(Tree tree) {
		Tree exp = tree.getChild(0);
		Type expType = this.check(exp);
		if (this.check(tree.getChild(1), expType) == TigerChecker.nilPseudoType) {
			this.reportError(exp, "the type of %s cannot be inferred", exp.toString());
		}
		this.treeTypes.put(tree, TigerChecker.intType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return TigerChecker.intType;
	}

	private Type checkOrdering(Tree tree) {
		Tree exp = tree.getChild(0);
		Type expType = this.check(exp);
		if ((expType != TigerChecker.intType) && (expType != TigerChecker.stringType)) {
			this.reportError(exp, "the type of %s is not a primitive type (%s or %s)");
			exp = tree.getChild(1);
			expType = this.check(exp);
			if ((expType != TigerChecker.intType) && (expType != TigerChecker.stringType)) {
				this.reportError(exp, "%s is not a primitive value (either an integer or a string)");
			}
		} else {
			exp = tree.getChild(1);
			expType = this.check(exp, expType);
		}
		this.treeTypes.put(tree, TigerChecker.intType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return TigerChecker.intType;
	}

	private Type checkOperator(Tree tree) {
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			this.check(tree.getChild(i), TigerChecker.intType);
		}
		this.treeTypes.put(tree, TigerChecker.intType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return TigerChecker.intType;
	}

	private Type checkIf(Tree tree) {
		Type returnType = null;
		this.check(tree.getChild(0), TigerChecker.intType);
		if (tree.getChildCount() > 2) {
			returnType = this.check(tree.getChild(2), this.check(tree.getChild(1)));
		} else {
			returnType = this.check(tree.getChild(1), null);
		}
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return returnType;
	}

	private Type checkWhile(Tree tree) {
		this.check(tree.getChild(0), TigerChecker.intType);
		this.check(tree.getChild(1), null);
		this.treeTypes.put(tree, null); // Associe à ce tree son type, pour garder cet information à la génération de code
		return null;
	}

	private Type checkFor(Tree tree) {
		this.check(tree.getChild(1), TigerChecker.intType); // Rempli la table des symboles pour la borne inférieure du for
		this.check(tree.getChild(2), TigerChecker.intType); // Rempli la table des symboles pour la borne supérieure du for
		SymbolTable table = new SymbolTable(this.currentTable);
		this.currentTable.getChildren().add(table);
		this.currentTable = table;
		Variable index = new Variable();
		index.configure(false);
		index.setType(TigerChecker.intType);
		index.setOffset(-2);
		this.currentTable.getFunctionsAndVariables().set(tree.getChild(0).toString(), index); // Ajout de la variable de boucle for dans sa table de symbole
		this.check(tree.getChild(3), null); // Remplissage de la table des symboles de la boucle for
		this.treeTypes.put(tree, null); // Associe à ce tree son type, pour garder cet information à la génération de code
		this.currentTable = this.currentTable.getParent();
		return null;
	}

	private Type checkLet(Tree tree) {
		/* Bloc d'instructions
		 * (1) Vérification de l'unicité de la déclaration d'un type au sein d'une suite de déclarations de types
		 * (2) Vérification de l'existence du type des éléments du tableau
		 * (3) Vérification de l'unicité de la déclaration d'un champ d'une structure
		 * (4) Vérification de l'existence du type d'un champ d'une structure
		 * (5) Vérification de la définition non cyclique d'un type
		 * (6) Vérification de l'unicité de la déclaration d'une fonction au sein d'une suite de déclarations de fonctions
		 * (7) Vérification de l'unicité de la déclaration d'un paramètre d'une fonction
		 * (8) Vérification de l'existence du type d'un paramètre d'une fonction
		 * (9) Vérification de l'existence du type de retour d'une fonction
		 * (10) Vérification du type de retour d'une fonction
		 * (11) Vérification du l'existence du type d'une variable
		 * (12) Vérification du type d'une variable
		 * (13) Vérification de la possibilité d'inférer le type d'une variable
		 */
		SymbolTable supTable = this.currentTable;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		int variableOffset = 0;
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": { // dans le cas d'une suite de déclarations de types
					SymbolTable table = new SymbolTable(this.currentTable); // on crée systématiquement une nouvelle table avant la première déclaration
					this.currentTable.getChildren().add(table);
					this.currentTable = table;
					variableOffset = 0;
					int lj = i;
					int remainingAliases = 0;
					boolean remainsAliases = false;
					boolean remainsArraysAndRecords = false;
					do {
						Type type = null;
						String name = symbol.getChild(0).toString();
						if (this.currentTable.getTypes().has(name)) { // Test sémantique (1)
							this.reportError(symbol.getChild(0), "redeclaration of type %s", name);
						}
						Tree shape = symbol.getChild(1);
						switch (shape.getType()) { // on détecte la sorte de déclaration de type
							case ARRTYPE: { // il s'agit d'un tableau ; on crée un nouveau type de tableau
								type = new Array();
								if (!remainsArraysAndRecords) {
									remainsArraysAndRecords = true;
								}
								break;
							}
							case RECTYPE: { // il s'agit d'un structure ; on crée un nouveau type de structure
								type = new Record();
								if (!remainsArraysAndRecords) {
									remainsArraysAndRecords = true;
								}
								break;
							}
							default: { // il s'agit d'un alias ; on ne crée rien pour l'instant
								remainingAliases++;
								if (!remainsAliases) {
									remainsAliases = true;
								}
							}
						}
						this.currentTable.getTypes().set(name, type); // on ajoute le type déclaré
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
					aliases: while (remainsAliases) { // on résout les dépendances entre types jusqu'à stabilité
						remainsAliases = false;
						for (int j = i; j < lj; ++j) {
							symbol = dec.getChild(j);
							String name = symbol.getChild(0).toString();
							Tree shape = symbol.getChild(1);
							Type type = this.currentTable.getTypes().get(name);
							if (type == null) {
								type = this.findType(shape.toString());
								if (type != null) {
									this.currentTable.getTypes().set(name, type);
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
					if (!remainsAliases || remainsArraysAndRecords) { // si un alias ne peut être résolu ou qu'un type composite a été déclaré, on effectue un dernier passage sur les déclarations de types concernées
						for (int j = i; j < lj; ++j) {
							symbol = dec.getChild(j);
							String name = symbol.getChild(0).toString();
							Tree shape = symbol.getChild(1);
							Type type = this.currentTable.getTypes().get(name);
							if (type instanceof Array) {
								// on effectue des vérifications spécifiques aux tableaux
								Array array = (Array) type;
								Type itemType = this.findType(shape.getChild(0).toString());
								if (itemType == null) { // Test sémantique (2)
									this.reportError(shape.getChild(0), "type %s is not defined", shape.getChild(0).toString());
								} else {
									array.setType(itemType);
								}
							} else if (type instanceof Record) {
								// on effectue des vérifications spécifiques aux structures
								Record record = (Record) type;
								Namespace<Variable> namespace = record.getNamespace();
								int fieldOffset = 0;
								for (int k = 0, lk = shape.getChildCount(); k < lk; k += 2) {
									Variable field = new Variable();
									String fieldName = shape.getChild(k).toString();
									if (namespace.has(fieldName)) { // Test sémantique (3)
										this.reportError(shape.getChild(k), "redeclaration of field %s", fieldName);
									}
									Type fieldType = this.findType(shape.getChild(k + 1).toString());
									if (fieldType == null) { // Test sémantique (4)
										this.reportError(shape.getChild(k + 1), "type %s is not defined", shape.getChild(k + 1).toString());
									} else {
										field.setType(fieldType);
									}
									field.setOffset(fieldOffset);
									if (fieldType != null) {
										fieldOffset += 2;
									}
									namespace.set(fieldName, field);
								}
							} else if (type == null) { // Test sémantique (5)
								 // on se trouve dans le cas d'un alias non résolu
								this.reportError(symbol.getChild(0), "circular definition of type %s", name);
							}
						}
					}
					i = lj - 1;
					break;
				}
				case "function": { // dans le cas d'une suite de déclarations de fonctions
					SymbolTable table = new SymbolTable(this.currentTable); // on crée systématiquement une nouvelle table avant la première déclaration
					this.currentTable.getChildren().add(table);
					this.currentTable = table;
					variableOffset = 0;
					int lj = i;
					do {
						SymbolTable subTable = new SymbolTable(this.currentTable); // chaque fonction dispose de sa propre table interne
						this.currentTable.getChildren().add(subTable);
						Function function = new Function();
						function.setSymbolTable(subTable);
						String name = symbol.getChild(0).toString();
						if (this.currentTable.getFunctionsAndVariables().has(name)) { // Test sémantique (6)
							this.reportError(symbol.getChild(0), "redeclaration of function %s", name);
						}
						Tree callType = symbol.getChild(1);
						int argumentOffset = 0;
						for (int k = 0, lk = callType.getChildCount(); k < lk; k += 2) {
							Variable argument = new Variable();
							String argumentName = callType.getChild(k).toString();
							if (subTable.getFunctionsAndVariables().has(argumentName)) { // Test sémantique (7)
								this.reportError(callType.getChild(k), "redeclaration of parameter %s", argumentName);
							}
							Type argumentType = this.findType(callType.getChild(k + 1).toString());
							if (argumentType == null) { // Test sémantique (8)
								this.reportError(callType.getChild(k + 1), "type %s is not defined", callType.getChild(k + 1).toString());
							} else {
								argument.setType(argumentType);
							}
							if (argumentType != null) {
								argumentOffset += 2;
							}
							argument.setOffset(argumentOffset);
							subTable.getFunctionsAndVariables().set(argumentName, argument);
						}
						for (Map.Entry<String, FunctionOrVariable> entry: subTable.getFunctionsAndVariables()) {
							Variable argument = (Variable) entry.getValue();
							argument.setOffset(argumentOffset - argument.getOffset());
						}
						Tree type = symbol.getChildCount() > 3
							? symbol.getChild(3)
							: null;
						if (type != null) { // on distigue les procédures des fonctions
							Type returnType = this.findType(type.toString());
							if (returnType == null) { // Test sémantique (9)
								this.reportError(type, "type %s is not defined", type.toString());
							} else {
								function.setType(returnType);
							}
						}
						this.currentTable.getFunctionsAndVariables().set(name, function); // on ajoute la fonction déclarée
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));
					for (int j = i; j < lj; ++j) {
						symbol = dec.getChild(j);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = (Function) this.currentTable.getFunctionsAndVariables().get(name);
						SymbolTable subTable = function.getSymbolTable();
						Type returnType = function.getType();
						this.currentTable = subTable;
						if (returnType == null) {
							this.check(body);
						} else {
							this.check(body, returnType); // Test sémantique (10)
						}
						this.currentTable = this.currentTable.getParent();
					}
					i = lj - 1;
					break;
				}
				case "var": { // dans le cas de la déclaration d'une variable
					Variable variable = new Variable(); // Création de la variable déclarée
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					Tree type = symbol.getChildCount() > 2 ? symbol.getChild(2) : null;
					Type returnType;
					// Détermination du type de la variable : il est soit renseigné dans la déclaration, soit par inférence
					if (type != null) { // Cas où le type de la variable est renseigné dans la déclaration
						returnType = this.findType(type.toString());
						if (returnType == null) { // Test sémantique (11)
							this.reportError(type, "type %s is not defined", type.toString());
							returnType = this.check(exp);
						} else {
							this.check(exp, returnType); // Test sémantique (12)
						}
					} else { // Cas où le type de la variable doit être déduit par inférence sur le type de l'expression à droite du ':='
						returnType = this.check(exp);
						if (returnType == null || returnType == TigerChecker.nilPseudoType) { // Test sémantique (13)
							this.reportError(exp, "the type of %s cannot be inferred", name);
						}
					}
					variable.setType(returnType); // Spécification du type de la variable
					if (this.currentTable == supTable || this.currentTable.getFunctionsAndVariables().get(name) != null) {
						SymbolTable table = new SymbolTable(this.currentTable); // on crée parfois (dans les cas d'une première déclaration de variable ou d'une redéclaration d'une variable) une nouvelle table
						this.currentTable.getChildren().add(table);
						this.currentTable = table;
						variableOffset = 0;
					}
					if (returnType != null) {
						variableOffset -= 2;
					}
					variable.setOffset(variableOffset);
					this.currentTable.getFunctionsAndVariables().set(name, variable);
					break;
				}
			}
		}
		Type returnType = this.check(seq);
		this.treeTypes.put(tree, returnType); // Associe à ce tree son type, pour garder cet information à la génération de code
		this.currentTable = supTable;
		return returnType;
	}

	private Type checkNil(Tree tree) {
		this.treeTypes.put(tree, TigerChecker.nilPseudoType); // Associe à ce tree son type, pour garder cet information à la génération de code
		return TigerChecker.nilPseudoType;
	}

	private Type checkBreak(Tree tree) {
		Tree parent = tree;
		Tree child = tree;
		while ((parent = parent.getParent()) != null && !parent.toString().equals("function")) {
			if (parent.toString().equals("while") || parent.toString().equals("for") && child == parent.getChild(3)) {
				this.treeTypes.put(tree, null); // Associe à ce tree son type, pour garder cet information à la génération de code
				return null;
			} else {
				child = parent;
			}
		}
		this.reportError(tree, "%s must be inside loop", tree.toString());
		this.treeTypes.put(tree, null); // Associe à ce tree son type, pour garder cet information à la génération de code
		return null;
	}

	public SymbolTable getSymbolTable() {
		return this.symbolTable;
	}

	public Map<Tree, Type> getTreeTypes() {
		return this.treeTypes;
	}

}
