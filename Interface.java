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

			int command;

			try {
				command = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid selection");
				continue;
			}

			System.out.println();

			switch (command) {
				case 1 -> getMetadata();
				case 2 -> getInRange();
				case 3 -> getWord();
				case 4 -> System.out.println("Uh oh");
				case 5 -> System.out.println("Uh oh");
				case 6 -> System.out.println("Uh oh");
				case 7 -> System.out.println("Uh oh");
				case 8 -> System.out.println("Uh oh");
				case 9 -> System.out.println("Uh oh");
				case 10 -> System.out.println("Uh oh");
				case 11 -> {
					scanner.close();
					return;
				}
				default -> System.out.println("Invalid selection");
			}
			System.out.println();
		}

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
		String startWord = scanner.next();
		
		System.out.print("Ending word: ");
		String endWord = scanner.next();
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
	
	private void getWord()
	{
		System.out.print("Select a word: ");
		String word = scanner.next();
		
		System.out.println();
		
		String[] definitions = glossary.getWord(word);
		
		for (String def : definitions)
			System.out.println(def);
	}
}
