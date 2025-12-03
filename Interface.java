package comprehensive;

// import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

/**
 * Console interface for glossary. Prints glossary data and provides interface for
 * updating and saving glossary.
 * 
 * @author Devin Santos and Tyler Christiansen
 * @version 2025-12-3
 */
public class Interface {
	Glossary glossary;
	// Scanner scanner;

	private final String[] commands = new String[] { "Get metadata", "Get words in range", "Get word", "Get first word",
			"Get last word", "Get parts of speech", "Update definition", "Delete definition", "Add new definition",
			"Save dictionary", "Quit" };

	/**
	 * Constructor for Interface. Initializes Glossary and Scanner.
	 * @param filePath - file
	 */
	public Interface(String filePath) {
		glossary = new Glossary(filePath);
		// scanner = new Scanner(System.in);
		// update();
	}

	public void update() {
		while (true) {
			System.out.println("Main menu");
			printArray(commands);
			System.out.print("\nSelect an option: ");
			int command = getInt();
			// System.out.println();

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
	 * Get user input.
	 * 
	 * @return string of user input
	 */
	@SuppressWarnings("resource")
	private String getInput() {
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	/**
	 * Get user integer input.
	 * 
	 * @return integer input from user or -1 if input is invalid
	 */
	@SuppressWarnings("resource")
	private int getInt() {
		Scanner s = new Scanner(System.in);
		int command;
		try {
			// scanner.nextInt(); 
			command = Integer.parseInt(s.nextLine());
		} catch (NumberFormatException e) { // InputMismatchException
			command = -1;
		}
		return command;
	}

	private void printArray(String[] arr) {
		for (int i = 0; i < arr.length; i++)
			System.out.println((i + 1) + ".\t" + arr[i]);
	}

	/**
	 * Display the glossary's metadata, including word count, definition count,
	 * definitions per word
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
	 * 
	 */
	private void getInRange() {
		System.out.print("Starting word: ");
		String startWord = getInput();

		System.out.print("Ending word: ");
		String endWord = getInput();
		System.out.println();

		if (startWord.compareTo(endWord) > 0) { // TODO: options - say "Invalid selection" and return, say "Invalid
												// selection" and prompt user for new word(s)
			System.out.println("Invalid selection");
			return;
		}
		Set<String> words = glossary.getInRange(startWord, endWord);
		System.out.println("The words between " + startWord + " and " + endWord + " are: ");
		for (String word : words) {
			System.out.println("\t" + word);
		}
	}

	// Condense the next three methods?

	/**
	 * Get and display the definitions for the given word. If the word is not found
	 * in the glossary, print "[word] not found."
	 */
	private void getWord() {
		System.out.print("Select a word: ");
		String word = getInput();

		String[] definitions = glossary.getWord(word);

		if (definitions != null) {

			StringBuilder sb = new StringBuilder();

			for (String def : definitions) {
				sb.append("\n");
				sb.append(def);
			}

			System.out.println(sb.toString());
		} else {
			System.out.println("\n" + word + " not found");
		}

	}

	private void getFirstWord() {
		System.out.println();
		String[] definitions = glossary.getWord(glossary.getFirst());

		if (definitions == null) {
			System.out.println("This dictionary is empty");
			return;
		}
		
		for (String def : definitions)
			System.out.println(def);
	}

	private void getLastWord() {
		System.out.println();
		String[] definitions = glossary.getWord(glossary.getLast());
		
		if (definitions == null) {
			System.out.println("\nThis dictionary is empty");
			return;
		}

		for (String def : definitions)
			System.out.println(def);
	}

	private void getPOS() {
		if (glossary.size() == 0) {
			System.out.println("This dictionary is empty");
			return;
		}
			
		boolean valid = false;

		while (!valid) {
			System.out.print("Select a word: ");
			String word = getInput();

			String[] definitions = glossary.getPOS(word);

			if (definitions != null) {
				valid = true;

				StringBuilder sb = new StringBuilder();

				for (String def : definitions) {
					sb.append("\n");
					sb.append(def);
				}

				System.out.println(sb.toString());
			} else {
				System.out.println("\n" + word + " not found\n");
			}

		}

	}

	private void updateDef() {
		if (glossary.size() == 0) {
			System.out.println("\nThis dictionary is empty");
			return;
		}
		boolean validWord = false;
		String[][] definitions = new String[0][0];
		String word = "";

		while (!validWord) {
			System.out.print("Select a word: ");
			word = getInput();

			definitions = glossary.getSplit(word);

			if (definitions == null) {
				System.out.println("Invalid selection\n");
			} else {
				validWord = true;
			}
		}

		int num = 1;

		System.out.println("\nDefinitions for " + word);

		for (String[] pair : definitions)
			System.out.println((num++) + ". " + pair[0] + ". \t" + pair[1]);
		System.out.print(num + ". Back to main menu\n");

		int command = 0;
		boolean validCommand = false;
		while (!validCommand) {
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

		System.out.println();

		if (glossary.updateDef(word, definitions[command - 1][0], definitions[command - 1][1], newDef))
			System.out.println("Definition updated");
		else
			System.out.println("Definition not updated");

	}

	private void deleteDef() {
		if (glossary.size() == 0) {
			System.out.println("\nThis dictionary is empty");
			return;
		}
		boolean validWord = false;
		String[][] definitions = new String[0][0];
		String word = "";

		while (!validWord) {
			System.out.print("Select a word: ");
			word = getInput();

			definitions = glossary.getSplit(word);

			if (definitions == null) {
				System.out.println("Invalid selection\n");
			} else {
				validWord = true;
			}
		}

		int num = 1;

		System.out.println("\nDefinitions for " + word);

		for (String[] pair : definitions)
			System.out.println((num++) + ". " + pair[0] + ". \t" + pair[1]);

		System.out.print(num + ". Back to main menu\n");

		int command = 0;
		boolean validCommand = false;
		while (!validCommand) { // do-while loop so above variables aren't initialized
			System.out.print("\nSelect a definition to remove: ");
			command = getInt();

			if (command == num)
				return;

			if (command < 1 || command > num)
				System.out.println("Invalid selection");
			else
				validCommand = true;
		}

		System.out.println();

		boolean[] deleted = glossary.deleteDef(word, definitions[command - 1][0], definitions[command - 1][1]);

		if (deleted[0])
			System.out.println("Definition removed");
		else
			System.out.println("Definition not removed");

		if (deleted[1])
			System.out.println(word + " removed");
	}

	private void addDef() {
		System.out.print("Type a word: ");
		String word = getInput();

		/*
		 * if (!glossary.containsWord(word)) { System.out.println("Invalid selection");
		 * return; }
		 */

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
