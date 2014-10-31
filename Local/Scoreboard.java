package hw12;


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
 * @author rm7536
 * @author afa2118
 */
public class Scoreboard extends JTextArea {

	Player _p1, _p2;
	
	/**
	 * Constructor to add a score board
	 * @param p1 score of player one
	 * @param p2 score of player two
	 */
	public Scoreboard(Player p1, Player p2) {
		super("Player 1: 0\tPlayer 2: 0", 0, 0);
		this.setFont(new Font("Serif", 1, 40));
        this.setDisabledTextColor(Color.WHITE);
		this.setEnabled(false);
        this.setOpaque(false);
        _p1 = p1;
        _p2 = p2;
	}

	/**
	 * updates the score
	 */
	public void updateScore() {
		this.setText("Player 1: " + _p1.getScore() + "\tPlayer 2: " + _p2.getScore());
	}
}
