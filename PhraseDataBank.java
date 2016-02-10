import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
public class PhraseDataBank {
	private static int placeInList;
	private ArrayList <String> phraseAndClueArray = new ArrayList <String> ();
	public PhraseDataBank () {
		phraseAndClueArray = getArray();
		placeInList = getPlaceInList();
	}
	private static int getPlaceInList(){
		try {
			File inputFile = new File ("./src/counter");
			Scanner sc = new Scanner (inputFile);
			placeInList = sc.nextInt();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return placeInList;
	}
	private static ArrayList <String> getArray() {
		ArrayList <String> list = new ArrayList <String> ();
		try {
			File inputFile = new File ("./src/gamePhrases");
			Scanner sc = new Scanner (inputFile);
			while (sc.hasNextLine()) {
				int counter = 0;
				list.add(counter, sc.nextLine());
				counter++;
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		return list;
	}
	public int returnPlaceInList(){
		return placeInList;
	}
	public ArrayList<String> returnPhraseAndClueArray () {
		return phraseAndClueArray;
	}
	public void repopTextFile (){
		FileWriter outFile;
		if (placeInList == phraseAndClueArray.size()-1)
			placeInList = 0;
		else
			placeInList++;
		try {
			outFile = new FileWriter("./src/counter");
			PrintWriter out = new PrintWriter(outFile);
			out.println(placeInList);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
