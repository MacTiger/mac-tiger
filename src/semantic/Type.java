package semantic;

import java.util.ArrayList;

public abstract class Type extends Symbol {

	public abstract String whichInstance();

	public abstract ArrayList<String> makeCellGraphviz(String nameOfThisTDS, String numOfCell);

}
