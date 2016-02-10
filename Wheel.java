import java.util.Random;
import java.util.ArrayList;
public class Wheel {
	private Random rand = new Random ();
	private ArrayList <String> wheelParts = new ArrayList <String> () {{
		add("100");
		add("150");
		add("200");
		add("250");
		add("300");
		add("350");
		add("400");
		add("450");
		add("500");
		add("550");
		add("600");
		add("650");
		add("700");
		add("750");
		add("800");
		add("850");
		add("900");
		add("950");
		add("1000");
		add("loseTurn");
		add("bankrupt");
	}};
	public Wheel () {

	}
	public String spin () {
		//pre: receives nothing
		//post: returns nothing, spins wheel gets the result and the 
		System.out.println("Spinning...");
		int rand0 = rand.nextInt(7)+14;
		int rand1 = rand.nextInt(7)+7;
		int rand2 = rand.nextInt(8);
		System.out.println("..."+wheelParts.get(rand0)+"..."+wheelParts.get(rand1)+"..."+wheelParts.get(rand2)+"...");
		int num = rand.nextInt(21);
		String result = wheelParts.get(num);
		if (result.equals("bankrupt"))
			System.out.println("You landed on Bankrupt!");
		else if (result.equals("loseTurn"))
			System.out.println("You landed on Lose a Turn!");
		else 
			System.out.println("You landed on $"+ result+"!");
		return result;
	}
}
