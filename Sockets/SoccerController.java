package hw13;
/**
 * SoccerContoller.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is the controller of the game.  It listens for all user input, and takes appropriate action.  It is also
 * responsible for sending the messages between the client and the server
 * 
 * @author rm7536
 * @author afa2118
 */

public class SoccerController extends Timer implements ComponentListener, KeyListener, MouseMotionListener, WindowListener {

	private JFrame _frame;
	private JPanel _panel;
	private Goal _net1, _net2;
	private Player _p1, _p2;
	private SoccerBall _ball;
	private Scoreboard _score;
	private PrintWriter _out; 
	private BufferedReader _in;
	private boolean _isServer;
	private int _changeX = StaticVars.DEFAULT_BALL_MOVE;
	private int	_changeY = StaticVars.DEFAULT_BALL_MOVE;
	private double nextX, nextY;
	private Pattern resizePattern, ballPattern;
	
	/**
	 * 
	 * @param interval for the timer to move the ball
	 * @param aPanel JPanel
	 * @param p1 left player
	 * @param p2 right player
	 * @param ball soccer ball
	 * @param net1 left goal
	 * @param net2 right goal
	 * @param score scoreboard of the game
	 * @param aFrame JFrame that comprises the view
	 * @param out output stream for server/client interaction
	 * @param in input stream for server/client interaction
	 * @param isServer whether this class is acting for the server or client
	 */
	public SoccerController(int interval, JFrame aFrame, JPanel aPanel, Player p1, Player p2, SoccerBall ball, Goal net1, 
								Goal net2, Scoreboard score, PrintWriter out, BufferedReader in, boolean isServer) {
		super(interval, null);
		_frame = aFrame;
		_panel = aPanel;
		_p1 = p1; 
		_p2 = p2;
		_ball = ball;
		_net1 = net1;
		_net2 = net2;
		_score = score;
		_out = out;
		_in = in;
		_isServer = isServer;
		ballPattern = Pattern.compile("Y");
		resizePattern = Pattern.compile("H");
		if (_isServer) {
			//this listener takes care of the timer for this class
			this.addActionListener( new BallListener() );
		}
		//this thread exists solely to listen for user input and take appropriate action
		new SocketListener().start();
	}
	
	
	public void keyReleased(KeyEvent e) { } 
	public void keyTyped(KeyEvent e) { } 
	/**
	 * 	User must press spacebar to launch the ball moving.
	 */
	public void keyPressed(KeyEvent e) { 
		if (e.getKeyChar() == 32) {
			//only the server can move the ball
			if (_isServer) {
				this.start();
			}
		}
	}
	
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) { }
	public void componentHidden(ComponentEvent e) { }
	/**
	 *  When the window is resized (which only a server's user can do directly with the mouse), all the components must be 
	 *  resized to maintain proportionality.  Otherwise, this method is called when the client receives the
	 *  command to resize his window from the server, in which case he resizes the JFrame and this method executes
	 */
	public void componentResized(ComponentEvent e) {
		
		int panel_width = _panel.getWidth();
		int panel_height = _panel.getHeight();
		int goal_width = panel_width / 20 + 2;
		int goal_height = panel_height / 2;
		int p_width = goal_width / 4;
		int p_height = panel_height / 8;
		int p_offset = goal_width / 6;
		int fontSize = _panel.getWidth() / 30;
		double ball_width = goal_width / 2;
		
		//nets are resized
		_net1.resize(0, panel_height / 2 - goal_height / 2, goal_width, goal_height );
		_net2.resize( panel_width - goal_width , panel_height / 2 - goal_height / 2, 
						goal_width, goal_height );
		//size of the net outline is resized
		_net1.setStroke( _frame.getWidth() / 120);
		_net2.setStroke( _frame.getWidth() / 120);
		//players and ball are resized
		_p1.resize(goal_width + p_offset, panel_height / 2 - p_height / 2, p_width, p_height );
		_p2.resize(panel_width - goal_width - p_offset - p_width, panel_height / 2 - p_height / 2, 
						p_width, p_height );
		_ball.setSize(ball_width, ball_width);
		//ball is placed back at the center
		nextX = ( ( _panel.getX() + _panel.getWidth() ) / 2 ) - _ball.getWidth() / 2;
		nextY = ( ( _panel.getY()+ _panel.getHeight() ) / 2 ) - _ball.getHeight() / 2;
		_ball.setLocation(nextX, nextY);
		//changes the size of the scoreboard
		_score.setFont( new Font(StaticVars.FONT, 1, fontSize) );
		//the server needs to tell the client to relocate the ball as well
		if (_isServer) {
			_out.println(nextX + "Y" + nextY);
			//Server tells the client to resize himself
			_out.println("R" + _frame.getWidth() + "H" + _frame.getHeight() );
		}
		_panel.repaint();
	}
	
	public void mouseDragged(MouseEvent e) { } 
	/**
	 * The only thing the client does is tell the server he has moved his mouse, so that the server can move his
	 * P1 location.  The server does everything else, but also needs to tell the client that he has moved his player's 
	 * position, not for functionality but for visual feedback to the client's player
	 */
	public void mouseMoved(MouseEvent e) { 
		if (_isServer) {
			_p2.move( e.getY() );
			//M means this is a mouse command
			_out.println( "M" + e.getY() );
		}
		else {
			//this is the only command the client sends, no need to preface it with 'M'
			_p1.move( e.getY() );
			_out.println( e.getY() );		
		}
		_panel.repaint();
	}
	
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	/**
	 * If either the server or the client closes the JFrame, they tell the other one they close it and 
	 * both exit the game.
	 */
	public void windowClosing(WindowEvent e) {
		_out.println("E");
		System.exit(0);
	}

	
	/**
	 * BallListener inner class for handling the timer movement of the ball, as well as any intersections of the model
	 * Only the server makes this class, so only the server tests for intersections of the ball with the model and then
	 * prints the score appropriately.  The client does not need to know about this, he only moves his paddle
	 * 
	 * @author rm7536
	 * @author afa2118
	 *
	 */
	private class BallListener implements ActionListener {
		
		/**
		 * Looks for any intersections with the other pieces of the model, and if so takes appropriate action
		 */
		public void actionPerformed(ActionEvent e) {
			if ( _p1.intersectFront(_ball, _changeX) || _p2.intersectFront(_ball, _changeX) ) {
				//change direction in x axis
				_changeX *= -1;
				nextX = _ball.getX() + _changeX;
				nextY = _ball.getY() + _changeY;
			}
			else if ( _p1.intersectSide(_ball, _changeY) || _p2.intersectSide(_ball, _changeY) 
						|| _net1.intersectSide(_ball, _changeY) || _net2.intersectSide(_ball, _changeY)) {
				//change direction in y axis
				_changeY *= -1;
				nextX = _ball.getX() + _changeX;
				nextY = _ball.getY() + _changeY;
			}
			else if ( _net1.intersectRightFront(_ball, _changeX) ) {
				//score occurred
				SoccerController.this.stop();
				nextX = ( ( _panel.getX() + _panel.getWidth() ) / 2 ) - _ball.getWidth() / 2;
				nextY = ( ( _panel.getY()+ _panel.getHeight() ) / 2 ) - _ball.getHeight() / 2;
				_score.updateP2Score();
				//S for score, let the client know to update his scoreboard as well
				_out.println( "S2" );
			}
			else if ( _net2.intersectLeftFront(_ball, _changeX) ) {
				SoccerController.this.stop();
				nextX = ( ( _panel.getX() + _panel.getWidth() ) / 2 ) - _ball.getWidth() / 2;
				nextY = ( ( _panel.getY()+ _panel.getHeight() ) / 2 ) - _ball.getHeight() / 2;
				_score.updateP1Score();
				//S for score, let the client know to update his scoreboard as well
				_out.println( "S1" );
			}
			//normal move with no intersections (except possible the side, where the ball needs to change directions)
			else {
				nextX = _ball.getX() + _changeX;
				nextY = _ball.getY() + _changeY;
				if( nextX < _ball.getMinBoundX() ) {
					_changeX *= -1;
					nextX = _ball.getMinBoundX();
				}
				else if( nextX > _ball.getMaxBoundX() ) {
					_changeX *= -1;
					nextX = _ball.getMaxBoundX();
				}
				else if( nextY < _ball.getMinBoundY() ) {
					_changeY *= -1;
					nextY = _ball.getMinBoundY();
				}
				else if( nextY > _ball.getMaxBoundY() ) {
					_changeY *= -1;
					nextY = _ball.getMaxBoundY();
				}
			}
			//after any kind of move or score, the balls location is updated and sent to the client
			_ball.setLocation(nextX, nextY);
			//we know this is the server, so print to the client
			_out.println(nextX + "Y" + nextY);
			_panel.repaint();
		}
	}
		
	/**
	 * This inner class is a thread that listens for any messages coming across the network and takes appropriate 
	 * action
	 * 
	 * @author rm7536
	 * @author afa2118
	 */
	private class SocketListener extends Thread {
		
		String command;
		
		SocketListener() {
			//set it to Daemon so that this thread will not prevent the JVM from terminating
			this.setDaemon(true);
		}
		
		public void run() {
			int i = 0;
			//infinite loop of listening for commands
			while(true) {
				try {
					if ( ( command = _in.readLine() ) != null) {
						
						//E means one player quit, the game exits
						if ( command.substring(0, 1).equals( "E" ) ) {
							System.exit(0);
						}
						
						if (_isServer) {
							//if this is the server the only command from the client will be position of 
							//his paddle
							_p1.move( Double.parseDouble(command) );
						}
						//the client receives more commands for mouse, score, and ball movement
						else {
							//this is a mouse command, meaning move the paddle
							if (command.substring(0, 1).equals("M") ) {
								_p2.move( Double.parseDouble( command.substring(1) ) );
							}
							//this is a score command, update your scoreboard
							else if (command.substring(0, 1).equals("S") ) {
								if (command.substring(1).equals("1") ) {
									_score.updateP1Score();
								}
								else {
									_score.updateP2Score();
								}
							}
							//This is a resize screen command, resize your screen to match the server's screen
							else if (command.substring(0, 1).equals("R") ) {
								String[] items = resizePattern.split( command.substring(1) );
								_frame.setSize( Integer.parseInt( items[0] ), 
										Integer.parseInt( items[1] ) );
							}
							//if it starts with an integer, this is a ball movement command
							else {
								String[] items = ballPattern.split(command);
								_ball.setLocation( Double.parseDouble( items[0] ), 
										Double.parseDouble( items[1] ) );
							}
						}
					}
				}
				catch (NumberFormatException e) {
					System.out.println(e);
					e.printStackTrace();
				}
				catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}
				_panel.repaint();
			}
		}
	}
}
