import java.io.InputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

import lexical.TigerLexer;
import syntactic.TigerParser;
import semantic.TigerChecker;
import debug.TigerIllustrator;
import compile.TigerTranslator;

public class Main {

	private static int defaultColor = 1;
	private static int defaultOutput = 2;

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
		System.exit(Main.process(System.in, color, output));
	}

	public static int process(InputStream stream, int color, int output) throws Exception {
		boolean noColor = color == 0;
		Notifier notifier = new Notifier(noColor);
		String[] tokenNames = TigerParser.tokenNames;
		ANTLRInputStream input = new ANTLRInputStream(stream);
		TigerLexer lexer = new TigerLexer(input) {
			public void reportError(RecognitionException exception) {
				String message = this.getErrorMessage(exception, tokenNames);
				int row = exception.line;
				int column = exception.charPositionInLine;
				notifier.lexicalError(message, row, column);
			}
			public String getCharErrorDisplay(int character) {
				String name = super.getCharErrorDisplay(character);
				return notifier.highlight(name, 3);
			}
			// public String getTokenErrorDisplay(Token token) {
			// 	String name = super.getTokenErrorDisplay(token);
			// 	return notifier.highlight(name, 3);
			// }
		};
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TigerParser parser = new TigerParser(tokens) {
			public void reportError(RecognitionException exception) {
				String message = this.getErrorMessage(exception, tokenNames);
				int row = exception.line;
				int column = exception.charPositionInLine;
				notifier.syntacticError(message, row, column);
			}
			public String getTokenErrorDisplay(Token token) {
				String name = super.getTokenErrorDisplay(token);
				return notifier.highlight(name, 3);
			}
			public String getErrorMessage(RecognitionException exception, String[] tokenNames) {
				String message = super.getErrorMessage(exception, tokenNames);
				return message.replaceAll("(?<= )([A-Z][0-9A-Z_a-z]*|'(?:[^'\\\\]|\\\\'|\\\\\\\\|\\\\)+')", notifier.highlight("$1", 3));
			}
		};
		Tree tree = (Tree) parser.program().getTree();
		TigerChecker checker = new TigerChecker(tree) {
			public void reportError(Tree tree, String message, String... names) {
				message = this.getErrorMessage(tree, message, names);
				int row = tree.getLine();
				int column = tree.getCharPositionInLine();
				notifier.semanticError(message, row, column);
			}
			public String getTokenErrorDisplay(String token) {
				String name = super.getTokenErrorDisplay(token);
				return notifier.highlight(name, 3);
			}
		};
		int[] errorCounts = notifier.consume();
		if (output != 0 && errorCounts[0] == 0 && errorCounts[1] == 0 && errorCounts[2] == 0) {
			if (output == 1) {
				TigerIllustrator illustrator = new TigerIllustrator(checker.getSymbolTable());
				System.out.println(illustrator);
			} else if (output == 2) {
				TigerTranslator translator = new TigerTranslator(tree, checker.getTreeTypes(), checker.getSymbolTable());
				System.out.println(translator);
			}
		}
		return errorCounts[0] > 0 ? 2 : errorCounts[1] > 0 ? 3 : errorCounts[2] > 0 ? 4 : 0;
	}

}
