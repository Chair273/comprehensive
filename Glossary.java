package comprehensive;

import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A glossary that contains words and Term objects, which store the terms'
 * definitions. Reads from a file upon instantiation and provides endpoints to
 * retrieve data about the glossary and terms, add/update/delete definitions,
 * and save to an external file.
 * 
 * @author Devin Santos and Tyler Christiansen
 * @version 2025-12-3
 */
public class Glossary {
	private TreeMap<String, Term> glossary;
	private HashMap<String, Integer> posCounts;
	private int definitions;

	/**
	 * Creates a new Glossary by reading data from a file into the appropriate data
	 * structues.
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
		// Documentation:
		// https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
		Pattern pattern = Pattern.compile("::|\n");

		try {
			sc = new Scanner(new File(filePath));
			// Documentation:
			// https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html#delimiter--
			sc.useDelimiter(pattern);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		while (sc.hasNext())
			add(sc.next(), sc.next(), sc.next());

		sc.close();
	}

	/**
	 * Gets the number of Terms in the glossary.
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
	 * Adds a definition to a given term. A new Term is added to the glossary if
	 * this word hasn't yet been added. Updates the count of the corresponding part
	 * of speech.
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
	 * Increments the count of the given part of speech.
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
	 * Gets the number of parts of speech in the glossary.
	 * 
	 * @return - the amount of parts of speech
	 */
	public int getPosCount() {
		return posCounts.size();
	}

	/**
	 * Gets the first word in the glossary when ordered lexicographically, or an
	 * empty string if the glossary is empty.
	 * 
	 * @return - the first word or an empty string
	 */
	public String getFirst() {
		return glossary.size() == 0 ? "" : glossary.firstKey();
	}

	/**
	 * Gets the last word in the glossary when ordered lexicographically, or an
	 * empty string if the glossary is empty.
	 * 
	 * @return - the last word or an empty string
	 */
	public String getLast() {
		return glossary.size() == 0 ? "" : glossary.lastKey();
	}

	/**
	 * Gets all words contained within the glossary that are larger than the
	 * starting word and smaller than the ending word when ordered
	 * lexicographically.
	 * 
	 * @param start - the starting word
	 * @param end   - the ending word
	 * @return - a Set of words within the specified range; can be empty
	 */
	public Set<String> getInRange(String start, String end) {
		NavigableMap<String, Term> subMap = glossary.subMap(start, true, end, true);
		return subMap.keySet();
	}

	/**
	 * Gets the formatted dictionary entries of a word, or null if the word is not
	 * present in the glossary. Each definition is merged with its part of speech.
	 * 
	 * @param word - the word to search for
	 * @return - the word's definitions or null
	 */
	public String[] getMerged(String word) {
		Term term = glossary.get(word);

		return term != null ? term.getMerged() : null;
	}

	/**
	 * Gets the parts of speech of a given word's definitions, or null if the word
	 * is not present in the glossary.
	 * 
	 * @param word - the word to search for
	 * @return - the word's parts of speech or null
	 */
	public String[] getPOS(String word) {
		Term term = glossary.get(word);

		return term != null ? term.getPOS() : null;
	}

	/**
	 * Gets the definitions of a word, or null if the word is not present in the
	 * glossary. Each row represents a full entry, while the columns contain the
	 * part of speech and definition, in that order.
	 * 
	 * @param word - the word to search for
	 * @return - a 2D array containing the word's definitions
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
	 * @param pos    - the part of speech of the new definition
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
	 * Deletes a specified definition of a given word. Removes the word itself if the last
	 * definition is removed. Adjusts the appropriate part of speech count.
	 * 
	 * @param word - the word corresponding with the definition to delete
	 * @param pos  - the part of speech corresponding with the definition to delete
	 * @param def  - the definition to delete
	 * @return - a boolean array: the value at index 0 represents whether or not the
	 *         definition was deleted successfully, while the value at index 1
	 *         represents whether or not the word was removed from the glossary
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
	 * Saves the glossary to a given file path. Must save to a file in an existing directory.
	 * 
	 * @param filePath - the path of the file to save to
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
