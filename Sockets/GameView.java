package hw13;
/**
 * GameView.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.swing.JFrame;

/**
 * GameView works as the View of the MVC system and runs the Soccer Pong program.  It is an extension of
 * JFrame.
 * 
 * @author rm7536
 * @author afa2118
 */
public class GameView extends JFrame {
	
	/**
	 * 
	 * @param title to print on the JFrame
	 * @param out the PrintWriter connecting server/client
	 * @param in the InputStream connecting server/client
	 * @param isServer boolean to know whether the server or client is this object.
	 * @throws HeadlessException if the computer doesn't have a mouse/keyboard
	 */
	public GameView(String title, PrintWriter out, BufferedReader in, boolean isServer) throws HeadlessException {
		super(title);
		this.setSize(StaticVars.DEFAULT_WIDTH, StaticVars.DEFAULT_HEIGHT);
		//close operations are defined inside the Controller
		this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		this.setLocation(StaticVars.START_X, StaticVars.START_Y);
		this.add( new PongPanel(this, out, in, isServer) );
		//only the server can resize the screen, as they must stay the same size
		if (!isServer) {
			this.setResizable(false);
		}
		//Don'y want to resize below an acceptable size, or else the game is unplayable
		else {
			this.setMinimumSize(new Dimension(StaticVars.MIN_WIDTH, StaticVars.MIN_HEIGHT));
		}
		this.setVisible(true);
	}
}
