package razeJangal.game.board;

import razeJangal.game.board.cells.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Pegah Jandaghi(90105978) consist of cells & players & dices
 */
public class Board implements Serializable{
	private int[][] positions;
	private Player[] players;
	public BoardCell[] board;
	private DicePool dice;
	private int[] treasures;
	private int numberOfOrange;
	private int numberOfViolets;
	private ArrayList<int[]> blueCells;
	private ArrayList<int[]> Arrows;
	private ArrayList<int[]> cypressPositions;
	private ArrayList<Integer> violetCell;
	private ArrayList<Integer> redCell;
	final static char[] goals = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M' };
	private int numberOfCells;

	private int[] label;

	
	public char[] getGoals(){
		char[] goals = new char[treasures.length];
		for (int i = 0; i < treasures.length; i++){
			goals[i] = ((orangeCell) board[treasures[i]]).getTreasure();
		}
		return goals;
	}
	/**
	 * constructor
	 * 
	 * @param numPlayer
	 *            number of players
	 * @throws
	 */
	public Board(int numPlayer, char[] trs) {
		this.dice = new DicePool();
		this.players = new Player[numPlayer];
		for (int i = 0; i < numPlayer; i++) {
			players[i] = new Player();
		}

		treasures = new int[13];
		violetCell = new ArrayList<Integer>();
		redCell = new ArrayList<Integer>();
		cypressPositions = new ArrayList<int[]>();
		Arrows = new ArrayList<int[]>();
		blueCells = new ArrayList<int[]>();

		// read map from file
		File f = new File("newMap.txt");
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		numberOfCells = sc.nextInt();
		int index = 0;
		char[] treasure;
		if (trs == null)
			treasure = setTreasure();
		else
			treasure = trs;
		
		board = new BoardCell[numberOfCells];
		positions = new int[numberOfCells][];
		for (int j = 0; j < numberOfCells; j++) {
			int n = sc.nextInt();
			String color = sc.next();
			int number = sc.nextInt();
			if (color.equals("Orange")) {
				numberOfOrange++;
				board[n] = new orangeCell(n, treasure[index]);
				treasures[index] = n;
				index++;
			} else if (color.equals("Violet")) {
				board[n] = new violetCell(n);
				violetCell.add(n);
				setNumberOfViolets(getNumberOfViolets() + 1);
			} else if (color.equals("Red")) {
				board[n] = new redCell(n);
				redCell.add(n);
			} else if (color.equals("Blue"))
				board[n] = new blueCell();
			else
				board[n] = new greenCell(n);
			for (int i = 0; i < number; i++) {
				board[n].setNeighbor(sc.nextInt());
			}
			positions[n] = new int[2];
			positions[n][1] = sc.nextInt();
			positions[n][0] = sc.nextInt();

			if (color.equals("Blue")) {
				blueCells.add(positions[n]);
			}
			if (color.equals("Orange") || color.equals("Violet")) {
				int[] tmp = new int[2];
				tmp[0] = sc.nextInt();
				tmp[1] = sc.nextInt();
				if (color.equals("Orange")) {
					Arrows.add(numberOfOrange - 1, tmp);
					int[] cypress = new int[2];
					cypress[0] = sc.nextInt();
					cypress[1] = sc.nextInt();
					cypressPositions.add(cypress);
				} else {
					Arrows.add(tmp);
				}
			}
		}

		int numBlue = sc.nextInt();
		for (int i = 0; i < numBlue; i++) {
			int[] tmp = new int[2];
			tmp[0] = sc.nextInt();
			tmp[1] = sc.nextInt();
			this.blueCells.add(tmp);
		}

		label = new int[2];
		label[0] = sc.nextInt();
		label[1] = sc.nextInt();
		for (int i = 0; i < this.treasures.length; i++){
			System.out.printf("%d -> %c   ", this.treasures[i], this.goals[i]);
		}
	}

	/**
	 * get orange cells positions
	 * 
	 * @return positions of orange cells
	 */
	public int[] getOrange() {
		return this.treasures;
	}

	/**
	 * place of violet cell
	 * 
	 * @return position of violet cell
	 */
	public ArrayList<Integer> getViolet() {
		return this.violetCell;
	}

	/**
	 * place of red cell
	 * 
	 * @return position of red cell
	 */
	public ArrayList<Integer> getRed() {
		return this.redCell;
	}

	/**
	 * get the position of players
	 * 
	 * @return the positions of players
	 */
	public int[] getPositions() {
		int[] positions = new int[players.length];
		for (int i = 0; i < players.length; i++) {
			positions[i] = players[i].getPosition();
		}
		return positions;
	}

	/**
	 * get the colors of the given cells
	 * 
	 * @param array
	 *            array of cells
	 * @return color of given cells
	 */
	public String[] getColors(int[] array) {
		String[] colorArray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			colorArray[i] = this.board[array[i]].getColor();
		}
		return colorArray;
	}

	/**
	 * move player to new position
	 * 
	 * @param player
	 *            number of player
	 * @param newPos
	 *            the cell to move to
	 */
	public void changePlace(int player, int newPos) {
		this.players[player - 1].moveTo(newPos);
	}

	/**
	 * @return dice numbers for player
	 */
	public int[] Dice() {
		int[] a = this.dice.getDice();
		return a;
	}

	/**
	 * check if two players are in one cell and one should move to blueCell
	 * 
	 * @param player
	 *            player number
	 * @param place
	 *            place of player
	 * @return if the player should move to blueCell
	 */
	public boolean lost(int player, int place) {
		if (players[place - 1].isLost(players[player - 1])) {
			return true;
		}
		return false;
	}

	/**
	 * if any of the players has won the round
	 * 
	 * @param n
	 *            player number
	 */
	public void won(int n) {
		this.players[n - 1].isWon();
	}

	/**
	 * find the winner of the game
	 * 
	 * @return number of the winner player
	 */
	public int getWinner() {
		int res = 0;
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i].getScore() > players[i].getScore())
				res = i;
		}
		return res + 1;
	}

	/**
	 * get the goal behind the orange cell
	 * 
	 * @param place
	 *            the place of orange cell
	 * @return the goal behind the orange cell
	 */
	public char getTreasure(int place) {
		char treasure = ((orangeCell) board[place]).getTreasure();
		return treasure;
	}

	public int getScore(int player) {
		return this.players[player - 1].getScore();
	}

	/**
	 * possible moves for a player with n moves
	 * 
	 * @param n
	 *            length of move
	 * @param player
	 *            the number of player which is it's turn
	 * @return possible cells to move to
	 */
	public int[] possibleChoices(int n, int player) {
		int x = this.players[player - 1].getPosition();
		return poss(n, x);
	}
	
	public int[] poss(int n, int place){
		int[][] choices = new int[7][0];

		choices[0] = board[place].getWaysOut();
		int[][][] helper = new int[6][][];
		int len = choices[0].length;

		helper[0] = new int[len][];
		for (int i = 0; i < len; i++) {
			int[] tmp = board[choices[0][i]].getWaysOut();
			int finder = finder(tmp, place);
			helper[0][i] = omitELement(tmp, finder);
		}

		for (int i = 1; i < 6; i++) {
			choices[i] = twoDmToOneDm(helper[i - 1]);
			if (i == n - 1) {
				return choices[i];
			}
			helper[i] = omitInWays(choices[i - 1], helper[i - 1]);
		}
		return choices[n - 1];
	}

	/**
	 * get the array b which contains the neighbors of the cells which are given
	 * in a and omit the the cell from the neighbors
	 * 
	 * @param mainCell
	 *            the in cell
	 * @param neighbors
	 *            neighbors of the cell
	 * @return the out ways
	 */
	private int[][] omitInWays(int[] mainCell, int[][] neighbors) {
		int size = countElements(neighbors);
		int pointer = 0;

		int[][] res = new int[size][];
		for (int i = 0; i < neighbors.length; i++) {
			for (int j = 0; j < neighbors[i].length; j++) {
				int[] cellNeighbor = board[neighbors[i][j]].getWaysOut();
				int indexOfMainCell = finder(cellNeighbor, mainCell[i]);
				if (indexOfMainCell < 15) {
					res[pointer] = this.omitELement(cellNeighbor,
							indexOfMainCell);
					pointer++;
				}
			}
		}
		return res;
	}

	/**
	 * set the goals in orange cell at the begining of the game randomly
	 * 
	 * @return array of goals
	 */
	private char[] setTreasure() {
		Random random = new Random();
		for (int i = 0; i < 13; i++) {
			int temp = random.nextInt(13 - i);
			swap(goals, temp, 12 - i);
		}
		return goals;
	}

	/**
	 * @param array
	 * @param element
	 *            wanted element
	 * @return index of wanted element in an array
	 */
	private int finder(int[] array, int element) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element) {
				return i;
			}
		}
		return 15;
	}

	/**
	 * swap 2 elements of an array
	 * 
	 * @param array
	 * @param index
	 * @param ind
	 * 
	 */
	private void swap(char[] array, int index, int ind) {
		char temp = array[index];
		array[index] = array[ind];
		array[ind] = temp;
	}

	/**
	 * convert 2 dimentional array to 1 dimentional array
	 * 
	 * @param array
	 * @return converted array
	 */
	private int[] twoDmToOneDm(int[][] array) {
		int count = countElements(array);
		int[] res = new int[count];
		int pointer = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				res[pointer] = array[i][j];
				pointer++;
			}
		}
		return res;
	}

	/**
	 * omit an element of an array
	 * 
	 * @param array
	 * @param index
	 *            index of unwanted element
	 * @return new array without unwanted element
	 */
	private int[] omitELement(int[] array, int index) {
		int length = array.length;

		// if the element is not in the array
		if (index >= length)
			return array;

		int[] newArray = new int[length - 1];
		int temp = array[index];
		array[index] = array[length - 1];
		array[length - 1] = temp;

		for (int i = 0; i < length - 1; i++) {
			newArray[i] = array[i];
		}
		return newArray;
	}

	/**
	 * count number of elements in in 2 dimentional array
	 * 
	 * @param array
	 * @return number of elements
	 */
	private int countElements(int[][] array) {
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++)
				count++;
		}
		return count;
	}

	private int max(Player[] array) {
		int res = array[0].getScore();
		for (int i = 0; i < array.length; i++) {
			if (array[i].getScore() > res)
				res = array[i].getScore();
		}
		return res;
	}

	public ArrayList<Integer> winner() {
		ArrayList<Integer> win = new ArrayList<Integer>();
		int n = max(this.players);
		for (int i = 0; i < this.players.length; i++) {
			if (players[i].getScore() == n)
				win.add(i);
		}
		return win;
	}

	// get the positions for creating graphical board
	public int[][] getArrows() {
		int[][] res = new int[Arrows.size()][];
		for (int i = 0; i < res.length; i++) {
			res[i] = new int[2];
			res[i][0] = Arrows.get(i)[0];
			res[i][1] = Arrows.get(i)[1];
		}
		return res;
	}

	public int[][] getcypress() {
		int[][] res = new int[cypressPositions.size()][];
		for (int i = 0; i < res.length; i++) {
			res[i] = new int[2];
			res[i][0] = cypressPositions.get(i)[0];
			res[i][1] = cypressPositions.get(i)[1];
		}
		return res;
	}

	public int[][] getBlue() {
		int[][] res = new int[blueCells.size()][];
		for (int i = 0; i < res.length; i++) {
			res[i] = new int[2];
			res[i][0] = blueCells.get(i)[0];
			res[i][1] = blueCells.get(i)[1];
		}
		return res;
	}

	public int getNumberOfCells() {
		return numberOfCells;
	}

	public int[][] getExactPositions() {
		return this.positions;
	}

	public boolean checkInRed(int player) {
		if (redCell.contains(players[player - 1].getPosition()))
			return true;
		return false;
	}

	public int[] getLabel() {
		return label;
	}

	public int getNumberOfViolets() {
		return numberOfViolets;
	}

	public void setNumberOfViolets(int numberOfViolets) {
		this.numberOfViolets = numberOfViolets;
	}
}
