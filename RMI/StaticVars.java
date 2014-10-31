package hw14;

/**
 * StaticVars.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */
import java.awt.Color;

/**
 * StaticVars is an interface that holds most of the variables in the game, so that they can be easily 
 * modified to improve the look and feel of the game.
 * 
 * @author rm7536
 * @author afa2118
 */

public interface StaticVars {
	
	public static final int DEFAULT_WIDTH = 1200; //Width of screen
	public static final int DEFAULT_HEIGHT = 700; //Height of screen
	public static final int MIN_WIDTH = 400; //Width of screen
	public static final int MIN_HEIGHT = 333; //Height of screen
	public static final int START_X = 150; //Starting x position
	public static final int START_Y = 100; // Starting y position
	public static final Color BACKGROUND = Color.GREEN;	//Background color of soccer field
	public static final Color BALL_COLOR = Color.WHITE;	//Color of soccer ball
	public static final Color PLAYER_COLOR = Color.BLACK; //Color of player/paddle	
	public static final int DEFAULT_INTERVAL = 7;  //Interval the timer clicks
	public static final int ROTATE_AMOUNT = 10; //Rotation speed of ball
	public static final int DEFAULT_BALL_MOVE = 4; //Interval the ball moves
	public static final int STROKE_WIDTH = 10; 
	public static final int EPSILON = 2; 
	public static final int MOUSE_OFFSET = 50; 
	public static final int FONT_SIZE = 40;
	public static final String FONT = "Serif";

}
