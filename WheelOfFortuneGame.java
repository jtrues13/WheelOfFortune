import java.util.Scanner;
import java.util.ArrayList;

public class WheelOfFortuneGame {
	public static Board board = new Board ();
	public static Scanner sc = new Scanner (System.in);
	public static Wheel wheel = new Wheel ();
	public static ArrayList <Scorecard> players = new ArrayList <Scorecard> ();
	public static void main(String[] args) {
		welcome();
		setInitialTurns();
		choices();
	}
	public static void setInitialTurns () {
		for (int index = 0; index < players.size(); index++)
			if (index==0)
				players.get(index).turnOnCurrentTurn();
			else
				players.get(index).turnOffCurrentTurn();
	}
	public static void choices () {
		//pre: receives Scorecard object
		//post: returns nothing, executes user's choice
		String userChoice;
		System.out.println();
		System.out.println();
		do {
			printOptions();
			userChoice = getValidChoice();
			if (userChoice.equalsIgnoreCase("play")) {
				playGame();
				System.exit(0);
			}
			else if (userChoice.equalsIgnoreCase("instructions")) {
				showInstructions();
				choices();	
			}
			else if (userChoice.equalsIgnoreCase("quit"))
				quitGame();
		} while (!userChoice.equalsIgnoreCase("quit"));
		System.out.println();
	}
	public static void displayPlayerStats () {
		//pre: receives nothing
		//post: returns nothing, prints out the players statistics
		System.out.println();
		System.out.println("Player stats: ");
		for (Scorecard card : players)
			System.out.println(card.returnPlayerName() + " has $"+card.returnMoney()+" in prize money, " +card.returnTurnsLeft()+" turns left and has won "+card.returnWins()+" rounds.");			
		System.out.println();

	}
	public static int getGameAmount() {
		System.out.println();
		System.out.println("How many games in a series (or one game) would you like to play?");
		System.out.println("Please enter an ODD number:");
		int games = sc.nextInt();
		while (games % 2 == 0){
			System.out.println("Please enter an ODD number:");
			games = sc.nextInt();
			sc.nextLine();
		}
		return games;
	}
	public static void resetTurns() {
		for (Scorecard player:players)
			player.resetTurns();
	}
	public static void playGame ()/*BIG*/ {
		//pre: receives Scorecard object
		//post: returns nothing, plays game
		int gameAmt = getGameAmount();
		board.displayPhrase();
		board.displayClue();
		//turns switch here, use arrayLists to switch scorecard objects
		int totalTurnsLeft = getTotalTurnsLeft();
		int currentPlayerIndex = 0;
		boolean roundWon = false;
		for (int gameNum = 0; gameNum < gameAmt; gameNum++) {
			if (roundWon) {
				board = new Board ();
				resetTurns();
				setInitialTurns();
				roundWon = false;
			}
			while (totalTurnsLeft > 0 && !roundWon) {
				if (players.size() > 1) {
					while (players.get(currentPlayerIndex).returnCurrentTurn()) {
						displayPlayerStats();	
						if (players.get(currentPlayerIndex).returnTurnsLeft() == 1)
							System.out.println(players.get(currentPlayerIndex).returnPlayerName() + ", this is your last turn!");
						else
							System.out.println(players.get(currentPlayerIndex).returnPlayerName() + ", it is your turn!");
						String playResult = getPlayOptions(players.get(currentPlayerIndex));
						if (playResult.equals("wrong")) {
							players.get(currentPlayerIndex).turnOffCurrentTurn();
							players.get(currentPlayerIndex).turnsMinusOne();
						} else if (playResult.equals("puzzleCorrect")) {
							System.out.println("Congratulations, you won the round!");
							System.out.println("You get to keep $"+ players.get(currentPlayerIndex).returnMoney()+"!");
							System.out.println();
							endRound(players.get(currentPlayerIndex));
							roundWon = true;
						}
						//END ELSE IF
						//END ELSE LOOP
					} //END While
					if (currentPlayerIndex == players.size()-1) {
						currentPlayerIndex = 0;
						players.get(currentPlayerIndex).turnOnCurrentTurn();
					} else {
						players.get(currentPlayerIndex).turnOffCurrentTurn();
						currentPlayerIndex++;
						players.get(currentPlayerIndex).turnOnCurrentTurn();
					}
				}//END IF
				else if (players.size()==1) {
					displayPlayerStats();
					if (players.get(0).returnTurnsLeft() == 1)
						System.out.println(players.get(0).returnPlayerName() + ", you are on your last turn!");
					else
						System.out.println(players.get(0).returnPlayerName() + ", it is your turn!");
					String playResult = getPlayOptions(players.get(0));
					if (playResult.equals("wrong")) {
						players.get(0).turnsMinusOne();
					} else if (playResult.equals("puzzleCorrect")) {
						System.out.println("Congratulations, you won the round!");
						System.out.println("You get to keep $"+ players.get(0).returnMoney()+"!");
						endRound(players.get(0));
						roundWon = true;
					}
				}//END else if LOOP
				totalTurnsLeft = getTotalTurnsLeft();
			}//END WHILE LOOP
			//BEGIN SOLVE-OFF
			if (totalTurnsLeft == 0 && !roundWon) {
				System.out.println();
				System.out.println("Since all players ran out of turns, ");
				System.out.println("now the first person to solve the puzzle wins it all!");
				System.out.println("Good luck!");
				System.out.println();
			}
			int counter = 0;
			while (totalTurnsLeft == 0 && !roundWon) {
				/*IF PLAYERS RUN OUT OF TURNS
				SOLVE OFF BEGINS
				WHOEVER SOLVES CORRECTLY FIRST WINS*/
				roundWon = solveOff(players);
				counter++;
				if (counter == 3){
					System.out.println("If you want to give up, enter QUIT (or to continue enter any other word):");
					String giveUp = sc.next();
					sc.nextLine();
					if (giveUp.equalsIgnoreCase("quit")) {
						Scorecard highestWinner = findPlayersLargestWinAmt();
						quitGame(highestWinner);
					}
				}
			} //END WHILE 
			Scorecard winner = findPlayersLargestWinAmt();
			if (winner.returnWins() == (gameAmt/2)+1)
				quitGame(winner);
		}
	}
	public static Scorecard findPlayersLargestWinAmt(){
		int high = 0;
		int highIndex = 0;
		for (int index = 0; index < players.size(); index++) {
			if (players.get(index).returnWins() > high) {
				high = players.get(index).returnWins();
				highIndex = index;
			}
		}
		return players.get(highIndex);
	}
	public static void endRound(Scorecard scorecard) {
		scorecard.addToWins();
		scorecard.turnOffCurrentTurn();
		board.repopTextFile();
	}
	public static boolean solveOff (ArrayList <Scorecard> list) {
		for (int playerIndex = 0; playerIndex < list.size(); playerIndex++) {
			System.out.println(players.get(playerIndex).returnPlayerName()+", it is your turn to solve!");
			int correctAmt = chooseToSolvePuzzle(list.get(playerIndex));
			if (correctAmt==0) {
				list.get(playerIndex).add1("ic");
			}
			else {
				System.out.println("Congratulations, "+players.get(playerIndex).returnPlayerName()+", you won the round!");
				System.out.println(players.get(playerIndex).returnPlayerName()+", you get to keep $"+ players.get(playerIndex).returnMoney()+"!");
				players.get(playerIndex).add1("cc");
				endRound(players.get(playerIndex));
				return true;
			}
		} //END FOR 
		return false;
	}
	public static int getTotalTurnsLeft() {
		int total = 0;
		for (Scorecard player : players) {
			total+=player.returnTurnsLeft();
		}
		return total;
	}
	public static String spinTheWheel () {
		String spinCode = "";
		while (!spinCode.contains("h")) {
			System.out.println("Slide your finger from 'H' key to RETURN key to spin the wheel!");
			System.out.println("		(For example: HJKL;'  or enter 'h')");
			spinCode = sc.next();
		}
		String wheelResult = wheel.spin();
		return wheelResult;
	}
	public static int handleSpinResult (Scorecard scorecard) {
		String result = spinTheWheel();
		int cashAmt;
		if (result.equals("bankrupt"))
			return 0;
		else if (result.equals("loseTurn"))
			return -1;
		else {
			cashAmt = Integer.parseInt(result);
			return cashAmt;
		}	
	}
	public static String getSBSChoice () {
		System.out.println();
		System.out.println("Would you like to spin the wheel to guess a consonant, buy a vowel, or solve the puzzle?");
		System.out.println("Enter SPIN to spin the wheel in order to guess a consonant.");
		System.out.println("Enter BUY to buy a vowel.");
		System.out.println("Enter SOLVE to solve the puzzle.");
		String word = "";
		do
			word = sc.next();
		while (!word.equalsIgnoreCase("spin") && !word.equalsIgnoreCase("buy") && !word.equalsIgnoreCase("solve"));
		return word;
	}
	public static String getPlayOptions (Scorecard scorecard)/*BIG*/ {
		//pre: receives scorecard
		//post: returns nothing, executes the player's option to play either guess, buy, or solve
		String choice = getSBSChoice();
		board.displayPhrase();
		board.displayClue();
		int correctAmt;
		//BEGIN SPIN if
		if (choice.equalsIgnoreCase("spin")) {
			int reward = handleSpinResult(scorecard);//deals with if bankrupt or loseTurn is landed and if money is landed on 
			if (reward > 0) {
				correctAmt = chooseToGuessLetter();
				if (correctAmt==0) { //there are 0 of the given letter in the phrase
					scorecard.add1("ic");
					return "wrong";
				}
				else if (correctAmt == -2) {//invalid
					scorecard.turnOnCurrentTurn();
				}
				else { //there are more than 0 of the given letter in the phrase
					int totalPrize = correctAmt * reward;
					scorecard.addMoney(totalPrize);
					scorecard.add1("cc");
					return "consonantCorrect";
				}
			}
			else if (reward == 0) { //spin landed on bankrupt
				System.out.println("Sorry, you landed on Bankrupt and lost all of your money!");
				scorecard.bankrupt();
				return "wrong";
			}
			else if (reward == -1) { //spin landed on loseTurn
				System.out.println("Sorry, you landed on Lose A Turn so there goes your turn!");
				return "wrong";
			}
		} //END SPIN IF &&& BEGIN BUY IF 
		else if (choice.equalsIgnoreCase("buy")) {
			if (scorecard.returnMoney() < 250) {
				System.out.println("Sorry, you do not have enough money to buy a vowel!");
				System.out.println("You need $250.");
				getPlayOptions(scorecard);
			} else { //if player has enough money to buy vowel
				correctAmt = chooseToBuyVowel(scorecard);
				scorecard.loseMoney(250);
				if (correctAmt == 0) {
					scorecard.add1("ic");
					return "wrong";
				}
				else {
					System.out.println("The vowel you bought is correct and occurred " +correctAmt + " times in the puzzle.");
					scorecard.add1("cc");
					return "vowelCorrect";
				}
			}
		} //END BUY IF &&& BEGIN SOLVE IF
		else if (choice.equalsIgnoreCase("solve")) {
			correctAmt = chooseToSolvePuzzle(scorecard);
			if (correctAmt==0) {
				scorecard.add1("ic");
				return "wrong";
			}
			else {
				scorecard.add1("cc");
				return "puzzleCorrect";
			}
		} //END SOLVE IF
		return "";
	}
	public static int chooseToGuessLetter () {
		//pre: receives scorecard object
		//post: returns int amount of letters correct, checks if the guessed letter is in the secret phrase and changes new displayed phrase
		System.out.println();
		System.out.println("Please guess a letter from the following list:");
		board.printRemainingLetters('c');
		int correctLetterAmt = board.guessLetter();
		return correctLetterAmt;
	}
	public static int chooseToBuyVowel (Scorecard scorecard) {
		//pre: receives scorecard
		//post: returns int amount of occurrences if vowel is correct, 0 otherwise, buys vowel, checks if vowel is in secret phrase, depletes $, and changes new displayed phrase
		System.out.println();
		System.out.println("Please choose a vowel from the following list:");
		board.printRemainingLetters('v');
		int correctAmt = board.guessLetter();
		return correctAmt;
	}
	public static int chooseToSolvePuzzle (Scorecard scorecard)/*WORK*/ {
		//pre: receives scorecard
		//post: returns 1 if puzzle guess is correct, 0 otherwise
		System.out.println("Type in the entire puzzle: ");
		sc.nextLine();
		String puzzleGuess = sc.nextLine();
		if (puzzleGuess.equalsIgnoreCase(board.returnSecret())) {
			System.out.println("You solved correctly!");
			return 1;
		}
		else {
			System.out.println("You solved incorrectly.");
			return 0;
		}
	}
	public static void showInstructions () {
		//pre: receives nothing
		//post: prints out the instructions
		System.out.println();
		System.out.println("Object");
		System.out.println("The object of the game is to earn money by solving a puzzle made up of blank spaces representing letters that form a word or words.");
		System.out.println(" Players are given a one-sentence clue that alludes to the puzzle answer,");
		System.out.println(" and then they spin a wheel to reveal their fortune or possibly misfortune.");
		System.out.println(" After he spins the wheel, the contestant can request a specific letter to chip away at solving the puzzle");
		System.out.println(" or he can solve the entire puzzle on the spot.");
		System.out.println("The player who earns the most money after four rounds wins the game.");
		System.out.println();
		System.out.println("How To Play");
		System.out.println("The host announces the first word clue and reveals a certain number of spaces for the puzzle.");
		System.out.println("The first player spins the wheel and lands on a certain amount of money or even a section that says \"Bankrupt\", where you lose everything.");
		System.out.println("You could also land on \"Lose a Turn\" which makes you lose your turn.");
		System.out.println("The player guesses one letter consonant and the host reveals if the word contains that consonant.");
		System.out.println("You can earn even more money if the letter appears in more spaces.");
		System.out.println("For example, if the spinner lands on $400, you guess the letter \"C\" and there happens to be three \"C\"'s in the word, you earn $1,200.");
		System.out.println("You get to take another turn if you guess the consonant correctly; however, if you guess a consonant that isn't in the word, you don't get the prize or money and the next player gets a turn.");
		System.out.println("When players think they may be close to solving the puzzle or want to identify vowels in the word, they must purchase a vowel. A vowel costs $250 and the player must have enough money in her bank to buy the vowel.");
		System.out.println("The first player to correctly guess the word wins the round and all money and prizes he's collected in his bank during that round. Every other player's money is wiped clean, and a new round begins.");
		System.out.println("The first player to win the majority of games in a series wins the series and therefore all of the money from all previous rounds.");
		System.out.println();

	}
	public static void quitGame () {
		//pre: receives nothing, used when no player data is created
		//post: returns nothing
		System.out.println();
		System.out.println("Thank you for playing Wheel Of Fortune!");
		System.exit(0);
	}
	public static void quitGame (Scorecard scorecard) {
		//pre: receives Scorecard object to send to high scores
		//post: quits game
		//WRITE TO TEXTFILE OF HIGHSCORES
		//sendScorecardToHighScores(scorecard);
		System.out.println();
		System.out.println(scorecard.returnPlayerName()+", you earned $"+scorecard.returnMoney()+".");
		System.out.println(scorecard.getCorrectRatio()+"% of the time you made the correct choice.");
		System.out.println(scorecard.getIncorrectRatio()+"% of the time you made the incorrect choice.");
		System.out.println();
		System.out.println("Thank you for playing Wheel Of Fortune!");
		System.exit(0);
	}
	/*public static void sendScorecardToHighScores (Scorecard scorecard) {
		//pre: receives Scorecard object to be put in highscores textfile
		//post: returns nothing
		//HighScores.readHighScores();
		System.out.println("Winner's game data:");
		HighScores.insertNewScore(scorecard);
		HighScores.printList();
		System.out.println();
		System.out.println("All players' game data:");
		for (Scorecard player:players) {
			HighScores.insertNewScore(player);
			HighScores.printList();
			System.out.println();
		}
	}*/

	public static void welcome () {
		//pre: receives nothing
		//post: adds players to global variable ArrayList players 
		System.out.println("Welcome to Wheel Of Fortune!");
		System.out.println();
		System.out.println("How many players?");
		int playerAmt = sc.nextInt();
		sc.nextLine();
		for (int counter = 0; counter < playerAmt; counter++) {
			int playerDisplayNum = counter+1;
			System.out.println("Player "+playerDisplayNum+", what is your first name?");
			String name = sc.next();
			players.add(new Scorecard (name));
		}
	}

	public static void printOptions () {
		//pre: receives nothing
		//post: returns nothing, prints options
		System.out.println("You can play, look at the instructions, or quit.");
		System.out.println("Enter 'Play' to begin the game.");
		System.out.println("Enter 'Instructions' to show the instructions.");
		System.out.println("Enter 'Quit' to quit.");
	}

	public static String getValidChoice () {
		//pre: receives nothing
		//post: returns the user's choice once the valid choice is confirmed
		String choice = sc.next();
		if (!choice.equalsIgnoreCase("play") && !choice.equalsIgnoreCase("instructions") && !choice.equalsIgnoreCase("quit")) {
			System.out.println("You did not enter a valid choice, please try again.");
			printOptions();
			getValidChoice();
		}
		return choice;
	}

}
