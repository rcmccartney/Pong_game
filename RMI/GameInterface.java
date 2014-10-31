package hw14;

/**
 * GameInterface.java  
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.rmi.RemoteException;

/**
 * This interface is what methods the server and client can call on each other
 * 
 * @author rm7536
 * @author afa2118
 */
public interface GameInterface extends java.rmi.Remote {
      
	void startGame(String hostName, int port) throws RemoteException;

	void resize(int width, int height) throws RemoteException;

	void moveBall(double nextX, double nextY) throws RemoteException;
	
	void moveOther(double loc) throws RemoteException;
	
	void quitIt() throws RemoteException;

	void updateScore(String player) throws RemoteException;
}
