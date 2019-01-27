import java.io.InputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;

import symboltable.SymbolTable;

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
		Main.compile(System.in, syntaxOnly);
	}

	public static void compile (InputStream stream, boolean syntaxOnly) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		TigerLexer lexer = new TigerLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TigerParser parser = new TigerParser(tokens);
		TigerParser.program_return result = parser.program();
		Tree tree = (Tree) result.getTree();
		if (!syntaxOnly) {
			SymbolTable root = new SymbolTable();
			root.fillWith(tree);
		}
	}

}
