import java.text.DecimalFormat;

public class Scorecard {	
	private DecimalFormat df = new DecimalFormat ("#.##");
	private String playerName;
	private int wins;
	private int money;
	private int turnsLeft;
	private boolean currentTurn;
	private double correctChoice;
	private double incorrectChoice;
	private double totalChoice;
	public Scorecard (String name) {
		playerName = name;
		turnsLeft = 4;
		wins = 0;
		money = 0;
		correctChoice = 0;
		incorrectChoice = 0;
		totalChoice = 0;
	}
	public void resetTurns () {
		turnsLeft =4;
	}
	public void add1 (String thing) {
		if (thing.equals("cc"))
			correctChoice++;
		if (thing.equals("ic"))
			incorrectChoice++;
		totalChoice++;
	}
	public String getCorrectRatio () {
		double ratio = (correctChoice/totalChoice)*100;
		Double r = ratio;
		String newRatio = (df.format(r)).toString();
		return newRatio;
	}
	public String getIncorrectRatio () {
		double ratio = (incorrectChoice/totalChoice)*100;
		Double r = ratio;
		String newRatio = (df.format(r)).toString();
		return newRatio;
	}
	public void bankrupt () {
		money = 0;
	}
	public void addToWins () {
		wins+=1;
	}
	public int returnWins () {
		return wins;
	}
	public void switchTurn () {
		if (currentTurn==true)
			currentTurn = false;
		if (currentTurn==false)
			currentTurn = true;
	}
	public void turnOffCurrentTurn () {
		currentTurn = false;
	}
	public void turnOnCurrentTurn () {
		currentTurn = true;
	}
	public boolean returnCurrentTurn () {
		return currentTurn;
	}
	public void turnsMinusOne () {
		turnsLeft-=1;
	}
	public int returnTurnsLeft () {
		return turnsLeft;
	}
	public int returnMoney () {
		return money;
	}
	public String returnPlayerName () {
		return playerName;
	}
	public void addMoney (int cashGain) {
		money += cashGain;
	}
	public void loseMoney (int cashLoss) {
		money -= cashLoss;
	}
}
