package symboltable;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.Tree;

public class SymbolTable {

	private SymbolTable parent;
	private ArrayList<SymbolTable> children;
	private Namespace types;
	private Namespace functionsAndVariables;

	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
		this.children = new ArrayList<SymbolTable>();
		this.types = new Namespace();
		this.functionsAndVariables = new Namespace();
	}

	public void fillWith(Tree tree) {
		switch (tree.toString()) {
			case "for": {
				SymbolTable table = new SymbolTable(this);
				Variable iterator = new Variable(0);
				iterator.setType(null);	// TODO : remplacer "null" par le type primitif "int" déclaré dans la TDS d'ordre 0, on le cherche avec un findType appliqué sur la TDS d'ordre 0
				table.functionsAndVariables.set(tree.getChild(0).toString(), iterator); // Ajout de la variable de boucle for dans sa table de symbole

				this.fillWith(tree.getChild(1));	// Rempli la table des symboles pour la borne inférieure du for
				this.fillWith(tree.getChild(2));	// Rempli la table des symboles pour la borne supérieure du for

				table.fillWith(tree.getChild(3));	// Remplissage de la table des symboles de la boucle for

				break;
			}
			case "let": {
				SymbolTable table = new SymbolTable(this);
				Tree dec = tree.getChild(0);
				Tree seq = tree.getChild(1);
				for (int i = 0, l = dec.getChildCount(); i < l; i++) {
					Tree child = dec.getChild(i);
					Tree id = child.getChild(0);
					switch (child.toString()) {
						case "type": {
							this.types.set(id.toString(), new Alias());
							int j = i;
							while (i++ < l && (child = dec.getChild(i)).toString().equals("type")) {
								this.types.set(child.getChild(0).toString(), new Alias());
							}
							for (int k = j; k < i; k++) {
								this.fillWith(dec.getChild(k));
							}
							break;
						}
						case "function": {
							this.functionsAndVariables.set(id.toString(), new Function());
							int j = i;
							while (i++ < l && (child = dec.getChild(i)).toString().equals("function")) {
								this.functionsAndVariables.set(child.getChild(0).toString(), new Function());
							}
							for (int k = j; k < i; k++) {
								this.fillWith(dec.getChild(k));
							}
							break;
						}
						case "var": {
							this.fillWith(child);
							this.functionsAndVariables.set(id.toString(), new Variable(0));
							break;
						}
					}
				}
				this.fillWith(seq);
				break;
			}
			case "type": {
				break;
			}
			case "function": {
				SymbolTable table = new SymbolTable(this);
				Tree name = tree.getChild(0);
				Tree callType = tree.getChild(1);
				Tree exp = tree.getChild(2);
				Tree type = tree.getChildCount() > 3 ? tree.getChild(3) : null;
				Function function = (Function) this.functionsAndVariables.get(name.toString()); /* TODO */
				table.functionsAndVariables = function.getNamespace();
				for (int i = 0, l = callType.getChildCount(); i < l; i += 2) {
					Tree id = callType.getChild(i);
					// Tree type = callType.getChild(i + 1);
					Variable parameter = new Variable(0);
					// parameter.setType();
					table.functionsAndVariables.set(id.toString(), parameter);
				}
				if (type != null) {
					// function.setType(type);
				}
				/* TODO */
				this.fillWith(exp);
				break;
			}
			case "var": {
				break;
			}
			default: {
				for (int i = 0, l = tree.getChildCount(); i < l; i++) {
					this.fillWith(tree.getChild(i));
				}
			}
		}
	}

}
