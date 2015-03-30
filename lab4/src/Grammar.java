import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

enum SymbolType {
	TERMINAL, NON_TERMINAL
}

class Symbol {
	String name;
	SymbolType type;

	Symbol(String name, SymbolType type) {
		this.name = name;
		this.type = type;
	}
}

class RightPart {
	List<Symbol> right;
	String action;

	boolean isEpsilon() {
		return right.size() == 1 && right.get(0).name.isEmpty();
	}

	RightPart(List<Symbol> right, String action) {
		this.right = right;
		this.action = action;
	}
}

public class Grammar {
	String start;

	Map<String, String> terminals;
	Map<String, String> nonTerminals;

	Map<String, List<RightPart>> grammar;

	Map<String, Set<String>> first;
	Map<String, Set<String>> follow;

	public Set<String> getFirst(String symbol) {
		return first.get(symbol);
	}

	public Set<String> getFollow(String symbol) {
		return follow.get(symbol);
	}

	private void buildFirst() {
		first = new HashMap<>();
		for (String term : terminals.keySet()) {
			Set<String> termFirst = new TreeSet<>();
			termFirst.add(term);
			first.put(term, termFirst);
		}
		for (String nonTerm : nonTerminals.keySet()) {
			first.put(nonTerm, new TreeSet<String>());
		}

		boolean changed = true;
		while (changed) {
			changed = false;
			for (Entry<String, List<RightPart>> e : grammar.entrySet()) {
				String left = e.getKey();
				Set<String> firstLeft = first.get(left);
				for (RightPart right : e.getValue()) {
					if (right.isEpsilon()) {
						changed |= firstLeft.add("");
						continue;
					}

					List<Symbol> symbols = right.right;
					int i = 0;
					while (i < symbols.size()) {
						String name = symbols.get(i).name;
						Set<String> toAdd = first.get(name);
						changed |= firstLeft.addAll(toAdd);

						if (!first.get(name).contains("")) {
							break;
						}

						i++;
					}

					if (i == symbols.size()) {
						changed |= firstLeft.add("");
					}
				}
			}
		}
	}

	private boolean addAllButEpsilon(Set<String> dst, Set<String> src) {
		boolean changed = false;
		for (String s : src) {
			if (s.isEmpty()) {
				continue;
			}
			changed |= dst.add(s);
		}
		return changed;
	}

	private void buildFollow() {
		follow = new HashMap<>();
		for (String nonTerm : nonTerminals.keySet()) {
			follow.put(nonTerm, new TreeSet<String>());
		}
		follow.get(start).add("END"); // TODO

		boolean changed = true;
		while (changed) {
			changed = false;
			for (Entry<String, List<RightPart>> e : grammar.entrySet()) {
				String left = e.getKey();
				for (RightPart right : e.getValue()) {
					if (right.isEpsilon()) {
						continue;
					}

					List<Symbol> symbols = right.right;

					for (int i = symbols.size() - 2; i >= 0; i--) {
						Symbol curr = symbols.get(i);
						if (curr.type == SymbolType.TERMINAL) {
							continue;
						}
						Set<String> toAdd = first.get(symbols.get(i + 1).name);
						changed |= addAllButEpsilon(follow.get(curr.name),
								toAdd);
					}

					for (int i = symbols.size() - 1; i >= 0; i--) {
						Symbol curr = symbols.get(i);
						if (curr.type == SymbolType.TERMINAL) {
							break;
						}
						changed |= follow.get(curr.name).addAll(
								follow.get(left));
						if (!first.get(curr.name).contains("")) {
							break;
						}
					}
				}
			}
		}
	}

	private void readSymbols(BufferedReader br, Map<String, String> symbols)
			throws IOException {
		String line;
		while (!(line = br.readLine()).equals(IOUtils.DEFAULT_DELIMITER)) {
			String[] words = line.split("\\s+");
			symbols.put(words[0], words.length == 2 ? words[1] : null);
		}
	}

	Grammar(BufferedReader br) throws IOException {
		terminals = new HashMap<>();
		nonTerminals = new HashMap<>();
		readSymbols(br, terminals);
		readSymbols(br, nonTerminals);

		grammar = new HashMap<>();
		for (String left : nonTerminals.keySet()) {
			grammar.put(left, new ArrayList<RightPart>());
		}

		String line;
		boolean firstNonTerminal = true;
		while (!(line = br.readLine()).equals(IOUtils.DEFAULT_DELIMITER)) {
			int arrowIdx = line.indexOf("->");
			int braceIdx = line.indexOf('{');
			String left = line.substring(0, arrowIdx).trim();
			String right = line.substring(arrowIdx + 2, braceIdx).trim();
			String action = line.substring(braceIdx).trim();
			if (action.charAt(0) != '{' && action.charAt(action.length() - 1) != '}') {
				throw new IOException();
			}
			action = action.substring(1, action.length() - 1).trim();
			
			// System.out.println(left + "|" + right + "|" + action);

			if (firstNonTerminal) {
				start = left;
				firstNonTerminal = false;
			}

			String[] rightPartArray = right.split("\\s+");
			ArrayList<Symbol> rightPartSymbols = new ArrayList<>();
			for (String symbol : rightPartArray) {
				SymbolType type;
				if (terminals.containsKey(symbol) || symbol.isEmpty()) {
					type = SymbolType.TERMINAL;
				} else if (nonTerminals.containsKey(symbol)) {
					type = SymbolType.NON_TERMINAL;
				} else {
					throw new IOException();
				}
				rightPartSymbols.add(new Symbol(symbol, type));
			}

			RightPart rightPart = new RightPart(rightPartSymbols, action);
			grammar.get(left).add(rightPart);
		}

		buildFirst();
		buildFollow();

		/*for (Entry<String, Set<String>> entry : first.entrySet()) {
			System.out.println("First(" + entry.getKey() + ") =");
			for (String s : entry.getValue()) {
				System.out.println(s);
			}
		}

		System.out.println();

		for (Entry<String, Set<String>> entry : follow.entrySet()) {
			System.out.println("Follow(" + entry.getKey() + ") =");
			for (String s : entry.getValue()) {
				System.out.println(s);
			}
		}

		System.out.println();*/
	}

}
