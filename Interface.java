package comprehensive;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Interface {
	Glossary glossary;
	Scanner scanner;

	private final String[] commands = new String[] { "Get metadata", "Get words in range", "Get word", "Get first word",
			"Get last word", "Get parts of speech", "Update definition", "Delete definition", "Add new definition",
			"Save dictionary", "Quit" };

	public Interface(String filePath) {
		glossary = new Glossary(filePath);
		scanner = new Scanner(System.in);
		update();
	}

	private void update() {
		while (true) {
			System.out.println("Main menu");
			printArray(commands);
			System.out.print("\nSelect an option: ");
			int command = getInt();
			System.out.println();

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

			default -> System.out.println("Invalid selection");
			}
			System.out.println();
		}

	}

	/**
	 * Get user input.
	 * 
	 * @return
	 */
	private String getInput() {
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	private int getInt() {
		Scanner s = new Scanner(System.in);
		int command = -1;
		try {
			command = scanner.nextInt();
			scanner.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Invalid selection: must be an integer");
			scanner.nextLine();
		}
		return command;
	}

	private void printArray(String[] arr) {
		for (int i = 0; i < arr.length; i++)
			System.out.println((i + 1) + ".\t" + arr[i]);
	}

	private void getMetadata() {
		System.out.println("words: " + glossary.size());
		System.out.println("definitions: " + glossary.definitions());
		Double defPerWord = (double) glossary.definitions() / glossary.size();
		System.out.println("definitions per word: " + String.format(defPerWord + "", "%.3f"));
		System.out.println("parts of speech: " + glossary.getPosCount());
		System.out.println("first word: " + glossary.getFirst());
		System.out.println("last word: " + glossary.getLast());
	}

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
		for (String word : words) {
			System.out.println("\t" + word);
		}
	}

	// Condense the next three methods?

	private void getWord() {
		System.out.print("Select a word: ");
		String word = getInput();

		System.out.println();

		String[] definitions = glossary.getWord(word);

		for (String def : definitions)
			System.out.println(def);
	}

	private void getFirstWord() {
		String[] definitions = glossary.getWord(glossary.getFirst());

		for (String def : definitions)
			System.out.println(def);
	}

	private void getLastWord() {
		String[] definitions = glossary.getWord(glossary.getLast());

		for (String def : definitions)
			System.out.println(def);
	}

	private void getPOS() {
		System.out.print("Select a word: ");
		String word = getInput();

		System.out.println();

		String[] definitions = glossary.getPOS(word);

		for (String def : definitions)
			System.out.println(def);
	}

	private void updateDef() {
		System.out.print("Select a word: ");
		String word = getInput();

		System.out.println();

		String[][] definitions = glossary.getSplit(word);// handle invalid word later

		if (definitions == null) {
			System.out.println("Invalid selection");
			return;
		}

		int num = 1;

		System.out.println("Definitions for " + word);

		for (String[] pair : definitions)
			System.out.println((num++) + ". " + pair[0] + ".\t" + pair[1]);

		System.out.print(num + ". Back to main menu\n\nSelect a definition to update: ");

		int command = getInt();

		if (command == num)
			return;

		if (command < 1 || command > num) {
			System.out.println("Invalid selection");
			return;
		}

		System.out.print("\nType a new definition: ");

		String newDef = getInput();

		System.out.println();

		if (glossary.updateDef(word, definitions[command - 1][0], definitions[command - 1][1], newDef))
			System.out.println("Definition updated");
		else
			System.out.println("Definition not updated");

	}

	private void deleteDef() {
		System.out.print("Select a word: ");
		String word = getInput();

		System.out.println();

		String[][] definitions = glossary.getSplit(word);

		if (definitions == null) {
			System.out.println("Invalid selection");
			return;
		}

		int num = 1;

		System.out.println("Definitions for " + word);

		for (String[] pair : definitions)
			System.out.println((num++) + ". " + pair[0] + " .\t" + pair[1]);

		System.out.print(num + ". Back to main menu\n\nSelect a definition to remove: ");

		int command = getInt();

		if (command == num)
			return;

		if (command < 1 || command > num) {
			System.out.println("\nInvalid selection");
			return;
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

		if (!glossary.containsWord(word)) {
			System.out.println("Invalid selection");
			return;
		}

		System.out.print(
				"\nValid parts of speech: [noun, verb, adj, adv, pron, prep, conj, interj]\nType a valid part of speech: ");
		String pos = getInput();

		System.out.println();

		boolean contains = false;

		for (String validPos : Term.posOrder)
			if (validPos.equals(pos)) {
				contains = true;
				break;
			}

		if (!contains) {
			System.out.println("Invalid selection");
			return;
		}

		System.out.print("Type a definition: ");

		if (glossary.add(word, pos, getInput()))
			System.out.println("\n\nSuccessfully added!");
		else
			System.out.println("\n\nThis definition was already added.");
	}
	
	private void saveToFile()
	{
		System.out.print("Type a filename with path: ");
		String filePath = getInput();
		System.out.println();
		
		if (glossary.saveToFile(filePath))
			System.out.println("Successfully saved dictionary to " + filePath);
		else
			System.out.println("Failed to save dictionary");
		
		
	}
}
