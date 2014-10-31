package hw14;

/**
 * PongPanel.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Draws the field, and adds the players, ball, and nets to the the game
 * 
 * @author rm7536
 * @author afa2118
 */
public class PongPanel extends JPanel {

	private BufferedImage _image;
	private Goal _net1, _net2;
	private Player _p1, _p2;
	private SoccerBall _ball;
	private boolean imported = false;
	
	/**
	 * Loads an image for the background screen and throws an exception if the image does not load and adds the players,
	 *  net, ball and score board to the paintComponent
	 *  
	 * @param p1 player1 to draw 
	 * @param p2 player2 to draw 
	 * @param ball to draw 
	 * @param net1 Goal to draw 
	 * @param net2 Goal to draw 
	 */
	public PongPanel( Player p1, Player p2, SoccerBall ball, Goal net1, 
			Goal net2, Scoreboard score ) {
		super();
		 try {
	          _image = ImageIO.read(new File("schematic-green-soccer-field.jpg"));
	          imported = true;
		 } 
		 catch (IOException ex) {
			 System.err.println("Could not load file");
			 this.setBackground(StaticVars.BACKGROUND);
		 }
		 _net1 = net1;
		 _net2 = net2;
		 _p1 = p1;
		 _p2 = p2;
		 _ball = ball;
		 _ball.setPanel(this); 
		 //scoreboard is part of the JPanel
		 this.add(score);
	}

	/**
	 * In charge of the painting components of ball, player, and net, as well as background image.
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		if ( imported ) {
			g2d.drawImage( _image, 0, 0, this.getWidth(), this.getHeight(), null);
		}
		_p1.paint(g2d);
		_p2.paint(g2d);
		_ball.paint(g2d);
		_net1.paint(g2d);
		_net2.paint(g2d);
	}
}
