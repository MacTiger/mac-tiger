package semantic;

import java.util.ArrayList;

public abstract class Type extends Symbol {

	public abstract int getSize();

	public abstract String whichInstance();

	public abstract ArrayList<String> makeCellGraphviz(String nameOfThisTDS, String numOfCell);

	/**
	 * Indique si cette variable est un pointeur ou non
	 * @return
	 */
	public boolean isPointer() {
		return false;
	}

}
