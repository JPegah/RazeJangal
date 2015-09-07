package razeJangal.game.board.cells;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * cells of the map
 * @author pegah jandaghi(90105978)
 */
public abstract class BoardCell implements Serializable{
	private int position;
	private ArrayList<Integer> neighbors;
	

	/**
	 * constructor
	 * @param position number of the cell
	 */
	public BoardCell(int position) {
		this.position = position;
		this.neighbors = new ArrayList<Integer>();
	}
	
	
	public int getPosition() {
		return position;
	}
	
	public abstract String getColor();
	
	
	public void setNeighbor(int numbers){
		this.neighbors.add(numbers);
	}
	/**
	 * set the given cell neighbor with this cell
	 * @param cell 
	 * @param x 
	 */
	public void setNeighbors(BoardCell cell, int x) {
		if (!this.neighbors.contains(cell.getPosition()))
			this.neighbors.add(cell.getPosition());
		else 
			return;
		cell.setNeighbors(this, 1);
	}

	
	/**
	 * get the neighbors of cell(not empty ones)
	 * @return cell neighbors
	 */
	public int[] getWaysOut() {
		int len = this.neighbors.size();
		if (this.neighbors.contains(0))
			len--;
		int[] res = new int[len];
		int j = 0;
		for (int i = 0; i < this.neighbors.size(); i++){
			if (this.neighbors.get(i).equals(0))
				continue;
			res[j] = this.neighbors.get(i);
			j++;
		}
		return res;
	}

}
