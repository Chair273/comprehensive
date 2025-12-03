package comprehensive;

import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A glossary that contains words and their definitions. Reads from a file upon instantiation, 
 * and can perform CRUD operations on its entries.
 * 
 * @author Devin Santos and Tyler Christiansen
 * @version 2025-12-3
 */
public class Glossary {
	private TreeMap<String, Term> glossary;
	private HashMap<String, Integer> posCounts;
	private int definitions;

	/**
	 * Glossary constructor, reads data from a file.
	 * 
	 * @param filePath - the file path to read from
	 */
	public Glossary(String filePath) {
		glossary = new TreeMap<String, Term>();
		posCounts = new HashMap<String, Integer>();
		definitions = 0;

		readFile(filePath);
	}

	/**
	 * Converts a file into glossary terms and definitions.
	 * 
	 * @param filePath - the file path to read from
	 */
	private void readFile(String filePath) {
		Scanner sc;

		try {
			sc = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		String[] data;

		while (sc.hasNextLine()) {
			data = sc.nextLine().split("::");

			add(data[0], data[1], data[2]);
		}

		sc.close();
	}

	/**
	 * Gets the size of the glossary.
	 * 
	 * @return - the size of the glossary
	 */
	public int size() {
		return glossary.size();
	}

	/**
	 * Gets the number of definitions contained within the glossary.
	 * 
	 * @return - the number of definitions
	 */
	public int definitions() {
		return definitions;
	}

	/**
	 * Adds a definition to a given term. If the part of speech is not present in
	 * the term, it creates it. If the term is not present in the glossary, it
	 * creates it.
	 * 
	 * @param word - the word to add a definition for
	 * @param pos  - the part of speech of the definition
	 * @param def  - the definition
	 * @return - true if the glossary was changed, otherwise false
	 */
	public boolean add(String word, String pos, String def) {
		boolean added = true;
		if (!glossary.containsKey(word))
			glossary.put(word, new Term(word, pos, def));
		else
			added = glossary.get(word).add(pos, def);

		if (added) {
			definitions++;
			addPos(pos);
		}
		return added;
	}

	/**
	 * Updates the part of speech tracker of the glossary (increases the value).
	 * 
	 * @param pos - the part of speech to increment
	 */
	private void addPos(String pos) {
		if (!posCounts.containsKey(pos))
			posCounts.put(pos, 0);

		int count = posCounts.get(pos);
		posCounts.put(pos, count + 1);
	}

	/**
	 * Gets the amount of parts of speech in the glossary.
	 * 
	 * @return - the amount of parts of speech
	 */
	public int getPosCount() {
		return posCounts.size();
	}

	/**
	 * Gets the first word lexicographically in the glossary, or an empty string if
	 * the glossary is empty.
	 * 
	 * @return - the first word, or an empty string
	 */
	public String getFirst() {
		return glossary.size() == 0 ? "" : glossary.firstKey();
	}

	/**
	 * Gets the last word lexicographically in the glossary, or an empty string if
	 * the glossary is empty.
	 * 
	 * @return - the last word, or an empty string
	 */
	public String getLast() {
		return glossary.size() == 0 ? "" : glossary.lastKey();
	}

	/**
	 * Gets all words contained within the glossary that are lexicographically
	 * larger than the starting word, and smaller than the ending word.
	 * 
	 * @param start - the starting word
	 * @param end   - the ending word
	 * @return - a set of words within the specified range, can be empty
	 */
	public Set<String> getInRange(String start, String end) {
		NavigableMap<String, Term> subMap = glossary.subMap(start, true, end, true);
		return subMap.keySet();
	}

	/**
	 * Gets the definitions of a word, or null if it is not present in the glossary.
	 * Each definition is merged with it's part of speech.
	 * 
	 * @param word - the word to search for
	 * @return - the word's definitions or null
	 */
	public String[] getMerged(String word) {
		Term term = glossary.get(word);

		return term != null ? term.getDefinitions() : null;
	}

	/**
	 * Gets the parts of speech of a given word, or null if it is not present in the
	 * glossary.
	 * 
	 * @param word - the word to search for
	 * @return - the word's parts of speech or null
	 */
	public String[] getPOS(String word) {
		Term term = glossary.get(word);

		return term != null ? term.getPOS() : null;
	}

	/**
	 * Gets the definitions of a word, or null if it is not present in the glossary.
	 * Each represents a full definition, while each row contains the part of speech
	 * and definition, in that order.
	 * 
	 * @param word - the word to search for
	 * @return - a 2d array containing the word's definitions
	 */
	public String[][] getSplit(String word) {
		Term term = glossary.get(word);
		if (term == null)
			return null;
		return term.getSplit();
	}

	/**
	 * Updates a specified definition of a given word.
	 * 
	 * @param word   - the word to update
	 * @param pos    - the part of the new definition
	 * @param oldDef - the old definition (gets removed)
	 * @param newDef - the new definition (gets added)
	 * @return - true if the definition was successfully updated
	 */
	public boolean updateDef(String word, String pos, String oldDef, String newDef) {
		Term term = glossary.get(word);

		if (term == null)
			return false;

		return term.updateDef(pos, oldDef, newDef);
	}

	/**
	 * Deletes a specified definition of a given word. Automatically removes the
	 * parts of speech and the word itself as needed.
	 * 
	 * @param word - the word to delete from
	 * @param pos  - the part of speech to delete from
	 * @param def  - the definition to delete
	 * @return - the 0th index represents whether or not the definition was deleted
	 *         successfully, while the 1st index represents whether or not the word
	 *         was removed from the glossary
	 */
	public boolean[] deleteDef(String word, String pos, String def) {
		Term term = glossary.get(word);
		boolean[] returnArr = new boolean[2];

		if (term == null)
			return returnArr;

		returnArr[0] = term.deleteDef(pos, def);

		if (returnArr[0]) {
			definitions--;
			int posAmount = posCounts.get(pos) - 1;

			if (posAmount > 0)
				posCounts.put(pos, posAmount);
			else
				posCounts.remove(pos);
		}

		if (term.getSize() == 0) {
			glossary.remove(word);
			returnArr[1] = true;
		}

		return returnArr;
	}

	/**
	 * Saves the glossary to a given file path. Must save to an existing directory.
	 * 
	 * @param filePath - the path to save to
	 * @return - true if the file was saved successfully
	 */
	public boolean saveToFile(String filePath) {
		StringBuilder sb = new StringBuilder();

		Set<String> keySet = glossary.keySet();

		for (String word : keySet) {
			Term term = glossary.get(word);

			String[][] data = term.getSplit();

			for (int i = 0; i < data.length; i++) {
				sb.append(word);
				sb.append("::");
				sb.append(data[i][0]); // POS
				sb.append("::");
				sb.append(data[i][1]); // Definition
				sb.append("\n");
			}
		}

		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(sb.substring(0, Math.max(0, sb.length() - 1)));
			writer.close();
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
