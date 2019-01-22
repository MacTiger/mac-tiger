import java.io.FileInputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;

import symboltable.SymbolTable;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length >= 1 && !args[0].equals("")) {
			System.setIn(new FileInputStream(args[0]));
		}
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		TigerLexer lexer = new TigerLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TigerParser parser = new TigerParser(tokens);
		TigerParser.program_return result = parser.program();
		Tree tree = (Tree) result.getTree();
		SymbolTable root = new SymbolTable(null);
		root.fillWith(tree);
	}

}
