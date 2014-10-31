package hw13;
/**
 * GameServer.java  
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.net.*;
import java.io.*;

/**
 * GameServer makes a new ServerSocket and waits for a connection, then it opens the input and output Reader and Writer 
 * streams and starts the Pong game for the server.
 * 
 * @author rm7536
 * @author afa2118
 */
public class GameServer {

	ServerSocket 	aServerSocket;
	static int		port     = 10002;

	/**
	 * Constructor starts the ServerSocket
	 */
	public GameServer() {
		try { 
			aServerSocket = new ServerSocket(port);
			System.out.println ("Listening on port: " + aServerSocket.getLocalPort());
        } 
		catch(Exception e) {
            System.out.println(e);
		    e.printStackTrace();
        }
	}
	
	/**
	 * This method connects to a client, opens the read/write streams, and starts the game
	 */
	public void startGame()	{
		try {
			Socket clnt = aServerSocket.accept();
			System.out.println(clnt.toString());
			PrintWriter out = new PrintWriter (clnt.getOutputStream (), true);
			BufferedReader in = new BufferedReader( new InputStreamReader( clnt.getInputStream()));
			 
			new GameView("Soccer Pong!", out, in, true);
			
			//out.close();
			//in.close();
			//clnt.close();
		} 
		catch(Exception e) {
			System.out.println(e);
		    e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * This method statically parses any inputs for the port to use, and stores it into
	 * the instance variable prior to startGame
	 * 
	 * @param args passed in on command line
	 */
	private static void parseArgs(String args[]) {
		boolean goodSyntax = false;
		for (int i = 0; i < args.length; i ++) {	
			if (args[i].equals("-p")) {
				port = new Integer(args[++i]).intValue();
				goodSyntax = true;
			}
		}
		//If the user specified command line argument without -p, this will print
		if (!goodSyntax) {
			System.out.println("Correct usage: java GameServer [-p portnumber]");
			System.exit(1);
		}
	}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ( args.length > 0 ) {
			parseArgs(args);
		}
		new GameServer().startGame();
	}
}

