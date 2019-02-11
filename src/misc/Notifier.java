package misc;

import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

public final class Notifier {

	private String[] tokenNames;
	private int lexicalErrorCount;
	private boolean noColor;
	private int syntacticErrorCount;
	private int semanticErrorCount;

	public Notifier(String[] tokenNames, boolean noColor) {
		this.tokenNames = tokenNames;
		this.noColor = noColor;
		this.lexicalErrorCount = 0;
		this.syntacticErrorCount = 0;
		this.semanticErrorCount = 0;
	}

	private String color(int color) {
		if (this.noColor) return "";
		switch(color) {
			case 0: return "\033[0m"; /* reset */
			case 1: return "\033[0;31m"; /* red */
			case 2: return "\033[0;32m"; /* green */
			case 3: return "\033[0;33m"; /* orange */
			default: return "";
		}
	}

	private void log(String tag, String message, int row, int column) {
		System.err.println(this.color(1) + tag + this.color(0) + " " + this.color(2) + row + ":" + (row != 0 ? column + 1 : 0) + this.color(0) + " " + message);
	}

	public void lexicalError(Lexer lexer, RecognitionException exception) {
		++this.lexicalErrorCount;
		this.log("Lexical error", lexer.getErrorMessage(exception, this.tokenNames), exception.line, exception.charPositionInLine);
	}

	public void syntacticError(Parser parser, RecognitionException exception) {
		++this.syntacticErrorCount;
		this.log("Syntactic error", parser.getErrorMessage(exception, this.tokenNames).replaceAll("(?<= )(EOF|'[\\S\\s]+'$)", this.color(3) + "$1" + this.color(0)), exception.line, exception.charPositionInLine);
	}

	public void semanticError(Tree tree, String message, String... names) {
		++this.semanticErrorCount;
		Object[] strings = new String[names.length];
		for (int i = 0, l = names.length; i < l; ++i) {
			strings[i] = this.color(3) + "'" + names[i] + "'" + this.color(0);
		}
		this.log("Semantic error", String.format(message, strings), tree.getLine(), tree.getCharPositionInLine());
	}

	public int[] reset() {
		int lexicalErrorCount = this.lexicalErrorCount;
		int syntacticErrorCount = this.syntacticErrorCount;
		int semanticErrorCount = this.semanticErrorCount;
		int errorCount = lexicalErrorCount + syntacticErrorCount + semanticErrorCount;
		if (errorCount > 0) {
			System.err.println(errorCount + " errors: " + lexicalErrorCount + " lexical errors, " + syntacticErrorCount + " syntactic errors, " + semanticErrorCount + " semantic errors");
		}
		this.lexicalErrorCount = 0;
		this.syntacticErrorCount = 0;
		this.semanticErrorCount = 0;
		return new int[] {
			lexicalErrorCount,
			syntacticErrorCount,
			semanticErrorCount
		};
	}

}
