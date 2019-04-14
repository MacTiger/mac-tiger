package semantic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static semantic.SymbolTable.makeAdresse;
import static semantic.SymbolTable.makeLink;

public class Record extends Type {

	private Namespace<Variable> namespace;

	public Record() {
		this.namespace = new Namespace<Variable>();
	}

	@Override
	public String whichInstance() {
		return "Record";
	}

	@Override
	public ArrayList<String> makeCellGraphviz(String nameOfThisTDS, String numOfCell) {
		String partOfGraph ="";
		String graphLinks = "";
		int i = 0;
		for(Map.Entry<String, Variable> stringVariableEntry : namespace){
			if (i > 0){
				partOfGraph += "|";
			}
			partOfGraph += stringVariableEntry.getKey() + "|" + "<" + "typeField_" + numOfCell + "_"+ i + ">"; // le port est différent pour chaque field de ce Record
			graphLinks += makeLink(nameOfThisTDS, "typeField_" + numOfCell + "_"+ i, "Types", makeAdresse(stringVariableEntry.getValue().getType().toString()),false);   // Lien du type du field
			i++;
		}
		return new ArrayList<>(Arrays.asList(partOfGraph, graphLinks));
	}

	public Namespace<Variable> getNamespace() {
		return this.namespace;
	}

}
