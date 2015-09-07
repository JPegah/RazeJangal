package razeJangal.servers;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import javazoom.jl.decoder.JavaLayerException;
import razeJangal.Exceptions.InvalidDiceNumbers;
import razeJangal.game.Game;
import razeJangal.game.board.cells.BoardCell;
import razeJangal.game.board.cells.orangeCell;
import razeJangal.game.choices.diceCheck;
import razeJangal.game.choices.diceCheckEqu;
import razeJangal.graphicalServer.ExtraChoicesComponents.ArrowToCell;
import razeJangal.graphicalServer.ExtraChoicesComponents.ChangeRoundButton;
import razeJangal.graphicalServer.ExtraChoicesComponents.ExtraChoices;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalPlayer;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.Line;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalBlueCell;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalBoardCell;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalGreenCell;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalOrangeCell;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalRedCell;
import razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell.GraphicalVioletCell;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.ChatArea;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.MyPanel;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.MyStatusBar;
import razeJangal.graphicalServer.GraphicalBoard.estateComponents.State;
import razeJangal.graphicalServer.components.DiceFrame;
import razeJangal.graphicalServer.components.resultFrame;
import razeJangal.gui.Client;
import razeJangal.gui.protocol.Chat;
import razeJangal.gui.protocol.MapRequest;
import razeJangal.gui.protocol.StateRequest;
import razeJangal.gui.protocol.X;
import razeJangal.gui.protocol.cheat;
import razeJangal.gui.protocol.name;
import razeJangal.gui.protocol.pause;
import razeJangal.gui.protocol.play;
import razeJangal.gui.protocol.stateNum;

/**
 * creates the graphical board of the game

 * @author Peagh Jandaghi
 * 
 */
public class graphicalServer implements Serializable {
	private int[] secondChoices;
	private ChatArea chat;
	private JFrame gameFrame;
	private JLayeredPane jlp;
	private JButton notGuessGoal;
	private JLabel backgroundLabel;
	private JLabel cardsLabel;
	private MyStatusBar statusbar;
	private MyPanel gameScoresPanel;
	private GraphicalBoardCell[] cells;
	private GraphicalPlayer[] players;
	private int[] violetCellPositions;
	private int[][] blueCellPositions;
	private int[][] cellPositions;
	private int[][] arrowPosition;
	private int[][] pinePositions;
	private int[] oranges;
	private ExtraChoices[] arrows;
	private BoardCell[] cell;
	private int numViolet;
	private int numOrange;
	private ArrayList<GraphicalBoardCell> SecondMove;
	private ArrayList<GraphicalBoardCell> firstMoveFirstDice;
	private ArrayList<GraphicalBoardCell> firstMoveSecondDice;
	private JButton Send;
	private JComboBox choices;	
	private Integer lastChoice;
	private Integer position;
	private HashMap<Integer, Character> seenOranges;
	public X gameState;
	public X displayingState;
	private JButton gotoCurrent;
	private JButton fileRequest;
	private JButton forward;
	private JButton back;
	public X client;


	/**
	 * Constructor
	 */
	public graphicalServer() {
		position = new Integer(0);
		lastChoice = new Integer(-1);
		forward = new JButton(">");
		back = new JButton("<");
		forward.setBounds(50, 0, 50, 40);
		back.setBounds(0, 0, 50, 40);
		this.statusbar = new MyStatusBar();
		this.firstMoveFirstDice = new ArrayList<GraphicalBoardCell>();
		this.SecondMove = new ArrayList<GraphicalBoardCell>();
		this.firstMoveSecondDice = new ArrayList<GraphicalBoardCell>();
		this.gameFrame = new JFrame();
		this.gameFrame.getContentPane().setPreferredSize(
				new Dimension(1350, 700));
		this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jlp = new JLayeredPane();
		gameFrame.add(jlp);
		jlp.add(statusbar, new Integer(3));
		this.notGuessGoal = new JButton("Don't want to guess!");
		this.notGuessGoal.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.notGuessGoal.setBounds(1190, 645, 150, 40);
		notGuessGoal.setVisible(false);
		fileRequest = new JButton("Map");
		fileRequest.setBounds(0, 40, 50, 40);
		gotoCurrent = new JButton("||");
		gotoCurrent.setBounds(50, 40, 50, 40);
		jlp.add(gotoCurrent, new Integer(4));
		jlp.add(fileRequest, new Integer(4));
		jlp.add(notGuessGoal, new Integer(4));
		jlp.add(back, new Integer(5));
		jlp.add(forward, new Integer(5));
		displayingState = new stateNum();
		gameState = new stateNum();
	}

	// setters and getters
	public GraphicalPlayer[] getPlayers() {
		return this.players;
	}

	public void setPositions(int[][] poses) {
		this.cellPositions = poses;
	}

	public void setPine(int[][] poses) {
		this.pinePositions = poses;
		drawPine();
	}

	public void setArrow(int[][] poses) {
		this.arrowPosition = poses;
	}

	public void setNumOrange(int n) {
		numOrange = n;
	}

	public void setBlueCell(int[][] b) {
		this.blueCellPositions = b;
	}

	public int getNumViolet() {
		return numViolet;
	}

	public void setNumViolet(int numViolet) {
		this.numViolet = numViolet;
	}

	/**
	 * highlight the choices of the given player
	 * 
	 * @param player
	 *            number of player
	 * @param d
	 *            contains possible moves for the player
	 * @param diceNumbers
	 */
	public void showFirstChoicesOfOthers(int player, diceCheck d,
			int[] diceNumbers) {
		showChoicesFirstDice(d.getpossibleMoves()[0], 0);
		showChoicesFirstDice(d.getpossibleMoves()[1], 1);

		if (d instanceof diceCheckEqu) {
			this.showLuckyChoices(((diceCheckEqu) d).getEmptyOranges());
		}
	}

	public void showFirstChoicesOfPlayer(int player, diceCheck d,
			int[] diceNumbers) {
		showFirstChoicesOfOthers(player, d, diceNumbers);
	}

	/**
	 * highlight the possible moves for the first move
	 * 
	 * @param player
	 * @param cells
	 * @param n
	 */
	public void showChoicesFirstDice(int[] cells, int n) {
		if (n == 0)
			this.firstMoveFirstDice = showC(cells);
		else
			this.firstMoveSecondDice = showC(cells);
	}

	/**
	 * highlight the cells for second move
	 * 
	 * @param player
	 * @param cells
	 * @param colors
	 */
	public void showSecondChoicesOfPlayer(int player, int[] cells,
			String[] colors) {
		secondChoices = cells;
		this.SecondMove = showC(cells);
	}

	public void showSecondChoicesOfOthers(int player, int[] cells,
			String[] colors) {
		showSecondChoicesOfPlayer(player, cells, colors);
	}

	/**
	 * highlight the cells belongs to the move of one dice
	 * 
	 * @param player
	 *            name of the player
	 * @param cells
	 *            cells to be highlighted
	 * @param colors
	 * @return
	 */
	public ArrayList<GraphicalBoardCell> showC(int[] cells) {
		ArrayList<GraphicalBoardCell> tmp = new ArrayList<GraphicalBoardCell>();
		for (int i = 0; i < cells.length; i++) {
			this.cells[cells[i]].setHighlight();
			tmp.add(this.cells[cells[i]]);
		}
		return tmp;
	}

	/**
	 * mark the orange to guess
	 */
	public void showgeusscells() {
		for (int i = 0; i < oranges.length; i++) {
			this.cells[oranges[i]].setMarked();
		}
	}

	//de highlight the guess cells
	public void removeGuess() {
		showInRed(numOrange, oranges, numOrange, false, true);
		for (int i = 0; i < oranges.length; i++) {
			this.cells[oranges[i]].setNormal();
		}
		gameFrame.repaint();
	}

	public void showResult(int[] scores, String[] players,
			ArrayList<String> winners) {
		resultFrame result = new resultFrame();
		result.showRsult(scores, players, winners);
	}

	/**
	 * highlight the arrows which points to the given cells
	 * 
	 * @param cellNumbers
	 */
	public void showLuckyChoices(int[] cellNumbers) {
		for (int i = 0; i < cellNumbers.length; i++) {
			for (int j = 0; j < this.oranges.length; j++) {
				if (((ArrowToCell) arrows[j]).getPointTo() == cellNumbers[i]) {
					((ArrowToCell) arrows[j]).setVisible(true);
				}
			}
		}
		for (int i = 0; i < violetCellPositions.length; i++)
			((ArrowToCell) arrows[i + oranges.length]).setVisible(true);
		((ChangeRoundButton) arrows[oranges.length + violetCellPositions.length])
				.setVisible(true);

	}

	/**
	 * set the state of the given cell to normal
	 * 
	 * @param cell
	 */
	private void removeHighlight(ArrayList<GraphicalBoardCell> cell) {
		int len2 = cell.size();
		for (int i = len2 - 1; i >= 0; i--) {
			this.cells[cell.get(i).getNumber()].setNormal();
			cell.remove(i);
		}
	}

	public void showDiceNumbers(String playerName, int player, int[] dice) {
		gameScoresPanel.setTurn(playerName, player - 1);
		gameScoresPanel.setDice(dice[0], dice[1]);
	}

	/**
	 * show the treasure behind the orange cell
	 * 
	 * @param orangeCellNumber
	 */
	public void showBehindOrange(int player, int orangeCellNumber, char goal) {
		String playerName = this.names()[player - 1];
		if (!this.seenOranges.isEmpty()
				&& this.seenOranges.keySet().contains(orangeCellNumber)) {
			char c = this.seenOranges.get(orangeCellNumber);
			if (c == goal) {
				((Client) client).send(new cheat(true));
			} else {
				((Client) client).send(new cheat(false));
				return;
			}
		} else {
			this.seenOranges.put(orangeCellNumber, goal);
			((Client) client).send(new cheat(true));
		}
		((GraphicalOrangeCell) this.cells[orangeCellNumber]).showGoal(goal);
		this.statusbar.seenOrange(playerName);
	}
	
	//when the player has seen the treasure of the orange cell 
	public void seenOrange(int playerNumber) {
		String playerName = this.names()[playerNumber - 1];
		this.statusbar.seenOrange(playerName);
	}

	/**
	 * get the name of the given players
	 * 
	 * @param numberOfPlayer
	 * @return
	 */
	public ArrayList<String> Names(int[] numberOfPlayer) {
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < numberOfPlayer.length; i++) {
			res.add(players[numberOfPlayer[i]].getName());
		}
		return res;
	}

	/**
	 * change the visibility of the guess related buttons to visibility
	 * 
	 * @param visibility
	 */
	public void showInRed(int n, int[] a, int red, boolean visibility, boolean b) {
		this.notGuessGoal.setVisible(visibility);
		showgeusscells();
		gameFrame.repaint();
	}

	/**
	 * the thread for showing extra choices
	 * 
	 * @author Pegah Jandaghi
	 * 
	 */
	public class ligthThread extends Thread {
		public void run() {
			while (true) {
				for (int i = 0; i < arrows.length; i++) {
					(arrows[i]).changeStatus();
					gameFrame.repaint();
					try {
						Thread.sleep(2000 / 50);
					} catch (InterruptedException e1) {
					}
				}
			}
		}
	}

	/**
	 * Draw a line between 2 cells
	 * 
	 * @param cell
	 * @param cell1
	 */
	private void createLine(GraphicalBoardCell cell, GraphicalBoardCell cell1) {
		int Xmin = Math.min(cell.getLocation().x, cell1.getLocation().x);
		int Ymin = Math.min(cell.getLocation().y, cell1.getLocation().y);
		int startX = cell.getLocation().x;
		int startY = cell.getLocation().y;
		int endX = cell1.getLocation().x;
		int endY = cell1.getLocation().y;
		int heigth = Math.abs(cell.getLocation().x - cell1.getLocation().x);
		int width = Math.abs(cell.getLocation().y - cell1.getLocation().y);
		Line line = new Line(startX - Xmin, startY - Ymin, endX - Xmin, endY
				- Ymin);
		int size = Math.max(heigth, width);
		line.setBounds(Xmin + 20, 20 + Ymin, size, size);
		jlp.add(line, new Integer(3));
	}

	/**
	 * get the name of the graphical players
	 * 
	 * @return name of players
	 */
	public String[] names() {
		String[] res = new String[players.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = players[i].getName();
		}
		return res;
	}

	/**
	 * move the player to the blue cell and show the message
	 * 
	 * @param playerNum
	 */
	public void playerLost(int playerNum) {
		this.players[playerNum - 1].setBounds(
				this.players[playerNum - 1].getStart()[0],
				this.players[playerNum - 1].getStart()[1], 50, 50);
		try {
			this.statusbar.showBlue(this.players[playerNum - 1].getName());
		} catch (JavaLayerException e) {
			return;
		}
	}

	/**
	 * the message that the chosen cell is not in choices
	 */
	public void showInvalid() {
		JOptionPane.showConfirmDialog(null, "you can't move to this cell", "",
				-1, 0, new ImageIcon("invalid.jpg"));
	}

	/**
	 * move the player to the given cell
	 * 
	 * @param place
	 */
	public void movePlayer(int place, int turn) {
		if (lastChoice != -1 && lastChoice != place) {
			((Client) client).send(new cheat(false));
			return;
		}

		if (lastChoice == place) {
			position = place;
			((Client) client).send(new cheat(true));
		}
		this.players[turn].setBounds(this.cells[place].getLocation().x - 5,
				this.cells[place].getLocation().y - 15, 50, 50);
		lastChoice = -1;
		changeToNormalState();
		SetInvisibles();
		gameFrame.repaint();
	}

	/**
	 * create the graphical board of the game
	 * 
	 * @param numCell
	 *            number of the cells
	 */
	public void createBoard(int numCell) {
		for (int i = 0; i < blueCellPositions.length - 1; i++) {
			cells[numCell + i] = new GraphicalBlueCell();
			cells[numCell + i].setBounds(blueCellPositions[i + 1][0],
					blueCellPositions[i + 1][1], 40, 40);
			cells[numCell + i].setContentAreaFilled(false);
			cells[numCell + i].setBorderPainted(true);
			cells[numCell + i].setNumber(numCell + i);
			cells[numCell + i].addMouseListener(new MouseClicked());
			jlp.add(cells[numCell + i], new Integer(4));
		}
		for (int i = 0; i < numCell; i++) {
			if (this.cell[i].getColor().equals("orange"))
				cells[i] = new GraphicalOrangeCell("");
			else if (cell[i].getColor().equals("violet")) {
				cells[i] = new GraphicalVioletCell("");
			} else if (cell[i].getColor().equals("blue")) {
				cells[i] = new GraphicalBlueCell();
			} else if (cell[i].getColor().equals("red"))
				cells[i] = new GraphicalRedCell("");
			else
				cells[i] = new GraphicalGreenCell("");
			cells[i].setBounds(cellPositions[i][0], cellPositions[i][1], 40, 40);
			cells[i].setContentAreaFilled(false);
			cells[i].setBorderPainted(true);
			cells[i].setNumber(i);
			jlp.add(cells[i], new Integer(4));
		}
		for (int i = 0; i < numCell; i++) {
			for (int j = 0; j < cell[i].getWaysOut().length; j++) {
				createLine(cells[i], cells[cell[i].getWaysOut()[j]]);
			}
		}
		jlp.repaint();
	}

	//recieve the chat from other players
	public void recieveChat(Chat message) {
		String s = message.getFrom();
		s += ":";
		s += message.getText();
		chat.setMessage(s);
		chat.repaint();
	}

	//sent the chat
	private class sendChat extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			super.mousePressed(arg0);
			String text = chat.getText();
			String to = (String) choices.getSelectedItem();
			Chat ch = new Chat(to, text);
			((Client) client).send(ch);
			chat.clear();
		}
	}

	/**
	 * create the players
	 * 
	 * @param numberOfPlayers
	 * @param names
	 *            name of the players
	 */
	public void setPlayers(int numberOfPlayers, String[] names) {
		this.chat = new ChatArea(numberOfPlayers);
		jlp.add(chat, new Integer(3));
		String[] receivers = new String[names.length + 1];
		for (int i = 0; i < names.length; i++){
			receivers[i] = names[i];
		}
		receivers[names.length] = "All";
		this.choices = new JComboBox(receivers);
		choices.setBounds(1170, 220 + (numberOfPlayers + 1) * 65, 90, 30);
		this.Send = new JButton("send");
		Send.setBounds(1260, 220 + (numberOfPlayers + 1) * 65, 90, 30);
		Send.addMouseListener(new sendChat());
		jlp.add(choices, new Integer(4));
		jlp.add(Send, new Integer(5));

		gameScoresPanel = new MyPanel(numberOfPlayers, names);
		jlp.add(gameScoresPanel, new Integer(10));
		this.players = new GraphicalPlayer[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			this.players[i] = new GraphicalPlayer(i);
			this.players[i].setBounds(blueCellPositions[i][0] - 5,
					blueCellPositions[i][1] - 15, 50, 50);
			this.players[i].setStart(blueCellPositions[i][0] - 5,
					blueCellPositions[i][1] - 15);
			this.players[i].setName(names[i]);
			jlp.add(players[i], new Integer(6));
		}
		gameFrame.pack();
		gameFrame.repaint();
	}

	/**
	 * update the score of the player and show the message
	 * 
	 * @param player
	 * @param score
	 */
	public void showScore(int player, int score) {
		this.gameScoresPanel.showScore(player - 1, score);
		try {
			statusbar.rightGuess(this.players[player - 1].getName());
		} catch (JavaLayerException e) {
			return;
		}
	}

	/**
	 * create the frame of the game
	 * 
	 * @param numCell
	 * @param goal
	 * @param cellOfBoard
	 * @param oranges
	 */
	public void setFrame(int numCell, String goal, BoardCell[] cellOfBoard,
			int[] oranges) {
		this.arrows = new ExtraChoices[numOrange + numViolet + 1];
		this.cell = cellOfBoard;
		this.oranges = oranges;

		backgroundLabel = new JLabel(new ImageIcon("14ab.jpg"));

		createArrow(0, oranges, numOrange);
		createArrow(numOrange, violetCellPositions, numViolet);

		backgroundLabel.setBounds(0, 0, 1350, 800);
		jlp.add(backgroundLabel, new Integer(1));
		this.arrows[numOrange + numViolet] = new ChangeRoundButton();
		((ChangeRoundButton) this.arrows[numOrange + numViolet])
				.setVisible(false);
		((ChangeRoundButton) this.arrows[numOrange + numViolet]).setBounds(
				1190, 645, 150, 40);
		this.jlp.add((ChangeRoundButton) arrows[numOrange + numViolet],
				new Integer(4));

		gameFrame.add(jlp, BorderLayout.CENTER);
		cells = new GraphicalBoardCell[numCell + blueCellPositions.length];
		createBoard(numCell);

		this.gameFrame.setVisible(true);
		gameFrame.pack();
		gameFrame.repaint();
	}

	//set the visibility of the frame
	public void set(boolean Visibility) {
		gameFrame.setVisible(Visibility);
	}

	/**
	 * draw the pines of the board
	 */
	private void drawPine() {
		for (int i = 0; i < pinePositions.length; i++) {
			JLabel tmp = new JLabel();
			tmp.setBounds(pinePositions[i][0] - 5, pinePositions[i][1], 50, 60);
			tmp.setIcon(new ImageIcon("pin.png"));
			jlp.add(tmp, new Integer(6));
		}
	}

	/**
	 * create the buttons that point to the choices of equal dices
	 * 
	 * @param start
	 * @param pointsTo
	 *            the cells which they point to
	 * @param numberOfArrows
	 *            the number of the arrows to be created
	 */
	private void createArrow(int start, int[] pointsTo, int numberOfArrows) {
		for (int i = 0; i < numberOfArrows; i++) {
			this.arrows[i + start] = new ArrowToCell("");
			((ArrowToCell) this.arrows[i + start]).setPointTo(pointsTo[i]);
			((ArrowToCell) this.arrows[i + start]).setVisible(false);
			((ArrowToCell) this.arrows[i + start]).setBounds(arrowPosition[i
					+ start][0], arrowPosition[i + start][1], 25, 25);
			this.jlp.add((ArrowToCell) this.arrows[i + start], new Integer(5));
		}
	}

	/**
	 * set the place of the arrows to violet cells
	 * 
	 * @param violet
	 */
	public void setViolet(ArrayList<Integer> violet) {
		this.violetCellPositions = new int[violet.size()];
		for (int i = 0; i < violetCellPositions.length; i++)
			violetCellPositions[i] = violet.get(i);
	}

	/**
	 * set the place of the cards place
	 * 
	 * @param x
	 * @param y
	 */
	public void setCardPlace(int x, int y) {
		this.cardsLabel = new JLabel();
		this.cardsLabel.setBounds(x, y, 155, 215);
		cardsLabel.setVisible(true);
		jlp.add(cardsLabel, new Integer(4));
	}

	/**
	 * set the arrow invisible
	 */
	public void SetInvisibles() {
		for (int i = 0; i < arrows.length; i++) {
			arrows[i].setInvisible();
		}
	}

	public void inPast() {
		statusbar.inPast();
	}

	public void notPast() {
		statusbar.notPast();
	}

	/**
	 * show the new image on the card labels
	 * 
	 * @param goal
	 */
	private void setImageOnCards(char goal) {
		this.cardsLabel.setIcon(new ImageIcon("" + goal + ".png"));
	}

	/**
	 * when the player has chosen to guess the round goal
	 * 
	 * @author Pegah Jandaghi
	 * 
	 */
	private class chooseGuess extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			showgeusscells();
		}
	}

	/**
	 * show the new goal when the goal is changed and show the message
	 * 
	 * @param goal
	 */
	public void showChangedGoal(int round, char goal) {
		setImageOnCards(goal);
		this.statusbar.changeRound();
	}

	/**
	 * remove the highlight of the cells
	 */
	public void changeToNormalState() {
		removeHighlight(SecondMove);
		removeHighlight(firstMoveFirstDice);
		removeHighlight(firstMoveSecondDice);
	}

	/**
	 * handle the click of the extra choices of equal dices
	 * 
	 * @author Pegah Jandaghi
	 * 
	 */
	private class handleEqualDiceChoice extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (((stateNum) gameState).getS() != ((stateNum) displayingState).getS()) {
				cannotContinue();
				return;
			}
			if ((e.getComponent() instanceof ChangeRoundButton)) {
				lastChoice = -1;
				((Client) client).send(-1000);
			} else {
				ArrowToCell tmp = (ArrowToCell) e.getComponent();
				((Client) client).send(-1 * tmp.getPointTo());
				lastChoice = tmp.getPointTo();
			}
		}
	}

	/**
	 * change the round to the next and show the new treasure
	 * 
	 * @param roundNum
	 * @param goal
	 */
	public void showRoundGoal(int roundNum, char goal) {
		gameScoresPanel.nextRound(roundNum);
		setImageOnCards(goal);
		if (roundNum > 1) {
			statusbar.nextRound(roundNum);
		}
	}

	/**
	 * handle the click when the player doesn't want to guess
	 * 
	 * @author Pegah Jandaghi
	 * 
	 */
	private class notGuess extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			showInRed(numOrange, oranges, numOrange, false, true);
			removeGuess();
			((Client) client).send(1);
		}
	}

	

	/**
	 * handle the click on the cells
	 * 
	 * @author Pegah Jandaghi
	 * 
	 */
	private class MouseClicked extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			GraphicalBoardCell tmp = (GraphicalBoardCell) e.getComponent();
			if (((stateNum) gameState).getS() != ((stateNum) displayingState).getS()) {
				cannotContinue();
				return;
			}
			((Client) client).send(tmp.getNumber());
			lastChoice = tmp.getNumber();
		}
	}
	
	//get the dice of the player
	public int[] getDice(int[] dice, String playerName) {
		int[] newDice = new int[2];
		DiceFrame getDiceFrame = new DiceFrame(dice, playerName);
		newDice[0] = Integer.parseInt(getDiceFrame.getFirstDice());
		((Client) client).send(newDice[0]);
		newDice[1] = Integer.parseInt(getDiceFrame.getSecondDice());
		((Client) client).send(newDice[1]);
		return newDice;
	}

	//get the state which is displayed
	public void getCurrentState() {
		((Client) client).send(displayingState);
	}

	//when the player wants to continue in history
	public void cannotContinue() {
		JOptionPane.showConfirmDialog(null,"the displaying state is in the past"
						+ "\nyou should continue game from the currnet state");
	}

	//go to the next state
	public void nextState() {
		((stateNum) gameState).next();
		((stateNum) displayingState).next();
	}

	class change extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			JButton button = (JButton) e.getComponent();
			if (button.getText().equals("||"))
				((Client) client).send(new pause());
			else
				((Client) client).send(new play());
		}
	}

	//change the text of the play/pause button
	public void changeMode() {
		String s = this.gotoCurrent.getText();
		if (s.equals("||")) {
			this.gotoCurrent.setText("Play");
		} else
			this.gotoCurrent.setText("||");
	}

	//set the listener of the buttons
	public void setListeners(boolean isRecorded) {
		ligthThread lightingthread = new ligthThread();
		lightingthread.start();
		if (isRecorded) {
			this.gotoCurrent.setText("||");
			this.gotoCurrent.addMouseListener(new change());
			return;
		}

		this.seenOranges = new HashMap<>();
		this.notGuessGoal.addMouseListener(new notGuess());
		for (int i = 0; i < this.cells.length - 1; i++) {
			this.cells[i].addMouseListener(new MouseClicked());
		}
		for (int i = 0; i < this.arrows.length - 1; i++) {
			((ArrowToCell) arrows[i])
					.addMouseListener(new handleEqualDiceChoice());
		}
		back.addMouseListener(new lastState());
		forward.addMouseListener(new nextState());
		((ChangeRoundButton) arrows[arrows.length - 1])
				.addMouseListener(new handleEqualDiceChoice());

		fileRequest.addMouseListener(new mapFileRequest());
		gotoCurrent.addMouseListener(new currentState());
	}

	private class mapFileRequest extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			((Client) client).send(new MapRequest());
		}
	}

	private class lastState extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			inPast();
			if (((stateNum) gameState).getS() - 1 <= 0)
				JOptionPane.showMessageDialog(null, "no more back!");
			else {
				((Client) client)
						.send(new StateRequest(((stateNum) gameState).getS() - 1, "back"));
				((stateNum) gameState).back();
			}
		}
	}

	private class nextState extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (((stateNum) gameState).getS() == ((stateNum) displayingState).getS() - 1) {
				JOptionPane.showMessageDialog(null, "no more forward!");
			} else {
				((Client) client).send(new StateRequest(((stateNum) gameState).getS() - 1,
						"forward"));
				((stateNum) gameState).next();
			}
		}
	}

	private class currentState extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			((Client) client).send(new StateRequest(((stateNum) displayingState).getS() - 2,
					"forward"));
			((stateNum) gameState).setS(((stateNum) displayingState).getS());
			notPast();
		}
	}

	//warning for invalid dice numbers
	public void InvalidDice() {
		JOptionPane.showConfirmDialog(
						null,"Invalid dice numbers!\nPlease enter a number between 1  to 6 for dices",
						"warning", -1, 2);
	}

	//warning for the other players turn
	public void turnError() {
		lastChoice = -1;
		JOptionPane.showConfirmDialog(null,
				"It is not yout turn and you can not do any action", "warning", -1, 2);
	}

	//warning for wrong choice of player
	public void moveError() {
		lastChoice = -1;
		JOptionPane.showConfirmDialog(null, "You can not move to that cell", "warning", -1, 2);
	}

	//get the name of the player
	public void getName() {
		String name = JOptionPane.showInputDialog("Please enter your name: ");
		((Client) client).send(new name(name));
	}

	//show the given state`
	public void showState(State s) {
		showDiceNumbers(s.getTurnName(), s.getPlayerTurn() + 1,
				s.getDiceNumbers());
		showRoundGoal(s.getRoundNum(), s.getRoundGoal());
		for (int i = 0; i < s.getPlaces().length; i++) {
			if (s.getPlaces()[i] == 0) {
				this.players[i].setBounds(this.players[i].getStart()[0],
						this.players[i].getStart()[1], 50, 50);
				changeToNormalState();
				SetInvisibles();
			} else
				movePlayer(s.getPlaces()[i], i);
		}
		gameFrame.repaint();
	}

}
