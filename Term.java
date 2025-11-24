package comprehensive;

import java.util.HashMap;
import java.util.TreeSet;

public class Term {
	private String word;
	
	private HashMap<String, TreeSet<String>> definitions;
	
	
	public Term(String word, String pos, String def) {
		this.word = word;
		
		definitions = new HashMap<String, TreeSet<String>>();
		
		add(pos, def);
	}
	
	public boolean add(String pos, String def) {
		if (!definitions.containsKey(pos)) {
			definitions.put(pos, new TreeSet<String>());
			//System.out.println("Term: " + word + "; Part of Speech: " + pos + "; Definition: " + def);
		}
		return definitions.get(pos).add(def);
	}
	
}
