package hw12;


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
 * @author rm7536
 * @author afa2118
 */
public class SoccerBall extends  java.awt.geom.Ellipse2D.Double {
	
	private BufferedImage _image;
	private boolean imported = false;
	private Color _color;
	private JPanel _panel;
	private int _changeX, _changeY, _rotation;
	
	/**
	 * Constructor to create the soccer ball
	 */
	public SoccerBall(JPanel panel, int w, int h) {
		super( StaticVars.DEFAULT_WIDTH / 2, StaticVars.DEFAULT_HEIGHT / 2, w, h);
		_panel = panel;
		_changeX = StaticVars.DEFAULT_BALL_MOVE;
		_changeY = StaticVars.DEFAULT_BALL_MOVE;
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
	 * Resests the position of the soccer ball
	 */
	public void reset() {
		this.setLocation( (( _panel.getX()+ _panel.getWidth() ) / 2) - this.getWidth() / 2, 
				( ( _panel.getY()+ _panel.getHeight() ) / 2 ) - this.getHeight() / 2);
	}
	
	public int getChangeX() {
		return _changeX;
	}
	
	public int getChangeY() {
		return _changeY;
	}
	
	/**
	 * The x position minimum of the ball
	 * @return the x position min of the ball
	 */
	private int getMinBoundX() {
		return _panel.getX();
	}
	
	/**
	 * The y position minimum of the ball
	 * @return the y position min of the ball
	 */
	private int getMinBoundY() {
		return _panel.getY();
	}
	
	/**
	 * The x position maximum of the ball
	 * @return the x position max of the ball
	 */
	private int getMaxBoundX() {
		return (int) (_panel.getX() + _panel.getWidth() - this.getWidth() );
	}
	
	/**
	 * The y position maximum of the ball
	 * @return the y position max of the ball
	 */
	private int getMaxBoundY() {
		return (int) (_panel.getY() + _panel.getHeight() - this.getHeight() );
	}
	
	/**
	 * Rotates the ball
	 * @param amount rotation speed
	 */
	public void rotate(int amount) {
		_rotation += amount;
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
	 * Moves the soccer ball
	 * @param xDirection x direction of the ball
	 * @param yDirection y direction of the ball
	 */
	public void move(int xDirection, int yDirection) {
		_changeX *= xDirection;
		_changeY *= yDirection;
		int nextX = (int) this.getX() + _changeX;
		int nextY = (int) this.getY() + _changeY;
		
		if( nextX < this.getMinBoundX() ) {
			_changeX *= -1;
			nextX = this.getMinBoundX();
		}
		else if( nextX > this.getMaxBoundX() ) {
			_changeX *= -1;
			nextX = this.getMaxBoundX();
		}
		else if( nextY < this.getMinBoundY() ) {
			_changeY *= -1;
			nextY = this.getMinBoundY();
		}
		else if( nextY > this.getMaxBoundY() ) {
			_changeY *= -1;
			nextY = this.getMaxBoundY();
		}
		this.setLocation(nextX, nextY);
	}
	
	/**
	 * Paints the color of the soccer ball
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
			g2d.rotate(_rotation, this.getCenterX(), this.getCenterY() );
			g2d.drawImage( _image, (int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
			g2d.rotate(-_rotation, this.getCenterX(), this.getCenterY() );
		}
	}
}
