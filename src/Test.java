import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class Test {
	public static void main (String [] args) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream (System.in);
		TigerLexer lexer = new TigerLexer (input);
		CommonTokenStream tokens = new CommonTokenStream (lexer);
		TigerParser parser = new TigerParser (tokens);
		parser.program ();
		// Tree tree = (Tree) parser.program ().getTree ();
		// System.out.println (tree.toStringTree ());
	}
}
