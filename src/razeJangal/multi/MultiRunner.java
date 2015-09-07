package razeJangal.multi;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import razeJangal.Exceptions.InvalidPlayers;
import razeJangal.game.Game;
import razeJangal.gui.Client;
import razeJangal.gui.mainServer;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;

/**
 * Create the server, clients and game 
 * for running server args: server portNumber
 * for running graphical client: client gui
 * for running console client: client console playername serverIP portNumber
 * @author Pegah Jandaghi
 * 
 */
public class MultiRunner {
	public static void main(String[] args) throws Exception {
		if (args[0].equals("server")) {
			ServerSocket serversocket = null;
			try {
				serversocket = new ServerSocket(Integer.parseInt(args[1]), 50);
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
			int count = 0;
			while (true) {
				count++;
				int n = getNumPlayer();
				gameThread newGame = new gameThread(n, serversocket,
						Integer.parseInt(args[1]), count);
				newGame.start();
			}
		} else {
			Client myClient = null;
			if (args[1].equals("gui")) {
				int n = Integer.parseInt(JOptionPane
						.showInputDialog("Please enter the port number"));
				String IP = JOptionPane
						.showInputDialog("Please enter the IP of the server");
				myClient = new Client(n, IP, false, "gui", "");
			} else {
				String IP = args[3];
				myClient = new Client(Integer.parseInt(args[4]), IP, false,
						"console", args[2]);
			}
			myClient.recieve();
		}
	}

	/**
	 * Get the number of the players
	 * 
	 * @return number of players
	 */
	public static int getNumPlayer() {
		while (true) {
			try {
				System.out.println("How many players are going to play?");
				Scanner sc = new Scanner(System.in);
				int number = sc.nextInt();
				if (number > 4 || number < 2)
					throw new InvalidPlayers("Invalid number for players");
				return number;
			} catch (InvalidPlayers e) {
				System.out.println("The number of players should be 2 - 4");
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
	}
}

/**
 * create a new game
 * 
 * @author Pegah Jandaghi
 * 
 */
class gameThread extends Thread {
	int numerOfPlayers;
	int port;
	ServerSocket serverSocket;
	int gameNumber;

	// constructor
	public gameThread(int numberOfPlayers, ServerSocket serversocket, int port,
			int gameNumber) {
		this.numerOfPlayers = numberOfPlayers;
		this.serverSocket = serversocket;
		this.port = port;
		this.gameNumber = gameNumber;
	}

	public void run() {
		try {
			mainServer ms = new mainServer(numerOfPlayers, serverSocket, port, gameNumber);
		} catch (Exception e) {
		}
	}
}
