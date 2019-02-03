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
		boolean syntaxOnly = false;
		for (String argument: arguments) {
			if (argument.equals("--syntax-only") && !syntaxOnly) {
				syntaxOnly = true;
			} else {
				throw new Exception("Illegal argument");
			}
		}
		System.exit(Main.compile(System.in, syntaxOnly));
	}

	public static int compile(InputStream stream, boolean syntaxOnly) throws Exception {
		Notifier notifier = new Notifier(TigerParser.tokenNames);
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		TigerLexer lexer = new TigerLexer(input) {
			public void reportError(RecognitionException exception) {
				notifier.lexicalError(this, exception);
			}
			public String getCharErrorDisplay(int character) {
				return "\033[0;33m" + super.getCharErrorDisplay(character) + "\033[0m";
			}
			// public String getTokenErrorDisplay(Token token) {
			// 	return "\033[1;33m" + super.getTokenErrorDisplay(token) + "\033[0m";
			// }
		};
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TigerParser parser = new TigerParser(tokens) {
			public void reportError(RecognitionException exception) {
				notifier.syntacticError(this, exception);
			}
			public String getTokenErrorDisplay(Token token) {
				return "\033[0;33m" + super.getTokenErrorDisplay(token) + "\033[0m";
			}
		};
		TigerParser.program_return result = parser.program();
		if (!syntaxOnly) {
			Tree tree = (Tree) result.getTree();
			SymbolTable root = new SymbolTable();
			root.fillWith(tree, notifier);
		}
		int[] errorCounts = notifier.reset();
		return errorCounts[0] > 0 ? 2 : errorCounts[1] > 0 ? 3 : errorCounts[1] > 0 ? 4 : 0;
	}

}
