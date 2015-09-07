package razeJangal.record;

import java.io.File;
import java.net.ServerSocket;
import java.util.Scanner;

import razeJangal.game.Game;
import razeJangal.gui.Server;
import razeJangal.gui.protocol.InvocationVo;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;


class PlayRecorded {
	public PlayRecorded(String fileName, int port, int Viewers, ServerSocket s) throws Exception {
		MultiServer g = new MultiServer();
		g.graphical = new graphicalServer();
		File f = new File("names.txt");
		Scanner sc = new Scanner(f);
		int numberOfPlayers = sc.nextInt();
		String[] names = new String[numberOfPlayers];
		for (int i = 0 ; i < numberOfPlayers; i++){
			names[i] = sc.next();
		}
		
		char[] trs = new char[13];
		for (int i = 0; i < trs.length; i++){
			trs[i] = sc.next().charAt(0);
		}
		char goal = sc.next().charAt(0);
		g.setNumberOfPlayers(Viewers);
		g.game = new Game(g, numberOfPlayers, trs, goal);
		g.server = new Server(s, Viewers, g, port, false);
		g.server.setGame(g.game);
		g.game.creatGraphic(g.graphical);
		g.server.setgBoard(g.graphical);
		g.game.setNames(names);
		g.graphical.setPlayers(names.length, names);
		g.game.roundGoal = goal;
		g.graphical.showRoundGoal(g.game.roundNum, g.game.roundGoal);
		g.server.sendPlayerNumbers();
		InvocationVo in = new InvocationVo("graphicalServer", "setPlayers", new Object[]{g.game.getNames().length, g.game.getNames()});
		g.sendInvocation(in);
		in = new InvocationVo("graphicalServer", "showRoundGoal",  new Object[]{g.game.roundNum, g.game.roundGoal});
		g.sendInvocation(in);
		
		sc.close();
		g.play(new Scanner(new File(fileName)), "outPut.txt");
	}

}