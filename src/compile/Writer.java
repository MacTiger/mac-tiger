package compile;

import java.util.ArrayList;
import java.util.Stack;

public class Writer {
	private ArrayList<String> fonctionCode; // Tableau du codes des fonctions assembleur
	private String mainBody;    // Code n'étant pas dans des fonctions
	private Stack<Integer> fonctionPile;    // Pile des fonctions en cours d'écriture

	public Writer(){
		fonctionCode = new ArrayList<>();
		fonctionPile = new Stack<>();
		mainBody = "";
	}

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

	public void descendFunctionAssembly(){
		int index = fonctionCode.size();
		fonctionCode.set(index,"");     // Initialise le code de la fonction assembleur
		fonctionPile.push(fonctionCode.size()); // Ajoute le nouvel index de fonction dans la pile
	}

	public void ascendFunctionAssembly(){
		// Quand on remonte une fonction (assembleur), on ne devrait plus jamais avoir à y retourner pour écrire du code
		fonctionPile.pop();
	}
}
