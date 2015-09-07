package razeJangal.game.board.cells;


/**
 * @author Pegah Jandaghi(90105978)
 * cell of board with red color
 */
public class redCell extends BoardCell{
	private String color;
	
	/**
	 * constructor
	 * @param positions place of cell
	 */
	public redCell(int positions) {
		super(positions);
		this.color = "red";
	}

	public String getColor(){
		return this.color;
	}
}

