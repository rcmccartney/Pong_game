package hw13;
/**
 * GameClient.java  
 * 
 * Version:
 * $Id:$
 * 
 * Revisions:
 *$Log:$
 */

import java.io.*;
import java.net.*;

/**
 * GameClient makes a new Socket to the already started GameServer, opens the input and output Reader and Writer streams,
 * and then starts the Pong game for the client.
 * 
 * @author rm7536
 * @author afa2118
 */
public class GameClient {

	static String hostName = ""; 
    static int port = 10002;
    
	/**
	 * Default constructor, does nothing but make an instance of this class.
	 */
	public GameClient() {
	}

	/**
	 * This method makes the Socket, based off of hostname and port which can be default or can be 
	 * passed in on the command line
	 */
	public void startGame()       {
		try {
			System.out.println("host: " +  hostName );
			System.out.println("port: " +  port );
			Socket s = new Socket(hostName, port);
			PrintWriter out = new PrintWriter( s.getOutputStream(), true);
			BufferedReader in = new BufferedReader( new InputStreamReader( s.getInputStream()));
			new GameView("Soccer Pong!", out, in, false);
    	} 
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);
        }	
	}
	
	/**
	 * This method statically parses any inputs for hostname and port, and stores them into
	 * the instance variables prior to startGame
	 * 
	 * @param args passed in on command line
	 */
	private static void parseArgs(String args[]) {
		boolean goodSyntax = false;
		for (int i = 0; i < args.length; i ++) {
			if (args[i].equals("-h")) {
				hostName = args[++i];
				goodSyntax = true;
			}
			else if (args[i].equals("-p")) {
				port = new Integer(args[++i]).intValue();
				goodSyntax = true;
			}
		}
		//If the user specified command line argument without -p or -h, this will print
		if (!goodSyntax) {
			System.out.println("Correct usage: java GameClient [-h hostname] [-p portnumber]");
			System.exit(1);
		}
	}
	
	/**
	 * @param args for hostname or port to not be default
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			parseArgs(args);
		}
		else {
			try {
				//default is to start on this computer
				hostName = InetAddress.getLocalHost().getHostAddress();
			} 
			catch (UnknownHostException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		new GameClient().startGame(); 
	}
}