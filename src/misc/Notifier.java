package misc;

import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

public final class Notifier {

	String[] tokenNames;
	int lexicalErrorCount;
	int syntacticErrorCount;
	int semanticErrorCount;

	public Notifier(String[] tokenNames) {
		this.tokenNames = tokenNames;
		this.lexicalErrorCount = 0;
		this.syntacticErrorCount = 0;
		this.semanticErrorCount = 0;
	}

	private void log(String tag, String message, int row, int column) {
		System.err.println(tag + " \033[0;32m" + row + ":" + (row != 0 ? column + 1 : 0) + "\033[0m " + message);
	}

	public void lexicalError(Lexer lexer, RecognitionException exception) {
		++this.lexicalErrorCount;
		this.log("\033[0;31mLexical error\033[0m", lexer.getErrorMessage(exception, this.tokenNames), exception.line, exception.charPositionInLine);
	}

	public void syntacticError(Parser parser, RecognitionException exception) {
		++this.syntacticErrorCount;
		this.log("\033[0;31mSyntactic error\033[0m", parser.getErrorMessage(exception, this.tokenNames).replaceAll("(?<= )(EOF|'[\\S\\s]+'$)", "\033[0;33m$1\033[0m"), exception.line, exception.charPositionInLine);
	}

	public void semanticError(Tree tree, String message, String... names) {
		++this.semanticErrorCount;
		Object[] strings = new String[names.length];
		for (int i = 0, l = names.length; i < l; ++i) {
			strings[i] = "\033[0;33m'" + names[i] + "'\033[0m";
		}
		this.log("\033[0;31mSemantic error\033[0m", String.format(message, strings), tree.getLine(), tree.getCharPositionInLine());
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
