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
import java.io.FileWriter;
import java.io.IOException;

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

		return term == null ? new String[] { word + " not found" } : term.getDefinitions();
	}

	public String[] getPOS(String word) {
		Term term = glossary.get(word);

		return term == null ? new String[] { "Invalid selection." } : term.getPOS();
	}

	public String[][] getSplit(String word) {
		Term term = glossary.get(word);
		if (term == null)
			return null;
		return term.getSplit();
	}

	public boolean updateDef(String word, String pos, String oldDef, String newDef) {
		Term term = glossary.get(word);
		return term.updateDef(pos, oldDef, newDef);
	}

	public boolean[] deleteDef(String word, String pos, String def) {
		Term term = glossary.get(word);
		boolean[] returnArr = new boolean[2];

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

	/*public boolean addDef(String word, String pos, String def) {
		return glossary.get(word).add(pos, def);
	}*/

	public boolean containsWord(String word) {
		return glossary.get(word) != null;
	}
	
	public boolean saveToFile(String filePath)
	{
		
		StringBuilder sb = new StringBuilder();
		
		Set<String> keySet = glossary.keySet();
		
		for (String word : keySet)
		{
			Term term = glossary.get(word);
			
			String[][] data = term.getSplit();
			
			for (int i = 0; i < data.length; i++)
			{		
				sb.append(word);
				sb.append("::");
				sb.append(data[i][0]); //POS
				sb.append("::");
				sb.append(data[i][1]); //Definition
				
				sb.append("\n");
			}
		}
		
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(sb.substring(0, sb.length() - 1));
			writer.close();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
}
