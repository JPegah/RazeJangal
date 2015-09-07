package razeJangal.game.choices;

import java.io.Serializable;


/**
 * 
 * @author Pegah Jandaghi(90105978)
 * contains the possible moves of dice numbers
 */
public class diceCheck implements Serializable{
	private int[][] possibleMoves;
	private boolean isLucky;
	private String[][] Colors;
	
	/**
	 * constructor
	 */
	public diceCheck(){
		Colors = new String[2][];
		possibleMoves = new int[2][];
	}

	//setters and getters
	public int[][] getpossibleMoves() {
		return possibleMoves;
	}

	public void setFirstMoves(int[] firstMoves, int[] secondMoves) {
		this.possibleMoves[0] = firstMoves;
		this.possibleMoves[1] = secondMoves;
	}

	public boolean isLucky() {
		return isLucky;
	}

	public void setLucky(boolean isLucky) {
		this.isLucky = isLucky;
	}

	
	public void setColors(String[] firstCols, String[] secondCols) {
		this.Colors[0] = firstCols;
		this.Colors[1] = secondCols;
		
	}

	public String[][] getColors() {
		return Colors;
	}
	
	public static void sort(int[] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = array.length - 1; j > i; j--) {
				if (array[j] < array[j - 1]) {
					int temp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = temp;
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if(!(o instanceof diceCheck))
			return false;
		diceCheck d = (diceCheck) o;
		if(possibleMoves[0].length != d.possibleMoves[0].length && possibleMoves[0].length != d.possibleMoves[1].length){
				return false;
		}
		sort(possibleMoves[0]);
		sort(possibleMoves[1]);
		sort(d.possibleMoves[0]);
		sort(d.possibleMoves[1]);
		if (eq(possibleMoves[0], d.possibleMoves[0]) && eq(possibleMoves[1], d.possibleMoves[1]))
			return true;
		if (eq(possibleMoves[1], d.possibleMoves[0]) && eq(possibleMoves[0], d.possibleMoves[1]))
			return true;
		return false;
	}
	
	public static boolean eq(int[] a, int[] b){
		if(a.length != b.length){
			return false;
		}
		for (int i = 0; i < a.length; i++){
			if (a[i] != b[i]){
				return false;
			}
		}
		return true;
	}
}
