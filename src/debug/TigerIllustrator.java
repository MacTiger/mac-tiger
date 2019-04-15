package debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import semantic.Array;
import semantic.Function;
import semantic.FunctionOrVariable;
import semantic.Primitive;
import semantic.Record;
import semantic.SymbolTable;
import semantic.Type;
import semantic.Variable;

public class TigerIllustrator {

	String result;

	public TigerIllustrator(SymbolTable table) {
		// Fonction créant le String qui permettra de générer l'illustration de la TDS
		// Elle appelle toString
		this.result = "digraph structs {\nrankdir=LR;\n";
		String allTypes = "\nTypes[shape=record, label=\"{Types déclarés}";

		allTypes += "|{ VOID | <VOID> }";

		ArrayList<String> resArray = this.draw(table.getParent(), "TDS", -1);
		this.result += resArray.get(0) + "\n" + resArray.get(1) + resArray.get(2);
		allTypes += resArray.get(3);

		resArray = this.draw(table, "TDS", 0);
		allTypes += resArray.get(3) + "\"]\n";
		this.result += resArray.get(0) + resArray.get(1) + resArray.get(2) + allTypes + this.makeLink(this.getName("TDS", 0), "parent", "TDS", "parent", false);

		this.result += "\n}";
	}

	private ArrayList<String> draw(SymbolTable table, String parent, int tableIndex) {
		// Renvoit : [graph, graphChildren, graphLinks]

		String name = this.getName(parent, tableIndex);
		String graph = name+ "[shape=record, label=\""; // Résultat à afficher
//		graph += " " + name + " }"; // Nom de la TDS, et port parent
		String linksOfGraph = "{<parent> "; // Partie de graph qui servira à la liaison avec les autres TDS
		String typesGraph = ""; // Partie de graph qui contiendra les types
		String varFuncGraph = ""; // Partie de graph qui contiendra les fonctions et variables

		String graphChildren = ""; // String des graphiques des TDS filles de cette TDS
		String graphLinks = ""; // String des liaisons entre TDS
		String allTypes = "";

		List<SymbolTable> symbolTablesOfFunctions = new ArrayList<SymbolTable>(); // Array des SymbolTable de fonction déjà liés

//		linksOfGraph += "{<parent>}"; // Prévoit le point d'ancrage du lient vers son père

		Variable var = null;
		Function function = null;
		String partOfGraph = ""; // String auxiliaire pour la création des divers cellules de la TDS

		int i = 0;

		for (Map.Entry<String, FunctionOrVariable> stringSymbolEntry: table.getFunctionsAndVariables()) { // Parcours des fonctions et variables déclarées dans cette TDS
			partOfGraph = "";
			if (i > 0) {
				varFuncGraph += " | "; // Séparation entre la variable/fonction qu'on est en train d'ajouter, et celle d'avant
			}
			if (stringSymbolEntry.getValue() instanceof Variable) { // Cellule d'une variable
				var = (Variable) stringSymbolEntry.getValue();
//				partOfGraph += "var|"+ stringSymbolEntry.getKey() + "|" + var.getOffset() + "| <" + "typeVar_" + i + ">";
//				graphLinks += this.makeLink(name, "typeVar_" + i, "Types", this.makeAddress(var.getType().toString()), false); // Lien du type de la variable
				ArrayList<String> res = this.makeCell(var, stringSymbolEntry.getKey(), name, String.valueOf(i));
				partOfGraph += res.get(0);
				graphLinks += res.get(1);
			} else if (stringSymbolEntry.getValue() instanceof Function) { // Cellule d'une fonction
				function = (Function) stringSymbolEntry.getValue();
				partOfGraph += "<function_" + i + "> "; // Ajout du port pour brancher la TDS de la fonction à sa cellule
				symbolTablesOfFunctions.add(i, function.getSymbolTable()); // Ajout de la SymbolTable en position i (numéro de la fonction) dans l'ensemble des SymbolTable de fonction
				partOfGraph += "function|" + stringSymbolEntry.getKey() + "| <" + "returnType_" + i + "> ";
				graphLinks += this.makeLink(name, "returnType_" + i, "Types", this.makeAddress(this.getType(function)), false); // Lien du type de retour de la fonction
			}
			varFuncGraph += "{" + partOfGraph + "}";
			i++;
		}

		i = 0;
		for (Map.Entry<String, Type> stringTypeEntry: table.getTypes()) { // Parcours des types déclarées dans cette TDS
			partOfGraph = "";
			if (i > 0) {
				typesGraph += " | "; // Séparation entre le type qu'on est en train d'ajouter, et celui d'avant
			}
			partOfGraph += "type|" + stringTypeEntry.getKey() + "| <" + "type_" + i + ">";
			graphLinks += this.makeLink(name, "type_" + i, "Types", this.makeAddress(stringTypeEntry.getValue().toString()), false); // Ajout du lien de la cellule du type vers son type dans le tableau allTypes

			allTypes += "|"; // Séparation avec les types précedents, faite d'office par construction de allTypes
			allTypes += "{" + this.whichInstance(stringTypeEntry.getValue()) + "| <" + this.makeAddress(stringTypeEntry.getValue().toString()) + "> }"; // Ajout du type au tableau regroupant tous les types déclarés
			typesGraph += "{" + partOfGraph + "}";

			if (stringTypeEntry.getValue() instanceof Array) { // Pour les types Array, il faut aussi relier la case Array de allTypes au type contenu dans l'Array
				Array array = (Array) stringTypeEntry.getValue();
				graphLinks += this.makeLink("Types", this.makeAddress(stringTypeEntry.getValue().toString()), "Types", this.makeAddress(array.getType().toString()), false);
			}
			i++;
		}

		for (SymbolTable symbolTable: table.getChildren()) { // Parcours des TDS filles de cette TDS
			ArrayList<String> symbolTableGraph = this.draw(symbolTable, name, i);
			// Récupère les graphes créés par la fille symbolTable :
			graphLinks += "\n" + symbolTableGraph.get(2); // Ajout des liaisons créées par symbolTable
			allTypes += symbolTableGraph.get(3);
			graphChildren += "\n" + symbolTableGraph.get(0) + "\n" + symbolTableGraph.get(1); // Ajout du graph de symbolTable et de ces enfants
			if (symbolTablesOfFunctions.contains(symbolTable)) { // N'ajoute de lien que si cela n'a pas déjà été fait (cas d'une SymbolTable d'une venant d'une fonction
				graphLinks += this.makeLink(this.getName(name, i), "parent", name, "function_" + symbolTablesOfFunctions.indexOf(symbolTable), true); // Ajout des liens entre cette TDS et sa TDS fille venant de cette fonction
			} else{
				graphLinks += this.makeLink(this.getName(name, i), "parent", name, "parent", false); // Ajout des liens entre cette TDS et sa fille symbolTable
			}
			i++;
		}

		linksOfGraph += name + " }"; // Ajout du nom de la TDS en entête de tableau
		// Gestion des séparation entres les différentes parties de graph
		if (!(typesGraph.equals(""))) {
			linksOfGraph += " | ";
		}
		if (!(varFuncGraph.equals(""))) {
			typesGraph += " | ";
		}
		graph += linksOfGraph + typesGraph + varFuncGraph; // Assemblage des parties de graph
		graph += "\"]";
		return new ArrayList<String>(Arrays.asList(graph, graphChildren, graphLinks, allTypes));
	}

	private String getName(String parent, int tableIndex) {
		if (tableIndex < 0) {
			return parent;
		}
		return parent + "_" + tableIndex;
	}

	private String getType(Function function) {
		Type type = function.getType();
		return type != null ? type.toString() : "VOID";
	}

	private ArrayList<String> makeCell(Type type, String name, String numOfCell) {
		if (type instanceof Primitive || type instanceof Array) {
			String partOfGraph = "<" + "typeVar_" + numOfCell + ">";
			String graphLinks = this.makeLink(name, "typeVar_" + numOfCell, "Types", this.makeAddress(type.toString()), false); // Lien du type de la variable
			return new ArrayList<String>(Arrays.asList(partOfGraph, graphLinks));
		} else if (type instanceof Record) {
			String partOfGraph = "";
			String graphLinks = "";
			int i = 0;
			for(Map.Entry<String, Variable> stringVariableEntry: ((Record) type).getNamespace()) {
				if (i > 0){
					partOfGraph += "|";
				}
				partOfGraph += stringVariableEntry.getKey() + "|" + "<" + "typeField_" + numOfCell + "_"+ i + ">"; // le port est différent pour chaque field de ce Record
				graphLinks += this.makeLink(name, "typeField_" + numOfCell + "_"+ i, "Types", this.makeAddress(stringVariableEntry.getValue().getType().toString()), false); // Lien du type du field
				i++;
			}
			return new ArrayList<String>(Arrays.asList(partOfGraph, graphLinks));
		} else {
			return null;
		}
	}

	private ArrayList<String> makeCell(Variable variable, String nameOfVar, String name, String numOfCell) {
		ArrayList<String> typeGraphs = this.makeCell(variable.getType(), name, numOfCell);
		String partOfGraph = "var|"+ nameOfVar + "|" + variable.getOffset() + "|";
		partOfGraph += typeGraphs.get(0);
		String graphLinks = typeGraphs.get(1);
		return new ArrayList<String>(Arrays.asList(partOfGraph, graphLinks));
	}

	private String makeLink(String source, String portSource, String dest, String portDest, boolean bidirectionnal) {
		String res = "\"" + source + "\":" + portSource + " -> \"" + dest + "\":" + portDest;
		if (bidirectionnal) {
			res += "[dir=\"both\"]";
		}
		return res + ";";
	}

	private String makeAddress(String stringToEscape) {
		return stringToEscape.toString().replace('.', '_').replace('@', '_');
	}

	private String whichInstance(Type type) {
		if (type instanceof Primitive) {
			return "Primitive";
		} else if (type instanceof Array) {
			return "Array";
		} else if (type instanceof Record) {
			return "Record";
		} else {
			return null;
		}
	}

	public String toString() {
		return this.result;
	}

}
