package razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell;

import java.awt.Color;
import java.io.Serializable;

/**
 * Cells with the color Blue
 * @author Pegah Jandaghi
 *
 */
public class GraphicalBlueCell extends GraphicalBoardCell implements Serializable {
	
	/**
	 * Constructor
	 */
	public GraphicalBlueCell() {
		super("");
		super.setColor("Blue");
		reChangeColor();
	}

	@Override
	public void changeColor() {
		this.setBackground(new Color(130, 170, 242));

	}

	@Override
	public void reChangeColor() {
		this.setBackground(new Color(50, 70, 240));

	}
}
