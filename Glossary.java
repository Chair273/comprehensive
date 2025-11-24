package comprehensive;

import java.util.Scanner;
import java.util.TreeSet;
import java.io.File;
import java.io.FileNotFoundException;

public class Glossary {
	private TreeSet<Term> glossary;
	
	public Glossary(String filePath)
	{
		glossary = new TreeSet<Term>();
		
		readFile(filePath);
	}
	
	
	private void readFile(String filePath)
	{
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
			
			addTerm(data);
		}

		sc.close();
	}
	
	public void addTerm(String[] data)
	{
		glossary.add(new Term(data));
	}
	
	public boolean changeDef()
	{
		return false;
	}
}
