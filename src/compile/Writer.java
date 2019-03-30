package compile;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Classe gérant les écritures de code au bon endroit (pour permettre d'écrire le code d'une fonction en plusieurs fois, si une autre fonction (assembleur) est nécessaire durant son écriture)
 */
public class Writer {
	private ArrayList<String> fonctionCode; // Tableau du codes des fonctions assembleur
	private String mainBody;    // Code n'étant pas dans des fonctions
	private Stack<Integer> fonctionPile;    // Pile des fonctions en cours d'écriture

	public Writer(){
		fonctionCode = new ArrayList<>();
		fonctionPile = new Stack<>();
		mainBody = "";
	}

	/**
	 * Ecrit les lignes de code au bon endroit, selon la fonction dans laquelle on est en train d'écrire
	 * @param stringToWrite : lignes de code à écrire
	 */
	public void write(String stringToWrite){
		if (fonctionPile.empty()){
			mainBody += stringToWrite;
		}
		else {
			int index = fonctionPile.peek();
			String oldCode = fonctionCode.get(index);
			fonctionCode.set(index, oldCode + stringToWrite);
		}
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
		String res = "";
		for (String codeOfFunction : fonctionCode){
			//TODO : gérer l'ajout des labels !
			res += codeOfFunction;
		}
		res += mainBody;
		return res;
	}
}
