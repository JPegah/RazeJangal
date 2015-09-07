package razeJangal.game.board.cells;


/**
 * @author Pegah Jandaghi(90105978)
 * cell of board with orange color
 */
public class orangeCell extends BoardCell {
	private char treasure;
	
	/**
	 * constructor
	 * @param position place of cell
	 * @param treasure treasure behind cell
	 */
	public orangeCell(int position,char treasure) {
		super(position);
		this.treasure = treasure;
	}

	public String getColor(){
		return "orange";
	}
	
	public char getTreasure() {
		return this.treasure;
	}
}
