package hw14;

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
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is the controller of the game.  It listens for all user input, and takes appropriate action.  It is also
 * responsible for calling the methods between the client and the server
 * 
 * @author rm7536
 * @author afa2118
 */

public class SoccerController extends UnicastRemoteObject implements ComponentListener, KeyListener, 
							MouseMotionListener, WindowListener, GameInterface {

	private Timer _timer;
	private JFrame _frame;
	private JPanel _panel;
	private Goal _net1, _net2;
	private Player _p1, _p2;
	private SoccerBall _ball;
	private Scoreboard _score;
	private GameInterface _opponent;
	private boolean _isServer;
	private int _changeX = StaticVars.DEFAULT_BALL_MOVE;
	private int	_changeY = StaticVars.DEFAULT_BALL_MOVE;
	private double nextX, nextY;
	
	/**
	 * 
	 * @param interval for the timer to move the ball
	 * @param isServer whether this class is acting for the server or client
	 */
	public SoccerController( GameInterface opponent, boolean isServer) throws RemoteException {
		_net1 = new Goal();
		_net2 = new Goal();
		_p1 = new Player();
		_p2 = new Player();
		_ball = new SoccerBall(StaticVars.DEFAULT_WIDTH / 40, StaticVars.DEFAULT_WIDTH / 40);
		_score = new Scoreboard();
		_panel = new PongPanel(_p1, _p2, _ball, _net1, _net2, _score);
		_frame = new GameView("Soccer Pong!", _panel, this, isServer);
		_opponent = opponent;
		_isServer = isServer;
		if (_isServer) {
			//this listener takes care of the timer for this class
			_timer = new Timer(StaticVars.DEFAULT_INTERVAL, null);
			_timer.addActionListener( new BallListener() );
		}
	}
	
	/**
	 * Called by the client to let the server know he is bound to the registry and the game
	 * can start
	 */
	public void startGame(String hostName, int port) throws RemoteException {
		
		try {
			_opponent = (GameInterface) 
					Naming.lookup("//" + hostName + "/Controller2");
		}	
		catch (Exception e) {
			System.out.println( "GameClient lookup exception: " + e.getMessage() );
			e.printStackTrace();
		}
		_frame.setVisible(true);
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
				_timer.start();
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
			try {
				_opponent.resize( _frame.getWidth(), _frame.getHeight() );
				_opponent.moveBall( nextX, nextY );
			}
			catch (Exception f) {
				System.out.println( "Resize exception: " + f.getMessage() );
				f.printStackTrace();
			}
		}
		_panel.repaint();
	}
	
	/**
	 * Server calls this method to let the client know to resize his window
	 */
	public void resize(int aWidth, int aHeight) throws RemoteException {
		_frame.setSize(aWidth, aHeight);
		_panel.repaint();
	}
	
	/**
	 * moveBall is called by the server to tell the client to move his ball
	 */
	public void moveBall( double nextX, double nextY ) throws RemoteException {
		_ball.setLocation(nextX,  nextY);
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
			//server moves p2, tells client to move his p2
			_p2.move( e.getY() );
			try {
				_opponent.moveOther( e.getY() );
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		else {
			//client moves p1, tells server to move his p1
			_p1.move( e.getY() );
			try {
				_opponent.moveOther( e.getY() );
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}		
		}
		_panel.repaint();
	}
	
	/**
	 * The client and the server call this on each other to move the other's goalie that they control
	 */
	public void moveOther(double loc) throws RemoteException {
		if (_isServer) {
			_p1.move( loc );
		}
		else {
			_p2.move( loc );
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
		try {
			_opponent.quitIt();
		} catch (RemoteException e1) { }
		System.exit(0);
	}

	/**
	 * Server or client calls this to tell the other to quit
	 */
	public void quitIt() throws RemoteException {
		System.exit(0);
	}
	
	/**
	 * Server calls this on the client to tell him to update his score (only the server
	 * is keeping track of the ball movements and looking for scores)
	 */
	public void updateScore(String player) throws RemoteException {
		if ( player.equals("1") ) {
			_score.updateP1Score();
		}
		else {
			_score.updateP2Score();
		}
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
				_timer.stop();
				nextX = ( ( _panel.getX() + _panel.getWidth() ) / 2 ) - _ball.getWidth() / 2;
				nextY = ( ( _panel.getY()+ _panel.getHeight() ) / 2 ) - _ball.getHeight() / 2;
				_score.updateP2Score();
				//S for score, let the client know to update his scoreboard as well
				try {
					_opponent.updateScore("2");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			else if ( _net2.intersectLeftFront(_ball, _changeX) ) {
				_timer.stop();
				nextX = ( ( _panel.getX() + _panel.getWidth() ) / 2 ) - _ball.getWidth() / 2;
				nextY = ( ( _panel.getY()+ _panel.getHeight() ) / 2 ) - _ball.getHeight() / 2;
				_score.updateP1Score();
				//S for score, let the client know to update his scoreboard as well
				try {
					_opponent.updateScore("1");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
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
			try {
				_opponent.moveBall( nextX, nextY);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} 
			_panel.repaint();
		}
	}
}
		
		
						

						
