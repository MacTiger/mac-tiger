package semantic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Namespace<Symbol extends semantic.Symbol> implements Iterable<Map.Entry<String, Symbol>> {

	private Map<String, Symbol> symbols;

	public Namespace() {
		this.symbols = new LinkedHashMap<String, Symbol>();
	}

	public void set(String name, Symbol symbol) {
		if (name == null) {
			return;
		}
		this.symbols.put(name, symbol);
	}

	public Symbol get(String name) {
		if (name == null) {
			return null;
		}
		return this.symbols.get(name);
	}

	public boolean has(String name) {
		return this.symbols.containsKey(name);
	}

	public Iterator<Map.Entry<String, Symbol>> iterator () {
		return this.symbols.entrySet().iterator();
	}

	public int getSize(){
		return symbols.size();
	}

}
