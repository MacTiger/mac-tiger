package compile;

import misc.Notifier;
import org.antlr.runtime.tree.Tree;
import semantic.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	private List<Integer> childrenIndexStack;  // Pile des childrenIndex, mis à jour en descente et en remontée de TDS
	private Writer writer;  // Classe gérant les écritures de code au bon endroit (pour permettre d'écrire le code d'une fonction en plusieurs fois, si une autre fonction (assembleur) est nécessaire durant son écriture)
	private LabelGenerator labelGenerator;
	private RegisterManager registerManager;
	private int heapBase; // l'adresse qui suit la partie statique du tas
	private Map<String, Integer> strings; // chaînes de caractères statiques
	private int nextString; // nombre de chaînes de caractères statiques

	public TigerTranslator(Tree tree, SymbolTable root) {
		// Pour lancer le translator sur l'ensemble du programme, passer la TDS de niveau 0 (pas le root)
		this.currentTDS = root;     // TDS actuelle
		this.childrenIndexStack = new ArrayList<Integer>();
		this.childrenIndexStack.add(0);
		this.labelGenerator = new LabelGenerator(16);
		this.writer = new Writer(this.labelGenerator);
		this.registerManager = new RegisterManager(this.writer);
		this.heapBase = 4098;
		this.strings = new HashMap<String, Integer>();
		this.writeHeader();
		this.writeMainAndFunction(tree);
	}

	private void writeHeader() {
		for (Map.Entry<String, FunctionOrVariable> entry: this.currentTDS.getParent().getFunctionsAndVariables()) {
			String name = entry.getKey();
			String label = this.labelGenerator.getLabel(((Function) entry.getValue()).getSymbolTable(), name);
			switch (name) {
				case "concat": {
					String str1 = "R1";
					String str2 = "R2";
					String str3 = "R0";
					String size1 = "R3";
					String size2 = "R4";
					String size3 = "R5";
					String character = "R6"; // Code ASCII du caractère courant
					String inputPointer = "R7"; // Adresse du caractère d'entrée courant
					String outputPointer = "HP"; // Là où on va écrire le prochain caractère
					String word = "R8"; // 0 si on en est au premier mot, 1 sinon
		                  String rest = "R9"; // Reste dans les divisions
		                  String two = "R10";

					// On empile dans le tas la taille du nouveau string
					this.writer.writeHeader(label, String.format("LDW %s, (SP)4", str1));
					this.writer.writeHeader(String.format("LDW %s, (SP)2", str2));
					this.writer.writeHeader(String.format("LDW %s, (R0)-2", size1));
					this.writer.writeHeader(String.format("LDW %s, (R0)-4", size2));
					this.writer.writeHeader(String.format("ADD %s, %s, %s", size1, size2, size3));
					this.writer.writeHeader(String.format("STW %s, (%s)+", size3, outputPointer));

					// Constantes
					this.writer.writeHeader(String.format("LDW %s, #2", two));

					// Le nouveau string sera stocké là où l'on va commencer à écrire (sans la taille)
					this.writer.writeHeader(String.format("LDW %s, %s", str3, outputPointer));

					// (1) Extraction d'un caractère
					this.writer.writeHeader(String.format("LDB %s, (%s)+", character, inputPointer));
					this.writer.writeHeader(String.format("BMP 8")); // Saute en (3b)

					// (2) Empile le caractère courant
					this.writer.writeHeader(String.format("LDB %s, (%s)+", character, outputPointer));
					this.writer.writeHeader(String.format("BMP 2")); // Saute en (3a)

					// (3a) Empiler un mot (sauf le \0)
					this.writer.writeHeader(String.format("BMP -8")); // Saute en (1)
					// (3b)
					this.writer.writeHeader(String.format("TST %s", character));
					this.writer.writeHeader(String.format("BEQ 4", character)); // Si character = 0 : saute en (4a)
					this.writer.writeHeader(String.format("BMP -10")); // Saute en (2)

					// (4a) Passer au mot suivant ou terminer
					this.writer.writeHeader(String.format("TST %s", word));
					this.writer.writeHeader(String.format("BEQ 14")); // Si word = 0 : saute en (4c)
					// (4b) word = 1, il faut empiler \0
					this.writer.writeHeader(String.format("LDQ 0, %s", character));
					this.writer.writeHeader(String.format("LDB %s, (%s)+", character, outputPointer));
					this.writer.writeHeader(String.format("LDW %s, %s", rest, outputPointer));
					this.writer.writeHeader(String.format("DIV %s, %s, %s", rest, two, rest));
					this.writer.writeHeader(String.format("TST %s", rest));
					this.writer.writeHeader(String.format("BNE -10")); // Saute en (4b)
					this.writer.writeHeader(String.format("RTS"));
					// (4c) word = 0
					this.writer.writeHeader(String.format("ADQ 1, %s", word));
					this.writer.writeHeader(String.format("LDW %s, %s", inputPointer, str2));
					this.writer.writeHeader(String.format("BMP -30")); // Saute en (3a)

					break;
				}
				case "print": {
					this.writer.writeHeader(label, "LDW R0, (SP)2");
					this.writer.writeHeader("LDW R1, #WRITE_EXC");
					this.writer.writeHeader("TRP R1");
					this.writer.writeHeader("RTS");
					break;
				}
				case "printi": {
					this.writer.writeHeader(label, "LDW R1, (SP)2");
					this.writer.writeHeader("LDW R2, #WRITE_EXC");
					this.writer.writeHeader("LDW R3, SP");
					this.writer.writeHeader("LDQ 10, R4");
					this.writer.writeHeader("LDQ 0, R0");
					this.writer.writeHeader("STB R0, -(SP)");
					this.writer.writeHeader("LDW R0, R1");
					this.writer.writeHeader("BGE 28");
					this.writer.writeHeader("LDQ 45, R0");
					this.writer.writeHeader("STB R0, -(SP)");
					this.writer.writeHeader("LDW R0, SP");
					this.writer.writeHeader("TRP R2");
					this.writer.writeHeader("ADQ 1, SP");
					this.writer.writeHeader("LDW R0, #-32768");
					this.writer.writeHeader("CMP R0, R1");
					this.writer.writeHeader("BNE 8");
					this.writer.writeHeader("LDQ 56, R0");
					this.writer.writeHeader("STB R0, -(SP)");
					this.writer.writeHeader("LDW R1, #-3276");
					this.writer.writeHeader("NEG R1, R1");
					this.writer.writeHeader("LDW R0, R1");
					this.writer.writeHeader("DIV R0, R4, R1");
					this.writer.writeHeader("ADQ 48, R0");
					this.writer.writeHeader("STB R0, -(SP)");
					this.writer.writeHeader("TST R1");
					this.writer.writeHeader("BNE -12");
					this.writer.writeHeader("LDW R0, SP");
					this.writer.writeHeader("TRP R2");
					this.writer.writeHeader("LDW SP, R3");
					this.writer.writeHeader("RTS");
					break;
				}
				case "size": {
					this.writer.writeHeader(label, "LDW R0, (SP)2");
					this.writer.writeHeader("LDW R0, (R0)-2");
					this.writer.writeHeader("RTS");
					break;
				}
				default: {
					this.writer.writeHeader(label, "RTS"); // TODO
					break;
				}
			}
			this.writer.writeHeader();
		}
	}

	private void writeMainAndFunction(Tree tree) {
		int registerIndex = this.registerManager.provideRegister();
		this.translate(tree, registerIndex);
		this.registerManager.freeRegister();
	}

	private SymbolTable next() {
		int indexOfNextChild = childrenIndexStack.get(childrenIndexStack.size()-1);
		return this.currentTDS.getChild(indexOfNextChild);
	}

	private void descend() {
		// Met à jour this.currentTDS pour y mettre la TDS fille de la currentTDS de bon index, met à jour childrenIndexStack
		int indexOfNextChild = childrenIndexStack.get(childrenIndexStack.size()-1); // Récupère l'index de la TDS fille à prendre
		childrenIndexStack.set(childrenIndexStack.size()-1, indexOfNextChild + 1);  // Incrémente l'index de la prochaine TDS à prendre
		this.currentTDS = this.currentTDS.getChild(indexOfNextChild);
		this.childrenIndexStack.add(0); // Ajoute une entrée dans childrenIndexStack pour reprendre le compte pour la nouvelle currentTDS
		this.registerManager.descend(); // Descend le registerManagaer
		this.writer.descend(); // Descend le writer pour écrire le code de cette fonction
		String label = labelGenerator.getLabel(this.currentTDS); // Création ou récupération du label de la nouvelle currentTDS
		// Empilage du chaînage dynamique :
		this.writer.writeFunction(label, "STW BP, -(SP)// Empilage du chaînage dynamique"); // Partie LINK
		this.writer.writeFunction("LDW BP, SP  // Mise à jour du Base pointer");
		//Empilage du chaînage statique
		this.writer.writeFunction("STW R0, -(SP) // Empilage du chaînage statique");
	}

	private void ascend() {
		// Met à jour this.currentTDS en remontant de TDS, met à jour childrenIndexStack
		this.writer.writeFunction("ADQ 2, SP  // Dépilage du chaînage statique");
		this.writer.writeFunction("LDW BP, (SP)+ // Restauration du base pointer avec le chaînage dynamique"); // Partie UNLINK
		this.writer.writeFunction("RTS");
		this.writer.ascend(); // Remonte le writer : on a finit d'écrire le code de cette fonction (assembleur)
		this.registerManager.ascend();
		this.childrenIndexStack.remove(childrenIndexStack.size() - 1); // Retire le dernière index empilé
		this.currentTDS = this.currentTDS.getParent(); // Remonte de TDS

	}

	private void translate(Tree tree, int registerIndex) {
		switch (tree.getType ()) {
			case SEQ: this.translateSEQ(tree, registerIndex); return;
			case ARR: this.translateARR(tree, registerIndex); return;
			case REC: this.translateREC(tree, registerIndex); return;
			case CALL: this.translateCALL(tree, registerIndex); return;
			case ITEM: this.translateITEM(tree, registerIndex); return;
			case FIELD: this.translateFIELD(tree, registerIndex); return;
			case ID: this.translateID(tree, registerIndex); return;
			case STR: this.translateSTR(tree, registerIndex); return;
			case INT: this.translateINT(tree, registerIndex); return;
		}
		switch (tree.toString()) {
			case ":=": this.translateAssignment(tree, registerIndex); return;
			case "=": this.translateEqual(tree, registerIndex); return;
			case "<>": this.translateNotEqual(tree, registerIndex); return;
			case ">": this.translateStrictGreaterThan(tree, registerIndex); return;
			case "<": this.translateStrictLessThan(tree, registerIndex); return;
			case ">=": this.translateGreaterOrEqualThan(tree, registerIndex); return;
			case "<=": this.translateLessOrEqualThan(tree, registerIndex); return;
			case "|": this.translateOrOperator(tree, registerIndex); return;
			case "&": this.translateAndOperator(tree, registerIndex); return;
			case "+": this.translateAddOperator(tree, registerIndex); return;
			case "-": this.translateSubOperator(tree, registerIndex); return;
			case "*": this.translateMulOperator(tree, registerIndex); return;
			case "/": this.translateDivOperator(tree, registerIndex); return;
			case "if": this.translateIf(tree, registerIndex); return;
			case "while": this.translateWhile(tree, registerIndex); return;
			case "for": this.translateFor(tree, registerIndex); return;
			case "let": this.translateLet(tree, registerIndex); return;
			case "nil": this.translateNil(tree, registerIndex); return;
			case "break": this.translateBreak(tree, registerIndex); return;
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.translate(tree.getChild(i), registerIndex);
				}
			}
		}
	}

	private void translateStrictGreaterThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) != SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BGT 6"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {
			// TODO: code translateStrictGreaterThan lorsque les fils sont des strings
		}
	}

	private void translateStrictLessThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BLW 6"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {
			int registerAddA;//contient adresse de A
			registerAddA=registerManager.provideRegister();
			int registerAddB;//contient adresse de B
			registerAddB=registerManager.provideRegister();
			int registerDep;//Déplacement tas
			registerDep=registerManager.provideRegister();
			int registerOp;//sert à faire des masques
			registerOp=registerManager.provideRegister();

			//Met adresse de string1 (resp2) dans registerAddA (reps.B)
			writer.writeFunction(String.format("LDW R"+registerAddA+",R"+registerLeft));
			writer.writeFunction(String.format("LDW R"+registerAddB+",R"+registerRight));
			//Met à 0 le décalage
			writer.writeFunction(String.format("LDW R"+registerDep+",#0"));
			//
			//Met les adresses des strings dans registerLeft et registerRight
			writer.writeFunction(String.format("LDW R"+registerLeft+",R"+registerAddA));
			writer.writeFunction(String.format("LDW R"+registerRight+",R"+registerAddB));

			//Décalage dans le tas de ce nécessaire
			writer.writeFunction(String.format("ADD R"+registerLeft+",R"+registerDep+",R"+registerLeft));
			writer.writeFunction(String.format("ADD R"+registerRight+",R"+registerDep+",R"+registerRight));

			//Charge deux caracteres dans registerLeft et registerRight
			writer.writeFunction(String.format("LDW R"+registerLeft+",(R"+registerLeft+")"));
			writer.writeFunction(String.format("LDW R"+registerRight+",(R"+registerRight+")"));

			//Masque
			writer.writeFunction(String.format("LDW R"+registerOp+",#65280"));
			writer.writeFunction(String.format("AND R"+registerLeft+",R"+registerOp+",R"+registerOp));
			writer.writeFunction(String.format("BNE 20"));//A VOIR
			//Premier caractere de str1 est vide
			writer.writeFunction(String.format("LDW R"+registerOp+",#65280"));
			writer.writeFunction(String.format("AND R"+registerRight+",R"+registerOp+",R"+registerOp));
			writer.writeFunction(String.format("BNE 6"));
			//Premier caractere de str2 est vide = égalité
			writer.writeFunction(String.format("LDW R"+registerIndex+", #0"));
			writer.writeFunction(String.format("BMP 4"));
			//Sinon : vrai
			writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
			writer.writeFunction(String.format("BMP 36"));

			writer.writeFunction(String.format("SUB R"+registerLeft+",R"+registerRight+",R"+registerOp));
			writer.writeFunction(String.format("BGE 6"));
			// str1 < str2
			writer.writeFunction(String.format("LDW R"+registerIndex+",#1"));
			writer.writeFunction(String.format("BMP 18"));
			writer.writeFunction(String.format("BEQ 6"));
			// str1 > str2
			writer.writeFunction(String.format("LDW R"+registerIndex+",#0"));
			writer.writeFunction(String.format("BMP 18"));

			//str1 == str2
			//Test de caractere 2 : masque #255
			writer.writeFunction(String.format("LDW R"+registerOp+",#255"));
			writer.writeFunction(String.format("AND R"+registerLeft+", R"+registerOp+",R"+registerOp));
			writer.writeFunction(String.format("BNE 6"));
			//caractere 2 vaut 0 Egalité : false
			writer.writeFunction(String.format("LDW R"+registerLeft+",#0"));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("ADQ 2, R"+registerDep));
			writer.writeFunction(String.format("BMP -76"));

			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();

			// TODO: code translateStrictLessThan lorsque les fils sont des strings

		}

		registerManager.freeRegister();
	}

	private void translateGreaterOrEqualThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) != SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BGE 6"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {
			// TODO: code translateGreaterOrEqualThan lorsque les fils sont des strings
		}
	}

	private void translateLessOrEqualThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isString = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);
		System.err.println(currentTDS.treeTypeHashMap.get(tree.getChild(0)));
		if (!isString) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BLE 6"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {//inf.src
			int registerAddA;//contient adresse de A
			registerAddA=registerManager.provideRegister();
			int registerAddB;//contient adresse de B
			registerAddB=registerManager.provideRegister();
			int registerDep;//Déplacement tas
			registerDep=registerManager.provideRegister();
			int registerOp;//sert à faire des masques
			registerOp=registerManager.provideRegister();

			//Met à 0 le décalage
			writer.writeFunction(String.format("LDW R"+registerDep+",#0"));

			//Met les adresses des strings dans regsiterAdd A et B
			writer.writeFunction(String.format("LDW R"+registerAddA+",R"+registerLeft));
			writer.writeFunction(String.format("LDW R"+registerAddB+",R"+registerRight));

			//Met les adresses des strings dans registerLeft et registerRight
			writer.writeFunction(String.format("LDW R"+registerLeft+",R"+registerAddA));
			writer.writeFunction(String.format("LDW R"+registerRight+",R"+registerAddB));

			//Se décaler dans le tas
			writer.writeFunction(String.format("ADD R"+registerLeft+",R"+registerDep+",R"+registerLeft));
			writer.writeFunction(String.format("ADD R"+registerRight+",R"+registerDep+",R"+registerRight));

			//Charge le registre avec les deux premiers caracteres
			writer.writeFunction(String.format("LDW R"+registerLeft+",(R"+registerLeft+")"));
			writer.writeFunction(String.format("LDW R"+registerRight+",(R"+registerRight+")"));


			//Met ff00 dans registerOp
			writer.writeFunction(String.format("LDW R"+registerOp+",#65280"));
			//Met le premier caractere de registerLeft en regIndex
			writer.writeFunction(String.format("AND R"+registerLeft+",R"+registerOp+",R"+registerOp));


			//test sur valeur du caractere1
			writer.writeFunction(String.format("BNE 6"));
			// => si vaut null : true => fin du programme
			writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
			writer.writeFunction(String.format("BMP 36"));//fin des instructions
			// => si ne vaut pas null : continue le programme

			//Fait str1-str2
			writer.writeFunction(String.format("SUB R"+registerLeft+",R"+registerRight+",R"+registerOp));
			//test si str1-str2 >= 0
			writer.writeFunction(String.format("BGE 6"));
			// => si str1 - str2 < 0 : true => fin du programme
			writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
			writer.writeFunction(String.format("BMP 18"));//fin des instructions

			//test si str1-str2 == 0
			// si str1 != str2, alors str1 > str2 : false
			writer.writeFunction(String.format("BEQ 6"));
			writer.writeFunction(String.format("LDW R"+registerIndex+", #0"));
			writer.writeFunction(String.format("BMP 18"));//fin des instructions
			//A tester !

			//Met 00ff dans le registerOp
			writer.writeFunction(String.format("LDW R"+registerOp+",#255"));
			//Met le caractere 2 de str1 dans registerIndex
			writer.writeFunction(String.format("AND R"+registerLeft+",R"+registerOp+",R"+registerOp));
			//test si caractere 2 vaut null
			writer.writeFunction(String.format("BNE 6"));
			// si caractere 2 de str1 est null : renvoyer true
			writer.writeFunction(String.format("LDW R"+registerIndex+",#1"));
			writer.writeFunction(String.format("BMP 4"));

			//ici on sait qu'il faut continuer : décalage
			writer.writeFunction(String.format("ADQ 2, R"+registerDep));
			writer.writeFunction(String.format("BMP -62"));//on boucle

			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();
		}
		registerManager.freeRegister();
	}

	private void translateEqual(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) != SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BEQ 4"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));

			registerManager.freeRegister();

		} else {
			// TODO: code translateEqual lorsque les fils sont des strings
		}
	}

	private void translateNotEqual(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) != SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BEQ 6"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));

			registerManager.freeRegister();

		} else {
			// TODO: code translateNotEqual lorsque les fils sont des strings
		}
	}

	private void translateAndOperator(Tree tree, int registerIndex) {

		String endLabel = this.labelGenerator.getLabel(tree, "end");

		for (int i = 0; i < tree.getChildCount(); i++) {

			this.translate(tree.getChild(i), registerIndex);

			// Si le résultat est nul, on peut sauter à la fin du *and*
			this.writer.writeFunction(String.format("BEQ %s-$-2", endLabel));
		}

		this.writer.writeFunction(endLabel, "NOP");
	}

	private void translateOrOperator(Tree tree, int registerIndex) {

		String endLabel = this.labelGenerator.getLabel(tree, "end");

		for (int i = 0; i < tree.getChildCount(); i++) {

			this.translate(tree.getChild(i), registerIndex);

			// Si le résultat n'est pas nul, on peut sauter à la fin du *or*
			this.writer.writeFunction(String.format("BNE %s-$-2", endLabel));
		}

		this.writer.writeFunction(endLabel, "NOP");
	}

	private void translateAddOperator(Tree tree, int registerIndex) {
		translate(tree.getChild(0), registerIndex);
		int register = registerManager.provideRegister();
		for (int i = 1; i < tree.getChildCount(); i++) {
			translate(tree.getChild(i), register);
			this.writer.writeFunction(String.format("ADD R%d, R%d, R%d", registerIndex, register, registerIndex));
		}
		registerManager.freeRegister();
	}

	private void translateSubOperator(Tree tree, int registerIndex) {
		translate(tree.getChild(0), registerIndex);
		if (tree.getChildCount() == 1) {
			this.writer.writeFunction(String.format("NEG R%d, R%d", registerIndex, registerIndex));
		} else {
			int register = registerManager.provideRegister();
			translate(tree.getChild(1), register);
			this.writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerIndex, register, registerIndex));
			registerManager.freeRegister();
		}
	}

	private void translateMulOperator(Tree tree, int registerIndex) {
		translate(tree.getChild(0), registerIndex);
		int register = registerManager.provideRegister();
		for (int i = 1; i < tree.getChildCount(); i++) {
			translate(tree.getChild(i), register);
			this.writer.writeFunction(String.format("MUL R%d, R%d, R%d", registerIndex, register, registerIndex));
		}
		registerManager.freeRegister();
	}

	// A/B
	private void translateDivOperator(Tree tree, int registerIndex) {

		//registerIndex contient A
		int registerA = registerManager.provideRegister();
		translate(tree.getChild(0), registerIndex);

		//Met B dans registerB
		int registerB=registerManager.provideRegister();
		translate(tree.getChild(1),registerB);

		/*
		//Met A dans registerAsave : dernier registre réservé
		int registerAsave=registerManager.provideRegister();
		this.writer.writeMain("LDW R"+registerAsave+", R"+registerA);
		*/

		//Opération diviser va modifier registerA
		this.writer.writeFunction(String.format("DIV R%d, R%d, R%d", registerA, registerB, registerIndex));

		/*
		this.writer.writeMain("LDW R"+registerA+"");
		*/
		registerManager.freeRegister();//Libère registerB
		registerManager.freeRegister();//Libère registerA
	}

	private void translateCALL(Tree tree, int registerIndex) {
		/* Appel d'une fonction
		 *
		 */
		//Récupère la tds de la fonction appelée
		String name = tree.getChild(0).toString();
		Function function = currentTDS.findFunction(name);
		Type returnType = null;
		SymbolTable table = function.getSymbolTable();


		// Sauvegarde les registres de l'environnement appelant
		registerManager.saveAll();

		int sizeOfArgs = 0;
		int sizeOfArg = 0;
		Tree child;
		for (int i = 1, l = tree.getChildCount(); i < l; ++i) {   //Parcours des arguments de la fonction
			child = tree.getChild(i);
			translate(child, registerIndex);
			sizeOfArg = SymbolTable.treeTypeHashMap.get(child).getSize();
			sizeOfArgs += sizeOfArg;
			this.writer.writeFunction(String.format("ADQ -%d,SP  //Empile l'argument numéro %d/%d", sizeOfArg, i, l-1));    // Il faut tenir compte de la taille de l'argument qu'on empile
			this.writer.writeFunction("STW R"+registerIndex+", (SP)");
		}//Tous les arguments sont empilés

		writeEntryFunction(table, name);    // Génération du code d'appel à la fonction et calcul de son chaînage statique

		this.writer.writeFunction(String.format("ADQ %d,SP  // Dépile les arguments de la fonction", sizeOfArgs));  // Dépile les arguments

		registerManager.restoreAll();
		if (function.getType() != null) {
			this.writer.writeFunction(String.format("LDW R%s, R0", registerIndex));
		}
	}

	private void translateREC(Tree tree, int registerIndex) {
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
	}

	private void translateARR(Tree tree, int registerIndex) {
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
	}

	private void translateAssignment(Tree tree, int registerIndex) {
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
	}

	private void translateSEQ(Tree tree, int registerIndex) {
		for (int i = 0, l = tree.getChildCount(); i < l; ++i) {
			//TODO : gérer les registres :
			this.translate(tree.getChild(i), registerIndex);
		}
	}

	private void translateSTR(Tree tree, int registerIndex) {
		List<Integer> ordinals = new ArrayList<Integer>();
		String inputString = tree.toString();
		String commentString = "";
		String outputString = "";
		for (int i = 1, li = inputString.length() - 1; i < li; ++i) {
			char character = inputString.charAt(i);
			if (character == '\\') {
				character = inputString.charAt(++i);
				switch (character) {
					case 'n': {
						commentString += "\\n";
						ordinals.add(10);
						outputString += "\n";
						break;
					}
					case 't': {
						commentString += "\\t";
						ordinals.add(9);
						outputString += "\t";
						break;
					}
					case '"': {
						commentString += "\\\"";
						ordinals.add(34);
						outputString += "\"";
						break;
					}
					case '\\': {
						commentString += "\\\\";
						ordinals.add(92);
						outputString += "\\";
						break;
					}
					case '^': {
						commentString += "\\^";
						character = inputString.charAt(++i);
						commentString += character;
						if (character != '@') { // on ignore les caractères nuls
							ordinals.add(character - 64);
							outputString += (char) (character - 64);
						}
						break;
					}
					case '0':
					case '1': {
						commentString += "\\" + character;
						int ordinal = (character - 48) * 100;
						character = inputString.charAt(++i);
						commentString += character;
						ordinal += (character - 48) * 10;
						character = inputString.charAt(++i);
						commentString += character;
						ordinal += character - 48;
						if (ordinal != 0) { // on ignore les caractères nuls
							ordinals.add(ordinal);
							outputString += (char) ordinal;
						}
						break;
					}
					default: {
						while ((character = inputString.charAt(++i)) != '\\');
						break;
					}
				}
			} else {
				commentString += character;
				ordinals.add((int) character);
				outputString += character;
			}
		}
		if (this.strings.containsKey(outputString)) {
			this.writer.writeFunction(String.format("LDW R%d, #%s", registerIndex, this.strings.get(outputString)));
		} else {
			int size = ordinals.size();
			ordinals.add(0); // on ajoute le caractère nul de fin de chaîne
			if ((ordinals.size() % 2) == 1) {
				ordinals.add(0); // on complète pour avoir une chaîne de taille paire
			}
			this.writer.writeMain(String.format("LDW R0, #%d // Allocation statique de la chaîne \"%s\"", size, commentString));
			this.writer.writeMain("STW R0, (HP)+");
			this.heapBase += 2;
			for (int i = 0, li = ordinals.size(); i < li; i += 2) {
				this.writer.writeMain(String.format("LDW R0, #%d", (ordinals.get(i) << 8) | ordinals.get(i + 1)));
				this.writer.writeMain("STW R0, (HP)+");
			}
			this.strings.put(outputString, this.heapBase);
			this.writer.writeFunction(String.format("LDW R%d, #%d", registerIndex, this.heapBase));
			this.heapBase += ordinals.size();
		}
	}

	/**
	 * Ecrit le code pour mettre l'adresse de l'identifiant dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 * @return
	 */
	private void translateID(Tree tree, int registerIndex) {
		String name = tree.toString();
		Variable variable = this.currentTDS.findVariable(name);

		int depl_stat = -2;  // Taille de zone de liaison : chaînage dynamique et chaînage statique // TODO : Vérifier la bonne valeur du déplacement statique
		int deplacementVariable = variable.getOffset();
		String typeOfVar;
		if (deplacementVariable < 0){   // Si variable est une variable locale
			deplacementVariable += depl_stat;   // On doit compter le déplacement statique
			typeOfVar = "variable locale";
		} else {    // variable est un argument de fonction
			deplacementVariable += 4;   // Taille de l'adresse de retour
			typeOfVar = "argument";
		}

		int countStaticChain = this.currentTDS.countStaticChainToVariable(tree.toString());

		this.writer.writeFunction(String.format("LDW R%d, BP  // ID_BEGIN : Préparation pour le chargement l'adresse de %s \"%s\" dans un registre", registerIndex, typeOfVar, name)); // Copie le registre de base dans le registre résultat  // TODO : à sortir de la boucle
		if (countStaticChain > 0) { // S'il y des chaînages statiques à remonter :
			int addressRegister = 0;    //TODO : réserver un registre pour la remontée de chaînage statique


			int loopRegister = this.registerManager.provideRegister();

			this.writer.writeFunction(String.format("LDQ %d, R%d  // Début de remontée de chaînage statique pour charger l'adresse de %s \"%s\" dans un registre" ,countStaticChain, loopRegister, typeOfVar, name));
			// Début de boucle :
			this.writer.writeFunction(String.format("LDW R%d, (R%d)%d", registerIndex, registerIndex, depl_stat));
			this.writer.writeFunction(String.format("ADQ -1, R%d", loopRegister));
			this.writer.writeFunction(String.format("BNE %d  // Fin de remontée de chaînage statique pour charger l'adresse de %s \"%s\" dans un registre", -6, typeOfVar, name)); // Jump à (6 - 2)/3 = 2 instructions plus tôt. Le -2 c'est pour cette instruction de saut
			// Fin de boucle

			this.registerManager.freeRegister();
		}
		this.writer.writeFunction(String.format("ADQ %d, R%d  // ID_END : Chargement de l'adresse de %s \"%s\" dans un registre", deplacementVariable, registerIndex,  typeOfVar, name));   // L'adresse de la variable recherchée est maintenant dans registre voulu

	}

	private void translateITEM(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		this.translate(exp, registerIndex);
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
	}

	private void translateFIELD(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		this.translate(exp, registerIndex);
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
	}

	private void translateINT(Tree tree, int registerIndex) {
		this.writer.writeFunction(String.format("LDW R%d, #%s", registerIndex, tree.toString()));
	}

	private void translateIf(Tree tree, int registerIndex) {
		this.translate(tree.getChild(0), registerIndex);
		boolean hasElse = (tree.getChildCount() == 3);

		if (hasElse) {
			// Labels
			String elseLabel = this.labelGenerator.getLabel(tree, "else");
			String endifLabel = this.labelGenerator.getLabel(tree, "endif");

			// On saute au `else` si l'instruction évaluée est fausse
			this.writer.writeFunction(String.format("BEQ %s-$-2", elseLabel));
			// On génère le code de `then`
			this.translate(tree.getChild(1), registerIndex);
			// On saute à la fin
			this.writer.writeFunction(String.format("BMP %s-$-2", endifLabel));
			// On génère le l'étiquette et le code de else
			this.writer.writeFunction(elseLabel, "NOP");
			this.translate(tree.getChild(2), registerIndex);
			// Étiquette endif
			this.writer.writeFunction(endifLabel, "NOP");

		} else {
			// Labels
			String endifLabel = this.labelGenerator.getLabel(tree, "endif");

			// On saute au `endif` si l'instruction évaluée est fausse
			this.writer.writeFunction(String.format("BEQ %s-$-2", endifLabel));
			// On génère le code de `then`
			this.translate(tree.getChild(1), registerIndex);
			// Étiquette endif
			this.writer.writeFunction(endifLabel, "NOP");

		}
	}

	private void translateWhile(Tree tree, int registerIndex) {
		//TODO : Génerer code de while (penser à réserver les registres nécessaires)
		labelGenerator.getLabel(tree, "start");   // Création des labels de cette boucle while
		this.translate(tree.getChild(0),registerIndex);
		this.translate(tree.getChild(1),registerIndex);
	}

	private void translateFor(Tree tree, int registerIndex) {   //TODO : générer le code de la boucle for
		labelGenerator.getLabel(tree, "test");    // Création des labels de la fonction assembleur liée à ce for (label de la ligne du test, de fin de for)

		Variable index = currentTDS.findVariable(tree.getChild(0).toString());  // Récupère la variable de boucle
		//TODO : gérer les registres :
		translate(tree.getChild(1), registerIndex);	// Génère le code pour la borne inférieure du for
		translate(tree.getChild(2), registerIndex);	// Génère le code pour la borne supérieure du for
		this.writer.writeFunction(String.format("JSR @%s", this.labelGenerator.getLabel(this.next())));
		this.descend(); // Met à jour this.currentTDS avec la bonne TDS fille
		translate(tree.getChild(3), registerIndex);	// Génère le code de la boucle for
		this.ascend();    // Met à jour this.currentTDS avec la TDS père (revient à la currentTDS en entrée de cette fonction)
	}

	/**
	 * Traduit l'AST passé en paramètre représentant une expression LET, met le résultat de l'expression dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 * @return
	 */
	private void translateLet(Tree tree, int registerIndex) {
		SymbolTable table = this.currentTDS;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": { // dans le cas d'une suite de déclarations de types
					this.registerManager.saveAll();
					writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next()));
					this.descend(); // On avait créé une nouvelle table avant la première déclaration, donc on y descend

					int lj = i;
					symbol = dec.getChild(lj);
					do{
					} while(++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
					i = lj - 1;
					break;
				}
				case "function": { // dans le cas d'une suite de déclarations de fonctions
					this.registerManager.saveAll();
					writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next()));
					this.descend();   // On avait créé une nouvelle table avant la première déclaration, donc on y descend
					int lj = i;
					do {
						symbol = dec.getChild(lj);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = this.currentTDS.findFunction(name);
						labelGenerator.getLabel(function.getSymbolTable(), name); // Création du label de la fonction
						this.descend();   // Descente dans la TDS de la fonction
						// TODO : choisir dans quel registre registerIndex mettre le résultat du corps de la fonction : probablement toujours R0
						translate(body, registerIndex); // Génère code pour le corps de la fonction
						this.ascend();
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));

					i = lj - 1;
//					ascendTDS();    // !! On ne remonte pas la TDS qu'on a créé à la première déclaration de fonction !!
					break;
				}
				case "var": { // dans le cas de la déclaration d'une variable
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					// TODO : choisir dans quel registre registerIndex mettre le résultat de l'initialisation de la variable ; attention la déclaration de la variable et son initialisation ne sont pas forcément dans le même environnement
					translate(exp, registerIndex);  //TODO : est-ce avant de descendre dans la TDS qu'il faut évaluer ?
					if (this.currentTDS == table) {
						this.registerManager.saveAll();
						writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next()));
						this.descend();
					} else {
						FunctionOrVariable functionOrVariable = this.currentTDS.getFunctionsAndVariables().get(name);
						if (functionOrVariable instanceof Variable && ((Variable) functionOrVariable).isTranslated()) {
							this.registerManager.saveAll();
							writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next()));
							this.descend();
						}
					}
					Variable variable = this.currentTDS.findVariable(name); // Récupération de la variable déclarée
					variable.translate(true);
					//TODO : générer le code de la déclaration de variable
					// TODO : empiler la variable
					break;
				}
			}
		}
		translate(seq, registerIndex);
		while (this.currentTDS != table) {    // On remonte les TDS pour revenir à la profondeur d'avant le LET
			this.ascend();
		}
	}

	private void translateNil(Tree tree, int registerIndex) {
		this.writer.writeFunction(String.format("LDW R%d, #NIL", registerIndex));
	}

	private void translateBreak(Tree tree, int registerIndex) {
		Tree parent = tree;
		Tree child = tree;
		//TODO : générer code pour Break
	}

	public String toString() {
		return this.writer.toString();
	}


	/**
	 * Ecrit le code (coté appelant) d'appel de fonction TIGER (portant un nom)
	 * En fin de cette méthode, le registre R0 contient le chaînage statique de la fonction appelée. Il n'a pas été reservé via le RegisterManager
	 * @param TDSDest TDS de la fonction appelée
	 * @param name nom de la fonction tiger appelée
	 */
	public void writeEntryFunction(SymbolTable TDSDest, String name){
		this.writeStaticLinking(TDSDest);
		this.writer.writeFunction(String.format("JSR @%s", labelGenerator.getLabel(TDSDest,name)));
	}

	/**
	 * Ecrit le code (coté appelant) d'appel de fonction ASSEMBLEUR (ne portant pas de nom)
	 * @param TDSDest TDS de la fonction appelée
	 */
	public void writeEntryFunction(SymbolTable TDSDest){
		this.writeStaticLinking(TDSDest);
		this.writer.writeFunction(String.format("JSR @%s", labelGenerator.getLabel(TDSDest)));
	}

	/**
	 * Calcule de chaînage statique de la fonction assembleur de TDS TDSDest et le met dans R0
	 * En fin de cette méthode, le registre R0 contient le chaînage statique de la fonction appelée. Il n'a pas été reservé via le RegisterManager
	 * @param TDSDest TDS de la fonction dont on calcule le chaînage statique
	 */
	public void writeStaticLinking(SymbolTable TDSDest){
		if (TDSDest.getDepth() == 1){   // On ne calcule pas le chaînage statique si on appelle une fonction built-in   //TODO : vérifier que c'est la bonne profondeur
			return ;
		}
		int depl_stat = -2; // TODO:  Vérifier la bonne valeur du déplacement statique

		this.writer.writeFunction("LDW R0, BP  // STATIC_LINK_BEGIN : Calcul du chaînage statique");

		int loopRegister = this.registerManager.provideRegister();
		this.writer.writeFunction(String.format("LDQ %d, R%s ", this.currentTDS.getDepth()-TDSDest.getDepth() + 2, loopRegister));  // TODO : pourquoi currentTDS.getDepth()-TDSDest.getDepth() + 2 ne convient pas ?
		// Début de boucle :
		this.writer.writeFunction(String.format("LDW R0, (R0)%d", depl_stat));
		this.writer.writeFunction(String.format("ADQ -1, R%d", loopRegister));
		this.writer.writeFunction(String.format("BNE %d  // STATIC_LINK_END : Fin du calcul du chaînage statique", -8)); // Jump à 2 +1 instructions (il faut compter celle là) plus tôt
		// Fin de boucle

		this.registerManager.freeRegister();    // On libère le registre de boucle
	}
}
