package comprehensive;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;

public class Glossary {
	private TreeMap<String, Term> glossary;
	private HashMap<String, Integer> posCounts;

	private int definitions;

	public Glossary(String filePath) {
		glossary = new TreeMap<String, Term>();
		posCounts = new HashMap<String, Integer>();
		definitions = 0;

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

	public int size() {
		return glossary.size();
	}

	public int definitions() {
		return definitions;
	}

	public void add(String word, String pos, String def) {
		boolean added = true;
		if (!glossary.containsKey(word))
			glossary.put(word, new Term(word, pos, def));
		else
			added = glossary.get(word).add(pos, def);

		if (added) {
			definitions++;
			addPos(pos);
		}
	}

	public boolean changeDef() {

		return false;
	}

	private void addPos(String pos) {
		if (!posCounts.containsKey(pos))
			posCounts.put(pos, 0);

		int count = posCounts.get(pos);
		posCounts.put(pos, count + 1);
	}

	public int getPosCount() {
		return posCounts.size();
	}

	public String getFirst() {
		return glossary.firstKey();
	}

	public String getLast() {
		return glossary.lastKey();
	}

	public Set<String> getInRange(String start, String end) {
		// TODO: Replace with keySet of entire glossary then iterate through that?
		NavigableMap<String, Term> subMap = glossary.subMap(start, true, end, true);
		return subMap.keySet();
	}

	public String[] getWord(String word) {
		Term term = glossary.get(word);

		return term == null ? new String[] { "Invalid selection." } : term.getDefinitions();
	}
	
	public String[] getPOS(String word)
	{
		Term term = glossary.get(word);

		return term == null ? new String[] { "Invalid selection." } : term.getPOS();
	}
}
