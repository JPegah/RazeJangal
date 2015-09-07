package razeJangal.game.board.cells;


/**
 * @author pegah jandaghi(90105978)
 * cells of board with green color
 */
public class greenCell extends BoardCell {
	private String color;
	
	/**
	 * constructor
	 * @param position place of cell
	 */
	public greenCell(int position) {
		super(position);
		this.color = "green";
	}

	public String getColor(){
		return this.color;
	}
}
