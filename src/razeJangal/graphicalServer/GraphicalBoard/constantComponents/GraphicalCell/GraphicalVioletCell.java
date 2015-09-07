package razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell;

import java.awt.Color;
import java.io.Serializable;

/**
 * Cells with the color Violet
 * @author Pegah Jandaghi
 *
 */
public class GraphicalVioletCell extends GraphicalBoardCell implements Serializable{

	/**
	 * Constructor
	 * @param label
	 */
	public GraphicalVioletCell(String label) {
		super(label);
		super.setColor("Violet");
		reChangeColor();
	}

	@Override
	public void changeColor() {
		this.setBackground(new Color(220, 135, 195));
	}

	@Override
	public void reChangeColor() {
		this.setBackground(new Color(160, 10, 220));
	}

}
