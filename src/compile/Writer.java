package compile;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Classe gérant les écritures de code au bon endroit (pour permettre d'écrire le code d'une fonction en plusieurs fois, si une autre fonction (assembleur) est nécessaire durant son écriture)
 */
public class Writer {

	private static String defaultHeader;
	private static String defaultMainStart;
	private static String defaultMainEnd;

	static {
		Writer.defaultHeader =
		"EXIT_EXC EQU 64 // Trappe exit\n" +
		"READ_EXC EQU 65 // Trappe read\n" +
		"WRITE_EXC EQU 66 // Trappe write\n" +
		"STACK_ADDR EQU 0X1000 // Adresse du bas de la pile\n" +
		"HEAP_ADDR EQU 0X1002 // Adresse du haut du tas\n" +
		"LOAD_ADRS EQU 0xF000 // Adresse du haut du programme\n" +
		"NIL EQU 0X1000 // Adresse spéciale pour la constante nil\n" +
		"\n" +
		"SP EQU R15 // Adresse du haut de la pile\n" +
		"WR EQU R14\n" +
		"BP EQU R13 // Adresse de la base de l'environnement courant\n" +
		"HP EQU R12 // Adresse du bas du tas\n";
		Writer.defaultMainStart =
		"ORG LOAD_ADRS // Chargement du programme\n" +
		"\n" +
		"START main // Lancement du programme\n" +
		"\n" +
		LabelGenerator.padOrTrimLabel("main") + "LDW SP, #STACK_ADDR // Initialisation du haut de la pile\n" +
		LabelGenerator.getEmptyLabel() + "LDW HP, #HEAP_ADDR // Initialisation du bas de la pile\n";
		Writer.defaultMainEnd =
		LabelGenerator.getEmptyLabel() + "ADQ -2, SP\n" +
		LabelGenerator.getEmptyLabel() + "STW BP, (SP)\n" +
		LabelGenerator.getEmptyLabel() + "LDW BP, SP\n" +
		LabelGenerator.getEmptyLabel() + "JSR @_0\n" +
		LabelGenerator.getEmptyLabel() + "LDW SP, BP\n" +
		LabelGenerator.getEmptyLabel() + "LDW BP, (SP)\n" +
		LabelGenerator.getEmptyLabel() + "ADQ 2, SP\n" +
		LabelGenerator.getEmptyLabel() + "TRP #EXIT_EXC\n";
	}

	private ArrayList<String> fonctionCode; // Tableau du code des fonctions assembleur
	private Stack<Integer> fonctionPile; // Pile des fonctions en cours d'écriture

	private String header;  // Définition des constantes
	private String main; // Code de la fonction principale
	private String footer; // Définition des fonctions built-ins

	public Writer(){
		fonctionCode = new ArrayList<>();
		fonctionPile = new Stack<>();
		header = "";
		main = "";
		footer = "";
	}

	/**
	 * Ecrit les lignes de code au bon endroit, selon la fonction dans laquelle on est en train d'écrire
	 * @param stringToWrite : lignes de code à écrire
	 */
	public void write(String stringToWrite){
		if (fonctionPile.empty()){
			throw new RuntimeException("Aucune fonction en cours d'écriture");
		}
		else {
			int index = fonctionPile.peek();
			String oldCode = fonctionCode.get(index);
			fonctionCode.set(index, oldCode + stringToWrite);
		}
	}

	/**
	 * Ecrit dans le header.
	 * @param headerToWrite : String à ajouter en fin de header.
	 */
	public void writeHeader(String headerToWrite){
		header += headerToWrite;
	}

	/**
	 * Ecrit dans le main.
	 * @param mainToWrite : String à ajouter en fin de main.
	 */
	public void writeMain(String mainToWrite) {
		main += mainToWrite;
	}

	/**
	 * Ecrit dans le footer.
	 * @param footerToWrite : String à ajouter en fin de footer.
	 */
	public void writeFooter(String footerToWrite) {
		footer += footerToWrite;
	}

	/**
	 * Indique à cette instance de Writer que l'on descend dans une nouvelle fonction
	 */
	public void descendFunctionAssembly(){
		int index = fonctionCode.size();
		fonctionCode.set(index,"");     // Initialise le code de la fonction assembleur
		fonctionPile.push(fonctionCode.size()); // Ajoute le nouvel index de fonction dans la pile
	}

	/**
	 * Indique à cette instance de Writer que l'on a finit d'écrire le code d'une fonction
	 */
	public void ascendFunctionAssembly(){
		// Quand on remonte une fonction (assembleur), on ne devrait plus jamais avoir à y retourner pour écrire du code
		fonctionPile.pop();
	}

	/**
	 * @return le code généré de l'ensemble des fonctions, suivis du code du corps principal du programme
	 */
	public String toString(){
		String res =
		Writer.defaultHeader +
		this.header +
		"\n" +
		Writer.defaultMainStart +
		this.main +
		Writer.defaultMainEnd +
		"\n" +
		this.footer;
		for (String codeOfFunction : fonctionCode){
			//TODO : gérer l'ajout des labels !
			res += "\n" + codeOfFunction;
		}
		return res;
	}
}
