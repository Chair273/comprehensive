package comprehensive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Term {
	private int size;

	private String word;

	private HashMap<String, TreeSet<String>> definitions;

	private static final String[] posOrder = new String[] { "adj", "adv", "conj", "interj", "noun", "prep", "pron",
			"verb" };

	public Term(String word, String pos, String def) {
		this.word = word;

		size = 0;

		definitions = new HashMap<String, TreeSet<String>>();

		add(pos, def);
	}

	public boolean add(String pos, String def) {
		if (!definitions.containsKey(pos))
			definitions.put(pos, new TreeSet<String>());

		boolean added = definitions.get(pos).add(def);
		if (added)
			size++;

		return added;
	}

	public String[] getDefinitions() { // change to getMerged or getStrings or getOutput
		int i = 1;
		String[] returnArr = new String[size + 1];
		TreeSet<String> posDef;

		returnArr[0] = word;

		for (String pos : posOrder) {
			if (!definitions.containsKey(pos))
				continue;

			posDef = definitions.get(pos);

			for (String def : posDef)
				returnArr[i++] = "\t" + pos + ".\t" + def;
		}

		return returnArr;
	}
	
	public String[][] getSplit() {
		int i = 0;
		String[][] returnArr = new String[size][2];
		TreeSet<String> posDef;

		for (String pos : posOrder) {
			if (!definitions.containsKey(pos))
				continue;

			posDef = definitions.get(pos);

			for (String def : posDef) {
				returnArr[i][0] = pos;
				returnArr[i++][1] = def;
			}
		}

		return returnArr;
	}

	public String[] getPOS() {
		int i = 1;
		String[] returnArr = new String[definitions.size() + 1];

		returnArr[0] = word;

		for (String pos : posOrder)
			if (definitions.containsKey(pos))
				returnArr[i++] = "\t" + pos;

		return returnArr;
	}
	
	public boolean updateDef(String pos, String oldDef, String newDef) {
		TreeSet<String> target = definitions.get(pos);
		if (!target.remove(oldDef)) {
			System.out.println("Old definition could not be removed.");
			return false;
		}
		return target.add(newDef);
	}

}
