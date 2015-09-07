package razeJangal.servers;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import razeJangal.game.Game;
import razeJangal.game.choices.diceCheck;
import razeJangal.game.choices.diceCheckEqu;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.State;
import razeJangal.graphicalServer.components.resultFrame;
import razeJangal.gui.Server;
import razeJangal.gui.protocol.Chat;
import razeJangal.gui.protocol.InvocationVo;

public class MultiServer {
	private int numberOfPlayers;
	public graphicalServer graphical;
	public Server server;
	public Game game;
	private Scanner scanner;
	public ArrayList<State> states;
	public boolean recorded;
	public boolean accepted;
	private File f;
	private FileWriter fstream;

	
	//Constructor
	public MultiServer(){
		states = new ArrayList<State>();
	}
	
	//set the number of the players
	public void setNumberOfPlayers(int numberOfPlayers){
		this.numberOfPlayers = numberOfPlayers;
	}

	//send the chat to the player
	public void sendChat(Chat chat, int player) throws IOException {
		server.send(new InvocationVo("graphicalServer", "recieveChat",new Object[] { chat }), player);
	}

	//send the message to the players that player has seen the goal behind an orange cell
	public void seenOrange(int player)throws IOException {
		graphical.seenOrange(player);
		InvocationVo in = new InvocationVo("graphicalServer", "seenOrange", new Object[] { player });
		for (int i = 0; i < numberOfPlayers; i++){
			if (i == player - 1 && !recorded)
				continue;
			server.send(in, i);
		}
	}
	
	//send the given invocation method to all the players
	public void sendInvocation(InvocationVo in) throws IOException{
		for (int i = 0; i < numberOfPlayers; i++){
			server.send(in, i);}
	}

	//send the message that the player is lost
	public void PlayerLost(int player) throws IOException {
		this.graphical.playerLost(player);
		sendInvocation(new InvocationVo("graphicalServer", "playerLost", new Object[] { player }));
	}
	
	//show the treasure of the orange cell
	public void showBehindOrange(int player, int n, char t) throws IOException {
		if(!recorded){
				server.send(new InvocationVo("graphicalServer", "showBehindOrange", new Object[] { player, n, t }), player - 1);
				try {
					accepted = !server.accept(player - 1);
				} catch (InterruptedException e) {}
			}
	}

	/**
	 * get the name of players
	 * 
	 * @param numberOfPlayers
	 * @return name of the players
	 */
	public String[] getPlayerNames(int numberOfPlayers) {
		String[] names = new String[numberOfPlayers];
		for (int i = 0; i < names.length; i++) {
			names[i] = this.server.reciveNames(i);
		}
		return names;
	}


	//get the right choice of the player
	public int moveCheck(int gamePlayer, diceCheck d, int[] dice) throws IOException, ClassNotFoundException {
		
		showChoicesOfPlayer(gamePlayer, d, dice);
		if(game.autoTurn()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return game.autoFirs(d, dice);
		}
		if (recorded) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			return scanner.nextInt();
		} 
		else {
			while (true) {
				int choice = 0;
				choice = (int) server.recieve(gamePlayer - 1);
				for (int j = 0; j < d.getpossibleMoves().length; j++) {
					for (int i = 0; i < d.getpossibleMoves()[j].length; i++) {
						if (choice == d.getpossibleMoves()[j][i])
							return choice;
					}
				}

				if (d instanceof diceCheckEqu) {
					for (int i = 0; i < ((diceCheckEqu) d).getEmptyOranges().length; i++) {
						if (choice * -1 == ((diceCheckEqu) d).getEmptyOranges()[i])
							return choice;
					}
					for (int i = 0; i < ((diceCheckEqu) d).getViolets().length; i++) {
						if (choice * -1 == ((diceCheckEqu) d).getViolets()[i])
							return choice;
					}
				}
				if (choice == -1000)
					return choice;
				InvocationVo in = new InvocationVo("graphicalServer", "moveError", new Object[] {});
				server.send(in, gamePlayer - 1);
			}
		}
	}


	/**
	 * get the number of the dices for the players
	 * 
	 * @param dice
	 *            default numbers of dices
	 * @param playerName
	 *            name of the player
	 * @return dices for the player
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public int[] getDice(int[] dice, String playerName, int player)throws IOException, ClassNotFoundException {
		int[] newDice = new int[2];
		if(game.autoTurn()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return dice;
			}
		if (recorded) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			newDice[0] = scanner.nextInt();
			newDice[1] = scanner.nextInt();
			return newDice;
		} else {
			while (true) {
				server.send(
						new InvocationVo("graphicalServer", "getDice",
								new Object[] { dice,
										graphical.getPlayers()[player].getName() }),
						player);
				int temp = (int) server.recieve(player);
				newDice[0] = temp;
				temp = (int) server.recieve(player);
				newDice[1] = temp;
				if (newDice[0] >= 1 && newDice[0] <= 6 && newDice[1] >= 1 && newDice[1] <= 6)
					return newDice;
				server.send(new InvocationVo("graphicalServer", "InvalidDice",
						new Object[] {}), player);
			}
		}
	}

	//show that this round's goal is changed to n
	public void showChangedGoal(int roundNum, char n) throws IOException {
		graphical.showChangedGoal(roundNum, n);
		InvocationVo in = new InvocationVo("graphicalServer", "showChangedGoal",
				new Object[] { roundNum, n });
		sendInvocation(in);
	}

	//show the score of the player
	public void showScore(int player, int score) throws IOException {
		this.graphical.showScore(player, score);
		sendInvocation(new InvocationVo("graphicalServer", "showScore",
				new Object[] { player, score }));
	}

	//show the goal of this round
	public void showRoundGoal(int roundNum, char goal) throws IOException {
		this.graphical.showRoundGoal(roundNum, goal);
		InvocationVo in = new InvocationVo("graphicalServer", "showRoundGoal", new Object[] { roundNum, goal });
		sendInvocation(in);
	}

	//show that the player is moved to the new place
	public void showMove(int player, int place) throws IOException {
		InvocationVo in = new InvocationVo("graphicalServer", "movePlayer", new Object[] { place, player - 1 });
		sendInvocation(in);
	}

	//show the message that the player is lost
	public void showLostPlayer(int player) throws IOException {
		sendInvocation(new InvocationVo("graphicalServer", "playerLost",
				new Object[] { player }));
		this.graphical.playerLost(player);
	}

	public State getState(int n) {
		return states.get(n);
	}

	public void addState(State stat) {
		this.states.add(stat);
		try {
			sendInvocation(new InvocationVo("graphicalServer", "nextState", new Object[] {}));
		} catch (IOException e) {
		}

	}

	//play the recorded game 
	public void play(Scanner sc, String name) {
		this.scanner = sc;
		recorded = true;
		server.continueRecorded = true;
		server.showRecorded = true;
		try {
			Play(name);
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
	}

	
	//save the names of the players
	public void save() throws IOException{
		File f = new File("names.txt");
		FileWriter fstream = new FileWriter(f);
		fstream.write(game.getNames().length + " ");
		for (int i = 0; i < game.getNames().length; i++)
			fstream.write(game.getNames()[i] + " ");

		for (int i = 0; i < this.game.getGoals().length; i++)
			fstream.write(game.getGoals()[i] + " ");
		
		fstream.write(game.roundGoal + " ");
		fstream.close();
	}
	public Game g;
	
	
	//send the clients to show the state
	public int[] showState(int gamePlayer) throws IOException, ClassNotFoundException{
		int[] dice = game.getDice();
		sendInvocation(new InvocationVo("graphicalServer", "showPositions",new Object[] { game.positions(), game.getScore(), game.getNames() }));
		dice = getDice(dice, "", gamePlayer - 1);
		fstream.write(dice[0] + " ");
		fstream.write(dice[1] + " ");
		String name;
		if(this.game.autoTurn())
			name = "automatic";
		else
			name= graphical.getPlayers()[gamePlayer - 1].getName();
		sendInvocation(new InvocationVo("graphicalServer", "showDiceNumbers",new Object[] {name, gamePlayer, dice }));
		return dice;
	}
	
	//handle the choice of the player for the first dice
	public void firstChoice(int choice, int gamePlayer) throws IOException, InterruptedException{
		sendInvocation(new InvocationVo("graphicalServer", "SetInvisibles", new Object[]{ }));
		if (choice == -1000) {
			if (recorded)
				game.changeGoal(scanner.next().charAt(0));
			else
				game.changeGoal();
			fstream.write(game.roundGoal + " ");
			return;
		}
		while(recorded && !server.continueRecorded){
			Thread.sleep(1000);
		}
		game.move(gamePlayer, -1 * choice);
		graphical.movePlayer(-1 * choice, gamePlayer - 1);
		game.changeTurn();
		return;
	}
	
	//main loop of the game
	public void Play(String fileName) throws IOException, ClassNotFoundException, InterruptedException {
		server.continueRecorded = true;
		f = new File(fileName);
		fstream = new FileWriter(f);
		this.graphical.set(false);
		save();
		while (!game.isFinished()) {
			int gamePlayer = game.getTurn();
			int[] dice = showState(gamePlayer);
			diceCheck d = game.possibleMoves(dice, gamePlayer);
			String name;
			if(game.autoTurn())
				name = "AutomaticPlayer";
			else
				name = graphical.getPlayers()[gamePlayer - 1].getName();
			addState(new State(game.roundGoal, dice, gamePlayer - 1,name , game.positions(), game.roundNum, true));
			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			int choice = moveCheck(gamePlayer, d, dice);
			sendInvocation(new InvocationVo("graphicalServer", "changeToNormalState", new Object[]{}));
			fstream.write(choice + " ");
			int tmp = 0;
			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			if (choice < 0) {
				firstChoice(choice, gamePlayer);
				continue;
			}
			game.move(gamePlayer, choice);
			boolean guessAgain = game.checkRed(gamePlayer);
			boolean getNextTurn = false;
			if (guessAgain)
				getNextTurn = guessLoop(false, gamePlayer, game, graphical, server, choice, this);
			if (game.isFinished()) {
				break;
			}

			for (int i = 0; i < d.getpossibleMoves().length; i++){
				for (int j = 0; j < d.getpossibleMoves()[i].length; j++){
					if (d.getpossibleMoves()[i][j] == choice){
						tmp = i;
						break;
					}
				}
			}
			int[] board;
		

			board = game.choice(dice[Math.abs(tmp - 1)], gamePlayer);
			State st = new State(game.roundGoal, dice, gamePlayer - 1, name, game.positions(),
					game.roundNum, false);
			st.setSecondDice(dice[Math.abs(tmp - 1)]);
			addState(st);
			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			choice = secondMoveCheck(board, gamePlayer);
			fstream.write(choice + " ");
			sendInvocation(new InvocationVo("graphicalServer", "changeToNormalState", new Object[]{}));
			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			game.move(gamePlayer, choice);
			graphical.movePlayer(choice, gamePlayer - 1);

			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			boolean guess = game.checkRed(gamePlayer);
			if (guess)
				getNextTurn = guessLoop(true, gamePlayer, game, graphical, server,
						choice, this);
			if (getNextTurn)
				continue;
			game.changeTurn();
		}
		
		fstream.close();
		sendInvocation(new InvocationVo("graphicalServer", "showResult", new Object[]{game.getScore(), graphical.names(),graphical.Names(game.winners())}));
		resultFrame showResults = new resultFrame();
		showResults.showRsult(game.getScore(), graphical.names(),
				graphical.Names(game.winners()));
	}

	//show the given method
	public void show(int player, Object[] params, String name) throws IOException{
		for (int i = 0; i < numberOfPlayers; i++){
			if(i == player - 1){
					server.send(new InvocationVo("graphicalServer", "show" + name + "ChoicesOfPlayer" , params), i);
			}else
				server.send(new InvocationVo("graphicalServer", "show" + name+ "ChoicesOfOthers", params), i);
		}
	}
	
	//send the clients to show the choices of the player
	public void showChoicesOfPlayer(int player, diceCheck d, int[] dice) throws IOException{
		show(player, new Object[]{player, d, dice}, "First");
	}
	
	//send the clients to show the second choices of the player
	public void showSecondMove(int[] board, int gamePlayer) throws IOException{
		show(gamePlayer, new Object[] { gamePlayer, board, game.getColors(board) }, "Second");
	}
	
	
	//give and handle the second choices of the player
	public int secondMoveCheck(int[] board, int gamePlayer) throws IOException, ClassNotFoundException {
		int choice = 0;
		showSecondMove(board, gamePlayer);
		if(game.autoTurn()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return game.autoSecond(board);
			}
		if (recorded) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return scanner.nextInt();
		} else {
			while (true) {
				choice = (int) server.recieve(gamePlayer - 1);
				for (int i = 0; i < board.length; i++) {
					if (board[i] == choice)
						return choice;
				}
				server.send(new InvocationVo("graphicalServer", "moveError", new Object[] {}), gamePlayer - 1);
			}
		}
	}

	//guess the treasure
	public boolean guessLoop(boolean hasSevondMove, int player, Game game, graphicalServer gui, Server server, int red, MultiServer ms)
			throws IOException, InterruptedException {
		boolean getNextTurn = false;
		boolean guessAgain = true;
		while (guessAgain && !game.isFinished()) {
			for(int i = 0; i < numberOfPlayers; i++){
				if(i == player - 1 && !recorded)
					server.send(new InvocationVo("graphicalServer", "showInRed",new Object[] { player, game.orangeCells(), red, true , false}),i);
				else
					server.send(new InvocationVo("graphicalServer", "showInRed",new Object[] { player, game.orangeCells(), red, true , true}), i);
			}
			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			int choice = 0;
			if (recorded) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				choice = scanner.nextInt();
			} else {
				try {
					choice = (int) server.recieve(player - 1);
				} catch (ClassNotFoundException e1) {}
			}
			fstream.write(choice + " ");
			sendInvocation(new InvocationVo("graphicalServer", "removeGuess", new Object[] {}));
			while(recorded && !server.continueRecorded){
				Thread.sleep(1000);
			}
			if (choice != 1) {
				State s = new State(game.roundGoal, ms.getState(ms.Statel() - 1).getDiceNumbers(), player - 1, gui.getPlayers()[player - 1].getName(), game.positions(), game.roundNum, hasSevondMove); 
				s.setSecondRed(true);
				ms.addState(s);
				if (!game.rightGuess(choice, player)) {
					getNextTurn = false;
					guessAgain = false;
				} else {
					getNextTurn = true;
				}
			} else {
				guessAgain = false;
			}
		}
		return getNextTurn;
	}

	public int Statel() {
		return states.size();
	}
}
