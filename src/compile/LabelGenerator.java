package compile;

import org.antlr.runtime.tree.Tree;
import semantic.Function;

import java.util.HashMap;

/**
 * Classe gérant la création de labels uniques pour toutes les fonctions assembleurs qui seront créées à la compilation (dans compile.TigerTranslator)
 */
public class LabelGenerator {
	private HashMap<Object,String> labelsFonctionsAssembly;

	public LabelGenerator(){
		labelsFonctionsAssembly = new HashMap<>();
	}

	/**
	 * Place la Function dans l'HashMap de LabelGenerator et renvoit le label unique associé à cette Function
	 * @param function : Function à ajouter et pour laquelle on génére un label unique
	 * @param nameOfFunction : nom de la Function function
	 * @return le label unique de function
	 */
	public String addFunction(Function function, String nameOfFunction){
		String label = "_"+labelsFonctionsAssembly.size()+"_"+nameOfFunction+"_";
		return labelsFonctionsAssembly.put(function,label);
	}

	/**
	 * Place le Tree tree dans l'HashMap de LabelGenerator et renvoit le label unique associé à ce tree
	 * @param tree : le Tree à placer et pour lequel on génére un label unique
	 * @return le label unique de tree
	 */
	public String addFunction(Tree tree){
		String label = "_"+labelsFonctionsAssembly.size()+"_"+tree.toString()+"_";
		return labelsFonctionsAssembly.put(tree,label);
	}

	/**
	 * Renvoit le label associé à l'Object object, s'il a déjà été créé
	 * @param object : Object dont on cherche le label
	 * @return : le label associé à l'Object object, vaut null s'il n'a pas encore été créé
	 */
	public String getLabel(Object object){
		return labelsFonctionsAssembly.get(object);
	}
}
