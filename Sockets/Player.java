package hw13;
/**
 * Player.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D.Double;

/**
 * This class is the goalie to the Pong Soccer game
 * 
 * @author rm7536
 * @author afa2118
 */
public class Player extends Double {
		
	public Player() {
		super();
	}
	
	/**
	 * @param x x position
	 * @param y y position
	 * @param w width
	 * @param h height
	 */
	public Player(double x, double y, double w, double h) {
		super(x, y, w, h);
	}
	
	/**
	 * Resizes the player
	 * 
	 * @param x x position
	 * @param y y position
	 * @param w width
	 * @param h height
	 */
	public void resize(double x, double y, double w, double h ) {
		super.setFrame(x, y, w, h);
	}
	
	/**
	 * Moves the player in the y-axis only 
	 * 
	 * @param y the current location of the mouse to move this player to
	 */
	public void move( double y ) {
		//mouse_offset used so the mouse isn't at the top corner of the player
		this.setFrame( this.getX() , y - StaticVars.MOUSE_OFFSET, this.getWidth(), this.getHeight() );
	}
	
	/**
	 * In charge of controlling the ball bouncing off of the player from the sides.  Can only rebound if the direction of travel of the 
	 * ball is correct for the side of the player it hits.  This is to prevent the ball changing direction after it has 
	 * already gone by the paddle, due to the Epsilon factor that gives wiggle room to the intersection criteria. 
	 * 
	 * @param ball the ball
	 * @param changeX ball's direction of travel
	 * @return boolean for an intersection or not
	 */
	public boolean intersectFront(SoccerBall ball, int changeX) {
		//First check the Right Front
		//first test x axis equality
		if (ball.getX() <= this.getX() + this.getWidth() + StaticVars.EPSILON && 
				ball.getX() >= this.getX() + this.getWidth() - StaticVars.EPSILON && changeX < 0) {
			//then y-axis
			if (ball.getY() + ball.getHeight() >= this.getY() && ball.getY() <= this.getY() + this.getHeight() ) {
				return true;
			}
		}
		//Check the Left Front
		//first test x axis equality
		if (ball.getX() + ball.getWidth() >= this.getX() - StaticVars.EPSILON && 
				ball.getX() + ball.getWidth() <= this.getX() + StaticVars.EPSILON && changeX > 0) {
			//then y-axis
			if (ball.getY() + ball.getHeight() >= this.getY() && ball.getY() <= this.getY() + this.getHeight() ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * In charge of controlling the ball bouncing off of the player from the top or bottom.  Can only rebound if the direction of travel of the 
	 * ball is correct for the side of the player it hits.  This is to prevent the ball changing direction after it has 
	 * already gone by the paddle, due to the Epsilon factor that gives wiggle room to the intersection criteria. 
	 * 
	 * @param ball the ball
	 * @param changeX ball's direction of travel
	 * @return boolean for an intersection or not
	 */
	public boolean intersectSide(SoccerBall ball, int changeY) {
		//first test top-side
		if (ball.getY() + ball.getHeight() >= this.getY() - StaticVars.EPSILON && 
				ball.getY() + ball.getHeight() <= this.getY() + StaticVars.EPSILON && changeY > 0) {
			if (ball.getX() + ball.getWidth() >= this.getX() && ball.getX() <= this.getX() + this.getWidth()) {
				return true;
			}
		}	
		//then test bottom side
		if (ball.getY() <= this.getY() + this.getHeight() + StaticVars.EPSILON && 
				ball.getY() >= this.getY() + this.getHeight() - StaticVars.EPSILON && changeY < 0) {
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
