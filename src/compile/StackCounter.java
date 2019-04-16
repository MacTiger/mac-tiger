package compile;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

import semantic.SymbolTable;

public class StackCounter {

	private Map<Tree, SymbolTable> tables;
	private Map<Tree, Integer> treeCounts;
	private Map<SymbolTable, Integer> tableCounts;
	private int count;

	public StackCounter() {
		this.tables = new HashMap<Tree, SymbolTable>();
		this.treeCounts = new HashMap<Tree, Integer>();
		this.tableCounts = new HashMap<SymbolTable, Integer>();
		this.count = 0;
	}

	public void addCount(int count) {
		this.count += count;
	}

	public void register(SymbolTable table) {
		this.tableCounts.put(table, this.count);
	}

	public void register(Tree tree, SymbolTable table) {
		this.tables.put(tree, table);
		this.treeCounts.put(tree, this.count);
	}

	public void unregister(SymbolTable table) {
		this.tableCounts.remove(table);
	}

	public void unregister(Tree tree) {
		this.tables.remove(tree);
		this.treeCounts.remove(tree);
	}

	public SymbolTable getSymbolTable(Tree tree) {
		return this.tables.get(tree);
	}

	public int getOffset(SymbolTable table) {
		return this.count - this.tableCounts.get(table);
	}

	public int getOffset(Tree tree) {
		return this.count - this.treeCounts.get(tree);
	}

}
