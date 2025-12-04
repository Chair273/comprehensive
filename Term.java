package comprehensive;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Stores the definitions for a word in the glossary.
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
	 * Constructor for Term: every term must have at least one definition, which is associated
	 * with a part of speech.
	 * 
	 * @param word	- the word this term is associated with
	 * @param pos	- the first part of speech to add
	 * @param def	- the first definition to add
	 */
	public Term(String word, String pos, String def) {
		this.word = word;
		size = 0;
		definitions = new HashMap<String, TreeSet<String>>();
		add(pos, def);
	}

	/**
	 * Adds a definition to the tree for its corresponding part of speech, creating a new
	 * tree if needed (i.e. when this part of speech doesn't yet exist for this Term).
	 * 
	 * @param pos	- the part of speech associated with the definition
	 * @param def	- the definition to add
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
	 * Gets the Term's definitions and their parts of speech as an array of Strings. The 
	 * entry at index 0 is the word itself. Each subsequent entry is an indented line with 
	 * the part of speech and definition.
	 * 
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
	 * Gets the definitions of the Term as a 2D array of Strings. Each row represents a 
	 * single entry for this Term. The first column contains the part of speech and 
	 * the second column contains the definition.
	 * 
	 * @return - a 2D array of split definitions of the Term, with parts of speech in one
	 * column and definitions in the other
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
	 * Returns an array containing the parts of speech used by this Term's definitions in 
	 * lexicographic order.
	 * 
	 * @return - the parts of speech used by this Term's definitions
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
	 * 
	 * @param pos		- the part of speech associated with the definition to update
	 * @param oldDef		- the definition to update
	 * @param newDef		- the updated definition
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
	 * Deletes a given definition. Removes the associated part of speech's backing TreeSet
	 * if needed.
	 * 
	 * @param pos	- the part of speech associated with the definition
	 * @param def	- the definition to delete
	 * @return - true if the definition was successfully removed
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
	 * 
	 * @return - the number of definitions associated with this Term
	 */
	public int getSize() {
		return size;
	}

}
