package comprehensive;

import java.util.HashMap;
import java.util.TreeSet;

public class Term {
	private String word;
	
	private HashMap<String, TreeSet<String>> definitions;
	
	
	public Term(String[] data) {
		this.word = data[0];
		
		definitions = new HashMap<String, TreeSet<String>>();
		
		add(data[1], data[2]);
	}
	
	public boolean add(String pos, String def) {
		if (!definitions.containsKey(pos))
			definitions.put(pos, new TreeSet<String>());
		
		return definitions.get(pos).add(def);
	}
}
