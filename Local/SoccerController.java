package hw12;



/**
 * SoccerContoller.java   
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author rm7536
 * @author afa2118
 */

public class SoccerController extends Timer implements ComponentListener, KeyListener, MouseMotionListener  {

	private JPanel _panel;
	private Goal _net1, _net2;
	private Player _p1, _p2;
	private SoccerBall _ball;
	private Scoreboard _score;
	
	/**
	 * 
	 * @param interval
	 * @param aPanel JPanel
	 * @param p1 left player
	 * @param p2 right player
	 * @param ball soccer ball
	 * @param net1 left goalie
	 * @param net2 right goalie
	 * @param score score of the game
	 */
	public SoccerController(int interval, JPanel aPanel, Player p1, Player p2, SoccerBall ball, Goal net1, 
								Goal net2, Scoreboard score) {
		super(interval, null);
		_panel = aPanel;
		_p1 = p1; 
		_p2 = p2;
		_ball = ball;
		_net1 = net1;
		_net2 = net2;
		_score = score;
		this.addActionListener( new BallListener() );
	}
	
	
	public void keyReleased(KeyEvent e) { } //ActionListener for the release of a key
	public void keyTyped(KeyEvent e) { } //ActionListener for typing of key
	public void keyPressed(KeyEvent e) { 
		if (e.getKeyChar() == 32) {
			this.start();
		}
		if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
			_p1.moveUp( StaticVars.DEFAULT_MOVE );
			_panel.repaint();
		}
		else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
			_p1.moveDown( StaticVars.DEFAULT_MOVE );
			_panel.repaint();
		}
	}
	
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) { }
	public void componentHidden(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) {
		
		int panel_width = _panel.getWidth();
		int panel_height = _panel.getHeight();
		int goal_width = panel_width / 20 + 2;
		int goal_height = panel_height / 2;
		int p_width = goal_width / 4;
		int p_height = panel_height / 8;
		int p_offset = goal_width / 6;
		double ball_width = goal_width / 2;
		
		_net1.resize(0, panel_height / 2 - goal_height / 2, goal_width, goal_height );
		_net2.resize( panel_width - goal_width , panel_height / 2 - goal_height / 2, 
						goal_width, goal_height );
		_p1.resize(goal_width + p_offset, panel_height / 2 - p_height / 2, p_width, p_height );
		_p2.resize(panel_width - goal_width - p_offset - p_width, panel_height / 2 - p_height / 2, 
						p_width, p_height );
		_ball.setSize(ball_width, ball_width);
		_panel.repaint();
	}
	
	public void mouseDragged(MouseEvent e) { } //Actionlistener for mouse
	public void mouseMoved(MouseEvent e) { 
		_p2.move(e);
		_panel.repaint();
	}
	
	/**
	 * BallListener inner class
	 * @author rm7536
	 * @author afa2118
	 *
	 */
	private class BallListener implements ActionListener {
		
		
		public void actionPerformed(ActionEvent e) {
			if ( _p1.intersectRightFront(_ball) || _p2.intersectLeftFront(_ball) ||
					_p2.intersectRightFront(_ball) || _p1.intersectLeftFront(_ball)) {
				_ball.move(-1,1);
				_ball.rotate(StaticVars.ROTATE_AMOUNT);
			}
			else if ( _p1.intersectSide(_ball) || _p2.intersectSide(_ball) 
						|| _net1.intersectSide(_ball) || _net2.intersectSide(_ball)) {
				_ball.move(1,-1);
				_ball.rotate(StaticVars.ROTATE_AMOUNT);
			}
			else if ( _net1.intersectRightFront(_ball) ) {
				_p2.score();
				SoccerController.this.stop();
				_ball.reset();
				_score.updateScore();
			}
			else if ( _net2.intersectLeftFront(_ball) ) {
				_p1.score();
				SoccerController.this.stop();
				_ball.reset();
				_score.updateScore();
			}
			else {
				_ball.move(1,1);
				_ball.rotate(StaticVars.ROTATE_AMOUNT);
			}
			_panel.repaint();
		}
	}
}
