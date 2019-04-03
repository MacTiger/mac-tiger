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
import compile.TigerTranslator;

public class Main {

	static int defaultColor = 1;
	static int defaultOutput = 2;

	public static void main(String[] arguments) throws Exception {
		int color = -1;
		int output = -1;
		for (String argument: arguments) {
			if (argument.equals("--no-color") && color == -1) {
				color = 0;
			} else if (argument.equals("--color") && color == -1) {
				color = 1;
			} else if (argument.equals("--no-output") && output == -1) {
				output = 0;
			} else if (argument.equals("--dot") && output == -1) {
				output = 1;
			} else if (argument.equals("--src") && output == -1) {
				output = 2;
			} else {
				throw new Exception("Illegal argument: " + argument);
			}
		}
		if (color == -1) {
			color = defaultColor;
		}
		if (output == -1) {
			output = defaultOutput;
		}
		System.exit(Main.compile(System.in, color, output));
	}

	public static int compile(InputStream stream, int color, int output) throws Exception {
		boolean noColor = color == 0;
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
		Tree tree = (Tree) result.getTree();
		root.fillWith(tree, notifier);
		int[] errorCounts = notifier.reset();
		if (output != 0 && errorCounts[0] == 0 && errorCounts[1] == 0 && errorCounts[2] == 0 ){ // S'il n'y a pas eu d'erreur : on peut faire la visualisation graphique
			if (output == 1) {   // Ecrit sur la sortie standard le code .gv permettant de visualiser la TDS
				System.out.println(root.toGraphVizFirst());
			} else if (output == 2) {
				System.out.println(new TigerTranslator(root).toString());
			}
		}
		return errorCounts[0] > 0 ? 2 : errorCounts[1] > 0 ? 3 : errorCounts[2] > 0 ? 4 : 0;
	}

}
