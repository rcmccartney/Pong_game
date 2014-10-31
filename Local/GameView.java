package hw12;


/**
 * GameView.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 * GameView works as the View of the MVC system and runs the Soccer Pong program
 * @author rm7536
 * @author afa2118
 */
public class GameView extends JFrame {
	
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public GameView(String title) throws HeadlessException {
		super(title);
		this.setSize(StaticVars.DEFAULT_WIDTH, StaticVars.DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(StaticVars.START_X, StaticVars.START_Y);
		this.add( new PongPanel(this) );
		this.setVisible(true);
	}
	
	/**
	 * Main runs the program
	 * @param args
	 */
	public static void main(String[] args) {
		new GameView("Soccer Pong!");
	}
}
