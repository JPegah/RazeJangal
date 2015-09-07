package razeJangal.graphicalServer.GraphicalBoard.estateComponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import razeJangal.graphicalServer.components.MyLabel;
/**
 * shows players, dice numbers, round numbers
 * @author Pegah Jandaghi
 *
 */
public class MyPanel extends JPanel implements Serializable {
	private JLabel[] players;
	private JLabel[] playersScore;
	private JLabel roundNumber;
	private JLabel firstDice;
	private JLabel secondDice;
	private JLabel playerTurn;

	/**
	 * Constructor
	 * 
	 * @param numberOfPlayers
	 * @param names
	 *            name of players
	 */
	public MyPanel(int numberOfPlayers, String[] names) {
		super();
		this.setBackground(new Color(186, 234, 239));
		this.setLayout(new GridLayout(numberOfPlayers + 5, 2));
		this.add(new MyLabel("  Round number: "));
		roundNumber = new MyLabel("0");
		secondDice = new MyLabel("");
		firstDice = new MyLabel("");
		this.playerTurn = new MyLabel("");

		this.add(roundNumber);

		players = new JLabel[numberOfPlayers];
		playersScore = new MyLabel[numberOfPlayers];

		for (int i = 0; i < numberOfPlayers; i++) {
			playersScore[i] = new MyLabel("0");
			players[i] = new MyLabel("");
			players[i].setIcon(new ImageIcon(i + ".png"));
			players[i].setText("" + names[i] + ": ");
			this.add(players[i]);
			this.add(playersScore[i]);
		}

		this.add(new MyLabel("    Turn: "));
		this.add(new JLabel());
		this.add(playerTurn);
		this.add(new JLabel(""));
		this.add(new MyLabel("  Dice Numbers: "));
		this.add(new JLabel());
		this.add(firstDice);
		this.add(secondDice);
		this.repaint();
		this.setBounds(1170, 0, 250, 150 + (1 + numberOfPlayers) * 65);
	}

	/**
	 * show the score of the given player
	 * 
	 * @param player
	 * @param score
	 */
	public void showScore(int player, int score) {
		playersScore[player].setText("" + score);
	}

	/**
	 * show round number
	 * 
	 * @param roundNumber
	 */
	public void nextRound(int roundNumber) {
		this.roundNumber.setText("" + roundNumber);
	}

	/**
	 * show the player the turn belongs to
	 * 
	 * @param playerName
	 * @param playerNumber
	 */
	public void setTurn(String playerName, int playerNumber) {
		this.playerTurn.setIcon(new ImageIcon(playerNumber + ".png"));
		this.playerTurn.setText(playerName);
	}

	/**
	 * show dices for the player
	 * 
	 * @param firstDice
	 * @param secondDice
	 */
	public void setDice(int firstDice, int secondDice) {
		this.firstDice.setIcon(new ImageIcon("dice" + firstDice + ".png"));
		this.firstDice.setText("     " + firstDice + "&" + secondDice);
		this.secondDice.setIcon(new ImageIcon("dice" + secondDice + ".png"));
	}

	/**
	 * change the score of the given player
	 * 
	 * @param playerNumber
	 * @param score
	 */
	public void changeScore(int playerNumber, int score) {
		String s = players[playerNumber + 2].getText();
		String[] temp = s.split("[ ]");
		players[playerNumber + 2].setText(temp[0] + " " + score);
	}
}
