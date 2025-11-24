package comprehensive;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Interface {
	Glossary glossary;

	private final String[] commands = new String[] { "Get metadata", "Get words in range", "Get word", "Get first word",
			"Get last word", "Get parts of speech", "Update definition", "Delete definition", "Add new definition",
			"Save dictionary", "Quit" };

	public Interface(String filePath) {
		glossary = new Glossary(filePath);

	}

	private void update() {
		while (true) {
			System.out.println("Main menu");
			printArray(commands);
			System.out.println("\nSelect an option: ");

			Scanner scanner = new Scanner(System.in);
			int command;

			try {
				command = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid selection");
				scanner.close();

				continue;
			}

			scanner.close();
			System.out.println();

			switch (command) {
				case 1: getMetadata();
					
				case 2:
	
				case 3:
	
				case 4:
	
				case 5:
	
				case 6:
	
				case 7:
	
				case 8:
	
				case 9:
	
				case 10:
	
				case 11:

				default:
					System.out.println("Invalid selection");
			}
		}
	}

	private void printArray(String[] arr) {
		for (int i = 0; i < arr.length; i++)
			System.out.println((i + 1) + ".\t" + arr[i]);
	}
	
	private void getMetadata() {
		System.out.println("Words: " + glossary.size());
		System.out.println("Definitions: " + glossary.definitions());
		Double defPerWord = (double) glossary.definitions() / glossary.size();
		System.out.println("Definitions per Word: " + String.format(defPerWord + "", "%.3f"));
		System.out.println("Parts of Speech: " + String.format(defPerWord + "", "%.3f"));
	}
}
