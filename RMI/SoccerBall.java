package hw14;

/**
 * SoccerBall.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Class the designs and controls the soccer ball
 * 
 * @author rm7536
 * @author afa2118
 */
public class SoccerBall extends  java.awt.geom.Ellipse2D.Double {
	
	private BufferedImage _image;
	private boolean imported = false;
	private Color _color;
	private JPanel _panel;
	private int _rotation;
	
	/**
	 * Constructor to create the soccer ball
	 */
	public SoccerBall(int w, int h) {
		super( StaticVars.DEFAULT_WIDTH / 2, StaticVars.DEFAULT_HEIGHT / 2, w, h);
		try {
			_image = ImageIO.read(new File("real-football-ball-no-shadow-hi.png") );
			imported = true;
		}
		catch (IOException e) {
			System.err.println("Soccer ball not imported.");
			_color = StaticVars.BALL_COLOR;
		}
	}
	
	/**
	 * This sets the JPanel that this ball resides in.  Needed because SoccerController makes the ball before he makes
	 * the JPanel, as JPanel needs to be passed the ball in as well.
	 * @param aPanel
	 */
	public void setPanel(JPanel aPanel) {
		_panel = aPanel;
	}
	
	/**
	 * The x position minimum of the ball
	 * @return the x position min of the ball
	 */
	public int getMinBoundX() {
		return _panel.getX();
	}
	
	/**
	 * The y position minimum of the ball
	 * @return the y position min of the ball
	 */
	public int getMinBoundY() {
		return _panel.getY();
	}
	
	/**
	 * The x position maximum of the ball
	 * @return the x position max of the ball
	 */
	public int getMaxBoundX() {
		return (int) (_panel.getX() + _panel.getWidth() - this.getWidth() );
	}
	
	/**
	 * The y position maximum of the ball
	 * @return the y position max of the ball
	 */
	public int getMaxBoundY() {
		return (int) (_panel.getY() + _panel.getHeight() - this.getHeight() );
	}
	
	/**
	 * Sets the size of the ball
	 * @param w width
	 * @param h height
	 */
	public void setSize(double w, double h) {
		this.setFrame( this.getX(), this.getY(), w, h );
	}
	
	/**
	 * Sets the location of the ball
	 * @param x sets the x position location
	 * @param y sets the y position location
	 */
	public void setLocation( double x, double y) {
		this.setFrame( x, y, this.getWidth(), this.getHeight() );
	}
	
	/**
	 * Paints the soccer ball using an image if it was imported correctly
	 * @param g2d
	 */
	public void paint(Graphics2D g2d) {
		if ( !imported ) {
			Color saved = g2d.getColor();
			g2d.setColor(_color);
			g2d.fill(this);
			g2d.setColor(saved);
		}
		else {
			//_rotation = (_rotation + 10 ) % 360;
			//g2d.rotate(_rotation, this.getCenterX(), this.getCenterY() );
			g2d.drawImage( _image, (int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
			//g2d.rotate(-_rotation, this.getCenterX(), this.getCenterY() );
		}
	}
}
