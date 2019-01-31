package misc;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

public final class Notifier {

	int lexicalErrorCount;
	int syntacticErrorCount;
	int semanticErrorCount;

	public Notifier() {
		this.lexicalErrorCount = 0;
		this.syntacticErrorCount = 0;
		this.semanticErrorCount = 0;
	}

	private void log(String tag, String message, int row, int column) {
		System.err.println(tag + " \033[0;32m" + row + ":" + (column + 1) + "\033[0m " + message);
	}

	public void lexicalError(RecognitionException exception, String message) {
		++this.lexicalErrorCount;
		this.log("\033[0;31mLexical error\033[0m", message, exception.line, exception.charPositionInLine);
	}

	public void syntacticError(RecognitionException exception, String message) {
		++this.syntacticErrorCount;
		this.log("\033[0;31mSyntactic error\033[0m", message, exception.line, exception.charPositionInLine);
	}

	public void semanticError(Tree tree, String message) {
		++this.semanticErrorCount;
		this.log("\033[0;31mSemantic error\033[0m", message, tree.getLine(), tree.getCharPositionInLine());
	}

	public int[] reset() {
		int lexicalErrorCount = this.lexicalErrorCount;
		int syntacticErrorCount = this.syntacticErrorCount;
		int semanticErrorCount = this.semanticErrorCount;
		int errorCount = syntacticErrorCount + semanticErrorCount;
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
