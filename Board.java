import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Scanner;
public class Board {
	private String secretPhrase;
	private String phraseOnBoard;
	private String clue;
	private String phraseAndClue;
	private static int listSpot;
	private ArrayList <Character> consonantsRemaining = new ArrayList <Character> () {{ 
		add('B');
		add('C');
		add('D');
		add('F');
		add('G');
		add('H');
		add('J');
		add('K');
		add('L');
		add('M');
		add('N');
		add('P');
		add('Q');
		add('R');
		add('S');
		add('T');
		add('V');
		add('W');
		add('X');
		add('Y');
		add('Z');
	}};
	private ArrayList <Character> vowelsRemaining = new ArrayList <Character> () {{
		add('A');
		add('E');
		add('I');
		add('O');
		add('U');
	}};
	private static Scanner scan = new Scanner (System.in);
	private static ArrayList <String> phrasesAndClues = new ArrayList<String> ();
	private PhraseDataBank phraseDB = new PhraseDataBank();
	public Board () {
		phrasesAndClues = phraseDB.returnPhraseAndClueArray();
		listSpot = phraseDB.returnPlaceInList();
		phraseAndClue = getPhraseAndClue();
		StringTokenizer tokenizer = new StringTokenizer (phraseAndClue, ".");//delimiter is period
		secretPhrase = tokenizer.nextToken();
		clue = tokenizer.nextToken();
		phraseOnBoard = createCodedPhrase(secretPhrase);
	}
	public void repopTextFile () {
		phraseDB.repopTextFile();
	}
	public void displayPhraseAndClue () {
		//pre:nothing
		//post:nothing
		displayPhrase();
		displayClue();
	}
	public void displayClue () {
		//pre: receives nothing
		//post: prints out the clue
		System.out.println();
		System.out.println("Clue: ");
		System.out.println(clue);
		System.out.println();
	}
	public void displayPhrase () {
		//pre: receives nothing
		//post: prints out the phraseOnBoard with appropriate dashed marks
		System.out.println();
		System.out.println("Phrase: ");
		System.out.println(phraseOnBoard);
	}
	public String returnSecret () {
		return secretPhrase;
	}
	public String returnPhrase () {
		return phraseOnBoard;
	}
	public static String getPhraseAndClue () {
		String phraseAndClue = phrasesAndClues.get(listSpot);
		return phraseAndClue;
	}
	public int guessLetter ()/*BIG*/ {
		//pre: receives player's guessed letter
		//post: checks if the letter is in the phrase, adds to phrase on board, deletes letter from list of consonants
		String letterString = scan.next();
		char letter = letterString.charAt(0);
		letter = Character.toLowerCase(letter);
		String check = checkIfLetterIsInRemainingLists(letter);
		if (check.equals("consonant") || check.equals("vowel")) {
			int correctLetters = findIndexOfLetter(letter);
			if (correctLetters > 0) {
				System.out.println();
				System.out.println("You guessed correctly!");
				displayPhraseAndClue();
				removeLetterFromList(letter, check);
				return correctLetters;
			} else {
				System.out.println();
				System.out.println("You guessed incorrectly!");
				return 0;
			}
		}
		else if (check.equals("invalid")){
			System.out.println();
			System.out.println("You did not guess a letter from the list. Make sure you guess a consonant.");
			System.out.println("Please try again!");
			guessLetter();
			return -2;
		}	
		return 0;
	}
	private String checkIfLetterIsInRemainingLists (char letter) {
		//pre: receives letter to check if in vowelsRemaining or consonantsRemaining list
		//post: returns true if letter is in the list, false otherwise
		letter = Character.toUpperCase(letter);
		int bothListsNotValidLetter = 0; //if this reaches 2 then they did not pick a valid letter
		for (Character ch : consonantsRemaining) 
			if (ch == letter)
				return "consonant";
		bothListsNotValidLetter++;
		for (Character vo : vowelsRemaining)
			if (vo == letter)
				return "vowel";
		bothListsNotValidLetter++;
		if (bothListsNotValidLetter == 2)
			return "invalid";
		return "none";
	}
	private void removeLetterFromList(char letter, String letterType) {
		//pre: receives letter to remove from vowelsRemaining or consonantsRemaining list because the letter has already been guessed
		//post: returns nothing, however deletes the letter from vowelsRemaining or consonantsRemaining list
		int index = 0;
		letter = Character.toUpperCase(letter);
		ArrayList <Character> list = new ArrayList <Character> ();
		if (letterType.equals("consonant"))
			list = consonantsRemaining;
		else if (letterType.equals("vowel"))
			list = vowelsRemaining;
		while (index < list.size()) {
			if (list.get(index) == letter) {
				list.remove(index);
			} else 
				index++;
		}
	}
	public void printRemainingLetters(Character vOrC) {
		//pre: receives char v(vowel) or c (consonant)
		//post: prints out the remaining consonants or vowels
		int x = 0;
		if (vOrC == 'v')
			for(Character vowel : vowelsRemaining) {
				if (x % 5 == 0)
					System.out.println();
				System.out.print(" "+vowel+" ");
				x++;
			}
		else if (vOrC == 'c')
			for(Character consonant:consonantsRemaining) {
				if (x % 4 == 0)
					System.out.println();
				System.out.print(" "+consonant+" ");
				x++;
			}
		System.out.println();
		x = 0;
	}
	private void addLetterToPhraseOnBoard (char letter, int pos) {
		StringBuffer phraseSB = new StringBuffer (phraseOnBoard);
		phraseSB.setCharAt(pos, letter);
		phraseOnBoard = phraseSB.toString();
	}
	private int findIndexOfLetter (char letter) {
		//pre: receives letter to find index of letter in secret phrase
		//post: returns amt of letter in phrase
		int letterAmt = 0;
		int x = 0;
		while (x < secretPhrase.length()) {
			if (letter == secretPhrase.charAt(x)) {
				addLetterToPhraseOnBoard(letter,x);
				letterAmt++;
			}
			x++;
		}
		return letterAmt;
	}
	public boolean checkLetter (char letter) {
		//pre: receives letter that player guesses
		//post: return true if letter is in the phrase
		int i = 0;
		while (i != secretPhrase.length()) {
			if (letter == secretPhrase.charAt(i)) 
				return true;
			i++;
		}
		return false;
	}
	private static String createCodedPhrase (String secret) {
		//pre: receives string of the secret phrase
		//post: dashes out each letter, leaves spaces alone, and returns modified string
		int index = 0;
		StringBuffer secretSB = new StringBuffer (secret);
		while (index != secretSB.length()) {
			if (secretSB.charAt(index) != ' ')
				secretSB.setCharAt(index, '-');
			index++;
		}
		String newSecret = secretSB.toString();
		return newSecret;
	}
}
