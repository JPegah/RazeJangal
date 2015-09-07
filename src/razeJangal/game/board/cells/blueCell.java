package razeJangal.game.board.cells;


/**
 * @author Pegah Jandaghi(90105978)
 * cell of board with blue color
 */
public class blueCell extends BoardCell{
	private String color;
	
	/**
	 * constructor
	 */
	public blueCell() {
		super(0);
		this.color = "blue";
	}

	public String getColor(){
		return this.color;
	}
}

