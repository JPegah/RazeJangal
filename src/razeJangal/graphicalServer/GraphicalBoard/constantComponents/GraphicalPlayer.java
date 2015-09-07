package razeJangal.graphicalServer.GraphicalBoard.constantComponents;

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * Graphical player of the game
 * @author Pegah Jandaghi
 *
 */
public class GraphicalPlayer extends JLabel implements Serializable{
	private int[] startPlace;

	/**
	 * Constructor
	 * @param playerNumber
	 */
	public GraphicalPlayer(int playerNumber) {
		super();
		this.startPlace = new int[2];
		this.setIcon(new ImageIcon(playerNumber + ".png"));
	}

	/**
	 * set the initial place of the player
	 * @param x
	 * @param y
	 */
	public void setStart(int x, int y) {
		this.startPlace[0] = x;
		this.startPlace[1] = y;
	}

	/**
	 * get the initial place of the player
	 * @return
	 */
	public int[] getStart() {
		int[] position = new int[2];
		position[0] = this.startPlace[0];
		position[1] = this.startPlace[1];
		return position;
	}
}