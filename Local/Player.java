package hw12;


/**
 * Player.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JPanel;

/**
 * @author rm7536
 * @author afa2118
 */
public class Player extends Double {
	
	private JPanel _panel;
	private int _score;
	
	public Player(JPanel aPanel) {
		super();
		_panel = aPanel;
	}
	
	/**
	 * @param x x position
	 * @param y y position
	 * @param w width
	 * @param h height
	 */
	public Player(double x, double y, double w, double h) {
		super(x, y, w, h);
		_score = 0;
	}
	
	/**
	 * increases the score
	 */
	public void score() {
		_score++;
	}
	
	/**
	 * Gets the score
	 * @return the score
	 */
	public int getScore() {
		return _score;
	}
	
	/**
	 * Resizes the graphics
	 * @param x x position
	 * @param y y position
	 * @param w width
	 * @param h height
	 */
	public void resize(double x, double y, double w, double h ) {
		super.setFrame(x, y, w, h);
	}
	
	/**
	 * Moves the player up
	 * @param y the y position
	 */
	public void moveUp( int y ) {
		int nextY =  (int) this.getY() - y;
		if (nextY < this.getMinBoundY()) {
			nextY = this.getMinBoundY();
		}
		this.setFrame( this.getX() , nextY, this.getWidth(), this.getHeight() );
	}
	
	/**
	 * Moves the player down
	 * @param y the y position
	 */
	public void moveDown( int y ) {
		int nextY =  (int) this.getY() + y;
		if ( nextY > this.getMaxBoundY() ) {
			nextY = this.getMaxBoundY();
		}
		this.setFrame( this.getX() , nextY, this.getWidth(), this.getHeight() );	}
	
	/**
	 * Moves the player
	 * @param e
	 */
	public void move(MouseEvent e) {
		this.setFrame( this.getX() , e.getY() - 50, this.getWidth(), this.getHeight() );
	}
	
	/**
	 * The minimum bound of the player
	 * @return the position boundary
	 */
	private int getMinBoundY() {
		return _panel.getY();
	}
	
	/**
	 * The maximum bound of the player
	 * @return the position boundary
	 */
	private int getMaxBoundY() {
		return (int) (_panel.getY() + _panel.getHeight() - this.getHeight() );
	}
	
	/**
	 * In charge of controlling the ball bouncing off the right plyaer
	 * @param ball the ball
	 * @return
	 */
	public boolean intersectRightFront(SoccerBall ball) {
		//first test x axis equality
		if (ball.getX() <= this.getX() + this.getWidth() + StaticVars.EPSILON && 
				ball.getX() >= this.getX() + this.getWidth() - StaticVars.EPSILON && ball.getChangeX() < 0) {
			//then y-axis
			if (ball.getY() + ball.getHeight() >= this.getY() && ball.getY() <= this.getY() + this.getHeight() ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * In charge of controlling the ball bouncing off the left player
	 * @param ball the ball
	 * @return
	 */
	public boolean intersectLeftFront(SoccerBall ball) {
		//first test x axis equality
		if (ball.getX() + ball.getWidth() >= this.getX() - StaticVars.EPSILON && 
				ball.getX() + ball.getWidth() <= this.getX() + StaticVars.EPSILON && ball.getChangeX() > 0) {
			//then y-axis
			if (ball.getY() + ball.getHeight() >= this.getY() && ball.getY() <= this.getY() + this.getHeight() ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Bounces the ball off the sides of the player
	 * @param ball the ball
	 * @return
	 */
	public boolean intersectSide(SoccerBall ball) {
		//first test top-side
		if (ball.getY() + ball.getHeight() >= this.getY() - StaticVars.EPSILON && 
				ball.getY() + ball.getHeight() <= this.getY() + StaticVars.EPSILON && ball.getChangeY() > 0) {
			if (ball.getX() + ball.getWidth() >= this.getX() && ball.getX() <= this.getX() + this.getWidth()) {
				return true;
			}
		}	
		//then test bottom side
		if (ball.getY() <= this.getY() + this.getHeight() + StaticVars.EPSILON && 
				ball.getY() >= this.getY() + this.getHeight() - StaticVars.EPSILON && ball.getChangeY() < 0) {
			if (ball.getX() + ball.getWidth() >= this.getX() && ball.getX() <= this.getX() + this.getWidth()) {
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * Paints the player
	 * @param g2d
	 */
	public void paint(Graphics2D g2d) {
		Color savedColor = g2d.getColor();
		g2d.setColor( StaticVars.PLAYER_COLOR );
		g2d.fill(this);
		g2d.setColor( savedColor );
	}
}
