package hw13;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
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
	private SoccerController _controller;
	private Scoreboard _score;
	private boolean imported = false;
	
	/**
	 * Loads an image for the background screen and throws an exception if the image does not load and adds the players,
	 *  net, ball and score board to the game
	 * @param aFrame
	 */
	
	/**
	 * Loads an image for the background screen and throws an exception if the image does not load and adds the players,
	 *  net, ball and score board to the game
	 *  
	 * @param aFrame the container JFrame
	 * @param out the output stream for client/server interaction
	 * @param in the input stream for client/server interaction
	 * @param isServer whether this class is acting for the client or the server
	 */
	public PongPanel(JFrame aFrame, PrintWriter out, BufferedReader in, boolean isServer) {
		super();
		 try {
	          _image = ImageIO.read(new File("schematic-green-soccer-field.jpg"));
	          imported = true;
		 } 
		 catch (IOException ex) {
			 System.err.println("Could not load file");
			 this.setBackground(StaticVars.BACKGROUND);
		 }
		 _net1 = new Goal();
		 _net2 = new Goal();
		 _p1 = new Player();
		 _p2 = new Player();
		 _ball = new SoccerBall(this, StaticVars.DEFAULT_WIDTH / 40, StaticVars.DEFAULT_WIDTH / 40);
		 _score = new Scoreboard();
		 _controller = new SoccerController(StaticVars.DEFAULT_INTERVAL, aFrame, this, _p1, _p2, _ball, _net1, _net2, _score,
				 out, in, isServer);
		 //scoreboard is part of the JPanel
		 this.add(_score);
		 //the controller does all the listening and responding
		 aFrame.addComponentListener(_controller);
		 aFrame.addWindowListener(_controller);
		 aFrame.addKeyListener(_controller);
		 aFrame.addMouseMotionListener(_controller);
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
