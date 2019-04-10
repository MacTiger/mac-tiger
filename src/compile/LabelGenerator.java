package compile;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

import semantic.SymbolTable;

/**
 * Classe gérant la création d'étiquettes uniques pour toutes les fonctions assembleurs qui seront créées à la compilation (dans compile.TigerTranslator)
 */
public class LabelGenerator {

	private int length; // longueur maximale d'un étiquette
	private String indent; // indentation d'une instruction sans étiquette
	private Map<SymbolTable, String> tableLabels; // étiquettes uniques associés aux tables de symboles
	private Map<Tree, Map<String, String>> treeLabels; // étiquettes uniques associées aux nœuds (for, while, if, opérateurs de comparaisons pour des STRINGS, & et |)

	public LabelGenerator(int length) {
		this.length = length;
		this.indent = this.padLabel("");
		this.tableLabels = new HashMap<SymbolTable, String>();
		this.treeLabels = new HashMap<Tree, Map<String, String>>();
	}

	/**
	 * Rogne une étiquette pour qu'elle ne dépasse par la longueur maximale d'une étiquette
	 * @return l'étiquette rognée
	 */
	public String trimLabel(String string) {
		if (string.length() >= this.length - 1) {
			return string.substring(0, this.length - 1);
		}
		return string;
	}

	/**
	 * Complète une étiquette avec des espaces pour qu'elle atteigne la longueur maximale d'une étiquette
	 * @return l'étiquette complétée
	 */
	public String padLabel(String string) {
		return String.format("%-" + this.length + "s ", string);
	}

	/**
	 * Renvoie une chaîne de caractères constituées d'espaces de la longueur maximale d'une étiquette
	 * @return la chaîne de caractères en question
	 */
	public String getIndent() {
		return this.indent;
	}

	/**
	 * Renvoie l'étiquette unique associée à la table des symboles, si elle existe déjà
	 * Sinon, génère une étiquette unique à partir de son nom et la renvoie
	 * @param table une table des symboles
	 * @param name un nom de la table
	 * @return l'étiquette unique de la table
	 */
	public String getLabel(SymbolTable table, String name) {
		if (this.tableLabels.containsKey(table)) {
			return this.tableLabels.get(table);
		}
		String label = this.trimLabel("A" + this.tableLabels.size() + name);
		this.tableLabels.put(table, label);
		return label;
	}

	/**
	 * Renvoie l'étiquette unique associée à la table des symboles, si elle existe déjà
	 * Sinon, génère une étiquette unique et la renvoie
	 * @return l'étiquette unique de la table
	 */
	public String getLabel(SymbolTable symbolTable) {
		return this.getLabel(symbolTable, "");
	}

	/**
	 * Renvoie l'étiquette unique associée à un nœud et une clé, si elle existe déjà
	 * Sinon, génère une étiquette unique et la renvoie
	 * @param tree un nœud
	 * @param key une clé
	 * @return l'étiquette unique du nœud et de la clé
	 */
	public String getLabel(Tree tree, String key) {
		Map<String, String> labels;
		if (this.treeLabels.containsKey(tree)) {
			labels = this.treeLabels.get(tree);
			if (labels.containsKey(key)) {
				return labels.get(key);
			}
		} else {
			labels = new HashMap<String, String>();
			this.treeLabels.put(tree, labels);
		}

		String label = this.trimLabel("B" + this.treeLabels.size() + "_" + key);
		labels.put(key, label);
		return label;
	}

}
