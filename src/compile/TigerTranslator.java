package compile;

import misc.Notifier;
import org.antlr.runtime.tree.Tree;
import semantic.*;

import java.util.ArrayList;
import java.util.Map;


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

public class TigerTranslator {

/*	private static Type nilPseudoType;
	private static Type intType;
	private static Type stringType;*/

	//TODO : faire un static pour les fonctions du langage (print ...)

/*
	private semantic.SymbolTable parent;
	private List<semantic.SymbolTable> children;
	private Namespace<Type> types;
	private Namespace<FunctionOrVariable> functionsAndVariables;
*/

//	private Tree currentASTNode;    // Noeud de l'AST actuel
	private SymbolTable currentTDS; // TDS actuelle
	private ArrayList<Integer> childrenIndexStack;  // Pile des childrenIndex, mis à jour en descente et en remontée de TDS
	private Writer writer;  // Classe gérant les écritures de code au bon endroit (pour permettre d'écrire le code d'une fonction en plusieurs fois, si une autre fonction (assembleur) est nécessaire durant son écriture)
	private LabelGenerator labelGenerator;

	private TigerTranslator(SymbolTable currentTDS) {
		// Pour lancer le translator sur l'ensemble du programme, passer la TDS de niveau 0 (pas le root)
		this.currentTDS=currentTDS;     // TDS actuelle
		childrenIndexStack = new ArrayList<>();
		this.writer = new Writer();
		this.labelGenerator = new LabelGenerator();
	}

	private void descendTDS(){
		// Met à jour this.currentTDS pour y mettre la TDS fille de la currentTDS de bon index, met à jour childrenIndexStack
		int indexOfNextChild = childrenIndexStack.get(childrenIndexStack.size()-1); // Récupère l'index de la TDS fille à prendre
		childrenIndexStack.set(childrenIndexStack.size()-1, indexOfNextChild + 1);  // Incrémente l'index de la prochaine TDS à prendre
		childrenIndexStack.add(0);  // Ajoute une entrée dans childrenIndexStack pour reprendre le compte pour la nouvelle currentTDS
		this.currentTDS = this.currentTDS.getChild(indexOfNextChild);
		writer.descendFunctionAssembly();   // Descend le writer pour écrire le code de cette fonction

	}

	private void ascendTDS(){
		// Met à jour this.currentTDS en remontant de TDS, met à jour childrenIndexStack
		this.currentTDS = this.currentTDS.getParent();  // Remonte de TDS
		childrenIndexStack.remove(childrenIndexStack.size() - 1);   // Retire le dernière index empilé
		writer.ascendFunctionAssembly();    // Remonte le writer : on a finit d'écrire le code de cette fonction (assembleur)

	}

	public Type translate(Tree tree, int registerIndex) {
		switch (tree.getType ()) {
			case SEQ: return this.translateSEQ(tree, registerIndex);
			case ARR: return this.translateARR(tree, registerIndex);
			case REC: return this.translateREC(tree, registerIndex);
			case CALL: return this.translateCALL(tree, registerIndex);
			case ITEM: return this.translateITEM(tree, registerIndex);
			case FIELD: return this.translateFIELD(tree, registerIndex);
			case ID: return this.translateID(tree, registerIndex);
			case STR: return this.translateSTR(tree, registerIndex);
			case INT: return this.translateINT(tree, registerIndex);
		}
		switch (tree.toString()) {
			case ":=": return this.translateAssignment(tree, registerIndex);
			case "=":
			case "<>": return this.translateEqualOrNot(tree, registerIndex);
			case ">":
			case "<":
			case ">=":
			case "<=": return this.translateComparator(tree, registerIndex);
			// "|", "&", "+", "-", "*" et "/" ont tous le même comportement pour les types de leurs opérandes :
			case "|":
			case "&":
			case "+":
			case "-":
			case "*":
			case "/": return this.translateIntOperator(tree, registerIndex);
			case "if": return this.translateIf(tree, registerIndex);
			case "while": return this.translateWhile(tree, registerIndex);
			case "for": return this.translateFor(tree, registerIndex);
			case "let": return this.translateLet(tree, registerIndex);
			case "nil": return this.translateNil(tree, registerIndex);
			case "break": return this.translateBreak(tree, registerIndex);
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.translate(tree.getChild(i), registerIndex);
				}
				return null;
			}
		}
	}

	private Type translateCALL(Tree tree, int registerIndex) {
		/* Appel d'une fonction
		 *
		 */
		String name = tree.getChild(0).toString();
		Function function = currentTDS.findFunction(name);
		Type returnType = null;
		//TODO : gérer code d'appel à fonction

		for (int i = 0 ; i < tree.getChildCount(); i++) {   //Parcours des arguments de la fonction
			translate(tree.getChild(i), registerIndex);
			//TODO : code pour empiler l'argument
		}

		//TODO : JSR labelFonction
		returnType = function.getType();

		return returnType;
	}

	private Type translateREC(Tree tree, int registerIndex) {
		/* Déclaration d'une structure
		 * (1) Vérification de l'existence du type de structure
		 * (2) Vérification du nombre de champs
		 * (3) Vérification des noms des champs
		 * (4) Vérification des types des champs
		 */
		String name = tree.getChild(0).toString();
		Type returnType = null;
		//TODO : Générer le code d'intanciation d'une structure
		/*Type returnType = this.findType(name);
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
			this.translate(tree.getChild(i + 1), registerIndex);
		}*/
		return returnType;
	}

	private Type translateARR(Tree tree, int registerIndex) {
		/* Déclaration d'un tableau
		 * (1) Vérification de l'existence du type de tableau
		 * (2) Vérification du type de la taille du tableau
		 * (3) Vérification du type des éléments du tableau
		 */
		String name = tree.getChild(0).toString();
		Type returnType = null;
//		Type returnType = this.findType(name);
		//TODO : générer le code d'instanciation d'un tableau
		/*if (returnType == null) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Array)) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not an array type", name);
			returnType = null;
		}
		this.checkType(tree.getChild(1), notifier, semantic.SymbolTable.intType); // Test sémantique (2)
		if (returnType != null) {
			Array array = (Array) returnType;
			this.checkType(tree.getChild(2), notifier, array.getType()); // Test sémantique (3)
		}*/
		return returnType;
	}

	private Type translateAssignment(Tree tree, int registerIndex) {
		Tree lValue = tree.getChild(0);
		Tree expToAssign = tree.getChild(1);
		switch (lValue.getType()) { // Disjonction de cas selon le type de lValue qui prendra l'assignement
			case ID: Variable variable = this.currentTDS.findVariable(lValue.toString());
			//TODO : Générer code d'assignement avec un ID
				break;
			case ITEM:
				//TODO : Générer code d'assignement avec un ITEM
			case FIELD:
				//TODO : Générer code d'assignement avec un FIELD
			default:
				// On ne doit pas arriver là si les tests sémantiques sont passés
		}
		return null;
	}

	private Type translateSEQ(Tree tree, int registerIndex) {
		Type returnType = null;
		for (int i = 0, l = tree.getChildCount(); i < l; ++i) {
			//TODO : gérer les registres :
			returnType = this.translate(tree.getChild(i), registerIndex);
		}
		return returnType;
	}

	private Type translateSTR(Tree tree, int registerIndex) {
		//TODO : Générer le code pour STRING
		return this.currentTDS.stringType;
	}

	private Type translateID(Tree tree, int registerIndex) {
		Variable variable = this.currentTDS.findVariable(tree.toString());
		//TODO : Générer code de recherche de la variable pour la placer dans le registre regiterIndex
		return variable.getType();
	}

	private Type translateITEM(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		Type expType = this.translate(exp, registerIndex);
		//TODO : générer le code pour récupérer l'index entier représenté par le fils droit
		//TODO : générer le code pour accèder à l'ITEM
		Type returnType = null;
		/*if (!(expType instanceof Array)) { // on regarde si le fils gauche est bien un tableau
			notifier.semanticError(exp, "%s is not an array", exp.toString());
		} else { // on sait qu'on a bien un tableau
			Array array = (Array) expType;
			returnType = array.getType(); // on retourne le type des éléments stockés dans le tableau
		}
		this.checkType(tree.getChild(1), notifier, semantic.SymbolTable.intType); // on regarde si le fils droit est bien un entier*/
		return returnType;
	}

	private Type translateFIELD(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		Type expType = this.translate(exp, registerIndex);
		//TODO : générer le code pour récupérer le FIELD
/*		if(!(expType instanceof Record)) { // on regarde si le fils gauche est bien une structure
			notifier.semanticError(exp, "%s is not a record", exp.toString());
		} else { // on sait qu'on a bien une structure
			Record record = (Record) expType;
			Namespace<Variable> fields = record.getNamespace();
			if (fields.get(tree.getChild(1).toString()) == null) { // on regarde si le champ existe
				notifier.semanticError(tree, "field %s is not defined", tree.getChild(1).toString());
			} else { // sinon c'est bon
				return fields.get(tree.getChild(1).toString()).getType();
			}
		}*/
		return null;
	}

	private Type translateINT(Tree tree, int registerIndex) {
		//TODO : Générer le code pour l'entier immédiat tree.toString()
		return this.currentTDS.intType;
	}

	private Type translateEqualOrNot(Tree tree, int registerIndex) {
		Tree exp0 = tree.getChild(0);
		Tree exp1 = tree.getChild(1);

		int registerIndex0 = 0;
		int registerIndex1 = 0;

		//TODO : gérer les registres où stocker les résultats des deux opérandes
		translate(exp0, registerIndex0);
		translate(exp1, registerIndex1);

		//TODO : Générer le code de l'égalité entre ce qui est stocké dans registerIndex0 et registerIndex1
		return semantic.SymbolTable.intType;
	}

	private Type translateComparator(Tree tree, int registerIndex) {
		Tree exp0 = tree.getChild(0);
		Tree exp1 = tree.getChild(1);

		int registerIndex0 = 0;
		int registerIndex1 = 0;

		//TODO : gérer les registres où stocker les résultats des deux opérandes
		translate(exp0, registerIndex0);
		translate(exp1, registerIndex1);

		//TODO : Générer le code de la comparaison entre ce qui est stocké dans registerIndex0 et registerIndex1
		return semantic.SymbolTable.intType;
	}

	private Type translateIntOperator(Tree tree, int registerIndex) {
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			//TODO : générer le code pour appliquer l'opération tree.toString() entre tous les fils de tree (accessibles avec tree.getChild(i))
		}
		return semantic.SymbolTable.intType;
	}

	private Type translateIf(Tree tree, int registerIndex) {
		//TODO : Générer le code pour le IF (penser à réserver les registres nécessaires)
		this.translate(tree.getChild(0),registerIndex);   //Pour le if
		Type result = this.translate(tree.getChild(1), registerIndex);  // Pour le then

		if (tree.getChildCount() == 2){    // Pour le else
			result = this.translate(tree.getChild(2), registerIndex);
		}
		return result;
	}

	private Type translateWhile(Tree tree, int registerIndex) {
		//TODO : Génerer code de while (penser à réserver les registres nécessaires)
		labelGenerator.addFunction(tree);   // Création des labels de cette boucle while
		this.translate(tree.getChild(0),registerIndex);
		this.translate(tree.getChild(1),registerIndex);
		return null;
	}

	private Type translateFor(Tree tree, int registerIndex) {
		descendTDS();   // Met à jour this.currentTDS avec la bonne TDS fille
		labelGenerator.addFunction(currentTDS);    // Création des labels de la TDS liée à ce for
		labelGenerator.addFunction(tree);    // Création des labels de la fonction assembleur liée à ce for (label de la ligne du test, de fin de for)

		Variable index = currentTDS.findVariable(tree.getChild(0).toString());  // Récupère la variable de boucle
		//TODO : gérer les registres :
		translate(tree.getChild(1), registerIndex);	// Génère le code pour la borne inférieure du for
		translate(tree.getChild(2), registerIndex);	// Génère le code pour la borne supérieure du for
		translate(tree.getChild(3), registerIndex);	// Génère le code de la boucle for
		ascendTDS();    // Met à jour this.currentTDS avec la TDS père (revient à la currentTDS en entrée de cette fonction)
		return null;
	}

	/**
	 * Traduit l'AST passé en paramètre représentant une expression LET, met le résultat de l'expression dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 * @return
	 */
	private Type translateLet(Tree tree, int registerIndex) {
		int initialDepth = this.childrenIndexStack.size();  // Sauvegarde de la profondeur actuelle de TDS, pour retourner à celui ci à la fin de la fonction
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": { // dans le cas d'une suite de déclarations de types
					descendTDS(); // On avait créé une nouvelle table avant la première déclaration, donc on y descend
					int lj = i;
					symbol = dec.getChild(lj);
					do{
					} while(++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
					break;
				}
				case "function": { // dans le cas d'une suite de déclarations de fonctions
					descendTDS();   // On avait créé une nouvelle table avant la première déclaration, donc on y descend
					int lj = i;
					do {
						symbol = dec.getChild(lj);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = this.currentTDS.findFunction(name);

						descendTDS();   // Descente dans la TDS de la fonction
						labelGenerator.addFunction(function,name);  // Création du label de la fonction

						// TODO : choisir dans quel registre registerIndex mettre le résultat du corps de la fonction : probablement toujours R0
						translate(body, registerIndex); // Génère code pour le corps de la fonction

						ascendTDS();
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));

					i = lj - 1;
//					ascendTDS();    // !! On ne remonte pas la TDS qu'on a créé à la première déclaration de fonction !!
					break;
				}
				case "var": { // dans le cas de la déclaration d'une variable
					Variable variable = this.currentTDS.findVariable(symbol.getChild(0).toString()); // Récupération de la variable déclarée
					Tree exp = symbol.getChild(1);
					if (variable.isAlreadyTranslated()) {   // on crée parfois (dans les cas d'une première déclaration de variable ou d'une redéclaration d'une variable) une nouvelle table
						descendTDS();
						this.currentTDS.findVariable(symbol.getChild(0).toString()); // On prend la bonne déclaration de cette variable
					}
					//TODO : générer le code de la déclaration de variable
					variable.setAlreadyTranslated(true);
					break;
				}
			}
		}
		Type result = translate(seq, registerIndex);
		while(childrenIndexStack.size() > initialDepth){    // On remonte les TDS pour revenir à la profondeur d'avant le LET
			ascendTDS();
		}
		return result;
	}

	private Type translateNil(Tree tree, int registerIndex) {
		return this.currentTDS.nilPseudoType;
	}

	private Type translateBreak(Tree tree, int registerIndex) {
		Tree parent = tree;
		Tree child = tree;
		//TODO : générer code pour Break
		return null;
	}
}
