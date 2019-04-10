package compile;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Classe gérant les écritures de code au bon endroit (pour permettre d'écrire le code d'une fonction en plusieurs fois, si une autre fonction (assembleur) est nécessaire durant son écriture)
 */
public class Writer {

	private static String defaultHeader;

	static {
		Writer.defaultHeader =
			"EXIT_EXC EQU 64 // Trappe exit\n" +
			"READ_EXC EQU 65 // Trappe read\n" +
			"WRITE_EXC EQU 66 // Trappe write\n" +
			"STACK_ADDR EQU 0x1000 // Adresse du bas de la pile\n" +
			"HEAP_ADDR EQU 0x1002 // Adresse du haut du tas\n" +
			"LOAD_ADDR EQU 0xF000 // Adresse du haut du programme\n" +
			"NIL EQU 0X1000 // Adresse spéciale pour la constante nil\n" +
			"\n" +
			"SP EQU R15 // Adresse du haut de la pile\n" +
			"HP EQU R14 // Adresse du bas du tas\n" +
			"BP EQU R13 // Adresse de la base de l'environnement courant\n" +
			"WR EQU R12 // Registre de travail\n" +
			"\n" +
			"ORG LOAD_ADDR // Chargement du programme\n" +
			"START MAIN // Lancement du programme\n" +
			"\n";
	}

	private LabelGenerator labelGenerator; // Générateur d'étiquettes
	private ArrayList<String> fonctionCode; // Tableau du code des fonctions assembleur
	private Stack<Integer> fonctionPile; // Pile des fonctions en cours d'écriture
	private String header; // Définition des constantes
	private String mainStart; // Début du code du corps du programme (initialisation des registres)
	private String main; // Corps du code du corps du programme (déclarations quasi-statiques)
	private String mainEnd; // Fin du code du corps du programme (terminaison normale du programme)

	public Writer(LabelGenerator labelGenerator) {
		this.labelGenerator = labelGenerator;
		this.fonctionCode = new ArrayList<String>();
		this.fonctionCode.add("");
		this.fonctionPile = new Stack<Integer>();
		this.fonctionPile.push(0);
		this.header = "";
		this.mainStart =
			labelGenerator.padLabel("MAIN") + "LDW SP, #STACK_ADDR // Initialisation du haut de la pile\n" +
			labelGenerator.getIndent() + "LDW HP, #HEAP_ADDR // Initialisation du bas de la pile\n" +
			labelGenerator.getIndent() + "LDW BP, #STACK_ADDR // Initialisation de la base de l'environnement courant\n";
		this.main = "";
		this.mainEnd =
			labelGenerator.getIndent() + "TRP #EXIT_EXC // Terminaison normale du programme\n";
	}

	private void writeHeaderLine(String line) {
		this.header += line + "\n";
	}

	/**
	 * Écrit l'instruction étiquettée dans l'en-tête
	 * @param label une étiquette
	 * @param statement une instruction
	 */
	public void writeHeader(String label, String statement) {
		this.writeHeaderLine(labelGenerator.padLabel(label) + statement);
	}

	/**
	 * Écrit une instruction dans l'en-tête
	 * @param statement une instruction à écrire
	 */
	public void writeHeader(String statement) {
		this.writeHeaderLine(labelGenerator.getIndent() + statement);
	}

	/**
	 * Écrit une ligne vide dans le corps du programme
	 */
	public void writeHeader() {
		this.writeHeaderLine("");
	}

	private void writeMainLine(String line) {
		this.main += line + "\n";
	}

	/**
	 * Écrit l'instruction étiquettée dans le corps du programme
	 * @param label une étiquette
	 * @param statement une instruction
	 */
	public void writeMain(String label, String statement) {
		this.writeMainLine(labelGenerator.padLabel(label) + statement);
	}

	/**
	 * Écrit une instruction dans le corps du programme
	 * @param statement une instruction à écrire
	 */
	public void writeMain(String statement) {
		this.writeMainLine(labelGenerator.getIndent() + statement);
	}

	/**
	 * Écrit une ligne vide dans le corps du programme
	 */
	public void writeMain() {
		this.writeMainLine("");
	}

	private void writeFunctionLine(String line) {
		int index = fonctionPile.peek();
		fonctionCode.set(index, fonctionCode.get(index) + line + "\n");
	}

	/**
	 * Écrit l'instruction étiquettée dans la fonction courante
	 * @param label une étiquette
	 * @param statement une instruction
	 */
	public void writeFunction(String label, String statement) {
		this.writeFunctionLine(labelGenerator.padLabel(label) + statement);
	}

	/**
	 * Écrit l'instruction non étiquettée dans la fonction courante
	 * @param statement une instruction
	 */
	public void writeFunction(String statement) {
		this.writeFunctionLine(labelGenerator.getIndent() + statement);
	}

	/**
	 * Écrit une ligne vide dans la fonction courante
	 */
	public void writeFunction() {
		this.writeFunctionLine("");
	}

	/**
	 * Indique à cette instance de Writer que l'on descend dans une nouvelle fonction
	 */
	public void descend() {
		int index = fonctionCode.size();
		fonctionCode.add("");     // Initialise le code de la fonction assembleur
		fonctionPile.push(index); // Ajoute le nouvel index de fonction dans la pile
	}

	/**
	 * Indique à cette instance de Writer que l'on a finit d'écrire le code d'une fonction
	 */
	public void ascend() {
		// Quand on remonte une fonction (assembleur), on ne devrait plus jamais avoir à y retourner pour écrire du code
		fonctionPile.pop();
	}

	/**
	 * @return le code généré de l'ensemble des fonctions, suivis du code du corps principal du programme
	 */
	public String toString() {
		String string =
			Writer.defaultHeader +
			this.header +
			this.mainStart +
			this.main +
			this.fonctionCode.get(0) +
			this.mainEnd;
		for (int i = 1, l = this.fonctionCode.size(); i < l; ++i) {
			string += "\n" + this.fonctionCode.get(i);
		}
		return string;
	}
}
