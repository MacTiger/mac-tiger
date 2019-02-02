package semantic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Namespace<Symbol> {

	private Map<String, Symbol> symbols;
	private Symbol lastSymbol;

	public Map<String, Symbol> getSymbols() {
		return symbols;
	}

	public Namespace() {
		this.symbols = new LinkedHashMap<String, Symbol>();
		this.lastSymbol = null;
	}

	public void set(String name, Symbol symbol) {
		if (name == null) {
			return;
		}
		boolean added = this.symbols.containsKey(name);
		this.symbols.put(name, symbol);
		if (added) {
			this.lastSymbol = symbol;
		}
	}

	public Symbol get(String name) {
		if (name == null) {
			return null;
		}
		return this.symbols.get(name);
	}

	public Symbol getLastSymbol() {
		return this.lastSymbol;
	}

	public Iterator <Map.Entry <String, Symbol>> iterator () {
	   return this.symbols.entrySet().iterator();
	}

}
