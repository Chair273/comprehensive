package comprehensive;

import java.util.Scanner;
import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;

public class Glossary {
	private TreeMap<String, Term> glossary;

	public Glossary(String filePath) {
		glossary = new TreeMap<String, Term>();

		readFile(filePath);
	}

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

	public void add(String word, String pos, String def) {
		if (!glossary.containsKey(word))
			glossary.put(word, new Term(word, pos, def));
		else
			glossary.get(word).add(pos, def);
	}

	public boolean changeDef() {
		
		return false;
	}
}
