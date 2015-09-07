package razeJangal.graphicalServer.GraphicalBoard.estateComponents;

import java.io.Serializable;

public class State implements Serializable {
	private char roundGoal;
	private int[] diceNumbers;
	private int playerTurn;
	private String turnName;
	private int[] places;
	private int roundNum;
	private int secondDice;
	private boolean displayingFirst;
	private boolean secondRed;
	public State(char goal, int[] dice, int turn, String name, int[] places, int roundNum, boolean b){
		this.roundNum = roundNum;
		this.roundGoal = goal;
		this.diceNumbers= dice;
		this.playerTurn = turn;
		this.turnName = name;
		this.places = places;
		displayingFirst = b;
	}
	
	public boolean getDisplaying(){
		return displayingFirst;
	}
	public char getRoundGoal() {
		return roundGoal;
	}
	public int[] getDiceNumbers() {
		return diceNumbers;
	}
	public int getPlayerTurn() {
		return playerTurn;
	}
	public String getTurnName() {
		return turnName;
	}
	public int[] getPlaces() {
		return places;
	}
	public int getRoundNum() {
		return roundNum;
	}

	public int getSecondDice() {
		return secondDice;
	}

	public void setSecondDice(int secondDice) {
		this.secondDice = secondDice;
	}

	public boolean isSecondRed() {
		return secondRed;
	}

	public void setSecondRed(boolean secondRed) {
		this.secondRed = secondRed;
	}
}
