package razeJangal.gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import razeJangal.game.Game;
import razeJangal.gui.protocol.InvocationVo;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;
/**
 * create the components for the game
 * @author Pegah Jandaghi
 *
 */
public class mainServer {
	private MultiServer multiServer;
	
	//constructor
	public mainServer(int numberOfPlayers, ServerSocket serverSocket, int port, int gameNumber) throws Exception {
		multiServer = new MultiServer();
		multiServer.setNumberOfPlayers(numberOfPlayers);		
		multiServer.graphical = new graphicalServer();
		boolean auto = false;
		multiServer.server = new Server(serverSocket, numberOfPlayers, multiServer, port, auto);
		multiServer.setNumberOfPlayers(numberOfPlayers);
		multiServer.game = new Game(multiServer, numberOfPlayers, auto);
		multiServer.server.setGame(multiServer.game);
		multiServer.game.creatGraphic(multiServer.graphical);
		multiServer.server.setgBoard(multiServer.graphical);
		multiServer.server.sendPlayerNumbers();
		multiServer.sendInvocation(new InvocationVo("graphicalServer", "getName", new Object[]{}));
		String[] names = multiServer.getPlayerNames(numberOfPlayers);
		multiServer.game.setNames(names);
		multiServer.graphical.setPlayers(multiServer.game.getNames().length, multiServer.game.getNames());
		multiServer.graphical.showRoundGoal(multiServer.game.roundNum, multiServer.game.roundGoal);
		InvocationVo in = new InvocationVo("graphicalServer", "setPlayers", new Object[]{multiServer.game.getNames().length, multiServer.game.getNames()});
		multiServer.sendInvocation(in);
		in = new InvocationVo("graphicalServer", "showRoundGoal",  new Object[]{multiServer.game.roundNum, multiServer.game.roundGoal});
		multiServer.sendInvocation(in);
		multiServer.Play("out"+gameNumber+".txt");
	}
}
