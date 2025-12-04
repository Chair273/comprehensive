package comprehensive;

import java.util.Scanner;
import java.util.Set;

/**
 * Console interface for glossary. Prints glossary data and provides interface
 * for updating and saving glossary.
 * 
 * @author Devin Santos and Tyler Christiansen
 * @version 2025-12-3
 */
public class Interface {
	Glossary glossary;

	private final String[] commands = new String[] { "Get metadata", "Get words in range", "Get word", "Get first word",
			"Get last word", "Get parts of speech", "Update definition", "Delete definition", "Add new definition",
			"Save dictionary", "Quit" };

	/**
	 * Constructor for Interface. Initializes instance of Glossary.
	 * 
	 * @param filePath - file
	 */
	public Interface(String filePath) {
		glossary = new Glossary(filePath);
	}

	/**
	 * Displays the main menu and calls the appropriate method based on the user
	 * input.
	 */
	public void update() {
		while (true) {
			System.out.println("Main menu");
			printArray(commands);
			System.out.print("\nSelect an option: ");
			int command = getInt();

			switch (command) {
				case 1 -> getMetadata();
				case 2 -> getInRange();
				case 3 -> getWord();
				case 4 -> getFirstWord();
				case 5 -> getLastWord();
				case 6 -> getPOS();
				case 7 -> updateDef();
				case 8 -> deleteDef();
				case 9 -> addDef();
				case 10 -> saveToFile();
				case 11 -> {
					return;
				}
				default -> System.out.println("\nInvalid selection");
			}
			System.out.println();
		}
	}

	/**
	 * Gets user input.
	 * 
	 * @return string of user input
	 */
	@SuppressWarnings("resource")
	private String getInput() {
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	/**
	 * Gets integer user input.
	 * 
	 * @return integer input from user or -1 if input is invalid
	 */
	@SuppressWarnings("resource")
	private int getInt() {
		Scanner s = new Scanner(System.in);
		int command;
		try {
			command = Integer.parseInt(s.nextLine());
		} catch (NumberFormatException e) {
			command = -1;
		}
		return command;
	}

	/**
	 * Enumerates and prints the menu options to the console.
	 * 
	 * @param arr - array of menu options to print
	 */
	private void printArray(String[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append((i + 1));
			sb.append(".\t");
			sb.append(arr[i]);
			sb.append("\n");
		}
		System.out.print(sb.toString());
	}

	/**
	 * Displays the glossary's metadata, including word count, definition count,
	 * definitions per word, part of speech count, and first/last words.
	 */
	private void getMetadata() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nwords: ");
		sb.append(glossary.size());
		sb.append("\ndefinitions: ");
		sb.append(glossary.definitions());

		Double defPerWord;
		if (glossary.size() == 0)
			defPerWord = 0.0;
		else
			defPerWord = (double) glossary.definitions() / glossary.size();

		sb.append("\ndefinitions per word: ");
		sb.append(String.format("%.3f", defPerWord));
		sb.append("\nparts of speech: ");
		sb.append(glossary.getPosCount());
		sb.append("\nfirst word: ");
		sb.append(glossary.getFirst());
		sb.append("\nlast word: ");
		sb.append(glossary.getLast());

		System.out.println(sb.toString());
	}

	/**
	 * Prompts the user for a starting and ending word, then prints the words
	 * between those two (in lexicographic order) that are in the glossary.
	 */
	private void getInRange() {
		System.out.print("Starting word: ");
		String startWord = getInput();

		System.out.print("Ending word: ");
		String endWord = getInput();
		System.out.println();

		if (startWord.compareTo(endWord) > 0) {
			System.out.println("Invalid selection");
			return;
		}
		Set<String> words = glossary.getInRange(startWord, endWord);
		System.out.println("The words between " + startWord + " and " + endWord + " are: ");
		for (String word : words)
			System.out.println("\t" + word);
	}

	/**
	 * Prints each given definition on a separate line.
	 * 
	 * @param definitions - a string array of definitions
	 */
	private void displayDefs(String[] definitions) {
		StringBuilder sb = new StringBuilder();

		for (String def : definitions) {
			sb.append("\n");
			sb.append(def);
		}

		System.out.println(sb.toString());
	}

	/**
	 * Prints each given definition, numbered, on a separate line. The last option
	 * is to return to the main menu.
	 * 
	 * @param definitions - a string array of defiinitions
	 */
	private int displayNumberedDefs(String word, String[][] definitions) {
		int num = 1;

		StringBuilder sb = new StringBuilder();
		sb.append("\nDefinitions for ");
		sb.append(word);

		for (String[] pair : definitions) {
			sb.append("\n");
			sb.append(num++);
			sb.append(". ");
			sb.append(pair[0]);
			sb.append(". \t");
			sb.append(pair[1]);
		}
		sb.append("\n");
		sb.append(num);
		sb.append(". Back to main menu\n");
		System.out.print(sb.toString());
		return num;
	}

	/**
	 * Gets and displays the definitions for the given word. If the word is not
	 * found in the glossary, prints "{word} not found."
	 */
	private void getWord() {
		System.out.print("Select a word: ");
		String word = getInput();

		String[] definitions = glossary.getMerged(word);

		if (definitions == null) {
			System.out.println("\n" + word + " not found");
			return;
		}

		displayDefs(definitions);
	}

	/**
	 * Prints the first word in the glossary if it isn't empty.
	 */
	private void getFirstWord() {
		if (glossary.size() == 0) {
			System.out.println("This dictionary is empty");
			return;
		} else {
			System.out.println();
		}

		String[] definitions = glossary.getMerged(glossary.getFirst());

		displayDefs(definitions);
	}

	/**
	 * Prints the last word in the glossary if it isn't empty.
	 */
	private void getLastWord() {
		if (glossary.size() == 0) {
			System.out.println("This dictionary is empty");
			return;
		} else {
			System.out.println();
		}

		String[] definitions = glossary.getMerged(glossary.getLast());

		displayDefs(definitions);
	}

	/**
	 * Prompts the user for a word and displays the parts of speech associated with
	 * that word. If the word isn't in the glossary, prints "{word} not found."
	 */
	private void getPOS() {
		System.out.print("Select a word: ");
		String word = getInput();

		String[] pos = glossary.getPOS(word);

		if (pos != null) {
			displayDefs(pos);
		} else {
			System.out.println("\n" + word + " not found\n");
		}
	}

	/**
	 * Prompts the user to select a word and displays its definitions, numbered, or
	 * "Invalid selection" if the word is not in the glossary. Prompts the user to
	 * select a definition to update and to type the new definition. Attempts to
	 * update the definition and outputs whether the attempt was successful.
	 */
	private void updateDef() {
		String[][] definitions = new String[0][0];
		String word = "";

		System.out.print("Select a word: ");
		word = getInput();

		definitions = glossary.getSplit(word);

		if (definitions == null) {
			System.out.println("Invalid selection\n");
			return;
		}

		int command = 0;
		boolean validCommand = false;
		while (!validCommand) {
			int num = displayNumberedDefs(word, definitions);
			System.out.print("\nSelect a definition to update: ");
			command = getInt();

			if (command == num)
				return;

			if (command < 1 || command > num)
				System.out.println("Invalid selection\n");
			else
				validCommand = true;
		}

		System.out.print("Type a new definition: ");
		String newDef = getInput();

		if (glossary.updateDef(word, definitions[command - 1][0], definitions[command - 1][1], newDef))
			System.out.println("\nDefinition updated");
		else
			System.out.println("\nDefinition not updated");

	}

	/**
	 * Prompts the user to select a word and displays its definitions, numbered, or
	 * "Invalid selection" if the word is not in the glossary. Prompts the user to
	 * select a definition to delete. Attempts to remove the definition and outputs
	 * whether the definition was removed and if the term itself was removed.
	 */
	private void deleteDef() {
		String[][] definitions = new String[0][0];
		String word = "";

		System.out.print("Select a word: ");
		word = getInput();

		definitions = glossary.getSplit(word);

		if (definitions == null) {
			System.out.println("Invalid selection\n");
			return;
		}

		int num = displayNumberedDefs(word, definitions);

		int command = 0;
		boolean validCommand = false;
		while (!validCommand) {
			System.out.print("\nSelect a definition to remove: ");
			command = getInt();

			if (command == num)
				return;

			if (command < 1 || command > num)
				System.out.println("Invalid selection");
			else
				validCommand = true;
		}

		boolean[] deleted = glossary.deleteDef(word, definitions[command - 1][0], definitions[command - 1][1]);

		if (deleted[0])
			System.out.println("\nDefinition removed");
		else
			System.out.println("\nDefinition not removed");

		if (deleted[1])
			System.out.println(word + " removed");
	}

	/**
	 * Prompts the user for a word, part of speech, and definition. If the part of
	 * speech is valid and if the entry is not already in the glossary, this method
	 * adds the data as a new entry.
	 */
	private void addDef() {
		System.out.print("Type a word: ");
		String word = getInput();

		System.out.println("Valid parts of speech: [noun, verb, adj, adv, pron, prep, conj, interj]");

		boolean isValidPOS = false;
		String pos = "";

		while (!isValidPOS) {
			System.out.print("Type a valid part of speech: ");
			pos = getInput();

			for (String validPOS : Term.posOrder)
				if (validPOS.equals(pos)) {
					isValidPOS = true;
					break;
				}
		}

		System.out.print("Type a definition: ");

		if (glossary.add(word, pos, getInput()))
			System.out.println("\nSuccessfully added!");
		else
			System.out.println("\nThis definition was already added.");
	}

	/**
	 * Prompts the user for a file path. If the given directory is valid, saves the
	 * glossary to that file.
	 */
	private void saveToFile() {
		System.out.print("Type a filename with path: ");
		String filePath = getInput();
		System.out.println();

		if (glossary.saveToFile(filePath))
			System.out.println("Successfully saved dictionary to " + filePath);
		else
			System.out.println("File not found");
	}
}
