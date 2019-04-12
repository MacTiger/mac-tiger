package compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

import static misc.Constants.wordSize;

import static syntactic.TigerParser.SEQ;
import static syntactic.TigerParser.ARR;
import static syntactic.TigerParser.REC;
import static syntactic.TigerParser.CALL;
import static syntactic.TigerParser.ITEM;
import static syntactic.TigerParser.FIELD;
import static syntactic.TigerParser.ID;
import static syntactic.TigerParser.STR;
import static syntactic.TigerParser.INT;

import semantic.*;

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
				case "chr": {
					// TODO: débugger cette fonction
					this.writer.writeHeader(label, "LDW R2, (SP)2");

					// Si on donne un nombre strictement négatif, sauter en (*)
					this.writer.writeHeader("BLW 20");
					this.writer.writeHeader("LDQ 127, R1");
					this.writer.writeHeader("SUB R1, R2, R1");

					// Si on donne un nombre plus grand ou égal à 128, sauter en (*)
					this.writer.writeHeader("BLW 14");

					// Si le nombre est valide, on ajoute la chaîne de (un seul) caractère dans le tas
					this.writer.writeHeader("LDW R1, #1");
					this.writer.writeHeader("STW R1, (HP)+");
					this.writer.writeHeader("LDW R0, HP");
					this.writer.writeHeader("SWB R2, R2");
					this.writer.writeHeader("STW R2, (HP)+");
					this.writer.writeHeader("RTS");

					// (1) Termine le programme
					this.writer.writeHeader("TRP #EXIT_EXC");
					break;
				}
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
					this.writer.writeHeader(String.format("LDW %s, (%s)-2", size1, str1));
					this.writer.writeHeader(String.format("LDW %s, (%s)-2", size2, str2));
					this.writer.writeHeader(String.format("ADD %s, %s, %s", size1, size2, size3));
					this.writer.writeHeader(String.format("STW %s, (%s)+", size3, outputPointer));

					// Initialisation des registres
		            this.writer.writeHeader(String.format("LDW %s, #0", word));
					this.writer.writeHeader(String.format("LDW %s, #2", two));
					this.writer.writeHeader(String.format("LDW %s, %s", str3, outputPointer));
					this.writer.writeHeader(String.format("LDW %s, %s", inputPointer, str1));

					// Saute en (3a)
					this.writer.writeHeader(String.format("BMP 8"));

					// (1) Extraction d'un caractère
					this.writer.writeHeader(String.format("LDB %s, (%s)+", character, inputPointer));
					this.writer.writeHeader(String.format("BMP 6")); // Saute en (3b)

					// (2) Empile le caractère courant
					this.writer.writeHeader(String.format("STB %s, (%s)+", character, outputPointer));
					this.writer.writeHeader(String.format("BMP 0")); // Saute en (3a)

					// (3a) Empiler un mot (sauf le \0)
					this.writer.writeHeader(String.format("BMP -10")); // Saute en (1)
					// (3b)
					this.writer.writeHeader(String.format("TST %s", character));
					this.writer.writeHeader(String.format("BEQ 2", character)); // Si character = 0 : saute en (4a)
					this.writer.writeHeader(String.format("BMP -12")); // Saute en (2)

					// (4a) Passer au mot suivant ou terminer
					this.writer.writeHeader(String.format("TST %s", word));
					this.writer.writeHeader(String.format("BEQ 14")); // Si word = 0 : saute en (4c)
					// (4b) word = 1, il faut empiler \0
					this.writer.writeHeader(String.format("LDQ 0, %s", character));
					this.writer.writeHeader(String.format("STB %s, (%s)+", character, outputPointer));
					this.writer.writeHeader(String.format("LDW %s, %s", rest, outputPointer));
					this.writer.writeHeader(String.format("DIV %s, %s, %s", rest, two, rest));
					this.writer.writeHeader(String.format("TST %s", rest));
					this.writer.writeHeader(String.format("BNE -12")); // Saute en (4b)
					this.writer.writeHeader(String.format("RTS"));
					// (4c) word = 0
					this.writer.writeHeader(String.format("ADQ 1, %s", word));
					this.writer.writeHeader(String.format("LDW %s, %s", inputPointer, str2));
					this.writer.writeHeader(String.format("BMP -32")); // Saute en (3a)

					break;
				}
				case "exit": {
					this.writer.writeHeader(label, "LDW R0, (SP)2");
					this.writer.writeHeader("TRP #EXIT_EXC");
					break;
				}
				case "not": {
					this.writer.writeHeader(label, "LDW R0, (SP)2");
					this.writer.writeHeader("BEQ 6");
					this.writer.writeHeader("LDW R0, #0");
					this.writer.writeHeader("BMP 4");
					this.writer.writeHeader("LDW R0, #1");
					this.writer.writeHeader("RTS");
					break;
				}
				case "ord": {
					this.writer.writeHeader(label, "LDW R1, (SP)2");
					this.writer.writeHeader("LDW R0, (R1)-2");
					this.writer.writeHeader("BNE 4");
					this.writer.writeHeader("LDQ -1, R0"); // Renvoit -1 si chaîne vide
					this.writer.writeHeader("RTS");
					this.writer.writeHeader("LDB R0, (R1)");
					this.writer.writeHeader("RTS");
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
				case "getchar": {
					this.writer.writeHeader(label, "LDQ 1, R0");
					this.writer.writeHeader("STW R0, (HP)+");
					this.writer.writeHeader("LDW R0, HP");
					this.writer.writeHeader("TRP #READ_EXC");
					this.writer.writeHeader("LDW R0, (HP)");
					this.writer.writeHeader("LDW R1, #0xFF00");
					this.writer.writeHeader("AND R1, R0, R0");
					this.writer.writeHeader("BEQ 8"); // Saute en (*) si R1 vaut zéro
					this.writer.writeHeader("STW R0, (HP)");
					this.writer.writeHeader("LDW R0, HP");
					this.writer.writeHeader("ADQ 2, HP");
					this.writer.writeHeader("BMP 10"); // Saute en (**)
					this.writer.writeHeader("LDW R0, HP"); // (*)
					this.writer.writeHeader("ADQ -2, HP");
					this.writer.writeHeader("LDQ 0, R1");
					this.writer.writeHeader("STW R1, (HP)");
					this.writer.writeHeader("ADQ 4, HP");
					this.writer.writeHeader("RTS"); // (**)
					break;
				}
				case "size": {
					this.writer.writeHeader(label, "LDW R0, (SP)2");
					this.writer.writeHeader("LDW R0, (R0)-2");
					this.writer.writeHeader("RTS");
					break;
				}
				//On suppose i >= 0 et n>=0
				case "substring":{

					//Adresse str
					this.writer.writeHeader(label,"LDW R1,(SP)6");
					//Numéro de la lettre i
					this.writer.writeHeader("LDW R3,(SP)4");
					//Lg demandée m
					this.writer.writeHeader("LDW R4,(SP)2");


					//Test préliminaire i et n
					this.writer.writeHeader("TST R3");
					this.writer.writeHeader("BGE 12");//Go fin prog i<0
					this.writer.writeHeader("LDW R5, #0 ");
					this.writer.writeHeader("STW R5, (HP)+ ");
					this.writer.writeHeader("LDW R0, HP" );
					this.writer.writeHeader("STW R5, (HP)+");
					this.writer.writeHeader("RTS");//FIN

					this.writer.writeHeader("TST R4");
					this.writer.writeHeader("BGE 12");//Go fin prog n<0
					this.writer.writeHeader("LDW R5, #0 ");
					this.writer.writeHeader("STW R5, (HP)+");
					this.writer.writeHeader("LDW R0, HP");
					this.writer.writeHeader("STW R5, (HP)+");
					this.writer.writeHeader("RTS");//FIN


					//Check les longueurs
					this.writer.writeHeader("ADQ -2,R1");
					this.writer.writeHeader("LDW R2,(R1)");
					this.writer.writeHeader("ADQ 2,R1");
					this.writer.writeHeader("ADD R3,R4,R5");
					this.writer.writeHeader("SUB R2,R5,R2");
					this.writer.writeHeader("BGE 12");//FIN
					this.writer.writeHeader("LDW R5, #0");
					this.writer.writeHeader("STW R5, (HP)+");
					this.writer.writeHeader("LDW R0, HP");
					this.writer.writeHeader("STW R5, (HP)+");
					this.writer.writeHeader("RTS");//FIN
					//Si n-(i+m) < 0 STOP


					//Fait i %2 => qutotient : nombre de mots à "sauter"
					//Reste = nb d'octet à sauter (0 ou 1)
					this.writer.writeHeader("LDW R2,#2");
					this.writer.writeHeader("DIV R3,R2,R2");
					this.writer.writeHeader("LDW R5, #2");
					this.writer.writeHeader("MUL R2,R5,R2");
					this.writer.writeHeader("ADD R2,R1,R1");
					//Ajoute quotient*2 à adresse str


					//Met lg en HP puis incrémente HP
					// R0 a @ du premier caractere
					this.writer.writeHeader("STW R4,(HP)");
					this.writer.writeHeader("ADQ 2,HP");
					this.writer.writeHeader("LDW R0,HP");


					this.writer.writeHeader("TST R3");
					this.writer.writeHeader("BEQ 14");
					//SI R3=1, on décalle de 1 octet pour écrire le suivant
					this.writer.writeHeader("ADQ 1,R1");
					this.writer.writeHeader("LDB R5,(R1)");
					this.writer.writeHeader("STB R5,(HP)");
					this.writer.writeHeader("ADQ 1,HP");
					this.writer.writeHeader("ADQ -1,R4");//-1 carac à recopier
					this.writer.writeHeader("ADQ 1,R1");

					//Ici R3=0
					// Fait R4 % 2 => quotient : nombre de mots à écrire
					// Reste = nombre d'octets à écrire
					// nombre d octets
					this.writer.writeHeader("LDW R2,#2");
					this.writer.writeHeader("DIV R4,R2,R2");
					this.writer.writeHeader("TST R2");
					this.writer.writeHeader("BEQ 14");
					//Si quotient != 0 >=1 mot à écrire
					this.writer.writeHeader("LDW R5,(R1)");
					this.writer.writeHeader("STW R5,(HP)");
					this.writer.writeHeader("ADQ 2,HP");
					this.writer.writeHeader("ADQ 2,R1");
					this.writer.writeHeader("ADQ -1,R2");//-1 mot à recopier
					this.writer.writeHeader("BNE -10");//Si R2 > 0 on boucle


					//Ici plus aucun mot à écrire regarde si reste un octet
					this.writer.writeHeader("TST R4");
					this.writer.writeHeader("BEQ 8");
					//Le reste est non nul, on écrit un octet
					this.writer.writeHeader("LDB R5,(R1)");
					this.writer.writeHeader("STB R5,(HP)");
					this.writer.writeHeader("ADQ 1,HP");


					//On a tout écrit : test HP : pair ou impair ?
					this.writer.writeHeader("LDW R2,#2");
					this.writer.writeHeader("LDW R3,HP");
					this.writer.writeHeader("DIV HP,R2,R2");
					this.writer.writeHeader("TST HP");
					this.writer.writeHeader("BEQ 10");
					//Taille de Hp est impaire
					this.writer.writeHeader("LDW HP,R3");
					this.writer.writeHeader("LDB R3,#0");
					this.writer.writeHeader("STB R3,(HP)");
					this.writer.writeHeader("ADQ 1,HP");

					//Taille de Hp est paire
					this.writer.writeHeader("LDW HP,R3");
					this.writer.writeHeader("LDW R3,#0");
					this.writer.writeHeader("STW R3,(HP)");
					this.writer.writeHeader("ADQ 2,HP");
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

	/**
	 * @return Somme des tailles des variables locales à cette TDS
	 */
	public int getSizeOfVars(Namespace<FunctionOrVariable> functionsAndVariables){
		int size = 0;
		for (Map.Entry<String, FunctionOrVariable> functionOrVariable : functionsAndVariables){
			if (functionOrVariable.getValue() instanceof Variable){
				Variable var = (Variable) functionOrVariable.getValue();
				if (var.getOffset() < 0) {
					size += wordSize;
				}
			}
		}
		return size;
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
		this.writer.writeFunction(String.format("ADI SP, SP, #%d  // Dépilage du chaînage statique et des variables locales s'il y en a", getSizeOfVars(this.currentTDS.getFunctionsAndVariables()) + wordSize));
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

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BGT 4"));
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
		} else {
			StrStrictLessThan(registerRight,registerLeft,registerIndex);
		}
	}

	private void StrStrictLessThan(int registerLeft,int registerRight,int registerIndex){

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
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
		} else {
			StrStrictLessThan(registerLeft,registerRight,registerIndex);
		}

		registerManager.freeRegister();
	}

	private void translateGreaterOrEqualThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BGE 6"));
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
		} else {
			// a<=b <=> b>=a
			StrLessOrEqualTh(registerRight,registerLeft,registerIndex);
		}
	}


	private void StrLessOrEqualTh(int registerLeft,int registerRight,int registerIndex){

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

	private void translateLessOrEqualThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isString = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);
		if (!isString) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BLE 6"));
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
		} else {//inf.src
			StrLessOrEqualTh(registerLeft,registerRight,registerIndex);
		}
		registerManager.freeRegister();
	}

	private void translateEqual(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BNE 6"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
		} else {
			//adresse str1
			int addOne;
			addOne = registerManager.provideRegister();
			//adresse str2
			int addTwo;
			addTwo = registerManager.provideRegister();
			//Pour appliquer les masques, diverses opérations
			int regOp;
			regOp = registerManager.provideRegister();


			// I
			//Vérifier la taille : avant dans le tas. Taille dans addOne resp two.
			writer.writeFunction(String.format("LDW R" + addOne + ", R" + registerLeft));
			writer.writeFunction(String.format("LDW R" + addTwo + ", R" + registerRight));
			//Enleve deux à adresse = adresse taille
			writer.writeFunction(String.format("ADQ -2, R" + addOne));
			writer.writeFunction(String.format("ADQ -2, R" + addTwo));
			//Déréférencement : addOne et two contiennent les longueurs
			writer.writeFunction(String.format("LDW R" + addOne + ", (R" + addOne + ")"));
			writer.writeFunction(String.format("LDW R" + addTwo + ", (R" + addTwo + ")"));
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", addOne, addTwo, regOp));
			writer.writeFunction(String.format("BEQ 6"));
			//Tailles différentes : FALSE
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 70"));// A REFLECHIR : go fin programme FIN
			// 70 OK


			// II
			//Premier test fait : size est identique
			//stocker les adresses respectives
			writer.writeFunction(String.format("STW R%d, R%d", registerLeft, addOne));
			writer.writeFunction(String.format("STW R%d, R%d", registerRight, addTwo));

			//On doit boucler à cet endroit
			//Met les bonnes adresses (décalées) dans regLeft et regRight
			writer.writeFunction(String.format("STW R%d, R%d", addOne, registerLeft));
			writer.writeFunction(String.format("STW R%d, R%d", addTwo, registerRight));
			//Charge les deux premiers caracteres regl et regr
			writer.writeFunction(String.format("LDW R%d, (R%d)", registerLeft, registerLeft));
			writer.writeFunction(String.format("LDW R%d, (R%d)", registerRight, registerRight));

			//Masque
			writer.writeFunction(String.format("LDW R%d, #65280", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerLeft, regOp, regOp));
			writer.writeFunction(String.format("BNE 20"));//A voir => (A)
			//Premier caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #65280", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerRight, regOp, regOp));
			writer.writeFunction(String.format("BNE 6"));//A voir
			//Premier caractere str2 vaut nul : égalité true
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 36"));//FIN : TRUE
			//A tester : temp

			//Premier caractere str2 différent de nul : égalité false
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 28"));//FIN : FALSE
			//A tester : temp


			//Premier caractere str1 différent de nul (A)
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, regOp));
			writer.writeFunction(String.format("BEQ 6"));//A voir
			// Ici str1-str2 != 0
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 20"));//FIN : FALSE
			// Ici str1-str2 = 0
			writer.writeFunction(String.format("LDW R%d, #255", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerLeft, regOp, regOp));
			writer.writeFunction(String.format("BNE 6"));
			// Ici deuxieme caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 8"));//FIN : TRUE

			// Ici deuxieme caractère str1 != nul : continuer
			//Décalage dans le tas
			writer.writeFunction(String.format("ADQ 2,R%d", addOne));
			writer.writeFunction(String.format("ADQ 2,R%d", addTwo));
			//On boucle !
			writer.writeFunction(String.format("BMP -66"));//A voir
			//A tester : temp

			//LIBERATION
			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();

		}


		registerManager.freeRegister();
	}

	private void translateNotEqual(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		boolean isStringComparison = (currentTDS.treeTypeHashMap.get(tree.getChild(0)) == SymbolTable.stringType);

		if (!isStringComparison) {
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, registerIndex));
			writer.writeFunction(String.format("BEQ 6"));
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 4"));
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));

			registerManager.freeRegister();

		} else {
			//adresse str1
			int addOne;
			addOne = registerManager.provideRegister();
			//adresse str2
			int addTwo;
			addTwo = registerManager.provideRegister();
			//Pour appliquer les masques, diverses opérations
			int regOp;
			regOp = registerManager.provideRegister();


			// I
			//Vérifier la taille : avant dans le tas. Taille dans addOne resp two.
			writer.writeFunction(String.format("LDW R" + addOne + ", R" + registerLeft));
			writer.writeFunction(String.format("LDW R" + addTwo + ", R" + registerRight));
			//Enleve deux à adresse = adresse taille
			writer.writeFunction(String.format("ADQ -2, R" + addOne));
			writer.writeFunction(String.format("ADQ -2, R" + addTwo));
			//Déréférencement : addOne et two contiennent les longueurs
			writer.writeFunction(String.format("LDW R" + addOne + ", (R" + addOne + ")"));
			writer.writeFunction(String.format("LDW R" + addTwo + ", (R" + addTwo + ")"));
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", addOne, addTwo, regOp));
			writer.writeFunction(String.format("BEQ 6"));
			//Tailles différentes : FALSE
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 70"));// A REFLECHIR : go fin programme FIN
			// 70 OK


			// II
			//Premier test fait : size est identique
			//stocker les adresses respectives
			writer.writeFunction(String.format("STW R%d, R%d", registerLeft, addOne));
			writer.writeFunction(String.format("STW R%d, R%d", registerRight, addTwo));

			//On doit boucler à cet endroit
			//Met les bonnes adresses (décalées) dans regLeft et regRight
			writer.writeFunction(String.format("STW R%d, R%d", addOne, registerLeft));
			writer.writeFunction(String.format("STW R%d, R%d", addTwo, registerRight));
			//Charge les deux premiers caracteres regl et regr
			writer.writeFunction(String.format("LDW R%d, (R%d)", registerLeft, registerLeft));
			writer.writeFunction(String.format("LDW R%d, (R%d)", registerRight, registerRight));

			//Masque
			writer.writeFunction(String.format("LDW R%d, #65280", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerLeft, regOp, regOp));
			writer.writeFunction(String.format("BNE 20"));//A voir => (A)
			//Premier caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #65280", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerRight, regOp, regOp));
			writer.writeFunction(String.format("BNE 6"));//A voir
			//Premier caractere str2 vaut nul : égalité true
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 36"));//FIN : TRUE
			//A tester : temp

			//Premier caractere str2 différent de nul : égalité false
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 28"));//FIN : FALSE
			//A tester : temp


			//Premier caractere str1 différent de nul (A)
			writer.writeFunction(String.format("SUB R%d, R%d, R%d", registerLeft, registerRight, regOp));
			writer.writeFunction(String.format("BEQ 6"));//A voir
			// Ici str1-str2 != 0
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 20"));//FIN : FALSE
			// Ici str1-str2 = 0
			writer.writeFunction(String.format("LDW R%d, #255", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerLeft, regOp, regOp));
			writer.writeFunction(String.format("BNE 6"));
			// Ici deuxieme caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 8"));//FIN : TRUE

			// Ici deuxieme caractère str1 != nul : continuer
			//Décalage dans le tas
			writer.writeFunction(String.format("ADQ 2,R%d", addOne));
			writer.writeFunction(String.format("ADQ 2,R%d", addTwo));
			//On boucle !
			writer.writeFunction(String.format("BMP -66"));//A voir
			//A tester : temp

			//LIBERATION
			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();
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
		translate(tree.getChild(0), registerA);

		//Met B dans registerB
		int registerB=registerManager.provideRegister();
		translate(tree.getChild(1), registerB);

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
		SymbolTable table = function.getSymbolTable();


		// Sauvegarde les registres de l'environnement appelant
		registerManager.saveAll();

		int registerIndex2 = registerManager.provideRegister(); // Réserve un registre pour empiler les arguments

		for (int i = 1, l = tree.getChildCount(); i < l; ++i) {   //Parcours des arguments de la fonction
			translate(tree.getChild(i), registerIndex2);
			this.writer.writeFunction(String.format("STW R%d, -(SP) // Empile l'argument numéro %d/%d de la fonction \"%s\"",registerIndex2, i, l-1, name));
		}//Tous les arguments sont empilés

		registerManager.freeRegister();

		writeEntryFunction(table, name);    // Génération du code d'appel à la fonction et calcul de son chaînage statique
		int size = (tree.getChildCount() - 1) * wordSize;
		if (size != 0) {
			this.writer.writeFunction(String.format("ADI SP, SP, #%d  // Dépile les arguments de la fonction", size));  // Dépile les arguments
		}

		registerManager.restoreAll();
		if (function.getType() != null) {
			this.writer.writeFunction(String.format("LDW R%s, R0", registerIndex));
		}
	}

	private void translateREC(Tree tree, int registerIndex) {
		this.writer.writeFunction(String.format("LDW R%d, HP", registerIndex));
		int register1 = this.registerManager.provideRegister();
		this.writer.writeFunction(String.format("LDW R%d, HP", register1));
		this.writer.writeFunction(String.format("ADI HP, HP, #%d", (tree.getChildCount() / 2) * wordSize));
		int register2 = this.registerManager.provideRegister();
		for (int i = 1, l = tree.getChildCount(); i < l; i += 2) {
			this.translate(tree.getChild(i + 1), register2);
			this.writer.writeFunction(String.format("STW R%d, (R%d)+", register2, register1));
		}
		this.registerManager.freeRegister();
		this.registerManager.freeRegister();
	}

	private void translateARR(Tree tree, int registerIndex) {
		this.translate(tree.getChild(1), registerIndex);
		this.writer.writeFunction("BGE 2");
		this.writer.writeFunction("TRP #EXIT_EXC");
		this.writer.writeFunction(String.format("STW R%d, (HP)+", registerIndex));
		this.writer.writeFunction(String.format("SHL R%d, R%d", registerIndex, registerIndex));
		this.writer.writeFunction(String.format("ADD R%d, HP, HP", registerIndex));
		int register1 = this.registerManager.provideRegister();
		this.writer.writeFunction(String.format("LDW R%d, HP", register1));
		int register2 = this.registerManager.provideRegister();
		this.translate(tree.getChild(2), register2);
		this.writer.writeFunction(String.format("TST R%d", registerIndex));
		this.writer.writeFunction("BEQ 6");
		this.writer.writeFunction(String.format("STW R%d, -(R%d)", register2, register1));
		this.writer.writeFunction(String.format("ADQ -2, R%d", registerIndex));
		this.writer.writeFunction("BNE -6");
		this.registerManager.freeRegister();
		this.writer.writeFunction(String.format("LDW R%d, R%d", registerIndex, register1));
		this.registerManager.freeRegister();
	}

	private void translateAssignment(Tree tree, int registerIndex) {
		Tree lValue = tree.getChild(0);
		Tree expToAssign = tree.getChild(1);
		switch (lValue.getType()) { // Disjonction de cas selon le type de lValue qui prendra l'assignement
			case ID:
				translateIDAdress(lValue, registerIndex); // registerIndex contient maintenant l'adresse de la variable de destination de l'affectation
				break;
			case ITEM:
				translateITEMAdress(lValue, registerIndex);
				break;
			case FIELD:
				translateFIELDAdress(lValue, registerIndex);
				break;
			default:
				// On ne doit pas arriver là si les tests sémantiques sont passés
		}
		// Calcule la partie droite du :=, l'assigne à la lValue
		int registerExp = this.registerManager.provideRegister();
		translate(expToAssign, registerExp);
		this.writer.writeFunction(String.format("STW R%d, (R%d)  // Affectation", registerExp, registerIndex));
		this.registerManager.freeRegister();
	}

	private void translateSEQ(Tree tree, int registerIndex) {
		for (int i = 0, l = tree.getChildCount(); i < l; ++i) {
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
	private void translateIDAdress(Tree tree, int registerIndex) {
		//TODO : charger l'adresse ou la valeur dans registerIndex ? Rendre le choix entre les deux avec un paramètre ?
		String name = tree.toString();
		Variable variable = this.currentTDS.findTranslatedVariable(name);

		int deplacementVariable = variable.getOffset();
		String typeOfVar;
		if (deplacementVariable < 0){   // Si variable est une variable locale
			deplacementVariable -= wordSize;   // On doit compter le déplacement statique
			typeOfVar = "variable locale";
		} else {    // variable est un argument de fonction
			deplacementVariable += 4;   // Taille de l'adresse de retour
			typeOfVar = "argument";
		}

		int countStaticChain = this.currentTDS.countStaticChainToVariable(tree.toString());

		this.writer.writeFunction(String.format("LDW R%d, BP  // ID_BEGIN : Préparation pour le chargement de l'adresse de %s \"%s\" dans un registre", registerIndex, typeOfVar, name)); // Copie le registre de base dans le registre résultat
		if (countStaticChain > 0) { // S'il y des chaînages statiques à remonter :
			int addressRegister = 0;


			int loopRegister = this.registerManager.provideRegister();

			this.writer.writeFunction(String.format("LDQ %d, R%d  // Début de remontée de chaînage statique pour charger l'adresse de %s \"%s\" dans un registre" ,countStaticChain, loopRegister, typeOfVar, name));
			// Début de boucle :
			this.writer.writeFunction(String.format("LDW R%d, (R%d)%d", registerIndex, registerIndex, -wordSize));
			this.writer.writeFunction(String.format("ADQ -1, R%d", loopRegister));
			this.writer.writeFunction(String.format("BNE %d  // Fin de remontée de chaînage statique pour charger l'adresse de %s \"%s\" dans un registre", -8, typeOfVar, name)); // Jump à (6 - 2)/3 = 2 instructions plus tôt. Le -2 c'est pour cette instruction de saut
			// Fin de boucle

			this.registerManager.freeRegister();
		}
		this.writer.writeFunction(String.format("ADI R%d, R%d, #%d  // ID_END : Chargement de l'adresse de %s \"%s\" dans un registre", registerIndex, registerIndex, deplacementVariable,  typeOfVar, name));   // L'adresse de la variable recherchée est maintenant dans registre voulu
	}

	/**
	 * Ecrit le code pour mettre la valeur de l'identifiant dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 * @return
	 */
	private void translateID(Tree tree, int registerIndex){
		String name = tree.toString();
		translateIDAdress(tree, registerIndex);   // Charge l'adresse de ID dans le registre
		this.writer.writeFunction(String.format("LDW R%d , (R%d) // Chargement de la valeur de \"%s\" dans un registre", registerIndex, registerIndex, name ));
	}

	/**
	 * Ecrit le code pour mettre l'adresse d'un élément de tableau (ARRAY) dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateITEMAdress(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		this.translate(exp, registerIndex); // register Index contient maintenant l'adresse du tableau

		int registerIndexOfArray = this.registerManager.provideRegister();
		this.translate(tree.getChild(1), registerIndexOfArray); // Evaluation de l'indice du tableau

		this.writer.writeFunction(String.format("SHL R%d, R%s  // ITEM : Calcul de l'offset de l'élément du tableau", registerIndexOfArray, registerIndexOfArray));
		this.writer.writeFunction(String.format("ADD R%d, R%s, R%s  // ITEM : Calcul de l'adresse de l'élement du tableau", registerIndex, registerIndexOfArray, registerIndex));

		this.registerManager.freeRegister();
	}

	/**
	 * Ecrit le code pour mettre la valeur d'un élément de tableau (ARRAY) dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateITEM(Tree tree, int registerIndex) {
		translateITEMAdress(tree, registerIndex);
		this.writer.writeFunction(String.format("LDW R%d, (R%d)  // ITEM : La valeur de l'élémént du tableau dans un registre", registerIndex, registerIndex));
	}

	/**
	 * Ecrit le code pour mettre l'adresse d'un champ dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateFIELDAdress(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		this.translate(exp, registerIndex); // Evaluation de la partie gauche, le pointeur du Record est maintenant dans registerIndex

		Record record = (Record) SymbolTable.treeTypeHashMap.get(exp);   // Récupère le type de exp
		Namespace<Variable> fields = record.getNamespace();
		String nameOfField = tree.getChild(1).toString();   // Récupère le nom du field

		this.writer.writeFunction(String.format("ADI R%d, R%s, #%d  // FIELD : Calcul de l'adresse du champ \"%s\"", registerIndex, registerIndex, fields.get(nameOfField).getOffset(), nameOfField));
	}

	/**
	 * Ecrit le code pour mettre la valeur d'un champ dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateFIELD(Tree tree, int registerIndex) {
		translateFIELDAdress(tree, registerIndex);

		Record record = (Record) SymbolTable.treeTypeHashMap.get(tree.getChild(0));   // Récupère le type de exp
		Namespace<Variable> fields = record.getNamespace();
		String nameOfField = tree.getChild(1).toString();   // Récupère le nom du field

		this.writer.writeFunction(String.format("LDW R%d, (R%d)  // FIELD : La valeur du champ \"%s\" est mise dans un registre", registerIndex, registerIndex, nameOfField));
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
		int testRegister = this.registerManager.provideRegister();
		int loopRegister = registerIndex;

		// Création des labels de cette boucle while
		String testLabel = labelGenerator.getLabel(tree, "test");
		String endLabel = labelGenerator.getLabel(tree, "end");

		this.writer.writeFunction(testLabel, "NOP");
		this.translate(tree.getChild(0), testRegister);
		this.writer.writeFunction(String.format("TST R%d", testRegister));
		this.writer.writeFunction(String.format("BNE 2"));
		this.writer.writeFunction(String.format("JEA @%s", endLabel));

		// Corps de la boucle
		this.translate(tree.getChild(1), loopRegister);
		this.writer.writeFunction(String.format("JEA @%s", testLabel));

		// Fin de la boucle
		this.writer.writeFunction(endLabel, "NOP");

		this.registerManager.freeRegister();
	}

	private void translateFor(Tree tree, int registerIndex) {   //TODO : générer le code de la boucle for
		labelGenerator.getLabel(tree, "test");    // Création des labels de la fonction assembleur liée à ce for (label de la ligne du test, de fin de for)

		Variable index = (Variable) this.next().getFunctionsAndVariables().get(tree.getChild(0).toString());  // Récupère la variable de boucle
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
					writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(), "type"));
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
					writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(),"function"));
					this.descend();   // On avait créé une nouvelle table avant la première déclaration, donc on y descend
					int lj = i;
					do {
						symbol = dec.getChild(lj);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = (Function) this.currentTDS.getFunctionsAndVariables().get(name);
						labelGenerator.getLabel(function.getSymbolTable(), name); // Création du label de la fonction
						this.descend();   // Descente dans la TDS de la fonction
						Namespace<FunctionOrVariable> namespace = this.currentTDS.getFunctionsAndVariables();
						for (Map.Entry<String, FunctionOrVariable> entry: namespace) {
							((Variable) entry.getValue()).translate(true);
						}
						int register = this.registerManager.provideRegister();
						translate(body, register); // Génère code pour le corps de la fonction
						this.writer.writeFunction(String.format("LDW R0, R%s", register));
						this.registerManager.freeRegister();
						this.ascend();
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));

					i = lj - 1;
//					ascendTDS();    // !! On ne remonte pas la TDS qu'on a créé à la première déclaration de fonction !!
					break;
				}
				case "var": { // dans le cas de la déclaration d'une variable
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					if (this.currentTDS == table) {
						this.registerManager.saveAll();
						writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(), "var"));
						this.descend();
					} else {
						FunctionOrVariable functionOrVariable = this.currentTDS.getFunctionsAndVariables().get(name);
						if (functionOrVariable instanceof Variable && ((Variable) functionOrVariable).isTranslated()) {
							this.registerManager.saveAll();
							writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(), "var"));
							this.descend();
						}
					}
					int register = this.registerManager.provideRegister();
					this.translate(exp, register);
					this.writer.writeFunction(String.format("STW R%s, -(SP) // Empilage de la variable locale \"%s\"", register, name));
					this.registerManager.freeRegister();
					Variable variable = (Variable) this.currentTDS.getFunctionsAndVariables().get(name); // Récupération de la variable déclarée
					variable.translate(true);
					break;
				}
			}
		}
		int register = this.registerManager.provideRegister();
		translate(seq, register);  // Evalue le IN
		this.writer.writeFunction(String.format("LDW R0, R%s", register));
		this.registerManager.freeRegister();
		while (this.currentTDS != table) {    // On remonte les TDS pour revenir à la profondeur d'avant le LET
			this.ascend();
		}
		this.registerManager.restoreAll(); // appeler une seule fois cette méthode suffit à restaurer les registres tels qu'avant le let
		if (SymbolTable.treeTypeHashMap.get(tree) != null) {
			this.writer.writeFunction(String.format("LDW R%s, R0", registerIndex));
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
		if (TDSDest.getDepth() <= 1){   // On ne calcule pas le chaînage statique si on appelle une fonction built-in   //TODO : vérifier que c'est la bonne profondeur
			return ;
		}
		int count_stat = this.currentTDS.getDepth()-TDSDest.getDepth() + 1;


		if (count_stat == 0){
			this.writer.writeFunction("LDW R0, BP  // STATIC_LINK : Calcul du chaînage statique");
			return;
		}

		this.writer.writeFunction("LDW R0, BP  // STATIC_LINK_BEGIN : Calcul du chaînage statique");
		int loopRegister = this.registerManager.provideRegister();
		this.writer.writeFunction(String.format("LDQ %d, R%s ", count_stat, loopRegister));  // TODO : pourquoi currentTDS.getDepth()-TDSDest.getDepth() + 2 ne convient pas ?
		// Début de boucle :
		this.writer.writeFunction(String.format("LDW R0, (R0)%d", -wordSize));
		this.writer.writeFunction(String.format("ADQ -1, R%d", loopRegister));
		this.writer.writeFunction(String.format("BNE %d  // STATIC_LINK_END : Fin du calcul du chaînage statique", -8)); // Jump à 2 +1 instructions (il faut compter celle là) plus tôt
		// Fin de boucle

		this.registerManager.freeRegister();    // On libère le registre de boucle
	}
}
