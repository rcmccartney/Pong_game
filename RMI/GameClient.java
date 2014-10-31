package hw14;

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
import java.rmi.Naming;

/**
 * GameClient looks up the server in the rmiregistry, then makes a new game and binds himself 
 * to his registry as well.  After this, he tells the server to look him up in the registry and start
 * the game.  This allows for 2-way communications  
 * 
 * @author rm7536
 * @author afa2118
 */
public class GameClient {

	static String hostName = ""; 
    static int port = 1099;
    
	/**
	 * Default constructor, gets server from registry, binds itself, then tells server to start game
	 */
	public GameClient() {
		try {
			GameInterface server = (GameInterface) 
					Naming.lookup("//" + hostName + "/Controller1");
			GameInterface client = new SoccerController(server, false);
			hostName = InetAddress.getLocalHost().getHostAddress();
			System.out.println("About to bind");
			Naming.rebind("//" + hostName + "/Controller2", client);
            System.out.println("GameClient bound in registry");
            server.startGame(hostName, port);
		} 
		catch (Exception e) {
			System.out.println( "GameClient exception: " + e.getMessage() );
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
		new GameClient(); 
	}
}