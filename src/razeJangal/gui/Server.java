package razeJangal.gui;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import razeJangal.game.Game;
import razeJangal.game.choices.diceCheck;
import razeJangal.graphicalServer.ExtraChoicesComponents.ArrowToCell;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalBoardCell;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.State;
import razeJangal.gui.protocol.Chat;
import razeJangal.gui.protocol.InvocationVo;
import razeJangal.gui.protocol.MapRequest;
import razeJangal.gui.protocol.StateRequest;
import razeJangal.gui.protocol.cheat;
import razeJangal.gui.protocol.name;
import razeJangal.gui.protocol.pause;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;
/**
 * the server of the game
 * @author Pegah Jandgahi
 *
 */
public class Server {
	private ServerSocket serversocket;
	private graphicalServer graphical;
	private Game game;
	private Socket[] sockets;
	private ObjectOutputStream[] outPutStreams;
	private ObjectInputStream[] inputStreams;
	private MultiServer multiServer;
	private Object[] lastInputs;
	public  boolean continueRecorded;
	public boolean showRecorded;
	private int turn;
	private boolean stopContinue;
	private boolean notAccepted;
	
	//Constructor
	public Server(ServerSocket serverSocket, int numberOfPlayers, MultiServer multiServer, int port, boolean hasAuto) throws IOException {
		lastInputs = new Object[numberOfPlayers];
		this.multiServer = multiServer;
		serversocket = serverSocket;
		sockets = new Socket[numberOfPlayers];
		inputStreams = new ObjectInputStream[numberOfPlayers];
		outPutStreams = new ObjectOutputStream[numberOfPlayers];
		
		for (int i = 0; i < numberOfPlayers; i++) {
			sockets[i] = serversocket.accept();
			outPutStreams[i] = new ObjectOutputStream(sockets[i].getOutputStream());
			inputStreams[i] = new ObjectInputStream(sockets[i].getInputStream());
			controlInputs controlInputs = new controlInputs(i);
			controlInputs.start();
		}
	}

	//set the game of the server
	public void setGame(Game g) {
		this.game = g;
	}

	//set the board of game
	public void setgBoard(graphicalServer gBoard) {
		this.graphical = gBoard;
	}

	//send the number of players to the clients
	public void sendPlayerNumbers() throws IOException {
		for (int n = 0; n < outPutStreams.length; n++) {
			outPutStreams[n].writeObject(new Integer(outPutStreams.length));
			outPutStreams[n].flush();
		}
	}

	/**
	 *send a method invocation to client 
	 * @param invocation method invocation name
	 * @param client receiver
	 * @throws IOException
	 */
	public void send(InvocationVo invocation, int client) throws IOException {
		outPutStreams[client].writeObject(invocation);
		outPutStreams[client].flush();
	}

	//handle the input when it's not player's turn
	public void notTurn(int player) {
		try {
			send(new InvocationVo("graphicalServer", "turnError",
					new Object[] {}), player);
		} catch (IOException e) {}
	}

	//handle the input objects when playing the recorded game
	public void handleRecord(Object object) throws IOException {
		multiServer.sendInvocation(new InvocationVo("graphicalServer", "changeMode",
				new Object[] {}));
		if (object instanceof pause) {
			continueRecorded = false;
		} else {
			continueRecorded = true;
		}
	}

	//handle the inputs from the clients
	public void handleObject(Object object, int player) throws IOException {
		if (object instanceof cheat) {
			lastInputs[player] = ((cheat) object).getCheat();
			notAccepted = true;
		} else if (object instanceof Chat) {
			game.sendChat((Chat) object, player);
		} else if (object instanceof Integer && player == turn) {
			lastInputs[player] = (Integer) object;
			stopContinue = true;
		} else if (object instanceof StateRequest) {
			int k = ((StateRequest) object).getNow();
			String str = ((StateRequest) object).getRequest();
			if (str.equals("back"))
				k--;
			else
				k++;
			State st = multiServer.getState(k);
			this.send(new InvocationVo("graphicalServer", "showState",
					new Object[] { st }), player);
			send(new InvocationVo("graphicalServer", "changeToNormalState",
					new Object[] {}), player);
			if (st.isSecondRed()) {
			} else if (st.getDisplaying()) {
				diceCheck d = multiServer.game.possibilities(st.getDiceNumbers(),
						st.getPlaces()[st.getPlayerTurn()], st.getPlaces());
				this.send(
						new InvocationVo("graphicalServer",
								"showFirstChoicesOfPlayer", new Object[] {
										st.getPlayerTurn(), d,
										st.getDiceNumbers() }), player);
			} else {
				send(new InvocationVo("graphicalServer",
						"showSecondChoicesOfPlayer", new Object[] {
								player,
								multiServer.game.getChoices(st.getSecondDice(),
										st.getPlaces()[st.getPlayerTurn()]),
								new String[0] }), player);
			}
		} else if (object instanceof MapRequest) {
			send(new File("newMap.txt"), player);
		} else if (object instanceof name) {
			lastInputs[player] = ((name) object).getname();
		} else
			notTurn(player);
	}

	/**
	 *	send the object to the client 
	 * @param object
	 * @param player receiver
	 */
	public void send(Object object, int player) {
		try {
			outPutStreams[player].writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//give the confirm that server is not cheating
	public boolean accept(int client) throws InterruptedException {
		notAccepted = false;
		boolean res = false;
		turn = client;
		while (notAccepted == false) {
			Thread.sleep(1000);
		}
		res = (Boolean) lastInputs[client];
		lastInputs[client] = null;
		return res;
	}

	/**
	 *receive the object from the player 
	 * @param player sender
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Object recieve(int player) throws ClassNotFoundException {
		int n = 0;
		try {
			stopContinue = false;
			turn = player;
			while (stopContinue == false) {
				Thread.sleep(1000);
			}
			n = (Integer) lastInputs[player];
			lastInputs[player] = null;
			return n;
		} catch (Exception e) {
		}
		return n;
	}

	/**
	 * recieve the name of players
	 * @param player sender
	 * @return
	 */
	public String reciveNames(int player) {
		String name = "";
		stopContinue = false;
		while (stopContinue == false && lastInputs[player] == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		name = (String) lastInputs[player];
		lastInputs[player] = null;
		return name;
	}
	
	
	/**
	 *	control the inputs from the clients 
	 * @author pegah jandgahi
	 *
	 */
	class controlInputs extends Thread {
		int number;

		public controlInputs(int n) {
			this.number = n;
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {}
				try {
					Object o = inputStreams[number].readObject();
					if (showRecorded)
						handleRecord(o);
					else
						handleObject(o, number);
				} catch (ClassNotFoundException | IOException e) {}
			}
		}
	}
}
