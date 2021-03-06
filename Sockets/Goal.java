package hw13;
/**
 * Goal.java   
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
import java.awt.geom.Rectangle2D.Double;

/**
 * Goal is a class in charge of the Goal net, which is a Swing Rectangle
 * @author rm7536
 * @author afa2118
 */
public class Goal extends Double {

	//for printing the outline of the goal
	BasicStroke myStroke;
	
	/**
	 * Constructor for the superclass Double
	 */
	public Goal() {
		super();
		myStroke = new BasicStroke( StaticVars.STROKE_WIDTH );
	}
	
	/**
	 * @param x x position
	 * @param y y position
	 * @param w width
	 * @param h height
	 */
	public Goal(double x, double y, double w, double h) {
		super(x, y, w, h);
		myStroke = new BasicStroke( StaticVars.STROKE_WIDTH );
	}
	
	/**
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
	 * If I was still on the track this I would have been going to ithaca in like 2 weeks
	 * @param ball soccer ball
	 * @return
	 */
	public boolean intersectRightFront(SoccerBall ball, int changeX) {
		//first test x axis equality
		if (ball.getX() + ball.getWidth() <= this.getX() + this.getWidth() + StaticVars.EPSILON && 
				ball.getX() + ball.getWidth() >= this.getX() + this.getWidth() - StaticVars.EPSILON 
				&& changeX < 0) {
			//then y-axis
			if (ball.getY() + ball.getHeight() >= this.getY() && ball.getY() <= this.getY() + this.getHeight() ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If the ball touches the front of a goal then its a goal
	 * 
	 * @param changeX the direction the ball is traveling when it intersects
	 * @param ball soccer ball
	 * @return boolean whether it is a goal or not
	 */
	public boolean intersectLeftFront(SoccerBall ball, int changeX) {
		//first test x axis equality
		if (ball.getX() >= this.getX() - StaticVars.EPSILON && 
				ball.getX() <= this.getX() + StaticVars.EPSILON && changeX > 0) {
			//then y-axis
			if (ball.getY() + ball.getHeight() >= this.getY() && ball.getY() <= this.getY() + this.getHeight() ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If the ball touches the sides of the goal its not a score, but needs to change course .
	 * @param ball the soccer ball
	 * @param changeY the direction the ball is traveling when it intersects
	 * @return boolean whether it touched the side or not
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
	
	public void setStroke(int size) {
		myStroke = new BasicStroke( size );
	}
	
	/**
	 * Paints the goal net
	 * 
	 * @param g2d
	 */
	public void paint(Graphics2D g2d) {
		Color savedColor = g2d.getColor();
		Stroke savedStroke = g2d.getStroke();
		g2d.setColor( StaticVars.BALL_COLOR );
		g2d.setStroke( myStroke );
		g2d.draw(this);
		g2d.setColor( savedColor );
		g2d.setStroke( savedStroke );
	}

}
