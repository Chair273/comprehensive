package comprehensive;

/**
 * Run the app in the console.
 * 
 * @author Devin Santos and Tyler Christiansen
 * @version 2025-12-3
 */
public class Main {
	public static void main(String[] args) {
		Interface app = new Interface(args[0]);
		app.update();
	}
}