import org.antlr.runtime.*;
import org.antlr.runtime.tree.Tree;
import symboltable.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length >= 1 && !args[0].equals("")){
			System.setIn(new FileInputStream(args[0]));
		}
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		TigerLexer lexer = new TigerLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TigerParser parser = new TigerParser(tokens);
		TigerParser.program_return result = parser.program();
		Tree tree = (Tree) result.getTree();

		SymbolTable rootSymbolTable = new SymbolTable(null);

		buildSymbolTable(tree, rootSymbolTable);
	}

	public static void buildSymbolTable2(Tree tree) {
		// Cette fonction doit créer la table de symboles depuis l'arbre tree
		// Pour l'instant, elle ne fait qu'un parcours en profondeur de l'arbre et l'affiche en post-fixé
		for (int i = 0; i < tree.getChildCount(); i++) {
			buildSymbolTable2(tree.getChild(i));
		}
		// Pour l'instant, on ne fait qu'un print
		System.out.println((String) tree.getText());
	}

	public static void buildSymbolTable(Tree tree, SymbolTable symbolTable) {
		// Cette fonction créer la table de symboles depuis l'arbre tree et ajoute ce qu'il faut dans symbolTable

		SymbolTable newSymbolTable = null;

		switch (tree.toString()){
			// Création de nouvelles SymbolTable :
			case "let" :
				newSymbolTable = createSymbolTable(symbolTable);
				buildSymbolTable(tree.getChild(0), newSymbolTable);	// Descente récursive dans le sous arbre "DEC" de "let"
				buildSymbolTable(tree.getChild(1), newSymbolTable);	// Descente récursive dans le sous arbre "SEQ" de "let"
				break;

			case "function" :
				Type returnType = tree.getChildCount() > 3 ? new Primitive(tree.getChild(3).toString(), 0) : null;
				// TODO: distinguer les différents types de types

				newSymbolTable = createSymbolTable(symbolTable); // Création de la table des symboles de la fonction
				buildSymbolTable(tree.getChild(1), newSymbolTable);	// Remplissage de la table des symboles contenant les arguments de la fonction

				Function function = new Function(tree.getChild(0).toString());
				function.setType(returnType);
				symbolTable.getChildren().add(function);	// Ajout de la fonction dans la table des symboles

				buildSymbolTable(tree.getChild(2),newSymbolTable);	// Remplissage de la table des symboles de la fonction avec le corps de la fonction
				break;

			case "for" :
				newSymbolTable = createSymbolTable(symbolTable);
				Variable index = new Variable(tree.getChild(0).toString());
				index.setType(new Primitive("int",64)));
				newSymbolTable.getFields().add(index);	// Ajout de la variable de boucle for dans sa table de symbole	//TODO : définir un type "int" de base //TODO : Calcul du shift
				buildSymbolTable(tree.getChild(1),symbolTable);	// Rempli la table des symboles pour la borne inférieure du for
				buildSymbolTable(tree.getChild(2),symbolTable);	// Rempli la table des symboles pour la borne supérieur du for
				buildSymbolTable(tree.getChild(3),newSymbolTable);	// Remplissage de la table des symboles de la boucle for
				break;

			// Création de SymbolTableField :
			case "var" :
				addVarToSymbolTable(tree.getChild(0).toString(),tree.getChild(1), symbolTable);
				break;

			case "type" :
				int sizeType = 0;
				Type newType = new Type(tree.getChild(0).toString(), sizeType);	//TODO : Calcul de sizeType grâce à la somme des sizeType trouvés dans le RECTYPE (ou par alias de type)
				symbolTable.addField(newType);
				break;


			// Autres cas :

			case "CALLTYPE" :
				for (int i = 0; i < tree.getChildCount()/2; i++) {
					addVarToSymbolTable(tree.getChild(2*i).toString(),tree.getChild(2*i + 1),symbolTable);
				}
				break;

			// Cas de parcours en profondeur (le "break;" du premier cas est omis volontairement car il faut parcourir de la même manière les deux arbres)
			case "DEC" :
			case "SEQ" :
				for (int i = 0; i < tree.getChildCount(); i++) {	// Appel récursif sur tous les fils du noeud "DEC" ou "SEQ"
					buildSymbolTable(tree.getChild(i), symbolTable);
				}
				break;

			default:
				break;
		}
	}

	public static SymbolTable createSymbolTable(SymbolTable symbolTable){
		// Création et ajout d'une nouvelle table de symboles
		SymbolTable newSymbolTable = new SymbolTable(symbolTable);
		symbolTable.getChildren().add(newSymbolTable);	// Informe le parent qu'il a une table de symboles de plus en fils

		return newSymbolTable;
	}

	public static Type findType(String typeName){
		// Retourne le type primitif correspondant au typeName passé en argument
		//TODO : Stocker dans une collection les types primitifs et leur "size" pour renvoyer le bon
		return new Type(typeName, 64);
	}

	public static void addVarToSymbolTable(String varName, Tree typeTree, SymbolTable symbolTable){
		// Ajoute dans symbolTable la variable créée à partir de son identifiant varName, son type de nom typeName
		int shift = 0;	// TODO : calculer shift à l'aide d'un paramètre de déplacement de symbolTable
		Variable variable = new Variable(varName);	// Gérer le cas où le type est un Reccord
		variable.setType(findType(typeTree.toString()));
		variable.setShift(shift);
		symbolTable.getChildren().add(variable);
	}


}
