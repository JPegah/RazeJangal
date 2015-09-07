package razeJangal.game.board.cells;


/**
 * @author Pegah Jandaghi(90105978)
 * cell of board with violet color
 */
public class violetCell extends BoardCell{
	private String color;
	
	/**
	 * constructor
	 * @param position place of cell
	 */
	public violetCell(int position) {
		super(position);
		this.color = "violet";
	}

	public String getColor(){
		return this.color;
	}
}
