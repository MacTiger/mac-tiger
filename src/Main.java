import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;

import misc.Constants;

import symboltable.Alias;
import symboltable.Array;
import symboltable.Function;
import symboltable.Primitive;
import symboltable.Record;
import symboltable.SymbolTable;
import symboltable.SymbolTableField;
import symboltable.Type;
import symboltable.Variable;

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
		SymbolTable root = Main.createSymbolTable();
		Main.fillSymbolTable(tree, root);
	}

	private static SymbolTable createSymbolTable() {
		SymbolTable root = new SymbolTable(null);
		List<SymbolTableField> fields = root.getFields();
		Type intType = new Primitive("int", Constants.intSize);
		Type stringType = new Primitive("string", Constants.pointerSize);
		Function[] functions = new Function [] {
			new Function("chr", root),
			new Function("concat", root),
			new Function("exit", root),
			new Function("flush", root),
			new Function("getchar", root),
			new Function("not", root),
			new Function("ord", root),
			new Function("print", root),
			new Function("printi", root),
			new Function("size", root),
			new Function("substring", root)
		};
		Variable intVariable = new Variable(null);
		intVariable.setType(intType);
		Variable stringVariable = new Variable(null);
		stringVariable.setType(stringType);
		functions[0].setType(stringType);
		functions[0].getSymbolTable().getFields().add(intVariable);
		functions[1].setType(stringType);
		functions[1].getSymbolTable().getFields().add(stringVariable);
		functions[1].getSymbolTable().getFields().add(stringVariable);
		functions[2].getSymbolTable().getFields().add(intVariable);
		functions[4].setType(stringType);
		functions[5].setType(intType);
		functions[5].getSymbolTable().getFields().add(intVariable);
		functions[6].setType(intType);
		functions[6].getSymbolTable().getFields().add(stringVariable);
		functions[7].getSymbolTable().getFields().add(stringVariable);
		functions[8].getSymbolTable().getFields().add(intVariable);
		functions[9].setType(intType);
		functions[9].getSymbolTable().getFields().add(stringVariable);
		functions[10].setType(stringType);
		functions[10].getSymbolTable().getFields().add(stringVariable);
		functions[10].getSymbolTable().getFields().add(intVariable);
		fields.add(intType);
		fields.add(stringType);
		for (Function function: functions) {
			fields.add(function);
		}
		return root;
	}

	private static SymbolTable createSymbolTable(SymbolTable parent) {
		// Création et ajout d'une nouvelle table de symboles
		SymbolTable child = new SymbolTable(parent);
		parent.getChildren().add(child);	// Informe le parent qu'il a une table de symboles de plus en fils
		return child;
	}

	private static void fillSymbolTable(Tree tree, SymbolTable parent) {
		// Cette fonction créer la table de symboles depuis l'arbre tree et ajoute ce qu'il faut dans parent
		switch (tree.toString()) {
			case "for": {
				SymbolTable child = Main.createSymbolTable(parent);
				child.getFields().add(new Variable(tree.getChild(0).toString()));	// Ajout de la variable de boucle for dans sa table de symbole	//TODO : définir un type "int" de base //TODO : Calcul du shift
				Main.fillSymbolTable(tree.getChild(1), parent);	// Rempli la table des symboles pour la borne inférieure du for
				Main.fillSymbolTable(tree.getChild(2), parent);	// Rempli la table des symboles pour la borne supérieur du for
				Main.fillSymbolTable(tree.getChild(3), child);	// Remplissage de la table des symboles de la boucle for
				break;
			}
			case "let": {
				SymbolTable child = Main.createSymbolTable(parent);
				Main.fillSymbolTable(tree.getChild(0), child);	// Descente récursive dans le sous arbre "DEC" de "let"
				Main.fillSymbolTable(tree.getChild(1), child);	// Descente récursive dans le sous arbre "SEQ" de "let"
				break;
			}
			case "type": {
				Type newType = new Primitive(tree.getChild(0).toString(), 0);	//TODO : Calcul de sizeType grâce à la somme des sizeType trouvés dans le RECTYPE (ou par alias de type)
				parent.getFields().add(newType);
				break;
			}
			case "var": {
				addVarToSymbolTable(tree.getChild(0).toString(),tree.getChild(1), parent);
				break;
			}
			case "function": {
				Type returnType = tree.getChildCount() > 3 ? new Primitive(tree.getChild(3).toString(), 0) : null;
				SymbolTable child = Main.createSymbolTable(parent); // Création de la table des symboles de la fonction
				Main.fillSymbolTable(tree.getChild(1), child);	// Remplissage de la table des symboles contenant les arguments de la fonction

				Function function = new Function(tree.getChild(0).toString(), parent);
				function.setType(returnType);
				parent.getFields().add(function);	// Ajout de la fonction dans la table des symboles

				Main.fillSymbolTable(tree.getChild(2), child);	// Remplissage de la table des symboles de la fonction avec le corps de la fonction
				break;
			}
			// Création de SymbolTableField :
			// Autres cas :
			case "CALLTYPE": {
				for (int i = 0, l = tree.getChildCount(); i < tree.getChildCount(); i += 2) {
					Main.addVarToSymbolTable(tree.getChild(i).toString(), tree.getChild(i + 1), parent);
				}
				break;
			}
			// Cas de parcours en profondeur (le "break;" du premier cas est omis volontairement car il faut parcourir de la même manière les deux arbres)
			default: {
				for (int i = 0, l = tree.getChildCount(); i < l; i++) {	// Appel récursif sur tous les fils
					Main.fillSymbolTable(tree.getChild(i), parent);
				}
			}
		}
	}

	private static Type findType(String typeName) {
		// Retourne le type primitif correspondant au typeName passé en argument
		//TODO : Stocker dans une collection les types primitifs et leur "size" pour renvoyer le bon
		return new Primitive(typeName, 64);
	}

	private static void addVarToSymbolTable(String varName, Tree typeTree, SymbolTable parent) {
		// Ajoute dans le parent la variable créée à partir de son identifiant varName, son type de nom typeName
		Variable variable = new Variable(varName);
		variable.setType(Main.findType(typeTree.toString()));
		// TODO : calculer shift à l'aide d'un paramètre de déplacement du parent
		parent.getFields().add(variable);
	}


}
