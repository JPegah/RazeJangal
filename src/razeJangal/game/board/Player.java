package razeJangal.game.board;

import java.io.Serializable;


/**
 * @author Pegah Jandaghi(90105978)
 * player of the game
 */
public class Player implements Serializable{
	protected int position;
	private int score;
//	private String name;

	/**
	 * constructor
	 */
	public Player() {
		//this.name = name;
		this.position = 0;
		this.score = 0;
	}
	
	public int getPosition(){
		return this.position;
	}
	public int getScore(){
		return this.score;
	}
	
	/**
	 * if player should move to blue cell
	 * @param x player number
	 * @return the player is moved to blue cells or not
	 */
	public boolean isLost(Player x) {
		if (x.getPosition() == this.position && this.position != 0){
			this.position = 0;
			return true;
		}
		return false;
	}

	/**
	 * player won the round and got a score
	 */
	public void isWon() {
		this.score++;
	}

	/**
	 * change the position of player
	 * @param y new position
	 */
	public void moveTo(int y) {
		this.position = y;
	}
}

