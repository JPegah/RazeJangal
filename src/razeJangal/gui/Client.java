package razeJangal.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Scanner;

import razeJangal.game.Game;
import razeJangal.gui.protocol.InvocationVo;
import razeJangal.gui.protocol.X;
import razeJangal.gui.protocol.stateNum;
import razeJangal.servers.MultiServer;
import razeJangal.servers.consoleServer;
import razeJangal.servers.graphicalServer;
/**
 * the client of the game
 * @author Pegah Jandaghi
 *
 */
public class Client implements X {
	private Socket socket;
	private consoleServer Console;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private graphicalServer Graphicalserver;
	private boolean recordedGame;
	private Game g;
	private String server;
	private String name;

	// Constructor
	public Client(int n, String IP, boolean b, String server, String name) throws Exception {
		socket = new Socket(IP, n);
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
		this.recordedGame = b;
		this.server = server;
		this.name = name;
	}

	/**
	 * send the Object to the server
	 * @param object
	 */
	public void send(Object object) {
		try {
			outputStream.writeObject(object);
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * save the map
	 * @param file
	 * @throws IOException
	 */
	public void saveFile(File file) throws IOException {
		Scanner scanner = new Scanner(file);
		File mapFile = new File("Map.txt");
		FileWriter cout = new FileWriter(mapFile);
		while (scanner.hasNext()) {
			cout.write(scanner.next() + " ");
		}
		cout.close();
	}

	/**
	 * recieve the methods for graphical server from the server
	 * @param numberOfPlayers
	 * @throws IOException
	 */
	private void reciveGraphical(int numberOfPlayers) throws IOException {
		this.Graphicalserver = new graphicalServer();
		g.creatGraphic(this.Graphicalserver);
		this.Graphicalserver.gameState = new stateNum();
		this.Graphicalserver.displayingState = new stateNum();
		this.Graphicalserver.client = this;
		this.Graphicalserver.setListeners(recordedGame);
		while (true) {
			InvocationVo in = null;
			try {
				Object object = inputStream.readObject();
				if (object instanceof File) {
					saveFile((File) object);
					continue;
				}
				in = (InvocationVo) object;
			} catch (Exception e1) {
				continue;
			}

			Method[] method = Graphicalserver.getClass().getMethods();
			inner: for (int i = 0; i < method.length; i++) {
				String[] s = method[i].toString().split("[(]");
				String[] s1 = s[0].split("[.]");
				if (s1[s1.length - 1].equals(in.methodName)) {
					try {
						method[i].invoke(Graphicalserver, in.parameters);
						break inner;
					} catch (Exception e) {
					}
					break inner;
				}
			}
		}
	}
	/**
	 *	recive the method invocations for console server frome the server 
	 */
	private void reciveConsole() {
		this.Console = new consoleServer();
		this.Console.setName(this.name);
		this.Console.client = this;

		while (true) {
			InvocationVo in = null;
			try {
				Object o = inputStream.readObject();
				if (o instanceof File) {
					saveFile((File) o);
					continue;
				}
				in = (InvocationVo) o;
			} catch (Exception e1) {
				continue;
			}
			Method[] method = Console.getClass().getMethods();
			inner: for (int i = 0; i < method.length; i++) {
				String[] s = method[i].toString().split("[(]");
				String[] s1 = s[0].split("[.]");
				if (s1[s1.length - 1].equals(in.methodName)) {
					try {
						method[i].invoke(Console, in.parameters);
						break inner;
					} catch (Exception e) {
						e.printStackTrace();
					}
					break inner;
				}
			}
		}

	}

	/**
	 *recieve the inputs from the server 
	 * @throws IOException
	 */
	public void recieve() throws IOException {
		Object object = null;
		boolean notStarted = true;
		while (notStarted) {
			try {
				object = inputStream.readObject();
				notStarted = false;
			} catch (ClassNotFoundException e2) {
			}
		}
		try {
			g = new Game(new MultiServer(), new Integer((int) object), false);
		} catch (Exception e2) {}
		if(this.server.equals("gui"))
			reciveGraphical((int) object);
		else
			reciveConsole();
	}
}
