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

	public static Type nilPseudoType;
	public static Type intType;
	public static Type stringType;
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

	public SymbolTable getChild(int i){
		return children.get(i);
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

	public Type findType(String name) {
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

	public Function findFunction(String name) {
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

	/**
	 * Compte le nombre de chaînages statiques à remonter pour accèder à la variable nameOfVariable
	 * @param nameOfVariable
	 * @return : nombre de chaînages statiques à remonter
	 */
	public int countStaticChainToVariable(String nameOfVariable){
		SymbolTable symbolTable = this;
		int staticChainCount = 0;
		while (! symbolTable.functionsAndVariables.has(nameOfVariable)){
			staticChainCount++;
			if (symbolTable.parent != null){
				symbolTable = symbolTable.parent;
			}
			else{
				return -1; // Cas d'erreur : la variable n'est pas trouvable
			}
		}
		return staticChainCount;
	}

	public Variable findVariable(String name) {
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
		switch (tree.getType()) {
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
		for (; i < l; ++i) {    //On parcourt les arguments supplémentaires
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
		if (!(expType instanceof Record)) { // on regarde si le fils gauche est bien une structure
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
		SymbolTable supTable = this;
		SymbolTable table = this;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		int variableOffset = 0;
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": { // dans le cas d'une suite de déclarations de types
					table = new SymbolTable(supTable); // on crée systématiquement une nouvelle table avant la première déclaration
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
						if (table.types.has(name)) { // Test sémantique (1)
							notifier.semanticError(symbol.getChild(0), "redeclaration of type %s", name);
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
						table.types.set(name, type); // on ajoute le type déclaré
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
					aliases: while (remainsAliases) { // on résout les dépendances entre types jusqu'à stabilité
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
					if (!remainsAliases || remainsArraysAndRecords) { // si un alias ne peut être résolu ou qu'un type composite a été déclaré, on effectue un dernier passage sur les déclarations de types concernées
						for (int j = i; j < lj; ++j) {
							symbol = dec.getChild(j);
							String name = symbol.getChild(0).toString();
							Tree shape = symbol.getChild(1);
							Type type = table.types.get(name);
							if (type instanceof Array) {
								// on effectue des vérifications spécifiques aux tableaux
								Array array = (Array) type;
								Type itemType = table.findType(shape.getChild(0).toString());
								if (itemType == null) { // Test sémantique (2)
									notifier.semanticError(shape.getChild(0), "type %s is not defined", shape.getChild(0).toString());
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
										notifier.semanticError(shape.getChild(k), "redeclaration of field %s", fieldName);
									}
									Type fieldType = table.findType(shape.getChild(k + 1).toString());
									if (fieldType == null) { // Test sémantique (4)
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
							} else if (type == null) { // Test sémantique (5)
								 // on se trouve dans le cas d'un alias non résolu
								notifier.semanticError(symbol.getChild(0), "circular definition of type %s", name);
							}
						}
					}
					i = lj - 1;
					break;
				}
				case "function": { // dans le cas d'une suite de déclarations de fonctions
					table = new SymbolTable(supTable); // on crée systématiquement une nouvelle table avant la première déclaration
					supTable.children.add(table);
					supTable = table;
					variableOffset = 0;
					int lj = i;
					do {
						SymbolTable subTable = new SymbolTable(table); // chaque fonction dispose de sa propre table interne
						table.children.add(subTable);
						Function function = new Function();
						function.setSymbolTable(subTable);
						String name = symbol.getChild(0).toString();
						if (table.functionsAndVariables.has(name)) { // Test sémantique (6)
							notifier.semanticError(symbol.getChild(0), "redeclaration of function %s", name);
						}
						Tree callType = symbol.getChild(1);
						int argumentOffset = 0;
						for (int k = 0, lk = callType.getChildCount(); k < lk; k += 2) {
							Variable argument = new Variable();
							String argumentName = callType.getChild(k).toString();
							if (subTable.functionsAndVariables.has(argumentName)) { // Test sémantique (7)
								notifier.semanticError(callType.getChild(k), "redeclaration of parameter %s", argumentName);
							}
							Type argumentType = table.findType(callType.getChild(k + 1).toString());
							if (argumentType == null) { // Test sémantique (8)
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
						if (type != null) { // on distigue les procédures des fonctions
							Type returnType = table.findType(type.toString());
							if (returnType == null) { // Test sémantique (9)
								notifier.semanticError(type, "type %s is not defined", type.toString());
							} else {
								function.setType(returnType);
							}
						}
						table.functionsAndVariables.set(name, function); // on ajoute la fonction déclarée
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));
					for (int j = i; j < lj; ++j) {
						symbol = dec.getChild(j);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = (Function) table.functionsAndVariables.get(name);
						SymbolTable subTable = function.getSymbolTable();
						Type returnType = function.getType();
						if (returnType == null) {
							subTable.fillWith(body, notifier);
						} else {
							subTable.checkType(body, notifier, returnType); // Test sémantique (10)
						}
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
					if (type != null) {	// Cas où le type de la variable est renseigné dans la déclaration
						returnType = table.findType(type.toString());
						if (returnType == null) { // Test sémantique (11)
							notifier.semanticError(type, "type %s is not defined", type.toString());
							returnType = table.fillWith(exp, notifier);
						} else {
							table.checkType(exp, notifier, returnType); // Test sémantique (12)
						}
					} else {	// Cas où le type de la variable doit être déduit par inférence sur le type de l'expression à droite du ':='
						returnType = table.fillWith(exp, notifier);
						if (returnType == null || returnType == SymbolTable.nilPseudoType) { // Test sémantique (13)
							notifier.semanticError(exp, "the type of %s cannot be inferred", name);
						}
					}
					variable.setType(returnType); // Spécification du type de la variable
					if (table == this || table.functionsAndVariables.get(name) != null) {
						table = new SymbolTable(supTable); // on crée parfois (dans les cas d'une première déclaration de variable ou d'une redéclaration d'une variable) une nouvelle table
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
		String allTypes = "\nTypes[shape=record, label=\"{Types déclarés}";

		allTypes += "|{ VOID | <VOID> }";

		ArrayList<String> resArray = root.toGraphViz("TDS",-1);
		res += resArray.get(0) + "\n"+ resArray.get(1) + resArray.get(2);
		allTypes += resArray.get(3);

		resArray = this.toGraphViz("TDS",0);
		allTypes += resArray.get(3) + "\"]\n";
		res += resArray.get(0) + resArray.get(1) + resArray.get(2) + allTypes + makeLink(nameOfTDS("TDS",0),"parent","TDS","parent",false);

		res += "\n}";
		return res;
	}

	private String nameOfTDS(String parent, int num_TDS){
		if (num_TDS < 0){
			return parent;
		}
		return parent + "_" + num_TDS;
	}

	public static String makeLink(String source, String portSource, String dest, String portDest, boolean bidirectionnal){
		String res = "\"" + source + "\":" + portSource + " -> \"" + dest + "\":"+  portDest;
		if (bidirectionnal){
			res += "[dir=\"both\"]";
		}
		return  res + ";";
	}

	public static String makeAdresse(String stringToEscape){
		return stringToEscape.toString().replace('.','_').replace('@','_');
	}

	public ArrayList<String> toGraphViz(String parent, int num_TDS){
		// Renvoit : [graph, graphChildren, graphLinks]

		String nameOfThisTDS = nameOfTDS(parent,num_TDS);
		String graph =  nameOfThisTDS+ "[shape=record, label=\""; // Résultat à afficher
//		graph += " " + nameOfThisTDS + " }";    //Nom de la TDS, et port parent
		String linksOfGraph = "{<parent> ";   // Partie de graph qui servira à la liaison avec les autres TDS
		String typesGraph = "";          // Partie de graph qui contiendra les types
		String varFuncGraph = "";        // Partie de graph qui contiendra les fonctions et variables

		String graphChildren = "";  // String des graphiques des TDS filles de cette TDS
		String graphLinks = ""; // String des liaisons entre TDS
		String allTypes = "";

		List<SymbolTable> symbolTablesOfFunctions = new ArrayList<>(); // Array des SymbolTable de fonction déjà liés


		int i = 0;

//		linksOfGraph += "{<parent>}";   // Prévoit le point d'ancrage du lient vers son père

		Variable var = null;
		Function function = null;
		String partOfGraph =""; // String auxiliaire pour la création des divers cellules de la TDS
		i =0;

		for (Map.Entry<String, FunctionOrVariable> stringSymbolEntry : this.functionsAndVariables){ // Parcours des fonctions et variables déclarées dans cette TDS
			partOfGraph = "";
			if (i > 0){
				varFuncGraph+=" | ";    // Séparation entre la variable/fonction qu'on est en train d'ajouter, et celle d'avant
			}
			if (stringSymbolEntry.getValue() instanceof Variable){  // Cellule d'une variable
				var = (Variable) stringSymbolEntry.getValue();
//				partOfGraph += "var|"+ stringSymbolEntry.getKey() + "|" + var.getOffset() + "| <" + "typeVar_" + i + ">";
//				graphLinks += makeLink(nameOfThisTDS, "typeVar_" + i, "Types", makeAdresse(var.getType().toString()),false);   // Lien du type de la variable
				ArrayList<String> res = var.makeCellGraphviz(stringSymbolEntry.getKey(),nameOfThisTDS,String.valueOf(i) );
				partOfGraph+= res.get(0);
				graphLinks += res.get(1);
			}

			else if (stringSymbolEntry.getValue() instanceof Function){ // Cellule d'une fonction
				function = (Function) stringSymbolEntry.getValue();
				partOfGraph += "<function_" + i + "> "; // Ajout du port pour brancher la TDS de la fonction à sa cellule
				symbolTablesOfFunctions.add(i,function.getSymbolTable()); // Ajout de la SymbolTable en position i (numéro de la fonction) dans l'ensemble des SymbolTable de fonction
				partOfGraph += "function|" + stringSymbolEntry.getKey() + "| <" + "returnType_" + i + "> ";
				graphLinks += makeLink(nameOfThisTDS, "returnType_" + i, "Types", makeAdresse(function.getTypeToGraphviz()),false);   // Lien du type de retour de la fonction
			}
			varFuncGraph += "{" + partOfGraph + "}";
			i++;
		}

		i = 0;
		for (Map.Entry<String, Type> stringTypeEntry : this.types){ // Parcours des types déclarées dans cette TDS
			partOfGraph = "";
			if (i > 0){
				typesGraph += " | ";    // Séparation entre le type qu'on est en train d'ajouter, et celui d'avant
			}
			partOfGraph += "type|" + stringTypeEntry.getKey() + "| <" + "type_" + i + ">";
			graphLinks += makeLink(nameOfThisTDS, "type_" + i, "Types", makeAdresse(stringTypeEntry.getValue().toString()),false); // Ajout du lien de la cellule du type vers son type dans le tableau allTypes

			allTypes += "|";    // Séparation avec les types précedents, faite d'office par construction de allTypes
			allTypes += "{" + stringTypeEntry.getValue().whichInstance() + "| <" + makeAdresse(stringTypeEntry.getValue().toString()) + "> }";   // Ajout du type au tableau regroupant tous les types déclarés
			typesGraph += "{" + partOfGraph + "}";

			if (stringTypeEntry.getValue() instanceof Array){   //Pour les types Array, il faut aussi relier la case Array de allTypes au type contenu dans l'Array
				Array array = (Array) stringTypeEntry.getValue();
				graphLinks += makeLink("Types",makeAdresse(stringTypeEntry.getValue().toString()), "Types", makeAdresse(array.getType().toString()), false);
			}
			i++;
		}

		for (SymbolTable symbolTable : this.children){ // Parcours des TDS filles de cette TDS
			ArrayList symbolTableGraph = symbolTable.toGraphViz(nameOfThisTDS, i);
			// Récupère les graphes créés par la fille symbolTable :
			graphLinks += "\n" + symbolTableGraph.get(2) ;  // Ajout des liaisons créées par symbolTable
			allTypes += symbolTableGraph.get(3);
			graphChildren += "\n" + symbolTableGraph.get(0) + "\n" + symbolTableGraph.get(1);   // Ajout du graph de symbolTable et de ces enfants
			if (symbolTablesOfFunctions.contains(symbolTable)){ // N'ajoute de lien que si cela n'a pas déjà été fait (cas d'une SymbolTable d'une venant d'une fonction
				graphLinks += makeLink(nameOfTDS(nameOfThisTDS,i), "parent",nameOfThisTDS, "function_" + symbolTablesOfFunctions.indexOf(symbolTable),true); // Ajout des liens entre cette TDS et sa TDS fille venant de cette fonction
			} else{
				graphLinks += makeLink(nameOfTDS(nameOfThisTDS,i), "parent",nameOfThisTDS, "parent",false); // Ajout des liens entre cette TDS et sa fille symbolTable
			}
			i++;
		}

		linksOfGraph += nameOfThisTDS + " }";   // Ajout du nom de la TDS en entête de tableau
		// Gestion des séparation entres les différentes parties de graph
		if( !(typesGraph.equals("")) ){
			linksOfGraph += " | ";
		}
		if ( !(varFuncGraph.equals("")) ){
			typesGraph += " | ";
		}
		graph += linksOfGraph + typesGraph + varFuncGraph;  // Assemblage des parties de graph
		graph += "\"]";
		return new ArrayList<>(Arrays.asList(graph, graphChildren, graphLinks, allTypes));
	}

}
