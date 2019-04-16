package compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

import semantic.Function;
import semantic.FunctionOrVariable;
import semantic.Namespace;
import semantic.Record;
import semantic.SymbolTable;
import semantic.Type;
import semantic.Variable;

import static syntactic.TigerParser.SEQ;
import static syntactic.TigerParser.ARR;
import static syntactic.TigerParser.REC;
import static syntactic.TigerParser.CALL;
import static syntactic.TigerParser.ITEM;
import static syntactic.TigerParser.FIELD;
import static syntactic.TigerParser.ID;
import static syntactic.TigerParser.STR;
import static syntactic.TigerParser.INT;

import static semantic.TigerChecker.stringType;

public class TigerTranslator {

	private Map<Tree, Type> treeTypes;
	private SymbolTable currentTable; // TDS actuelle
	private List<Integer> childrenIndexStack; // Pile des childrenIndex, mis à jour en descente et en remontée de TDS
	private LabelGenerator labelGenerator;
	private Writer writer; // Classe gérant les écritures de code au bon endroit (pour permettre d'écrire le code d'une fonction en plusieurs fois, si une autre fonction (assembleur) est nécessaire durant son écriture)
	private StackCounter stackCounter;
	private RegisterManager registerManager;
	private int heapBase; // l'adresse qui suit la partie statique du tas
	private Map<String, Integer> strings; // chaînes de caractères statiques

	public TigerTranslator(Tree tree, Map<Tree, Type> treeTypes, SymbolTable table) {
		// Pour lancer le translator sur l'ensemble du programme, passer la TDS de niveau 0 (pas le root)
		this.treeTypes = treeTypes; // TDS actuelle
		this.currentTable = table; // TDS actuelle
		this.childrenIndexStack = new ArrayList<Integer>();
		this.childrenIndexStack.add(0);
		this.labelGenerator = new LabelGenerator(16);
		this.writer = new Writer(this.labelGenerator);
		this.stackCounter = new StackCounter();
		this.registerManager = new RegisterManager(this.writer, this.stackCounter, 12);
		this.heapBase = 4096;
		this.strings = new HashMap<String, Integer>();
		this.writeMainAndFunction(tree);
	}

	private void writeHeader(String name, Function function) {
		String label = this.labelGenerator.getLabel(function.getSymbolTable(), name);
		function.setWritten();
		switch (name) {
			case "chr": {
				// TODO : débugger cette fonction
				this.writer.writeHeader(label, "LDW R2, (SP)2");

				// Si on donne un nombre strictement négatif, sauter en (*)
				this.writer.writeHeader("BLW 20");
				this.writer.writeHeader("LDQ 127, R1");
				this.writer.writeHeader("CMP R1, R2");

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
			case "exit": { // TODO : mieux gérer les entrées et les sorties
				this.writer.writeHeader(label, "LDW R0, (SP)2");
				this.writer.writeHeader("TRP #EXIT_EXC");
				break;
			}
			case "flush": { // TODO : mieux gérer les entrées et les sorties
				this.writer.writeHeader(label, "RTS");
				break;
			}
			case "getchar": { // TODO : mieux gérer les entrées et les sorties
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
			case "not": {
				this.writer.writeHeader(label, "LDW R0, (SP)2");
				this.writer.writeHeader("BEQ 4");
				this.writer.writeHeader("LDQ 0, R0");
				this.writer.writeHeader("BMP 2");
				this.writer.writeHeader("LDQ 1, R0");
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
			case "print": { // TODO : mieux gérer les entrées et les sorties
				this.writer.writeHeader(label, "LDW R0, (SP)2");
				this.writer.writeHeader("LDW R1, #WRITE_EXC");
				this.writer.writeHeader("TRP R1");
				this.writer.writeHeader("RTS");
				break;
			}
			case "printi": { // TODO : mieux gérer les entrées et les sorties
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
			case "read": { // TODO : mieux gérer les entrées et les sorties
				/*R0 : adresse du prochain byte lu
				* R1 : résultat (entier)
				* R2 : working registory
				* R3 : wording registory 2
				* R4 : booleen indiquant si l'entier lu doit être négatif
				* */
				this.writer.writeHeader(label, "LDW R0, HP"); // La lecture sera mise dans le tas, mais pas réservée (pourra être écrasée après cette fonction)
				this.writer.writeHeader("TRP #READ_EXC");

				this.writer.writeHeader("LDQ 0, R1 // Initialise le résultat à 0"); // Initialise le registre de résultat
				this.writer.writeHeader("LDQ 0, R4 // Initialise l'information : nombre négatif"); // Permettra de conserver l'information : "l'entier lu doit être négatif"

				// Récupère le premier caractère lu
				this.writer.writeHeader("LDB R2, (R0)+ // Récupère un caractère");
				this.writer.writeHeader("BEQ 38"); // Saute en fin de fonction si R1 vaut zéro // TODO : calculer le jump

				// Gère le cas où le premier caractère est un signe moins :
				this.writer.writeHeader("LDW R3, R2");
				this.writer.writeHeader("ADQ -45, R3 // Vérifie si le premier caractère est un signe moins");
				this.writer.writeHeader("BNE 6"); // saute directement dans la boucle si le caractère n'est pas moins // TODO : calculer le jump
				this.writer.writeHeader("LDQ 1, R4");

				// Parcours des autres caractères lus jusqu'à trouver un \0 (NULL, 0 en ASCII)
				this.writer.writeHeader("LDB R2, (R0)+ // Parcours des autres caractères lus jusqu'à trouver un \\0 : Récupère un caractère");
				this.writer.writeHeader("BEQ 20"); // Saute en fin de fonction // TODO : calculer le jump

				// Teste si le caractère est un chiffre :
				this.writer.writeHeader("ADQ -48, R2 // Passe du code ASCII à un entier"); // Début boucle parcours
				this.writer.writeHeader("BLW 16 // Teste la borne inférieure"); // Saute en fin de fonction // TODO : jump à la fin du parcours
				this.writer.writeHeader("ADI R2, R3, #-10");
				this.writer.writeHeader("BGE 10 // Teste la borne supérieure"); // Saute en fin de fonction // TODO : jump à la fin du parcours

				// Ajoute le chiffre au résultat :
				this.writer.writeHeader("LDQ 10, R3 // Décalage des unités");
				this.writer.writeHeader("MUL R1, R3, R1 // Décalage des unités");
				this.writer.writeHeader("ADD R2, R1, R1 // Ajout du chiffre lu au résultat");


				this.writer.writeHeader("JMP #-22 // Boucle sur les caractères lus"); // TODO : jump au début de boucle parcours

				// Gére le passage au négatif si l'entier doit être négatif :
				this.writer.writeHeader("TST R4 // Vérifie si l'entier doit être négatif");
				this.writer.writeHeader("BEQ 2");
				this.writer.writeHeader("NEG R1, R1 // Charge l'entier lu dans R0");

				this.writer.writeHeader("LDW R0, R1 // Charge l'entier lu dans R0");

				this.writer.writeHeader("RTS");
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
				this.writer.writeHeader(label, "LDW R1, (SP)6");
				//Numéro de la lettre i
				this.writer.writeHeader("LDW R3, (SP)4");
				//Lg demandée m
				this.writer.writeHeader("LDW R4, (SP)2");


				//Test préliminaire i et n
				this.writer.writeHeader("TST R3");
				this.writer.writeHeader("BGE 12"); // Go fin prog i<0
				this.writer.writeHeader("LDW R5, #0 ");
				this.writer.writeHeader("STW R5, (HP)+ ");
				this.writer.writeHeader("LDW R0, HP" );
				this.writer.writeHeader("STW R5, (HP)+");
				this.writer.writeHeader("RTS"); // FIN

				this.writer.writeHeader("TST R4");
				this.writer.writeHeader("BGE 12"); // Go fin prog n<0
				this.writer.writeHeader("LDW R5, #0 ");
				this.writer.writeHeader("STW R5, (HP)+");
				this.writer.writeHeader("LDW R0, HP");
				this.writer.writeHeader("STW R5, (HP)+");
				this.writer.writeHeader("RTS"); // FIN


				//Check les longueurs
				this.writer.writeHeader("ADQ -2, R1");
				this.writer.writeHeader("LDW R2, (R1)");
				this.writer.writeHeader("ADQ 2, R1");
				this.writer.writeHeader("ADD R3, R4, R5");
				this.writer.writeHeader("CMP R2, R5");
				this.writer.writeHeader("BGE 12"); // FIN
				this.writer.writeHeader("LDW R5, #0");
				this.writer.writeHeader("STW R5, (HP)+");
				this.writer.writeHeader("LDW R0, HP");
				this.writer.writeHeader("STW R5, (HP)+");
				this.writer.writeHeader("RTS"); // FIN
				//Si n-(i+m) < 0 STOP


				//Fait i %2 => quotient : nombre de mots à "sauter"
				//Reste = nb d'octet à sauter (0 ou 1)
				this.writer.writeHeader("LDW R2, #2");
				this.writer.writeHeader("DIV R3, R2, R2");
				this.writer.writeHeader("LDW R5, #2");
				this.writer.writeHeader("MUL R2, R5, R2");
				this.writer.writeHeader("ADD R2, R1, R1");
				//Ajoute quotient*2 à adresse str


				//Met lg en HP puis incrémente HP
				// R0 a @ du premier caractere
				this.writer.writeHeader("STW R4, (HP)");
				this.writer.writeHeader("ADQ 2, HP");
				this.writer.writeHeader("LDW R0, HP");


				this.writer.writeHeader("TST R3");
				this.writer.writeHeader("BEQ 14");
				//SI R3=1, on décalle de 1 octet pour écrire le suivant
				this.writer.writeHeader("ADQ 1, R1");
				this.writer.writeHeader("LDB R5, (R1)");
				this.writer.writeHeader("STB R5, (HP)");
				this.writer.writeHeader("ADQ 1, HP");
				this.writer.writeHeader("ADQ 1, R1");
				this.writer.writeHeader("ADQ -1, R4"); // -1 carac à recopier


				//Ici R3=0
				//R4 nb de carac à recopier
				this.writer.writeHeader("BEQ 14 ");
				this.writer.writeHeader("LDB R5, (R1) ");
				this.writer.writeHeader("STB R5, (HP)");
				this.writer.writeHeader("ADQ 1, HP");
				this.writer.writeHeader("ADQ 1, R1");
				this.writer.writeHeader("ADQ -1, R4"); // -1 carac à recopier
				this.writer.writeHeader("BNE -12");



				//On a tout écrit : test HP : pair ou impair ?
				this.writer.writeHeader("LDW R2, #2");
				this.writer.writeHeader("LDW R3, HP");
				this.writer.writeHeader("DIV HP, R2, R2");
				this.writer.writeHeader("TST HP");
				this.writer.writeHeader("BEQ 10");
				//Taille de Hp est impaire
				this.writer.writeHeader("LDW HP, R3");
				this.writer.writeHeader("LDB R3, #0");
				this.writer.writeHeader("STB R3, (HP)");
				this.writer.writeHeader("ADQ 1, HP");

				//Taille de Hp est paire
				this.writer.writeHeader("LDW HP, R3");
				this.writer.writeHeader("LDW R3, #0");
				this.writer.writeHeader("STW R3, (HP)");
				this.writer.writeHeader("ADQ 2, HP");
				this.writer.writeHeader("RTS");
				break;

			}
		}
		this.writer.writeHeader();
	}

	private void writeMainAndFunction(Tree tree) {
		int registerIndex = this.registerManager.provideRegister();
		this.translate(tree, registerIndex);
		this.registerManager.freeRegister();
	}

	/**
	 * Ecrit le code (coté appelant) d'appel de fonction TIGER (portant un nom)
	 * En fin de cette méthode, le registre R0 contient le chaînage statique de la fonction appelée. Il n'a pas été reservé via le RegisterManager
	 * @param TDSDest TDS de la fonction appelée
	 * @param name nom de la fonction tiger appelée
	 */
	private void writeEntryFunction(SymbolTable TDSDest, String name) {
		this.writeStaticLinking(TDSDest);
		this.writer.writeFunction(String.format("JSR @%s", labelGenerator.getLabel(TDSDest, name)));
	}

	/**
	 * Calcule de chaînage statique de la fonction assembleur de TDS TDSDest et le met dans R0
	 * En fin de cette méthode, le registre R0 contient le chaînage statique de la fonction appelée. Il n'a pas été reservé via le RegisterManager
	 * @param TDSDest TDS de la fonction dont on calcule le chaînage statique
	 */
	private void writeStaticLinking(SymbolTable TDSDest) {
		if (TDSDest.getDepth() <= 1) { // On ne calcule pas le chaînage statique si on appelle une fonction built-in // TODO : vérifier que c'est la bonne profondeur
			return ;
		}
		int count_stat = this.currentTable.getDepth()-TDSDest.getDepth() + 1;


		if (count_stat == 0) {
			this.writer.writeFunction("LDW R0, BP // STATIC_LINK : Calcul du chaînage statique");
			return;
		}

		this.writer.writeFunction("LDW R0, BP // STATIC_LINK_BEGIN : Calcul du chaînage statique");
		int loopRegister = this.registerManager.provideRegister();
		this.writer.writeFunction(String.format("LDQ %d, R%d ", count_stat, loopRegister)); // TODO ne pas utiliser LDQ
		// Début de boucle :
		this.writer.writeFunction("LDW R0, (R0)-2");
		this.writer.writeFunction(String.format("ADQ -1, R%d", loopRegister));
		this.writer.writeFunction("BNE -8 // STATIC_LINK_END : Fin du calcul du chaînage statique"); // Jump à 2 +1 instructions (il faut compter celle là) plus tôt
		// Fin de boucle

		this.registerManager.freeRegister(); // On libère le registre de boucle
	}

	/**
	 * @return Somme des tailles des variables locales à cette TDS
	 */
	private int getSizeOfVars(Namespace<FunctionOrVariable> functionsAndVariables) {
		int size = 0;
		for (Map.Entry<String, FunctionOrVariable> functionOrVariable : functionsAndVariables) {
			if (functionOrVariable.getValue() instanceof Variable) {
				Variable var = (Variable) functionOrVariable.getValue();
				if (var.getOffset() < 0) {
					size += 2;
				}
			}
		}
		return size;
	}

	/**
	 * Compte le nombre de chaînages statiques à remonter pour accèder à la variable name
	 * @param name
	 * @return : nombre de chaînages statiques à remonter
	 */
	private int countStaticChainToVariable(String name) {
		int staticChainCount = 0;
		SymbolTable table = this.currentTable;
		FunctionOrVariable functionOrVariable;
		while (
			(functionOrVariable = table.getFunctionsAndVariables().get(name)) == null ||
			!(functionOrVariable instanceof Variable) ||
			!((Variable) functionOrVariable).isTranslated()
		) {
			++staticChainCount;
			table = table.getParent();
		}
		return staticChainCount;
	}

	private Function findFunction(String name) {
		// recherche la fonction indiquée dans les tables des symboles supérieures et la retourne
		SymbolTable table = this.currentTable;
		FunctionOrVariable functionOrVariable;
		while (
			(functionOrVariable = table.getFunctionsAndVariables().get(name)) == null ||
			!(functionOrVariable instanceof Function)
		) {
			table = table.getParent();
		}
		return (Function) functionOrVariable;
	}

	private Variable findVariable(String name) {
		// recherche la variable indiquée dans les tables des symboles supérieures et la retourne
		SymbolTable table = this.currentTable;
		FunctionOrVariable functionOrVariable;
		while (
			(functionOrVariable = table.getFunctionsAndVariables().get(name)) == null ||
			!(functionOrVariable instanceof Variable) ||
			!((Variable) functionOrVariable).isTranslated()
		) {
			table = table.getParent();
		}
		return (Variable) functionOrVariable;
	}

	private SymbolTable next() {
		int indexOfNextChild = childrenIndexStack.get(childrenIndexStack.size()-1);
		return this.currentTable.getChildren().get(indexOfNextChild);
	}

	private void descend() {
		// Met à jour la TDS courante pour y mettre la TDS fille de la TDS courante de bon index et met à jour childrenIndexStack
		int indexOfNextChild = childrenIndexStack.get(childrenIndexStack.size()-1); // Récupère l'index de la TDS fille à prendre
		childrenIndexStack.set(childrenIndexStack.size()-1, indexOfNextChild + 1); // Incrémente l'index de la prochaine TDS à prendre
		this.currentTable = this.currentTable.getChildren().get(indexOfNextChild);
		this.childrenIndexStack.add(0); // Ajoute une entrée dans childrenIndexStack pour reprendre le compte pour la nouvelle TDS
		this.registerManager.descend(); // Descend le registerManagaer
		this.writer.descend(); // Descend le writer pour écrire le code de cette fonction
		String label = labelGenerator.getLabel(this.currentTable); // Création ou récupération du label de la nouvelle TDS
		// Empilage du chaînage dynamique :
		this.writer.writeFunction(label, "STW BP, -(SP) // Empilage du chaînage dynamique"); // Partie LINK
		this.writer.writeFunction("LDW BP, SP // Mise à jour de la base de l'environnement courant");
		//Empilage du chaînage statique
		this.stackCounter.addCount(4);
		this.stackCounter.register(this.currentTable);
		this.writer.writeFunction("STW R0, -(SP) // Empilage du chaînage statique");
		this.stackCounter.addCount(2);
	}

	private void ascend() {
		// Met à jour la TDS courante en remontant de TDS et met à jour childrenIndexStack
		int size = this.getSizeOfVars(this.currentTable.getFunctionsAndVariables());
		this.stackCounter.addCount(-size - 2);
		this.writer.writeFunction(String.format("ADI SP, SP, #%d // Dépilage des variables locales et du chaînage statique", size + 2));
		this.stackCounter.unregister(this.currentTable);
		this.stackCounter.addCount(-4);
		this.writer.writeFunction("LDW BP, (SP)+ // Restauration de la base de l'environnement courant avec le chaînage dynamique"); // Partie UNLINK
		this.writer.writeFunction("RTS");
		this.writer.ascend(); // Remonte le writer : on a finit d'écrire le code de cette fonction (assembleur)
		this.registerManager.ascend();
		this.childrenIndexStack.remove(childrenIndexStack.size() - 1); // Retire le dernière index empilé
		this.currentTable = this.currentTable.getParent(); // Remonte de TDS
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

		if (this.treeTypes.get(tree.getChild(0)) != stringType) {
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BGT 4"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {
			StrStrictLessThan(registerRight, registerLeft, registerIndex);
		}
		registerManager.freeRegister();
	}

	private void StrStrictLessThan(int registerLeft, int registerRight, int registerIndex) {

		int registerAddA; // contient adresse de A
		registerAddA=registerManager.provideRegister();
		int registerAddB; // contient adresse de B
		registerAddB=registerManager.provideRegister();
		int registerDep; // Déplacement tas
		registerDep=registerManager.provideRegister();
		int registerOp; // sert à faire des masques
		registerOp=registerManager.provideRegister();

		//Met adresse de string1 (resp2) dans registerAddA (reps.B)
		writer.writeFunction(String.format("LDW R"+registerAddA+", R"+registerLeft));
		writer.writeFunction(String.format("LDW R"+registerAddB+", R"+registerRight));
		//Met à 0 le décalage
		writer.writeFunction(String.format("LDW R"+registerDep+", #0"));
		//
		//Met les adresses des strings dans registerLeft et registerRight
		writer.writeFunction(String.format("LDW R"+registerLeft+", R"+registerAddA));
		writer.writeFunction(String.format("LDW R"+registerRight+", R"+registerAddB));

		//Décalage dans le tas de ce nécessaire
		writer.writeFunction(String.format("ADD R"+registerLeft+", R"+registerDep+", R"+registerLeft));
		writer.writeFunction(String.format("ADD R"+registerRight+", R"+registerDep+", R"+registerRight));

		//Charge deux caracteres dans registerLeft et registerRight
		writer.writeFunction(String.format("LDW R"+registerLeft+", (R"+registerLeft+")"));
		writer.writeFunction(String.format("LDW R"+registerRight+", (R"+registerRight+")"));

		//Masque
		writer.writeFunction(String.format("LDW R"+registerOp+", #65280"));
		writer.writeFunction(String.format("AND R"+registerLeft+", R"+registerOp+", R"+registerOp));
		writer.writeFunction(String.format("BNE 20")); // A VOIR
		//Premier caractere de str1 est vide
		writer.writeFunction(String.format("LDW R"+registerOp+", #65280"));
		writer.writeFunction(String.format("AND R"+registerRight+", R"+registerOp+", R"+registerOp));
		writer.writeFunction(String.format("BNE 6"));
		//Premier caractere de str2 est vide = égalité
		writer.writeFunction(String.format("LDW R"+registerIndex+", #0"));
		writer.writeFunction(String.format("BMP 4"));
		//Sinon : vrai
		writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
		writer.writeFunction(String.format("BMP 36"));

		writer.writeFunction(String.format("CMP R"+registerLeft+", R"+registerRight));
		writer.writeFunction(String.format("BGE 6"));
		// str1 < str2
		writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
		writer.writeFunction(String.format("BMP 18"));
		writer.writeFunction(String.format("BEQ 6"));
		// str1 > str2
		writer.writeFunction(String.format("LDW R"+registerIndex+", #0"));
		writer.writeFunction(String.format("BMP 18"));

		//str1 == str2
		//Test de caractere 2 : masque #255
		writer.writeFunction(String.format("LDW R"+registerOp+", #255"));
		writer.writeFunction(String.format("AND R"+registerLeft+", R"+registerOp+", R"+registerOp));
		writer.writeFunction(String.format("BNE 6"));
		//caractere 2 vaut 0 Egalité : false
		writer.writeFunction(String.format("LDW R"+registerIndex+", #0"));
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

		if (this.treeTypes.get(tree.getChild(0)) != stringType) {
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BLW 4"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {
			StrStrictLessThan(registerLeft, registerRight, registerIndex);
		}

		registerManager.freeRegister();
	}

	private void translateGreaterOrEqualThan(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		if (this.treeTypes.get(tree.getChild(0)) != stringType) {
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BGE 4"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else {
			// a<=b <=> b>=a
			StrLessOrEqualTh(registerRight, registerLeft, registerIndex);
		}
		registerManager.freeRegister();
	}


	private void StrLessOrEqualTh(int registerLeft, int registerRight, int registerIndex) {

		int registerAddA; // contient adresse de A
		registerAddA=registerManager.provideRegister();
		int registerAddB; // contient adresse de B
		registerAddB=registerManager.provideRegister();
		int registerDep; // Déplacement tas
		registerDep=registerManager.provideRegister();
		int registerOp; // sert à faire des masques
		registerOp=registerManager.provideRegister();

		//Met à 0 le décalage
		writer.writeFunction(String.format("LDW R"+registerDep+", #0"));

		//Met les adresses des strings dans regsiterAdd A et B
		writer.writeFunction(String.format("LDW R"+registerAddA+", R"+registerLeft));
		writer.writeFunction(String.format("LDW R"+registerAddB+", R"+registerRight));

		//Met les adresses des strings dans registerLeft et registerRight
		writer.writeFunction(String.format("LDW R"+registerLeft+", R"+registerAddA));
		writer.writeFunction(String.format("LDW R"+registerRight+", R"+registerAddB));

		//Se décaler dans le tas
		writer.writeFunction(String.format("ADD R"+registerLeft+", R"+registerDep+", R"+registerLeft));
		writer.writeFunction(String.format("ADD R"+registerRight+", R"+registerDep+", R"+registerRight));

		//Charge le registre avec les deux premiers caracteres
		writer.writeFunction(String.format("LDW R"+registerLeft+", (R"+registerLeft+")"));
		writer.writeFunction(String.format("LDW R"+registerRight+", (R"+registerRight+")"));


		//Met ff00 dans registerOp
		writer.writeFunction(String.format("LDW R"+registerOp+", #65280"));
		//Met le premier caractere de registerLeft en regIndex
		writer.writeFunction(String.format("AND R"+registerLeft+", R"+registerOp+", R"+registerOp));


		//test sur valeur du caractere1
		writer.writeFunction(String.format("BNE 6"));
		// => si vaut null : true => fin du programme
		writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
		writer.writeFunction(String.format("BMP 36")); // fin des instructions
		// => si ne vaut pas null : continue le programme

		//Fait str1-str2
		writer.writeFunction(String.format("CMP R"+registerLeft+", R"+registerRight));
		//test si str1-str2 >= 0
		writer.writeFunction(String.format("BGE 6"));
		// => si str1 - str2 < 0 : true => fin du programme
		writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
		writer.writeFunction(String.format("BMP 18")); // fin des instructions

		//test si str1-str2 == 0
		// si str1 != str2, alors str1 > str2 : false
		writer.writeFunction(String.format("BEQ 6"));
		writer.writeFunction(String.format("LDW R"+registerIndex+", #0"));
		writer.writeFunction(String.format("BMP 18")); // fin des instructions
		//A tester !

		//Met 00ff dans le registerOp
		writer.writeFunction(String.format("LDW R"+registerOp+", #255"));
		//Met le caractere 2 de str1 dans registerIndex
		writer.writeFunction(String.format("AND R"+registerLeft+", R"+registerOp+", R"+registerOp));
		//test si caractere 2 vaut null
		writer.writeFunction(String.format("BNE 6"));
		// si caractere 2 de str1 est null : renvoyer true
		writer.writeFunction(String.format("LDW R"+registerIndex+", #1"));
		writer.writeFunction(String.format("BMP 4"));

		//ici on sait qu'il faut continuer : décalage
		writer.writeFunction(String.format("ADQ 2, R"+registerDep));
		writer.writeFunction(String.format("BMP -62")); // on boucle

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

		if (this.treeTypes.get(tree.getChild(0)) != stringType) {
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BLE 4"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
		} else { // inf.src
			StrLessOrEqualTh(registerLeft, registerRight, registerIndex);
		}
		registerManager.freeRegister();
	}

	private void translateEqual(Tree tree, int registerIndex) {
		int registerLeft = registerIndex;
		translate(tree.getChild(0), registerLeft);
		int registerRight = registerManager.provideRegister();
		translate(tree.getChild(1), registerRight);

		if (this.treeTypes.get(tree.getChild(0)) != stringType) {
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BEQ 4"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));
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
			writer.writeFunction(String.format("CMP R%d, R%d", addOne, addTwo));
			writer.writeFunction(String.format("BEQ 6"));
			//Tailles différentes : FALSE
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 70")); // A REFLECHIR : go fin programme FIN
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
			writer.writeFunction(String.format("BNE 20")); // A voir => (A)
			//Premier caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #65280", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerRight, regOp, regOp));
			writer.writeFunction(String.format("BNE 6")); // A voir
			//Premier caractere str2 vaut nul : égalité true
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 36")); // FIN : TRUE
			//A tester : temp

			//Premier caractere str2 différent de nul : égalité false
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 28")); // FIN : FALSE
			//A tester : temp


			//Premier caractere str1 différent de nul (A)
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BEQ 6")); // A voir
			// Ici str1-str2 != 0
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 20")); // FIN : FALSE
			// Ici str1-str2 = 0
			writer.writeFunction(String.format("LDW R%d, #255", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerLeft, regOp, regOp));
			writer.writeFunction(String.format("BNE 6"));
			// Ici deuxieme caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 6")); // FIN : TRUE

			// Ici deuxieme caractère str1 != nul : continuer
			//Décalage dans le tas
			writer.writeFunction(String.format("ADQ 2, R%d", addOne));
			writer.writeFunction(String.format("ADQ 2, R%d", addTwo));
			//On boucle !
			writer.writeFunction(String.format("BMP -66")); // A voir
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

		if (this.treeTypes.get(tree.getChild(0)) != stringType) {
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BNE 4"));
			writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
			writer.writeFunction(String.format("BMP 2"));
			writer.writeFunction(String.format("LDQ 1, R%d", registerIndex));

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
			writer.writeFunction(String.format("CMP R%d, R%d", addOne, addTwo));
			writer.writeFunction(String.format("BEQ 6"));
			//Tailles différentes : FALSE
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 70")); // A REFLECHIR : go fin programme FIN
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
			writer.writeFunction(String.format("BNE 20")); // A voir => (A)
			//Premier caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #65280", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerRight, regOp, regOp));
			writer.writeFunction(String.format("BNE 6")); // A voir
			//Premier caractere str2 vaut nul : égalité true
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 36")); // FIN : TRUE
			//A tester : temp

			//Premier caractere str2 différent de nul : égalité false
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 28")); // FIN : FALSE
			//A tester : temp


			//Premier caractere str1 différent de nul (A)
			writer.writeFunction(String.format("CMP R%d, R%d", registerLeft, registerRight));
			writer.writeFunction(String.format("BEQ 6")); // A voir
			// Ici str1-str2 != 0
			writer.writeFunction(String.format("LDW R%d, #1", registerIndex));
			writer.writeFunction(String.format("BMP 20")); // FIN : FALSE
			// Ici str1-str2 = 0
			writer.writeFunction(String.format("LDW R%d, #255", regOp));
			writer.writeFunction(String.format("AND R%d, R%d, R%d", registerLeft, regOp, regOp));
			writer.writeFunction(String.format("BNE 6"));
			// Ici deuxieme caractere str1 vaut nul
			writer.writeFunction(String.format("LDW R%d, #0", registerIndex));
			writer.writeFunction(String.format("BMP 8")); // FIN : TRUE

			// Ici deuxieme caractère str1 != nul : continuer
			//Décalage dans le tas
			writer.writeFunction(String.format("ADQ 2, R%d", addOne));
			writer.writeFunction(String.format("ADQ 2, R%d", addTwo));
			//On boucle !
			writer.writeFunction(String.format("BMP -66")); // A voir
			//A tester : temp

			//LIBERATION
			registerManager.freeRegister();
			registerManager.freeRegister();
			registerManager.freeRegister();
		}
		registerManager.freeRegister();
	}

	private void translateAndOperator(Tree tree, int registerIndex) {

		String endLabel = this.labelGenerator.getLabel(tree, "end");

		for (int i = 0; i < tree.getChildCount(); i++) {

			this.translate(tree.getChild(i), registerIndex);

			// Si le résultat est nul, on peut sauter à la fin du *and*
			this.writer.writeFunction(String.format("TST R%d", registerIndex));
			this.writer.writeFunction(String.format("BNE 4"));
			this.writer.writeFunction(String.format("JEA @%s", endLabel));
		}

		this.writer.writeFunction(endLabel, "NOP");
	}

	private void translateOrOperator(Tree tree, int registerIndex) {

		String endLabel = this.labelGenerator.getLabel(tree, "end");

		for (int i = 0; i < tree.getChildCount(); i++) {

			this.translate(tree.getChild(i), registerIndex);

			// Si le résultat n'est pas nul, on peut sauter à la fin du *or*
			this.writer.writeFunction(String.format("TST R%d", registerIndex));
			this.writer.writeFunction(String.format("BEQ 4"));
			this.writer.writeFunction(String.format("JEA @%s", endLabel));
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

		// Vérification d'erreur à l'exécution : cas d'une division par zéro
		this.writer.writeFunction(String.format("TST R%d", registerB));
		this.writer.writeFunction("BNE 4 // Vérifie que l'on ne divise par zéro");
		this.writer.writeFunction("TRP #EXIT_EXC");

		//Opération diviser va modifier registerA
		this.writer.writeFunction(String.format("DIV R%d, R%d, R%d", registerA, registerB, registerIndex));

		/*
		this.writer.writeMain("LDW R"+registerA+"");
		*/
		registerManager.freeRegister(); // Libère registerB
		registerManager.freeRegister(); // Libère registerA
	}

	/** Appel d'une fonction
	 * @param tree
	 * @param registerIndex
	 */
	private void translateCALL(Tree tree, int registerIndex) {
		registerManager.saveAll(registerIndex);
		//Récupère la tds de la fonction appelée
		String name = tree.getChild(0).toString();
		Function function = this.findFunction(name);
		if (function.isNative() && !function.isWritten()) { // Fonction native et pas encore écrite
			this.writeHeader(name, function); // Ecrit le code de la fonction dans le header
		}
		SymbolTable table = function.getSymbolTable();

		int registerIndex2 = registerManager.provideRegister(); // Réserve un registre pour empiler les arguments

		for (int i = 1, l = tree.getChildCount(); i < l; ++i) { // Parcours des arguments de la fonction
			translate(tree.getChild(i), registerIndex2);
			this.writer.writeFunction(String.format("STW R%d, -(SP) // Empile l'argument numéro %d/%d de la fonction \"%s\"", registerIndex2, i, l-1, name));
			this.stackCounter.addCount(2);
		} // Tous les arguments sont empilés

		writeEntryFunction(table, name); // Génération du code d'appel à la fonction et calcul de son chaînage statique

		int size = (tree.getChildCount() - 1) * 2;
		if (size != 0) {
			this.stackCounter.addCount(-size);
			this.writer.writeFunction(String.format("ADI SP, SP, #%d // Dépile les arguments de la fonction", size)); // Dépile les arguments
		}

		registerManager.freeRegister();

		registerManager.restoreAll(registerIndex);

		if (function.getType() != null) {
			this.writer.writeFunction(String.format("LDW R%d, R0", registerIndex));
		}
	}

	private void translateREC(Tree tree, int registerIndex) {
		this.writer.writeFunction(String.format("LDW R%d, HP", registerIndex));
		int size = tree.getChildCount() / 2 * 2;
		if (size > 0) {
			int register1 = this.registerManager.provideRegister();
			this.writer.writeFunction(String.format("LDW R%d, HP", register1));
			this.writer.writeFunction(String.format("ADI HP, HP, #%d", size));
			int register2 = this.registerManager.provideRegister();
			for (int i = 1, l = tree.getChildCount(); i < l; i += 2) {
				this.translate(tree.getChild(i + 1), register2);
				this.writer.writeFunction(String.format("STW R%d, (R%d)+", register2, register1));
			}
			this.registerManager.freeRegister();
			this.registerManager.freeRegister();
		} else {
			this.writer.writeFunction("ADQ 2, HP");
		}
	}

	private void translateARR(Tree tree, int registerIndex) {
		this.translate(tree.getChild(1), registerIndex);
		this.writer.writeFunction(String.format("TST R%d", registerIndex));
		this.writer.writeFunction("BGE 4");
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
		this.writer.writeFunction(String.format("STW R%d, (R%d) // Affectation", registerExp, registerIndex));
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
			this.writer.writeFunction(String.format("LDW R%d, #%d", registerIndex, this.strings.get(outputString)));
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
		String name = tree.toString();
		Variable variable = this.findVariable(name);

		int deplacementVariable = variable.getOffset();
		String typeOfVar;
		if (deplacementVariable < 0) { // Si variable est une variable locale
			deplacementVariable -= 2; // On doit compter le déplacement statique
			typeOfVar = "variable locale";
		} else { // variable est un argument de fonction
			deplacementVariable += 4; // Taille de l'adresse de retour
			typeOfVar = "argument";
		}

		int countStaticChain = this.countStaticChainToVariable(tree.toString());

		this.writer.writeFunction(String.format("LDW R%d, BP // ID_BEGIN : Préparation pour le chargement de l'adresse de %s \"%s\" dans un registre", registerIndex, typeOfVar, name)); // Copie le registre de base dans le registre résultat
		if (countStaticChain > 0) { // S'il y des chaînages statiques à remonter :

			int loopRegister = this.registerManager.provideRegister();

			this.writer.writeFunction(String.format("LDQ %d, R%d // Début de remontée de chaînage statique pour charger l'adresse de %s \"%s\" dans un registre", countStaticChain, loopRegister, typeOfVar, name)); // TODO ne pas utiliser LDQ
			// Début de boucle :
			this.writer.writeFunction(String.format("LDW R%d, (R%d)%d", registerIndex, registerIndex, -2));
			this.writer.writeFunction(String.format("ADQ -1, R%d", loopRegister));
			this.writer.writeFunction(String.format("BNE %d // Fin de remontée de chaînage statique pour charger l'adresse de %s \"%s\" dans un registre", -8, typeOfVar, name)); // Jump à (6 - 2)/3 = 2 instructions plus tôt. Le -2 c'est pour cette instruction de saut
			// Fin de boucle

			this.registerManager.freeRegister();
		}
		this.writer.writeFunction(String.format("ADI R%d, R%d, #%d // ID_END : Chargement de l'adresse de %s \"%s\" dans un registre", registerIndex, registerIndex, deplacementVariable, typeOfVar, name)); // L'adresse de la variable recherchée est maintenant dans registre voulu
	}

	/**
	 * Ecrit le code pour mettre la valeur de l'identifiant dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 * @return
	 */
	private void translateID(Tree tree, int registerIndex) {
		String name = tree.toString();
		translateIDAdress(tree, registerIndex); // Charge l'adresse de ID dans le registre
		this.writer.writeFunction(String.format("LDW R%d, (R%d) // Chargement de la valeur de \"%s\" dans un registre", registerIndex, registerIndex, name ));
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

		// Tests sur l'indice du tableau : negativeIndex
		this.writer.writeFunction(String.format("TST R%d", registerIndexOfArray));
		this.writer.writeFunction("BGE 4 // Vérifie que l'indice est positif");
		this.writer.writeFunction("TRP #EXIT_EXC");

		// Tests sur l'indice du tableau : outOfBound Exception
		int registerOfArraySize = this.registerManager.provideRegister();
		this.writer.writeFunction(String.format("LDW R%d, (R%d)-2 // Charge la taille du tableau", registerOfArraySize, registerIndex));
		this.writer.writeFunction(String.format("CMP R%d, R%d ", registerIndexOfArray, registerOfArraySize));
		this.writer.writeFunction(String.format("BLW 4 // Vérifie que l'indice est strictement inférieur à la taille du tableau"));
		this.writer.writeFunction("TRP #EXIT_EXC");
		this.registerManager.freeRegister();

		this.writer.writeFunction(String.format("SHL R%d, R%d // ITEM : Calcul de l'offset de l'élément du tableau", registerIndexOfArray, registerIndexOfArray));
		this.writer.writeFunction(String.format("ADD R%d, R%d, R%d // ITEM : Calcul de l'adresse de l'élement du tableau", registerIndex, registerIndexOfArray, registerIndex));

		this.registerManager.freeRegister();
	}

	/**
	 * Ecrit le code pour mettre la valeur d'un élément de tableau (ARRAY) dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateITEM(Tree tree, int registerIndex) {
		translateITEMAdress(tree, registerIndex);
		this.writer.writeFunction(String.format("LDW R%d, (R%d) // ITEM : La valeur de l'élémént du tableau dans un registre", registerIndex, registerIndex));
	}

	/**
	 * Ecrit le code pour mettre l'adresse d'un champ dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateFIELDAdress(Tree tree, int registerIndex) {
		Tree exp = tree.getChild(0);
		this.translate(exp, registerIndex); // Evaluation de la partie gauche, le pointeur du Record est maintenant dans registerIndex

		// Teste l'erreur à l'exécution : accès à un champ d'un record valant nil
		this.writer.writeFunction(String.format("TST R%d", registerIndex));
		this.writer.writeFunction("BNE 4 // Vérifie que la structure n'est pas nil");
		this.writer.writeFunction("TRP #EXIT_EXC");

		Record record = (Record) this.treeTypes.get(exp); // Récupère le type de exp
		Namespace<Variable> fields = record.getNamespace();
		String nameOfField = tree.getChild(1).toString(); // Récupère le nom du field

		this.writer.writeFunction(String.format("ADI R%d, R%d, #%d // FIELD : Calcul de l'adresse du champ \"%s\"", registerIndex, registerIndex, fields.get(nameOfField).getOffset(), nameOfField));
	}

	/**
	 * Ecrit le code pour mettre la valeur d'un champ dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 */
	private void translateFIELD(Tree tree, int registerIndex) {
		translateFIELDAdress(tree, registerIndex);

		String nameOfField = tree.getChild(1).toString(); // Récupère le nom du field

		this.writer.writeFunction(String.format("LDW R%d, (R%d) // FIELD : La valeur du champ \"%s\" est mise dans un registre", registerIndex, registerIndex, nameOfField));
	}

	private void translateINT(Tree tree, int registerIndex) {
		this.writer.writeFunction(String.format("LDW R%d, #%s", registerIndex, tree.toString()));
	}

	private void translateIf(Tree tree, int registerIndex) {
		// Label
		String endifLabel = this.labelGenerator.getLabel(tree, "end");
		this.translate(tree.getChild(0), registerIndex);
		this.writer.writeFunction(String.format("TST R%d", registerIndex));
		this.writer.writeFunction(String.format("BNE 4"));
		if (tree.getChildCount() == 3) {
			// Label
			String elseLabel = this.labelGenerator.getLabel(tree, "else");
			// On saute au `else` si l'instruction évaluée est fausse
			this.writer.writeFunction(String.format("JEA @%s", elseLabel));
			// On génère le code de `then`
			this.translate(tree.getChild(1), registerIndex);
			// On saute à la fin
			this.writer.writeFunction(String.format("JEA @%s", endifLabel));
			// On génère le l'étiquette et le code de else
			this.writer.writeFunction(elseLabel, "NOP");
			this.translate(tree.getChild(2), registerIndex);

		} else {
			// On saute au `endif` si l'instruction évaluée est fausse
			this.writer.writeFunction(String.format("JEA @%s", endifLabel));
			// On génère le code de `then`
			this.translate(tree.getChild(1), registerIndex);
		}
		// Étiquette endif
		this.writer.writeFunction(endifLabel, "NOP");
	}

	private void translateWhile(Tree tree, int registerIndex) {
		this.registerManager.saveAll(registerIndex);
		String testLabel = labelGenerator.getLabel(tree, "test");
		String endLabel = labelGenerator.getLabel(tree, "end");
		int registerIndex2 = this.registerManager.provideRegister();
		this.stackCounter.register(tree, this.currentTable);
		this.writer.writeFunction(testLabel, "NOP");
		this.translate(tree.getChild(0), registerIndex2);
		this.writer.writeFunction(String.format("TST R%d", registerIndex2));
		this.writer.writeFunction(String.format("BNE 4"));
		this.writer.writeFunction(String.format("JEA @%s", endLabel));
		this.translate(tree.getChild(1), registerIndex2);
		this.writer.writeFunction(String.format("JEA @%s", testLabel));
		this.writer.writeFunction(endLabel, "NOP");
		this.stackCounter.unregister(tree);
		this.registerManager.freeRegister();
		this.registerManager.restoreAll(registerIndex);
	}

	private void translateFor(Tree tree, int registerIndex) {
		this.registerManager.saveAll(registerIndex);
		String testLabel = labelGenerator.getLabel(tree, "test");
		String endLabel = labelGenerator.getLabel(tree, "end");
		int registerIndex2 = this.registerManager.provideRegister();
		this.translate(tree.getChild(1), registerIndex2);
		this.writer.writeFunction(String.format("STW R%d, -(SP) // Empilage de la borne inférieure", registerIndex2));
		this.stackCounter.addCount(2);
		this.translate(tree.getChild(2), registerIndex2);
		this.writer.writeFunction(String.format("STW R%d, -(SP) // Empilage de la borne supérieure", registerIndex2));
		this.stackCounter.addCount(2);
		SymbolTable table = this.next();
		String label = this.labelGenerator.getLabel(table, "for");
		this.writeEntryFunction(table, label);
		this.descend();
		for (Map.Entry<String, FunctionOrVariable> entry: table.getFunctionsAndVariables()) {
			this.writer.writeFunction(String.format("ADQ -2, SP // Empilage de l'indice de boucle"));
			this.stackCounter.addCount(2);
			((Variable) entry.getValue()).translate(true);
		}
		int register1 = this.registerManager.provideRegister();
		int register2 = this.registerManager.provideRegister();
		int register3 = this.registerManager.provideRegister();
		this.stackCounter.register(tree, this.currentTable);
		this.writer.writeFunction(String.format("LDW R%d, (BP)6", register1));
		this.writer.writeFunction(String.format("STW R%d, (BP)-4 // Initialisation de l'indice de boucle", register1));
		this.writer.writeFunction(String.format("LDW R%d, (BP)4", register2));
		this.writer.writeFunction(testLabel, String.format("CMP R%d, R%d", register2, register1));
		this.writer.writeFunction(String.format("BGE 4", register2, register1));
		this.writer.writeFunction(String.format("JEA @%s", endLabel));
		this.translate(tree.getChild(3), register3);
		this.writer.writeFunction(String.format("ADQ 1, R%d", register1));
		this.writer.writeFunction(String.format("STW R%d, (BP)-4 // Incrémentation de l'indice de boucle", register1));
		this.writer.writeFunction(String.format("JEA @%s", testLabel));
		this.writer.writeFunction(endLabel, "NOP");
		this.stackCounter.unregister(tree);
		this.registerManager.freeRegister();
		this.registerManager.freeRegister();
		this.registerManager.freeRegister();
		this.ascend();
		this.stackCounter.addCount(-4);
		this.writer.writeFunction("ADQ 4, SP // Dépilage des bornes de la boucle");
		this.registerManager.freeRegister();
		this.registerManager.restoreAll(registerIndex);
	}

	/**
	 * Traduit l'AST passé en paramètre représentant une expression LET, met le résultat de l'expression dans le registre registerIndex
	 * @param tree
	 * @param registerIndex
	 * @return
	 */
	private void translateLet(Tree tree, int registerIndex) {
		this.registerManager.saveAll(registerIndex);
		SymbolTable table = this.currentTable;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": { // dans le cas d'une suite de déclarations de types
					writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(), "type"));
					this.descend(); // On avait créé une nouvelle table avant la première déclaration, donc on y descend
					int lj = i;
					symbol = dec.getChild(lj);
					do; while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
					i = lj - 1;
					break;
				}
				case "function": { // dans le cas d'une suite de déclarations de fonctions
					writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(), "function"));
					this.descend(); // On avait créé une nouvelle table avant la première déclaration, donc on y descend
					int lj = i;
					do {
						symbol = dec.getChild(lj);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = (Function) this.currentTable.getFunctionsAndVariables().get(name);
						labelGenerator.getLabel(function.getSymbolTable(), name); // Création du label de la fonction
						this.descend(); // Descente dans la TDS de la fonction
						Namespace<FunctionOrVariable> namespace = this.currentTable.getFunctionsAndVariables();
						for (Map.Entry<String, FunctionOrVariable> entry: namespace) {
							((Variable) entry.getValue()).translate(true);
						}
						int register = this.registerManager.provideRegister();
						translate(body, register); // Génère code pour le corps de la fonction
						this.writer.writeFunction(String.format("LDW R0, R%d", register));
						this.registerManager.freeRegister();
						this.ascend();
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));
					i = lj - 1;
					break;
				}
				case "var": { // dans le cas de la déclaration d'une variable
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					FunctionOrVariable functionOrVariable;
					if (this.currentTable == table || (functionOrVariable = this.currentTable.getFunctionsAndVariables().get(name)) instanceof Function || functionOrVariable instanceof Variable && ((Variable) functionOrVariable).isTranslated()) {
						writeEntryFunction(this.next(), this.labelGenerator.getLabel(this.next(), "var"));
						this.descend();
					}
					int register = this.registerManager.provideRegister();
					this.translate(exp, register);
					this.writer.writeFunction(String.format("LDW R0, R%d // Copie de la variable locale", register));
					this.registerManager.freeRegister();
					this.writer.writeFunction(String.format("STW R0, -(SP) // Empilage de la variable locale \"%s\"", name));
					this.stackCounter.addCount(2);
					Variable variable = (Variable) this.currentTable.getFunctionsAndVariables().get(name); // Récupération de la variable déclarée
					variable.translate(true);
					break;
				}
			}
		}
		int register = this.registerManager.provideRegister();
		translate(seq, register); // Evalue le IN
		this.writer.writeFunction(String.format("LDW R0, R%d", register));
		this.registerManager.freeRegister();
		while (this.currentTable != table) { // On remonte les TDS pour revenir à la profondeur d'avant le LET
			this.ascend();
		}
		this.registerManager.restoreAll(registerIndex);
		if (this.treeTypes.get(tree) != null) {
			this.writer.writeFunction(String.format("LDW R%d, R0", registerIndex));
		}
	}

	private void translateNil(Tree tree, int registerIndex) {
		this.writer.writeFunction(String.format("LDQ 0, R%d", registerIndex));
	}

	private void translateBreak(Tree tree, int registerIndex) {
		Tree parent = tree;
		Tree child = tree;
		while (!(parent = parent.getParent()).toString().equals("while") && !(parent.toString().equals("for") && child == parent.getChild(3))) {
			child = parent;
		}
		int offset = this.stackCounter.getOffset(parent);
		if (offset > 0) {
			this.writer.writeFunction(String.format("ADI SP, SP, #%d // Dépilage avant le break", offset));
		}
		offset = this.stackCounter.getOffset(this.stackCounter.getSymbolTable(parent)) - this.stackCounter.getOffset(this.currentTable);
		if (offset > 0) {
			this.writer.writeFunction(String.format("ADI BP, BP, #%d // Restauration de la base de l'environnement courant avant le break", offset));
		}
		String label = this.labelGenerator.getLabel(parent, "end");
		this.writer.writeFunction(String.format("JEA @%s", label));
	}

	public String toString() {
		return this.writer.toString();
	}

}
