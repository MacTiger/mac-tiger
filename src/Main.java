import java.io.InputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

import misc.Notifier;
import lexical.TigerLexer;
import syntactic.TigerParser;
import semantic.SymbolTable;

public class Main {

	public static void main(String[] arguments) throws Exception {
		boolean noColor = false;
		boolean syntaxOnly = false;
		boolean graphic = false;
		for (String argument: arguments) {
			if (argument.equals("--no-color") && !noColor) {
				noColor = true;
			} else if (argument.equals("--syntax-only") && !syntaxOnly) {
				syntaxOnly = true;
			} else if (argument.equals("--graphic")){
				graphic = true;
			} else {
				throw new Exception("Illegal argument");
			}
		}
		System.exit(Main.compile(System.in, noColor, syntaxOnly, graphic));
	}

	public static int compile(InputStream stream, boolean noColor, boolean syntaxOnly, boolean graphic) throws Exception {
		Notifier notifier = new Notifier(TigerParser.tokenNames, noColor);
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		TigerLexer lexer = new TigerLexer(input) {
			public void reportError(RecognitionException exception) {
				notifier.lexicalError(this, exception);
			}
			public String getCharErrorDisplay(int character) {
				String name = super.getCharErrorDisplay(character);
				return noColor ? name : "\033[0;33m" + name + "\033[0m";
			}
			// public String getTokenErrorDisplay(Token token) {
			// 	String name = super.getTokenErrorDisplay(token);
			// 	return noColor ? name : "\033[0;33m" + name + "\033[0m";
			// }
		};
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TigerParser parser = new TigerParser(tokens) {
			public void reportError(RecognitionException exception) {
				notifier.syntacticError(this, exception);
			}
			public String getTokenErrorDisplay(Token token) {
				String name = super.getTokenErrorDisplay(token);
				return noColor ? name : "\033[0;33m" + name + "\033[0m";
			}
		};
		TigerParser.program_return result = parser.program();
		SymbolTable root = new SymbolTable();
		if (!syntaxOnly) {
			Tree tree = (Tree) result.getTree();
			root.fillWith(tree, notifier);
		}
		int[] errorCounts = notifier.reset();
		if (errorCounts[0] == 0 && errorCounts[1] == 0 && errorCounts[2] == 0 ){ // S'il n'y a pas eu d'erreur : on peut faire la visualisation graphique
			if (graphic){   // Ecrit sur la sortie standard le code .gv permettant de visualiser la TDS
				System.out.println(root.toGraphVizFirst());
			}
		}
		return errorCounts[0] > 0 ? 2 : errorCounts[1] > 0 ? 3 : errorCounts[2] > 0 ? 4 : 0;
	}

}
