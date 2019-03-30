package compile;

import org.antlr.runtime.tree.Tree;
import semantic.Function;
import semantic.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe gérant la création de labels uniques pour toutes les fonctions assembleurs qui seront créées à la compilation (dans compile.TigerTranslator)
 */
public class LabelGenerator {
	private HashMap<SymbolTable, String> labelsTDS; // Une TDS = un label
	private HashMap<Tree, ArrayList<String>> labelsTrees;   // Plusieurs labels pour les noeuds : for, while, if, opérateurs de comparaisons pour des STRINGS, & et |

	public LabelGenerator(){
		labelsTDS = new HashMap<>();
		labelsTrees = new HashMap<>();
	}

	/**
	 * Place la SymbolTable de la Function dans l'HashMap de LabelGenerator et renvoit le label unique associé à cette Function
	 * @param function : Function à ajouter et pour laquelle on génére un label unique
	 * @param nameOfFunction : nom de la Function function
	 * @return le label unique de function
	 */
	public String addFunction(Function function, String nameOfFunction){
		String label = "TDS_func_"+ labelsTDS.size()+"_"+nameOfFunction+"_";
		return labelsTDS.put(function.getSymbolTable(),label);
	}

	/**
	 * Place la SymbolTable dans l'HashMap de LabelGenerator et renvoit le label unique associé à cette Function
	 * @param symbolTable : SymbolTable à ajouter et pour laquelle on génére un label unique
	 * @return le label unique de SymbolTable
	 */
	public String addFunction(SymbolTable symbolTable){
		String label = "TDS_"+ labelsTDS.size()+"_"+symbolTable.toString()+"_";
		return labelsTDS.put(symbolTable,label);
	}

	/**
	 * Place la SymbolTable associée au Tree tree dans l'HashMap de LabelGenerator et renvoit l'ArrayList de labels uniques associés à ce tree
	 * @param tree : le Tree à placer et pour lequel on génére des labels uniques
	 * @return le label unique de tree
	 */
	public ArrayList<String> addFunction(Tree tree){
		//TODO : gérer les label pour les tree "while", "for" ...
		String label = "tree_"+ labelsTrees.size()+"_"+tree.toString()+"_";
		ArrayList<String> labels = new ArrayList<>();
		labels.add(label);
		return labelsTrees.put(tree,labels);
	}

	/**
	 * Renvoit le premier label associé à la SymbolTable symbolTable, s'il a déjà été créé
	 * @param symbolTable : SymbolTable dont on cherche le label
	 * @return : le label associé à la SymbolTable symbolTable, vaut null s'il n'a pas encore été créé
	 */
	public String getLabel(SymbolTable symbolTable){
		return labelsTDS.get(symbolTable);
	}

	/**
	 * Renvoit le i_eme label associé à la Tree tree, s'il a déjà été créé
	 * @param tree : Tree dont on cherche le label
	 * @param i : numéro de label associé au tree
	 * @return : le label associé au Tree tree, vaut null s'il n'a pas encore été créé
	 */
	public String getLabel(Tree tree, int i){
		ArrayList<String> labels = labelsTrees.get(tree);
		if(labels != null){
			return labels.get(i);
		}
		return null;
	}

}
