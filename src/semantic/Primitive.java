package semantic;

import java.util.ArrayList;
import java.util.Arrays;

import static semantic.SymbolTable.makeAdresse;
import static semantic.SymbolTable.makeLink;

public class Primitive extends Type {

	@Override
	public String whichInstance() {
		return "Primitive";
	}

	@Override
	public ArrayList<String> makeCellGraphviz(String nameOfThisTDS, String numOfCell) {
		String partOfGraph = "<" + "typeVar_" + numOfCell + ">";
		String graphLinks = makeLink(nameOfThisTDS, "typeVar_" + numOfCell, "Types", makeAdresse(this.toString()),false);   // Lien du type de la variable
		return new ArrayList<>(Arrays.asList(partOfGraph, graphLinks));
	}

}
