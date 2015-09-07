package razeJangal.servers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

import razeJangal.game.choices.diceCheck;
import razeJangal.game.choices.diceCheckEqu;
import razeJangal.graphicalServer.components.resultFrame;
import razeJangal.gui.Client;
import razeJangal.gui.protocol.Chat;
import razeJangal.gui.protocol.X;
import razeJangal.gui.protocol.cheat;
import razeJangal.gui.protocol.name;
/**
 * read the user inputs from the console and send them to server
 * @author pegah Jandaghi
 *
 */
public class consoleServer {	
	private  Scanner scan;
	public X client;
	private boolean continueGame;
	private String readInput;
	private String name;
	private Integer lastChoice;
	

	//Constructor
	public consoleServer(){
		seenOranges = new HashMap<>();
		this.lastChoice = new Integer(-1);
		this.scan = new Scanner(System.in);
		Interruption in = new Interruption();
		in.start();
	}
	
	//handle the inputs from the server
	public class Interruption extends Thread {
		public void run(){	
			while(true){
				String str = scan.next();
				if (str.equals("#")){
					System.out.println("Please enter the name you want to chat with:");
					String s = scan.next();
					System.out.println("you can start chatting with" + " " + s + ": ");
					String txt = scan.nextLine();
					while(!txt.equals("#")){
						Chat chat = new Chat(s, txt);
						((Client)client).send(chat);
						txt  = scan.nextLine();
					}
					continue;
				}
				if (!continueGame)
					readInput = str;
				else
					((Client)client).send(str);
			}
			}
	}


	//get the dice numbers from the user
	public int[] getDice(int[] dice, String playerName) {
		continueGame = false;
		System.out.printf("Dice Numbers for %s are: %d & %d", playerName, dice[0], dice[1]);
		int[] newDice = new int[2];
		newDice[0] = Integer.parseInt(getInput());
		((Client) client).send(newDice[0]);
		newDice[1] = Integer.parseInt(getInput());
		((Client)client).send(newDice[1]);
		return newDice;			
	}
	
	//show the positions of the player
	public void showPositions(int[] positions, int[] scores, String[] names){
		System.out.println("Current Positions: ");
		for (int i = 0; i < positions.length; i++){
			System.out.printf("Player%d(%s) -> %d\t(Score: %d)\n", i + 1,names[i], positions[i], scores[i]);
		}
	}
	
	
	/**
	 * show choices of player 
	 * @param player player number
	 * @param d possible moves of dices
	 * @param diceNumbers number of dices
	 * @param treasures empty orange cells
	 * @param violetCell place of violet cell
	 */
	public void showFirstChoicesOfPlayer(int player, diceCheck d, int[] diceNumbers){
		lastChoice = 1;
		showCells(d.getpossibleMoves()[0], d.getColors()[0], 0, diceNumbers[1]);
		if (d instanceof diceCheckEqu) {
			for (int i = 0; i < ((diceCheckEqu) d).getEmptyOranges().length; i++) 
				System.out.printf("%d. Move to %d(Orange)\n", i + 1 + d.getpossibleMoves()[0].length,((diceCheckEqu) d).getEmptyOranges()[i]);
			
			for (int i = 0; i < ((diceCheckEqu) d).getViolets().length; i++)
				System.out.printf("%d. Move to %d(Violet)\n", d.getpossibleMoves()[0].length + 1 + ((diceCheckEqu) d).getEmptyOranges().length, ((diceCheckEqu) d).getViolets()[i]);
			System.out.printf("%d. Change current round's goal treasure\n", d.getpossibleMoves()[0].length + 1 + ((diceCheckEqu) d).getEmptyOranges().length +  ((diceCheckEqu) d).getViolets().length);
			System.out.flush();
			
			int choice = Integer.parseInt(getInput());
			
			if (choice > d.getpossibleMoves()[0].length) {
				if (choice <= d.getpossibleMoves()[0].length + ((diceCheckEqu) d).getEmptyOranges().length)
					((Client)client).send(-1 * ((diceCheckEqu) d).getEmptyOranges()[choice - d.getpossibleMoves()[0].length - 1]);
				else if  (choice == 1 + d.getpossibleMoves()[0].length + ((diceCheckEqu) d).getEmptyOranges().length+ ((diceCheckEqu) d).getViolets().length)
					((Client)client).send(-1000);
				else
					((Client)client).send(-1 * ((diceCheckEqu) d).getViolets()[choice - d.getpossibleMoves()[0].length - ((diceCheckEqu) d).getEmptyOranges().length - 1]);
			}
			else
				((Client)client).send(d.getpossibleMoves()[0][choice -1]);
			return;
		}
		
		showCells(d.getpossibleMoves()[1], d.getColors()[1], d.getpossibleMoves()[0].length, diceNumbers[0]);
		int choice = Integer.parseInt(getInput());			
		if (choice <= d.getpossibleMoves()[0].length) 
			((Client)client).send(d.getpossibleMoves()[0][choice - 1]);
		else 
			((Client)client).send(d.getpossibleMoves()[1][choice - d.getpossibleMoves()[0].length - 1]);
		return ;
	}
	
	//move the player to the place
	public void movePlayer(int player, int place){
		if(lastChoice != -1){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			((Client)client).send(new cheat(true));
			}
		lastChoice = -1;
	}
	
	//show the cells which are choices of other players
	public void showFirstChoicesOfOthers(int player, diceCheck d, int[] diceNumbers){
		showCells(d.getpossibleMoves()[0], d.getColors()[0], 0, diceNumbers[1]);
		
		if (d instanceof diceCheckEqu) {
			for (int i = 0; i < ((diceCheckEqu) d).getEmptyOranges().length; i++) {
				System.out.printf("%d. Move to %d(Orange)\n", i + 1 + d.getpossibleMoves()[0].length,((diceCheckEqu) d).getEmptyOranges()[i]);
			}
			for (int i = 0; i < ((diceCheckEqu) d).getViolets().length; i++)
				System.out.printf("%d. Move to %d(Violet)\n", d.getpossibleMoves()[0].length + 1 + ((diceCheckEqu) d).getEmptyOranges().length, ((diceCheckEqu) d).getViolets()[i]);
			System.out.printf("%d. Change current round's goal treasure\n", d.getpossibleMoves()[0].length + 1 + ((diceCheckEqu) d).getEmptyOranges().length +  ((diceCheckEqu) d).getViolets().length);
			System.out.flush();
		}
		else
			showCells(d.getpossibleMoves()[1], d.getColors()[1], d.getpossibleMoves()[0].length, diceNumbers[0]);
		return ;
	}
	
	
	/**
	 * show the possible moves
	 * @param cells possible cells
	 * @param colors possible cells colors
	 * @param init first cell to show
	 * @param dice dice number
	 */
	public  void showCells(int[] cells, String[] colors, int init, int dice){
		for (int i = 0; i < cells.length; i++) {
			System.out.printf("%d. ", i + 1 + init);
			if (colors[i].equals("green")) {
				System.out.printf("Move to %d, and then move %d cells\n", cells[i],dice);
			} else if(colors[i].equals("violet"))
				System.out.printf("Move to %d(Violet), and then move %d cells\n", cells[i],dice);
			else if(colors[i].equals("red"))
				System.out.printf("Move to %d(Red), and then move %d cells\n", cells[i],dice);
				
			else
				System.out.printf("Move to %d(Orange), and then move %d cells\n", cells[i],dice);
		}
	}
	
	
	/**
	 * make an array with given elements
	 * @param element1
	 * @param element2
	 * @return array
	 */
	private int[] fillArray(int element1, int element2){
		int[] newArray = new int[2];
		newArray[0] = element1;
		newArray[1] = element2;
		return newArray;
	}
	
	/**
	 * show choices for player(second dice)
	 * @param player player number
	 * @param cells position of possible moves
	 * @param colors color of possible cells
	 * @return the chosen cell number
	 */
	public int showSecondChoicesOfPlayer(int player, int[] cells, String[] colors) {
		showSecondChoicesOfOthers(player, cells, colors);
		int res = Integer.parseInt(getInput());
		((Client)client).send(cells[res - 1]);
		lastChoice = 1;
		return cells[res - 1];
	}

	//show the choices of other players
	public void showSecondChoicesOfOthers(int player, int[] cells, String[] colors) {
		System.out.printf("Player %d's Choices(Second Dice):\n", player);
		for (int i = 0; i < cells.length; i++) {
			System.out.printf("%d. Move to %d", i + 1, cells[i]);
			if (colors[i].equals("orange"))
				System.out.println("(Orange)");
			else if (colors[i].equals("red"))
				System.out.println("(Red)");
			else if (colors[i].equals("violet"))
				System.out.println("(Violet)");
			else
				System.out.println("");
		}
		System.out.flush();
	}
	
	/**
	 * show dices for the player 
	 * @param player player number
	 * @param dice dice numbers
	 */
	public void showDiceNumbers(String playerName, int player, int[] dice) {
		System.out.printf("Dice Numbers for player %d (%s): %d %d\n", player, playerName, dice[0], dice[1]);
		System.out.printf("Player %d's(%s) Choices:\n", player, playerName);
	}

	
	/**
	 * show this round's goal
	 * @param roundNum number of round
	 * @param goal goal of round
	 */
	public void showRoundGoal(int roundNum, char goal) {
		System.out.printf("Round number %d started, this round's goal treasure is %c\n", roundNum, goal);
	}

	
	/**
	 * show this round's goal if the goal is changed
	 * @param roundNum
	 * @param goal new goal
	 */
	public void showChangedGoal(int roundNum, char goal) {
		System.out.printf("Round number %d, this round's goal treasure has changed to %c\n", roundNum, goal);
	}

	
	/**
	 * show if any of the players is back to blue cells
	 * @param player player number
	 */
	public void playerLost(int player) {
		System.out.printf("player %d is moved to Blue cells.\n", player);
	}


	/**
	 * show the possible choices for red cell
	 * @param n  player number
	 * @param a
	 * @param red orange cells
	 * @return guess of player
	 */
	public void showInRed(int n, int[] a, int red, boolean v, boolean b) {
		System.out.printf("Player %d is in %d(Red), and can attempt to guess goal treasure's place:\n", n, red);
		System.out.println("1. I don't know");
		for (int i = 0; i < a.length; i++) {
			System.out.printf("%d. %d\n", i + 2, a[i]);
		}
		System.out.flush();
		if(b)
			return;
		int res = Integer.parseInt(getInput());
		continueGame = false;
		if(res == 1)
			((Client) client).send(1);
		else{
			((Client) client).send(a[res - 2]);
		}
	}
	
	//show the result of the game
	public void showResult(int[] scores, String[] players, ArrayList<String> winners){
		System.out.println("The End");
		System.out.println("Winners: ");
		for (int i = 0; i < winners.size(); i++)
			System.out.print(winners.get(i) + "\t");
		System.out.print("\nScores: \n");
		for (int i = 0; i < players.length; i++){
			System.out.println(players[i] + "............." + scores[i]);
		}
	}


	/**
	 * show if the player has won this round's treasure
	 * @param player player number
	 * @param goal round goal
	 */
	public void showRoundWinner(int player, char goal) {
		System.out.printf("Player %d has won this round's goal treasure, %c\n", player, goal);
	}

	private HashMap<Integer, Character> seenOranges;
	/**
	 *  show the behind of the orange cell
	 * @param player player number
	 * @param cell position of orange cell
	 * @param tresure the goal of orange cell
	 * @throws InterruptedException 
	 */
	public  void showBehindOrange(int player, int cell, char tresure) throws InterruptedException {
		if(!this.seenOranges.isEmpty() && this.seenOranges.keySet().contains(cell)){
			char c = this.seenOranges.get(cell);
			Thread.sleep(1000);
			if(c == tresure){
				((Client)client).send(new cheat(true));
			}else{
				((Client)client).send(new cheat(false));
				return;
			}
		}else{
			this.seenOranges.put(cell, tresure);
			((Client)client).send(new cheat(true));
		}
		System.out.printf("Cell %d's treasure as seen by player %d is %c\n", cell, player, tresure);
	}
	
	//show the message that the player has seen the treasure of the cell
	public void seenOrange(int player){
		System.out.printf("Player %d has seen behind the goal of the cell\n", player);
	}
	
	//show that dice numbers are not between 1 & 6
	public void InvalidDice(){
		System.out.println("Invalid dice number! you should choose a number between 1 to 6");
	}
	
	//show that the player can not do any action because it's another's turn
	public void turnError(){
		System.out.println("It is not your turn now & you can't do any action");
	}
	
	//show the invalid choice of the player
	public void moveError(){
		System.out.println("you can not move to that cell");
	}

	//read the inputs of the player
	public String getInput(){
		continueGame = false;
		while(readInput == null || readInput.length() == 0){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		String res = readInput;
		readInput = "";
		continueGame = true;
		return res;
	}
	
	//get the name of the player
	public void getName(){
		((Client)client).send(new name(this.name));
	}
	
	//recive the chat message from others
	public void recieveChat(Chat caht){
		String message = caht.getFrom();
		message += ": ";
		message += caht.getText();
		System.out.println(message);
	}

	//set the name of the player
	public void setName(String name) {
		this.name = name;
	}
}

