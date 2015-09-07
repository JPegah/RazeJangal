package razeJangal.game.board;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Pegah Jandaghi(90105978) 
 * pool the dices for the player
 */
class DicePool implements Serializable{
	public int[] getDice() {
		Random r = new Random();
		int[] x;
		x = new int[2];
		x[0] = r.nextInt(6) + 1;
		x[1] = r.nextInt(6) + 1;
		return x;
	}

}
