package compile;

import misc.Constants;
import misc.Notifier;
import org.antlr.runtime.tree.Tree;
import semantic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import java.util.*;

import org.antlr.runtime.tree.Tree;

import misc.Constants;
import misc.Notifier;

import static syntactic.TigerParser.ARRTYPE;
import static syntactic.TigerParser.RECTYPE;
import static syntactic.TigerParser.SEQ;
import static syntactic.TigerParser.ARR;
import static syntactic.TigerParser.REC;
import static syntactic.TigerParser.CALL;
import static syntactic.TigerParser.ITEM;
import static syntactic.TigerParser.FIELD;
import static syntactic.TigerParser.ID;
import static syntactic.TigerParser.STR;
import static syntactic.TigerParser.INT;
import static syntactic.TigerParser.*;

public class TigerTranslator {

/*	private static Type nilPseudoType;
	private static Type intType;
	private static Type stringType;*/

	//TODO : faire un static pour les fonctions du langage (print ...)

/*
	private semantic.SymbolTable parent;
	private List<semantic.SymbolTable> children;
	private Namespace<Type> types;
	private Namespace<FunctionOrVariable> functionsAndVariables;
*/

	private Tree currentASTNode;    // Noeud de l'AST actuel
	private SymbolTable currentTDS; // TDS actuelle

	private TigerTranslator(SymbolTable currentTDS) {
		// Pour lancer le translator sur l'ensemble du programme, passer la TDS de niveau 0 (pas le root)
		this.currentTDS=currentTDS;     // TDS actuelle
	}

	public Type fillWith(Tree tree, Notifier notifier) {
		switch (tree.getType ()) {
			case SEQ: return this.fillWithSEQ(tree, notifier);
			case ARR: return this.fillWithARR(tree, notifier);
			case REC: return this.fillWithREC(tree, notifier);
			case CALL: return this.fillWithCALL(tree, notifier);
			case ITEM: return this.fillWithITEM(tree, notifier);
			case FIELD: return this.fillWithFIELD(tree, notifier);
			case ID: return this.fillWithID(tree, notifier);
			case STR: return this.fillWithSTR(tree, notifier);
			case INT: return this.fillWithINT(tree, notifier);
		}
		switch (tree.toString()) {
			case ":=": return this.fillWithAssignment(tree, notifier);
			case "=":
			case "<>": return this.fillWithEqualOrNot(tree, notifier);
			case ">":
			case "<":
			case ">=":
			case "<=": return this.fillWithComparator(tree, notifier);
			// "|", "&", "+", "-", "*" et "/" ont tous le même comportement pour les types de leurs opérandes :
			case "|":
			case "&":
			case "+":
			case "-":
			case "*":
			case "/": return this.fillWithIntOperator(tree, notifier);
			case "if": return this.fillWithIf(tree, notifier);
			case "while": return this.fillWithWhile(tree, notifier);
			case "for": return this.fillWithFor(tree, notifier);
			case "let": return this.fillWithLet(tree, notifier);
			case "nil": return this.fillWithNil(tree, notifier);
			case "break": return this.fillWithBreak(tree, notifier);
			default: {
				for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
					this.fillWith(tree.getChild(i), notifier);
				}
				return null;
			}
		}
	}

	private Type fillWithCALL(Tree tree, Notifier notifier) {
		/* Appel d'une fonction
		 *
		 */
		String name = tree.getChild(0).toString();
		Function function = currentTDS.findFunction(name);
		Type returnType = null;
		//TODO : gérer code d'appel à fonction

		for (int i = 0 ; i < tree.getChildCount(); i++) {   //Parcours des arguments de la fonction
			fillWith(tree.getChild(i), notifier);
			//TODO : code pour empiler l'argument
		}
		returnType = function.getType();

		return returnType;
	}

	private Type fillWithREC(Tree tree, Notifier notifier) {
		/* Déclaration d'une structure
		 * (1) Vérification de l'existence du type de structure
		 * (2) Vérification du nombre de champs
		 * (3) Vérification des noms des champs
		 * (4) Vérification des types des champs
		 */
		String name = tree.getChild(0).toString();
		Type returnType = this.findType(name);
		int i = 1;
		int l = tree.getChildCount();
		if (returnType == null) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Record)) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not a record type", name);
			returnType = null;
		} else {
			Record record = (Record) returnType;
			Namespace<Variable> namespace = record.getNamespace();
			for (Map.Entry<String, Variable> entry: namespace) {
				if (i >= l) { // Test sémantique (2)
					notifier.semanticError(tree, "%s requires more fields", name);
					break;
				}
				if (!tree.getChild(i).toString().equals(entry.getKey())) { // Test sémantique (3)
					notifier.semanticError(tree, "field name %s was expected but field name %s was found", entry.getKey(), tree.getChild(i).toString());
				}
				this.checkType(tree.getChild(i + 1), notifier, entry.getValue().getType()); // Test sémantique (4)
				i += 2;
			}
			if (i < l) { // Test sémantique (2)
				notifier.semanticError(tree, "%s requires fewer fields", name);
			}
		}
		for (; i < l; i += 2) {
			this.fillWith(tree.getChild(i + 1), notifier);
		}
		return returnType;
	}

	private Type fillWithARR(Tree tree, Notifier notifier) {
		/* Déclaration d'un tableau
		 * (1) Vérification de l'existence du type de tableau
		 * (2) Vérification du type de la taille du tableau
		 * (3) Vérification du type des éléments du tableau
		 */
		String name = tree.getChild(0).toString();
		Type returnType = this.findType(name);
		if (returnType == null) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not defined", name);
		} else if (!(returnType instanceof Array)) { // Test sémantique (1)
			notifier.semanticError(tree, "type %s is not an array type", name);
			returnType = null;
		}
		this.checkType(tree.getChild(1), notifier, semantic.SymbolTable.intType); // Test sémantique (2)
		if (returnType != null) {
			Array array = (Array) returnType;
			this.checkType(tree.getChild(2), notifier, array.getType()); // Test sémantique (3)
		}
		return returnType;
	}

	private Type fillWithAssignment(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		switch (exp.getType()) { // vérification que l'expression à gauche du ":=" est un lValue (cherche si le token de 'exp' est bien parmis les tokens imaginaires ANTLR correspondant à un lValue)
			case ID:
			case ITEM:
			case FIELD: {
				if (exp.getType() == ID) {
					Variable variable = this.findVariable(exp.toString());
					if (variable != null && !variable.isWritable()) {
						notifier.semanticError(exp, "loop index %s cannot be assigned", exp.toString());
						this.fillWith(tree.getChild(1), notifier);
						break;
					}
				}
				this.checkType(tree.getChild(1), notifier, this.fillWith(exp, notifier)); // vérification de cohérence de type entre l'expression gauche et droite
				break;
			}
			default: {
				notifier.semanticError(exp, "%s is not a valid target for an assignment", exp.toString());
				this.fillWith(tree.getChild(1), notifier);
			}
		}
		return null;
	}

	private Type fillWithSEQ(Tree tree, Notifier notifier) {
		Type returnType = null;
		for (int i = 0, l = tree.getChildCount(); i < l; ++i) {
			returnType = this.fillWith(tree.getChild(i), notifier);
		}
		return returnType;
	}

	private Type fillWithSTR(Tree tree, Notifier notifier) {
		return semantic.SymbolTable.stringType;
	}

	private Type fillWithID(Tree tree, Notifier notifier) {
		Variable variable = this.findVariable(tree.toString());
		if (variable == null) {
			notifier.semanticError(tree, "variable %s is not defined", tree.toString());
		} else{
			return variable.getType();
		}
		return null;
	}

	private Type fillWithITEM(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		Type returnType = null;
		if (!(expType instanceof Array)) { // on regarde si le fils gauche est bien un tableau
			notifier.semanticError(exp, "%s is not an array", exp.toString());
		} else { // on sait qu'on a bien un tableau
			Array array = (Array) expType;
			returnType = array.getType(); // on retourne le type des éléments stockés dans le tableau
		}
		this.checkType(tree.getChild(1), notifier, semantic.SymbolTable.intType); // on regarde si le fils droit est bien un entier
		return returnType;
	}

	private Type fillWithFIELD(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		if(!(expType instanceof Record)) { // on regarde si le fils gauche est bien une structure
			notifier.semanticError(exp, "%s is not a record", exp.toString());
		} else { // on sait qu'on a bien une structure
			Record record = (Record) expType;
			Namespace<Variable> fields = record.getNamespace();
			if (fields.get(tree.getChild(1).toString()) == null) { // on regarde si le champ existe
				notifier.semanticError(tree, "field %s is not defined", tree.getChild(1).toString());
			} else { // sinon c'est bon
				return fields.get(tree.getChild(1).toString()).getType();
			}
		}
		return null;
	}

	private Type fillWithINT(Tree tree, Notifier notifier) {
		return semantic.SymbolTable.intType;
	}

	private Type fillWithEqualOrNot(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		if (this.checkType(tree.getChild(1), notifier, expType) == semantic.SymbolTable.nilPseudoType) {
			notifier.semanticError(exp, "the type of %s cannot be inferred", exp.toString());
		}
		return semantic.SymbolTable.intType;
	}

	private Type fillWithComparator(Tree tree, Notifier notifier) {
		Tree exp = tree.getChild(0);
		Type expType = this.fillWith(exp, notifier);
		if ((expType != semantic.SymbolTable.intType) && (expType != semantic.SymbolTable.stringType)) {
			notifier.semanticError(exp, "the type of %s is not a primitive type (%s or %s)");
			exp = tree.getChild(1);
			expType = this.fillWith(exp, notifier);
			if ((expType != semantic.SymbolTable.intType) && (expType != semantic.SymbolTable.stringType)) {
				notifier.semanticError(exp, "%s is not a primitive value (either an integer or a string)");
			}
		} else {
			exp = tree.getChild(1);
			expType = this.checkType(exp, notifier, expType);
		}
		return semantic.SymbolTable.intType;
	}

	private Type fillWithIntOperator(Tree tree, Notifier notifier) {
		for (int i = 0, li = tree.getChildCount(); i < li; ++i) {
			this.checkType(tree.getChild(i), notifier, semantic.SymbolTable.intType);
		}
		return semantic.SymbolTable.intType;
	}

	private Type fillWithIf(Tree tree, Notifier notifier) {
		this.fillWith(tree.getChild(0),notifier);   //Pour le if
		this.fillWith(tree.getChild(1), notifier);  // Pour le then

		if (tree.getChildCount() == 2){    // Pour le else
			this.fillWith(tree.getChild(2), notifier);
		}
	}

	private Type fillWithWhile(Tree tree, Notifier notifier) {
		//TODO : Génerer code de while
		this.fillWith(tree.getChild(0),notifier);
		this.fillWith(tree.getChild(1),notifier);
		return null;
	}

	private Type fillWithFor(Tree tree, Notifier notifier) {
		semantic.SymbolTable table = new semantic.SymbolTable(this);
		this.children.add(table);
		Variable index = new Variable();
		index.configure(false);
		index.setType(semantic.SymbolTable.intType);
		table.functionsAndVariables.set(tree.getChild(0).toString(), index); // Ajout de la variable de boucle for dans sa table de symbole
		this.checkType(tree.getChild(1), notifier, semantic.SymbolTable.intType);	// Rempli la table des symboles pour la borne inférieure du for
		this.checkType(tree.getChild(2), notifier, semantic.SymbolTable.intType);	// Rempli la table des symboles pour la borne supérieure du for
		table.checkType(tree.getChild(3), notifier, null);	// Remplissage de la table des symboles de la boucle for
		return null;
	}

	private Type fillWithLet(Tree tree, Notifier notifier) {
		semantic.SymbolTable supTable = this;
		semantic.SymbolTable table = this;
		Tree dec = tree.getChild(0);
		Tree seq = tree.getChild(1);
		int variableOffset = 0;
		for (int i = 0, li = dec.getChildCount(); i < li; ++i) {
			Tree symbol = dec.getChild(i);
			switch (symbol.toString()) {
				case "type": {
					table = new semantic.SymbolTable(supTable);
					supTable.children.add(table);
					supTable = table;
					variableOffset = 0;
					int lj = i;
					int remainingAliases = 0;
					boolean remainsAliases = false;
					boolean remainsArraysAndRecords = false;
					do {
						Type type = null;
						String name = symbol.getChild(0).toString();
						if (table.types.has(name)) {
							notifier.semanticError(symbol.getChild(0), "redeclaration of type %s", name);
						}
						Tree shape = symbol.getChild(1);
						switch (shape.getType()) {
							case ARRTYPE: {
								type = new Array();
								if (!remainsArraysAndRecords) {
									remainsArraysAndRecords = true;
								}
								break;
							}
							case RECTYPE: {
								type = new Record();
								if (!remainsArraysAndRecords) {
									remainsArraysAndRecords = true;
								}
								break;
							}
							default: {
								remainingAliases++;
								if (!remainsAliases) {
									remainsAliases = true;
								}
							}
						}
						table.types.set(name, type);
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("type"));
					aliases: while (remainsAliases) {
						remainsAliases = false;
						for (int j = i; j < lj; ++j) {
							symbol = dec.getChild(j);
							String name = symbol.getChild(0).toString();
							Tree shape = symbol.getChild(1);
							Type type = table.types.get(name);
							if (type == null) {
								type = table.findType(shape.toString());
								if (type != null) {
									table.types.set(name, type);
									if (remainingAliases == 1) {
										break aliases;
									} else {
										remainingAliases--;
										if (!remainsAliases) {
											remainsAliases = true;
										}
									}
								}
							}
						}
					}
					if (!remainsAliases || remainsArraysAndRecords) {
						for (int j = i; j < lj; ++j) {
							symbol = dec.getChild(j);
							String name = symbol.getChild(0).toString();
							Tree shape = symbol.getChild(1);
							Type type = table.types.get(name);
							if (type instanceof Array) {
								Array array = (Array) type;
								Type itemType = table.findType(shape.getChild(0).toString());
								if (itemType == null) {
									notifier.semanticError(shape.getChild(0), "type %s is not defined", shape.getChild(0).toString());
								} else {
									array.setType(itemType);
								}
							} else if (type instanceof Record) {
								Record record = (Record) type;
								Namespace<Variable> namespace = record.getNamespace();
								int fieldOffset = 0;
								for (int k = 0, lk = shape.getChildCount(); k < lk; k += 2) {
									Variable field = new Variable();
									String fieldName = shape.getChild(k).toString();
									if (namespace.has(fieldName)) {
										notifier.semanticError(shape.getChild(k), "redeclaration of field %s", fieldName);
									}
									Type fieldType = table.findType(shape.getChild(k + 1).toString());
									if (fieldType == null) {
										notifier.semanticError(shape.getChild(k + 1), "type %s is not defined", shape.getChild(k + 1).toString());
									} else {
										field.setType(fieldType);
									}
									field.setOffset(fieldOffset);
									if (fieldType != null) {
										fieldOffset += fieldType.getSize();
									}
									namespace.set(fieldName, field);
								}
							} else if (type == null) {
								notifier.semanticError(symbol.getChild(0), "circular definition of type %s", name);
							}
						}
					}
					i = lj - 1;
					break;
				}
				case "function": {
					table = new semantic.SymbolTable(supTable);
					supTable.children.add(table);
					supTable = table;
					variableOffset = 0;
					int lj = i;
					do {
						semantic.SymbolTable subTable = new semantic.SymbolTable(table);
						table.children.add(subTable);
						Function function = new Function();
						function.setSymbolTable(subTable);
						String name = symbol.getChild(0).toString();
						if (table.functionsAndVariables.has(name)) {
							notifier.semanticError(symbol.getChild(0), "redeclaration of function %s", name);
						}
						Tree callType = symbol.getChild(1);
						int argumentOffset = 0;
						for (int k = 0, lk = callType.getChildCount(); k < lk; k += 2) {
							Variable argument = new Variable();
							String argumentName = callType.getChild(k).toString();
							if (subTable.functionsAndVariables.has(argumentName)) {
								notifier.semanticError(callType.getChild(k), "redeclaration of parameter %s", argumentName);
							}
							Type argumentType = table.findType(callType.getChild(k + 1).toString());
							if (argumentType == null) {
								notifier.semanticError(callType.getChild(k + 1), "type %s is not defined", callType.getChild(k + 1).toString());
							} else {
								argument.setType(argumentType);
							}
							argument.setOffset(argumentOffset);
							if (argumentType != null) {
								argumentOffset += argumentType.getSize();
							}
							subTable.functionsAndVariables.set(argumentName, argument);
						}
						for (Map.Entry<String, FunctionOrVariable> entry: subTable.functionsAndVariables) {
							Variable argument = (Variable) entry.getValue();
							argument.setOffset(argument.getOffset() - argumentOffset);
						}
						Tree type = symbol.getChildCount() > 3 ? symbol.getChild(3) : null;
						if (type != null) {
							Type returnType = table.findType(type.toString());
							if (returnType == null) {
								notifier.semanticError(type, "type %s is not defined", type.toString());
							} else {
								function.setType(returnType);
							}
						}
						table.functionsAndVariables.set(name, function);
					} while (++lj < li && (symbol = dec.getChild(lj)).toString().equals("function"));
					for (int j = i; j < lj; ++j) {
						symbol = dec.getChild(j);
						String name = symbol.getChild(0).toString();
						Tree body = symbol.getChild(2);
						Function function = (Function) table.functionsAndVariables.get(name);
						semantic.SymbolTable subTable = function.getSymbolTable();
						Type returnType = function.getType();
						if (returnType == null) {
							subTable.fillWith(body, notifier);
						} else {
							subTable.checkType(body, notifier, returnType);
						}
					}
					i = lj - 1;
					break;
				}
				case "var": {
					Variable variable = new Variable(); // Création de la variable déclarée
					String name = symbol.getChild(0).toString();
					Tree exp = symbol.getChild(1);
					Tree type = symbol.getChildCount() > 2 ? symbol.getChild(2) : null;
					Type returnType;
					// Détermination du type de la variable : il est soit renseigné dans la déclaration, soit par inférence
					if (type != null) {	// Cas où le type de la variable est renseigné dans la déclaration
						returnType = table.findType(type.toString());
						if (returnType == null) {
							notifier.semanticError(type, "type %s is not defined", type.toString());
							returnType = table.fillWith(exp, notifier);
						} else {
							table.checkType(exp, notifier, returnType);
						}
					} else {	// Cas où le type de la variable doit être déduit par inférence sur le type de l'expression à droite du ':='
						returnType = table.fillWith(exp, notifier);
						if (returnType == null || returnType == semantic.SymbolTable.nilPseudoType) {
							notifier.semanticError(exp, "the type of %s cannot be inferred", name);
						}
					}
					variable.setType(returnType); // Spécification du type de la variable
					if (table == this || table.functionsAndVariables.get(name) != null) {
						table = new semantic.SymbolTable(supTable);
						supTable.children.add(table);
						supTable = table;
						variableOffset = 0;
					}
					variable.setOffset(variableOffset);
					if (returnType != null) {
						variableOffset += returnType.getSize();
					}
					table.functionsAndVariables.set(name, variable);
					break;
				}
			}
		}
		return table.fillWith(seq, notifier);
	}

	private Type fillWithNil(Tree tree, Notifier notifier) {
		return semantic.SymbolTable.nilPseudoType;
	}

	private Type fillWithBreak(Tree tree, Notifier notifier) {
		Tree parent = tree;
		Tree child = tree;
		while ((parent = parent.getParent()) != null && !parent.toString().equals("function")) {
			if (parent.toString().equals("while") || parent.toString().equals("for") && child == parent.getChild(3)) {
				return null;
			} else {
				child = parent;
			}
		}
		notifier.semanticError(tree, "%s must be inside loop", tree.toString());
		return null;
	}
}
