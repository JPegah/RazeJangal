package razeJangal.game;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import razeJangal.game.board.Board;
import razeJangal.game.choices.diceCheck;
import razeJangal.game.choices.diceCheckEqu;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.State;
import razeJangal.gui.Server;
import razeJangal.gui.automaticPlayer;
import razeJangal.gui.protocol.Chat;
import razeJangal.multi.MultiRunner;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;

/**
 * 
 * @author Pegah Jandaghi(90105978) contains rules of the game and functions to
 *         play
 */
public class Game implements Serializable{
	private boolean hasAuto;
	private transient graphicalServer gui;
	private Board board;
	public int roundNum;
	public char roundGoal;
	private int turn;
	private int numPlayers;
	private int[] treasures;
	final static char[] tr = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M' };
	private char[] foundTre;
	private int foundGoals;
	public automaticPlayer auto;
	private String[] names;
	private MultiServer server;

	/**
	 * constructor
	 * 
	 * @param console
	 * @throws Exception 
	 */
	public Game(MultiServer server, int n, boolean auto) throws Exception {
		this.server = server;
		if(auto){
			this.auto = new automaticPlayer(server, n, n, this);
			n++;
		}
		this.hasAuto = auto;
		set(n);
	}
	
	//set the information of the game
	private void set(int n){
		this.roundNum = 1;
		this.turn = 1;
		this.setTreasure();
		this.roundGoal = this.getFirstTreasure();
		this.foundGoals = 0;
		char[] tmp = null;
		this.setNumPlayers(n, tmp);
		treasures = this.board.getOrange();
	
	}
	
	//Constructors
	public Game(int n){
		set(n);
	}
	
	public Game(MultiServer server, int n, char[] trs, char goal) throws Exception {
		this.server = server;
		this.roundNum = 1;
		this.turn = 1;
		this.setTreasure();
		this.roundGoal = goal;
		this.foundGoals = 0;
		this.setNumPlayers(n, trs);
		treasures = this.board.getOrange();
	}
	
	//send the chat
	public void sendChat(Chat message, int player){
		message.setFrom(names[player]);
		for (int i = 0; i < names.length; i++){
			if (names[i].equals(message.getTo()))
				try {
					server.sendChat(message, i);
				} catch (IOException e) {}
			if(message.getTo().equals("All"))
				try {
					server.sendChat(message, i);
				} catch (IOException e) {
			}
		}
	}
	
	//set the information of the graphical board
	public void creatGraphic(graphicalServer gui){
		this.gui = gui;
		this.gui.setViolet(board.getViolet());
		this.gui.setArrow(board.getArrows());
		this.gui.setPine(board.getcypress());
		this.gui.setPositions(board.getExactPositions());
		this.gui.setNumViolet(board.getNumberOfViolets());
		this.gui.setNumOrange(board.getOrange().length);
		this.gui.setBlueCell(board.getBlue());
		this.gui.setFrame(board.getNumberOfCells(), "" + this.roundGoal, this.board.board, treasures);
		this.gui.setCardPlace(board.getLabel()[0], board.getLabel()[1]);
	}

	
	public int[] orangeCells(){
		return this.board.getOrange();
	}

	/**
	 * set the treasures for this game randomly
	 */
	private void setTreasure() {
		this.foundTre = new char[13];
		for (int i = 0; i < 13; i++) {
			this.foundTre[i] = tr[i];
		}
	}

	/**
	 * @return first round goal
	 */
	private char getFirstTreasure() {
		Random r = new Random();
		int temp = r.nextInt(13);
		char firstTreasure = tr[temp];
		return firstTreasure;
	}

	/**
	 * go to next round
	 */
	private void nextRound() {
		this.roundNum++;
	}

	/**
	 * give turn to next player
	 */
	public void changeTurn() {
		this.turn %= this.numPlayers;
		this.turn += 1;
	}

	/**
	 * change goal of current round
	 * @throws IOException 
	 */
	public void changeGoal() throws IOException {
		Random random = new Random();
		int k = random.nextInt(13 - this.foundGoals);
		this.roundGoal = this.foundTre[k];
		this.server.showChangedGoal(this.roundNum, this.roundGoal);
		changeTurn();
	}

	public void changeGoal(char ch) throws IOException{
		roundGoal = ch;
		server.showChangedGoal(this.roundNum, this.roundGoal);
	}
	/**
	 * set the number of players and make a board
	 * 
	 * @param n
	 *            number of player
	 */
	public void setNumPlayers(int n, char[] goals) {
		board = new Board(n, goals);
		this.numPlayers = n;
	}
	
	public void setNames(String[] names){
		if(this.hasAuto){
			this.names = new String[names.length + 1];
			for (int i = 0; i < names.length; i++)
				this.names[i] = names[i];
			this.names[names.length] = this.auto.getName();
		}
		else
		this.names = names;
	}
	public boolean checkRed(int player){
		return this.board.checkInRed(player);
	}


	/**
	 * check if the game is finished
	 * 
	 * @return the game is finished or not
	 */
	public boolean isFinished() {
		if (this.roundNum == 14)
			return true;
		return false;
	}

	/**
	 * @return players positions
	 */
	public int[] positions() {
		int[] positions = this.board.getPositions();
		return positions;
	}

	/**
	 * get the player whose it is the turn
	 * 
	 * @return player number
	 */
	public int getTurn() {
		return this.turn;
	}

	/**
	 * move the player to the new place and check the new place
	 * 
	 * @param player
	 *            player number
	 * @param place
	 *            new position to move
	 * @return if the new position is red cell
	 * @throws IOException 
	 */
	public boolean move(int player, int place) throws IOException {
		this.board.changePlace(player, place);
		server.accepted = true;
		server.showMove(player, place);
		boolean ans = false;
		int newPos = place;
		int i = this.checkLost(player);

		if (i != 0) 
			this.server.PlayerLost(i);

		if (isOrange(newPos)) {
			server.accepted = true;
			while(server.accepted && !server.recorded&& !this.autoTurn()){
				server.showBehindOrange(player, newPos, this.board.getTreasure(newPos));
			}
			server.seenOrange(player);
		}
		return ans;
	}

	/**
	 * find possible moves for each dice number
	 * 
	 * @param dice
	 *            dice numbers
	 * @param player
	 *            player number
	 * @return possible moves
	 */
	public diceCheck possibleMoves(int[] dice, int player) {
		diceCheck d = null;
		if (isLucky(dice)){
			d = new diceCheckEqu();
		}else
		d = new diceCheck();
		
		d.setFirstMoves(choice(dice[0], player), choice(dice[1], player));
		d.setColors(getColors(d.getpossibleMoves()[0]),getColors(d.getpossibleMoves()[1]));
		d.setLucky(isLucky(dice));
		if (d instanceof diceCheckEqu){
			((diceCheckEqu) d).setEmptyOranges(this.getEmptyOranges(this.positions()));
			int[] tmp = new int[this.board.getViolet().size()];
			for (int i = 0; i < tmp.length; i++){
				tmp[i] = this.board.getViolet().get(i);
			}
			((diceCheckEqu) d).setViolets(tmp);
		}
		return d;
	}
	
	public diceCheck possibles(int[] dice, int place){
		diceCheck d = null;
		if (isLucky(dice))
			d = new diceCheckEqu();
		else
			d = new diceCheck();
		
		d.setFirstMoves(board.poss(dice[0], place), board.poss(dice[1], place));
		d.setColors(getColors(d.getpossibleMoves()[0]),getColors(d.getpossibleMoves()[1]));
		d.setLucky(isLucky(dice));
		if (d instanceof diceCheckEqu){
			int[] tmp = new int[this.board.getViolet().size()];
			for (int i = 0; i < tmp.length; i++){
				tmp[i] = this.board.getViolet().get(i);
			}
			((diceCheckEqu) d).setViolets(tmp);
		}
		return d;
	}
	public diceCheck possibilities(int[] dice , int place, int[] players){
		diceCheck d = possibles(dice, place);
		if (d instanceof diceCheckEqu){
			((diceCheckEqu)d).setEmptyOranges(this.getEmptyOranges(players));
		}
		return d;
	}

	/**
	 * number of dices
	 * 
	 * @return dice numbers
	 */
	public int[] getDice() {
		int[] diceNum = this.board.Dice();
		return diceNum;
	}

	/**
	 * check if the guess for the red cell is right
	 * 
	 * @param position
	 *            index of chosen treasure
	 * @param player
	 *            player number
	 * @return credit of guess
	 * @throws IOException 
	 */
	public boolean rightGuess(int position , int player) throws IOException {
		server.accepted = true;
		while(server.accepted && !server.recorded){
			server.showBehindOrange(player, position, this.board.getTreasure(position));
		}
		server.seenOrange(player);
		
		// for last round
		if (this.board.getTreasure(position) == this.roundGoal) {
			if (this.foundGoals == 12) {
				this.foundGoals++;
				return true;
			}

			int j = 0;
			for (; j < 13; j++) {
				if (this.roundGoal == this.foundTre[j]) {
					break;
				}
			}
			if (j >= 13) {
				move(player, 0);
				server.PlayerLost(player);
				return false;
			}

			this.nextRound();
			
			this.board.won(player);
			this.server.showScore(player, this.board.getScore(player));

			Random random = new Random();
			this.swap(this.foundTre, j, 13 - this.foundGoals - 1);
			this.foundGoals++;
			int k = random.nextInt(13 - this.foundGoals);
			this.roundGoal = this.foundTre[k];
			server.showRoundGoal(this.roundNum, this.roundGoal);
			return true;
		}

		this.board.changePlace(player, 0);
		server.PlayerLost(player);
		return false;
	}

	/**
	 * check if any of the players is lost
	 * @param player player number
	 * @return players moved to blue cell
	 */
	private int checkLost(int player) {
		for (int i = 1; i <= this.numPlayers; i++) {
			if (i != player)
				if (board.lost(player, i))
					return i;
		}
		return 0;
	}

	/**
	 * swap 2 elements of an array
	 * 
	 * @param a
	 *            array
	 * @param element1
	 * @param element2
	 */
	private void swap(char[] array, int element1, int element2) {
		char temp = array[element1];
		array[element1] = array[element2];
		array[element2] = temp;
	}

	/**
	 * get the colors of the given cells
	 * 
	 * @param cells
	 *            array of cells
	 * @return color cells
	 */
	public String[] getColors(int[] cells) {
		String[] colors = this.board.getColors(cells);
		return colors;
	}

	/**
	 * get the possible choices for the cell with the initial place and the
	 * number of moves
	 * 
	 * @param n
	 *            length of move
	 * @param player
	 *            player number
	 * @return possible cells to move
	 */
	public int[] choice(int n, int player) {
		int[] a = this.board.possibleChoices(n, player);
		return a;
	}
	
	public int[] getChoices(int n, int place){
		int[] a = this.board.poss(n, place);
		return a;
	}

	/**
	 * check if the number belongs to an orange cell
	 * 
	 * @param n
	 *            number if cell
	 * @return
	 */
	public boolean isOrange(int n) {
		for (int i = 0; i < 13; i++) {
			if (n == this.treasures[i])
				return true;
		}
		return false;
	}

	/**
	 * get goal behind orange cell
	 * 
	 * @param n
	 *            orange cell position
	 * @return the goal behind orange cell
	 */
	private char getOrangeGoal(int n) {
		char res = this.board.getTreasure(n);
		return res;
	}

	/**
	 * check if the numbers of the dice are equal
	 * 
	 * @param a
	 *            dice numbers
	 * @return
	 */
	private boolean isLucky(int[] dice) {
		if (dice[0] == dice[1])
			return true;
		return false;
	}

	/**
	 * get the places of orange cells which are empty
	 * 
	 * @return empty orange cells
	 */
	private int[] getEmptyOranges(int[] pos) {
	//	int[] pos = this.positions();
		int[] counter = new int[pos.length];
		int j = 0;
		for (int i = 0; i < pos.length; i++) {
			if (isOrange(pos[i])) {
				counter[j] = pos[i];
				j++;
			}
		}
		int index = 0;
		int[] res = new int[13 - j];
		outer: for (int i = 0; i < 13; i++) {
			for (int k = 0; k < j; k++) {
				if (this.treasures[i] == counter[k])
					continue outer;
			}
			res[index] = this.treasures[i];
			index++;
		}
		return res;
	}
	
	/**
	 * get the score of players
	 * @return scores of the players
	 */
	public int[] getScore(){
		int[] scores = new int[this.numPlayers];
		for (int i = 0; i < scores.length; i++)
			scores[i] = this.board.getScore(i + 1);
		return scores;
	}
	
	/**
	 * the winner of the game
	 * @return
	 */
	public int[] winners(){
		int[] res = new int[this.board.winner().size()];
		for (int i = 0; i < res.length; i++){
			res[i] = this.board.winner().get(i);
		}
		return res;
	}
	
	public String[] getNames(){
		return names;
	}
	public char[] getGoals(){
		return board.getGoals();
	}
	
	public int autoFirs(diceCheck d, int[] dice){
		return this.auto.showFirstChoicesOfPlayer(0, d, dice);
	}
	
	public int autoSecond(int[] cells){
		return this.auto.showSecondChoicesOfPlayer(0, cells, this.getColors(cells));
	}
	
	// returns true if it's automatic players turn
	public boolean autoTurn(){
		if(!hasAuto)
			return false;
		if(turn == numPlayers)
			return true;
		return false;
	}
}
