package misc;

import org.antlr.runtime.tree.Tree;

public final class Helpers {

	public static void alert(Tree tree, String message) {
		System.err.println("[\033[0;31mError\033[0m]   Ligne " + tree.getLine() + ", colonne " + tree.getCharPositionInLine() + ": " + message);
	}

	public static void warn(Tree tree, String message) {
		System.err.println("[\033[0;33mWarning\033[0m] Ligne " + tree.getLine() + ", colonne " + tree.getCharPositionInLine() + ": " + message);
	}

}
