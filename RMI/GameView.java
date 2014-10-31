package hw14;

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
import javax.swing.JFrame;
import javax.swing.JPanel;

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
	 * @param panel the JPanel that prints the components of the game
	 * @param controller handles this Frame's Window, Key, Mouse, Component listening
	 * @param isServer boolean to know whether the server or client is this object.
	 * @throws HeadlessException if the computer doesn't have a mouse/keyboard
	 */
	public GameView( String title, JPanel panel, SoccerController controller, boolean isServer ) throws HeadlessException {
		super(title);
		this.setSize(StaticVars.DEFAULT_WIDTH, StaticVars.DEFAULT_HEIGHT);
		//close operations are defined inside the Controller
		this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		this.setLocation(StaticVars.START_X, StaticVars.START_Y);
		this.add( panel );
		//only the server can resize the screen, as they must stay the same size
		if (!isServer) {
			this.setResizable(false);
			this.setVisible(true);
		}
		//Don't want to resize below an acceptable size, or else the game is unplayable
		else {
			this.setMinimumSize(new Dimension(StaticVars.MIN_WIDTH, StaticVars.MIN_HEIGHT));
		}
		this.addComponentListener(controller);
		this.addWindowListener(controller);
		this.addKeyListener(controller);
		this.addMouseMotionListener(controller);
	}
}
