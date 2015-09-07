package razeJangal.gui;

import java.util.ArrayList;
import java.util.HashMap;

import razeJangal.game.Game;
import razeJangal.game.board.Board;
import razeJangal.game.board.Player;
import razeJangal.game.board.cells.BoardCell;
import razeJangal.game.board.cells.orangeCell;
import razeJangal.game.choices.diceCheck;
import razeJangal.game.choices.diceCheckEqu;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;
/**
 * automatic Player
 * @author Pegah Jandaghi
 *
 */
public class automaticPlayer extends Player {
	private HashMap<Character, Integer> seen;
	private boolean chaseRed;
	private Game game;
	private int[] orangeCells = { 39, 43, 50, 58, 60, 66, 70, 82, 85, 88, 97,
			103, 108 };
	private int[] positions;
	private int position;
	private char roundTreasure;
	private int playerNumber;
	final int[][] loops = { { 48, 59 }, { 60, 67 }, { 35, 47 }, { 79, 93 },
			{ 94, 109 }, { 110, 116 }, { 68, 78 } };

	// Constructor
	public automaticPlayer(MultiServer m, int number, int numberOfPlayers, Game game) {
		super();
		this.game = game;
		this.positions = new int[numberOfPlayers];
		position = 0;
		roundTreasure = game.roundGoal;
		this.playerNumber = number;
	}

	// report the automatic player that the player is moved to blue cells
	public void playerLost(int player) {
		if (player == playerNumber)
			position = 0;
		positions[player] = 0;
	}

	// return the maximum element of the array
	private int max(int[] array) {
		int res = array[0];
		if (array[1] > res)
			res = array[1];
		return res;
	}

	//check that if any of the elements in array in in the way to red cell
	private int hasMainElement(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] <= 34 || array[i] == 117)
				return array[i];
		}
		return -1;
	}


	//find the best way to the mainWay(way to red cell)
	private int bestWayToMain(diceCheck d, int[] dice) {
		int currentloop = findLoop();

		if (currentloop < 4) {
			for (int j = 0; j < d.getpossibleMoves().length; j++){
				for (int i = 0; i < d.getpossibleMoves()[j].length; i++) {
					if (d.getpossibleMoves()[j][i] <= position) {
						return d.getpossibleMoves()[j][i];
					}
				}
			}
		} else {
			for (int j = 0; j < d.getpossibleMoves().length; j++){
				for (int i = 0; i < d.getpossibleMoves()[j].length; i++) {
					if (d.getpossibleMoves()[j][i] >= position) {
						return d.getpossibleMoves()[j][i];
					}
				}
			}
		}
		return -1;
	}

	//find the current loop that player is in it
	private int findLoop() {
		for (int i = 0; i < loops.length; i++) {
			if (position >= loops[i][0] && position <= loops[i][1]) {
				return i;
			}
		}
		return -1;
	}

	//find the cell in choices that leads to main way
	public int getToMainWay(int[] choices) {
		int currentLoop = findLoop();
		if (currentLoop == -1) {
			if (position <= 24) {
				for (int i = 0; i < choices.length; i++) {
					if (choices[i] == 117 || choices[i] > position) {
						return choices[i];
					}
				}
			} else {
				for (int i = 0; i < choices.length; i++) {
					if (choices[i] == 117 || choices[i] < position) {
						return choices[i];
					}
				}
			}
		}
		if (hasMainElement(choices) != -1) {
			return hasMainElement(choices);
		}

		return bestWayToMain(choices);
	}

	//find the cell that leads to main way
	private int bestWayToMain(int[] choices) {
		int currentLoop = findLoop();

		if (currentLoop < 4) {
			for (int i = 0; i < choices.length; i++) {
				if (choices[i] <= position) {
					return choices[i];
				}
			}
		} else {
			for (int i = 0; i < choices.length; i++) {
				if (choices[i] >= position) {
					return choices[i];
				}
			}
		}
		return -1;
	}

	//find the best way to the red cell
	public int getToMainWay(int dice[], diceCheck d) {
		int currentLoop = findLoop();
		if (currentLoop == -1) {
			if (position <= 24) {
				for (int j = 0; j < d.getpossibleMoves().length; j++){
					for (int i = 0; i < d.getpossibleMoves()[j].length; i++) {
						if (d.getpossibleMoves()[j][i] == 117 || d.getpossibleMoves()[j][i] > position) {
							return d.getpossibleMoves()[j][i];
					}
				}
			}
			} else {
				for (int j = 0; j < d.getpossibleMoves().length; j++){
				for (int i = 0; i < d.getpossibleMoves()[j].length; i++) {
					if (d.getpossibleMoves()[j][i] == 117 || d.getpossibleMoves()[j][i] < position) {
						return d.getpossibleMoves()[j][i];
					}
				}}
			}
		}
		if (hasMainElement(d.getpossibleMoves()[0]) != -1) {
			return hasMainElement(d.getpossibleMoves()[0]);
		}
		if (hasMainElement(d.getpossibleMoves()[1]) != -1) {
			return hasMainElement(d.getpossibleMoves()[1]);
		}
		return bestWayToMain(d, dice);
	}

	//check if any of the cells in choices is red cell
	private int Red(int[] choices) {
		for (int i = 0; i < choices.length; i++) {
			if (choices[i] == 25)
				return 25;
		}
		return -1;
	}

	//find the cell that is closer to red cell
	public int followRed(int[] choices) {
		if (Red(choices) != -1)
			return 25;
		return getToMainWay(choices);
	}

	//find the cell that is closer to red cell
	public int followRed(int[] dice, diceCheck d) {
		int max = max(dice);
		if (Red(d.getpossibleMoves()[0]) != -1
				|| Red(d.getpossibleMoves()[1]) != -1)
			return 25;

		int dif = Math.abs(dice[0] - dice[1]);
		if (position + dif == 25 || position - dif == 25)
			return 25;
		return getToMainWay(dice, d);
	}

	//show the treasure behind the orange cell to the automatic player
	public void showBehindOrange(int player, int orangeCellNumber, char goal) {
		if (seen.containsValue(orangeCellNumber))
			return;
		seen.put(goal, orangeCellNumber);
		if (this.roundTreasure == goal)
			chaseRed = true;
	}

	//show the choices of the player and choose the best choice
	public int showFirstChoicesOfPlayer(int player, diceCheck d, int[] dice) {
		return choice(dice, d);
	}

	//check if the player can beat other players in one choice
	public int canBeatStraight(int[] choices, int[] positions) {
		for (int i = 0; i < choices.length; i++) {
			for (int j = 0; j < positions.length; j++) {
				if (choices[i] == positions[j])
					return choices[i];
			}
		}
		return -1;
	}

	//check if the player can beat other players at all
	public int beat(int[] dice, int[] first, int[] second, int[] array) {
		int beatStraightFirstDice = canBeatStraight(first, array);
		int beatNonStraightSecondDice = canBeatStraight(second, array);
		if (beatStraightFirstDice != -1)
			return beatStraightFirstDice;
		if (beatNonStraightSecondDice != -1)
			return beatNonStraightSecondDice;
		int canbeatNonStraight = canBeat(first, second, dice[1], array);
		int canbeatonStraight2 = canBeat(second, first, dice[0], array);
		if (canbeatNonStraight != -1)
			return canbeatNonStraight;
		if (canbeatonStraight2 != -1)
			return canbeatonStraight2;
		return -1;
	}

	//check if the player can reach the cells in the given array
	private int canBeat(int[] first, int[] second, int dice, int[] array) {
		for (int i = 0; i < first.length; i++) {
			int[] next = game.getChoices(dice, first[i]);
			int can = canBeatStraight(next, array);
			if (can != -1)
				return can;
		}
		return -1;
	}

	//set the name of the player
	public String getName() {
		return "AutomaticPlayer";
	}

	//choose to see the treasure of the orange cell in extra choices
	private int chooseOrange(int[] oranges) {
		for (int i = 0; i < oranges.length; i++) {
			if (seen.size() >= 1 && seen.containsValue(oranges[i]))
				return oranges[i];
		}
		return -1;
	}

	//check if any of other players is close to red
	private boolean close() {
		for (int i = 0; i < game.positions().length; i++) {
			if (game.positions()[i] > 15 && game.positions()[i] <= 34)
				return true;
			if (game.positions()[i] == 117)
				return true;
		}
		return false;
	}

	//report that the round goal is changed
	public void showChangedGoal(int roundNum, char c) {
		this.roundTreasure = c;
		if (seen.containsKey(c)) {
			chaseRed = true;
		} else
			chaseRed = false;
	}

	//show that the automatic is in red cell
	public int showInRed(int n, int[] a, int red, boolean v, boolean canGuess) {
		if (seen.size() == 0)
			return 1;
		if (seen.containsKey(this.roundTreasure)) {
			return seen.get(this.roundTreasure);
		} else
			return 1;
	}

	//show the choices of player and get the best choice
	public int secondChoice(int[] choices) {
		int beat = this.canBeatStraight(choices, game.positions());
		if (beat != -1)
			return beat;
		int findOrange = this.canBeatStraight(choices, orangeCells);
		if (findOrange != -1 && seen.size() != 0 && !seen.containsValue(findOrange))
			return findOrange;

		if (chaseRed) {
			return followRed(choices);
		}

		return choices[0];
	}

	//show the choices of both dices
	public int choice(int[] dice, diceCheck d) {
		int[] first = d.getpossibleMoves()[0];
		int[] second = d.getpossibleMoves()[1];
		int beat = this.beat(dice, first, second, game.positions());
		if (beat != -1 && beat != 0)
			return beat;
		int findOrange = this.beat(dice, first, second, this.orangeCells);
		if (findOrange != -1 && !seen.containsValue(findOrange) && findOrange != 0)
			return findOrange;

		if (d instanceof diceCheckEqu) {
			if (close())
				return -1000;
		}
		if (chaseRed) {
			return followRed(dice, d);
		}

		if (d instanceof diceCheckEqu) {
			int tmp = chooseOrange(((diceCheckEqu) d).getEmptyOranges());
			return tmp * -1;
		}
		return d.getpossibleMoves()[0][0];
	}

	//show choices of player for second dice
	public int showSecondChoicesOfPlayer(int player, int[] cells,
			String[] colors) {
		int n = secondChoice(cells);
		if (n != -1)
			return n;
		else
			return cells[0];
	}

	//show the round goal
	public void showRoundGoal(int roundNum, char goal) {
		this.roundTreasure = goal;
	}

}
