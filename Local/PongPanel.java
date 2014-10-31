package hw12;


/**
 * PongPanel.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Draws the field, and adds the players, ball, and nets to the the game
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
	public PongPanel(JFrame aFrame) {
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
		 _p1 = new Player(this);
		 _p2 = new Player(this);
		 _ball = new SoccerBall(this, StaticVars.DEFAULT_WIDTH / 40, StaticVars.DEFAULT_WIDTH / 40);
		 _score = new Scoreboard(_p1, _p2);
		 _controller = new SoccerController(StaticVars.DEFAULT_INTERVAL, this, _p1, _p2, _ball, _net1, _net2, _score);
		 this.add(_score);
		 aFrame.addComponentListener(_controller);
		 aFrame.addKeyListener(_controller);
		 aFrame.addMouseMotionListener(_controller);
	}

	/**
	 * In charge of the painting components of ball, player, and net.
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
