package comprehensive;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Stores the definitions for each word in the glossary.
 * 
 * @author Devin Santos and Tyler Christiansen
 * @version 2025-12-3
 */
public class Term {
	private int size;

	private String word;

	private HashMap<String, TreeSet<String>> definitions;

	public static final String[] posOrder = new String[] { "adj", "adv", "conj", "interj", "noun", "prep", "pron",
			"verb" };

	
	/**
	 * Constructor for Term, every term must have at least one definition, which in turn must be associated
	 * with a part of speech.
	 * @param word - the word this term is associated with
	 * @param pos - the first part of speech to add
	 * @param def - the first definition to add
	 */
	public Term(String word, String pos, String def) {
		this.word = word;

		size = 0;

		definitions = new HashMap<String, TreeSet<String>>();

		add(pos, def);
	}

	/**
	 * Adds a definition to the Term, and if necessary its corresponding part of speech.
	 * @param pos - the part of speech associated with the definition
	 * @param def - the definition to add
	 * @return - true if the definition was successfully added
	 */
	public boolean add(String pos, String def) {
		if (!definitions.containsKey(pos))
			definitions.put(pos, new TreeSet<String>());

		boolean added = definitions.get(pos).add(def);
		if (added)
			size++;

		return added;
	}

	/** 
	 * Gets the definitions of the Term as an array of Strings. Each definition is merged
	 * with its part of speech. The 0th index is the word itself.
	 * @return - the definitions of the Term
	 */
	public String[] getMerged() {
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

	/** 
	 * Gets the definitions of the Term as a 2d array of Strings. Each row represents a 
	 * full definition, while each column contains the part of speech and definition, in that order.
	 * @return - the split definitions of the Term
	 */
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

	/**
	 * Returns an array containing the types of parts of speech used by this term in order.
	 * @return - the parts of speech used by this term
	 */
	public String[] getPOS() {
		int i = 1;
		String[] returnArr = new String[definitions.size() + 1];

		returnArr[0] = word;

		for (String pos : posOrder)
			if (definitions.containsKey(pos))
				returnArr[i++] = "\t" + pos;

		return returnArr;
	}

	/**
	 * Replaces a given definition.
	 * @param pos - the part of speech associated with the definition
	 * @param oldDef - the definition to be removed
	 * @param newDef - the definition to add
	 * @return - true if the old definition was successfully removed and the new definition 
	 * was successfully added
	 */
	public boolean updateDef(String pos, String oldDef, String newDef) {
		TreeSet<String> target = definitions.get(pos);
		if (!target.remove(oldDef)) {
			System.out.println("Old definition could not be removed.");
			return false;
		}

		return target.add(newDef);
	}

	/**
	 * Deletes a given definition. Removes the associated part of speech as necessary.
	 * @param pos - the part of speech associated with the definition
	 * @param def - the definition to delete
	 * @return true if the definition was successfully removed.
	 */
	public boolean deleteDef(String pos, String def) {

		TreeSet<String> target = definitions.get(pos);

		if (!target.remove(def))
			return false;

		size--;

		if (target.size() == 0)
			definitions.remove(pos);

		return true;
	}

	/**
	 * Gets the amount of definitions stored in this Term.
	 * @return - the amount of definitions stored in this Term
	 */
	public int getSize() {
		return size;
	}

}
