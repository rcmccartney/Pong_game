package hw14;

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
import java.net.UnknownHostException;
import java.io.*;
import java.rmi.*;

/**
 * GameServer binds a Soccer controller and waits for a connection.  
 * 
 * @author rm7536
 * @author afa2118
 */
public class GameServer {

	static int port = 1099;
	static String hostName = "";
	
	/**
	 * Constructor binds the controller to the rmiregistry
	 */
	public GameServer() {
		
		try {
			GameInterface obj = new SoccerController(null, true);
			Naming.rebind("//" + hostName + "/Controller1", obj);
            System.out.println("GameServer bound in registry at " + hostName);
		} 
		catch (Exception e) {
			System.out.println( "GameServer error: " + e.getMessage() );
			System.out.println( "Did you run 'rmiregistry' first then 'java GameServer'?" );
			System.exit(1);
		}
	}
	
	/**
	 * This method statically parses any inputs for the port to use, and stores it into
	 * the instance variable prior to the constructor
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
	 * @param args port number to use
	 */
	public static void main(String[] args) {
		if ( args.length > 0 ) {
			parseArgs(args);
		}
		try {
			hostName = InetAddress.getLocalHost().getHostAddress();
		} 
		catch (UnknownHostException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		new GameServer();
	}
}

