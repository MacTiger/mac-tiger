package compile;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

public class StackCounter {

	private Map<Tree, Integer> counts;
	private int count;

	public StackCounter() {
		this.counts = new HashMap<Tree, Integer>();
		this.count = 0;
	}

	public void addCount(int count) {
		this.count += count;
	}

	public void register(Tree tree) {
		this.counts.put(tree, this.count);
	}
	public void unregister(Tree tree) {
		this.counts.remove(tree);
	}

	public int getOffsetSince(Tree tree) {
		return this.count - this.counts.get(tree);
	}

}
