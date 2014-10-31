package hw14;

/**
 * Scoreboard.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;

/**
 * Adds and operates a score board to the game as a text
 * 
 * @author rm7536
 * @author afa2118
 */
public class Scoreboard extends JTextArea {

	private int scoreP1, scoreP2;	
	
	/**
	 * Constructor to add a score board
	 */
	public Scoreboard() {
		super("Player 1: 0\tPlayer 2: 0", 0, 0);
		this.setFont(new Font(StaticVars.FONT, 1, StaticVars.FONT_SIZE));
        this.setDisabledTextColor(Color.WHITE);
		//so no one can select or highlight this text box
        this.setEnabled(false);
        //so the white background disappears
        this.setOpaque(false);
	}

	/**
	 * updates the score, called whenever P1 scores a goal
	 */
	public void updateP1Score() {
		this.setText("Player 1: " + (++scoreP1) + "\tPlayer 2: " + scoreP2);
	}
	
	/**
	 * updates the score, called whenever P2 scores a goal
	 */
	public void updateP2Score() {
		this.setText("Player 1: " + scoreP1 + "\tPlayer 2: " + (++scoreP2) );
	}
}
