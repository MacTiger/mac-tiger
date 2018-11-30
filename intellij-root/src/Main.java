import org.antlr.runtime.*;
import org.antlr.runtime.tree.Tree;
import parser.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws Exception {
        String path = "sample.tiger";
        InputStream file = new FileInputStream(new File(path));
        ANTLRInputStream input = new ANTLRInputStream(file);
        TigerLexer lexer = new TigerLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TigerParser parser = new TigerParser(tokens);
        TigerParser.program_return result = parser.program();
        Tree tree = (Tree) result.getTree();
        buildSymbolTable(tree);
    }

    public static void buildSymbolTable(Tree tree) {
        // Cette fonction doit créer la table de symboles depuis l'arbre tree
        // Pour l'instant, elle ne fait qu'un parcours en profondeur de l'arbre et l'affiche en post-fixé
        for (int i = 0; i < tree.getChildCount(); i++) {
            buildSymbolTable(tree.getChild(i));
        }
        // Pour l'instant, on ne fait qu'un print
        System.out.println((String) tree.getText());
    }
}