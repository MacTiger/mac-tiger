public class Notifier {

	private boolean noColor;
	private int lexicalErrorCount;
	private int syntacticErrorCount;
	private int semanticErrorCount;

	public Notifier(boolean noColor) {
		this.noColor = noColor;
		this.lexicalErrorCount = 0;
		this.syntacticErrorCount = 0;
		this.semanticErrorCount = 0;
	}

	private String color(int color) {
		if (this.noColor) return "";
		switch (color) {
			case 0: return "\033[0m"; /* reset */
			case 1: return "\033[0;31m"; /* red */
			case 2: return "\033[0;32m"; /* green */
			case 3: return "\033[0;33m"; /* orange */
			default: return "";
		}
	}

	public String highlight(String name, int color) {
		return color > 0 && color <= 3 ? this.color(color) + name + this.color(0) : name;
	}

	private void log(String tag, String message, int row, int column) {
		System.err.println(this.highlight(tag, 1) + " " + this.highlight(row + ":" + (row != 0 ? column + 1 : 0), 2) + " " + message);
	}

	public void lexicalError(String message, int row, int column) {
		++this.lexicalErrorCount;
		this.log("Lexical error", message, row, column);
	}

	public void syntacticError(String message, int row, int column) {
		++this.syntacticErrorCount;
		this.log("Syntactic error", message, row, column);
	}

	public void semanticError(String message, int row, int column) {
		++this.semanticErrorCount;
		this.log("Semantic error", message, row, column);
	}

	public int[] consume() {
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
