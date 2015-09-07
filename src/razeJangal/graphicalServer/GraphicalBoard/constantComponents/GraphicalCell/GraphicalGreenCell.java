package razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell;

import java.awt.Color;
import java.io.Serializable;

/**
 * Cells with the color Green
 * @author Pegah Jandaghi
 *
 */
public class GraphicalGreenCell extends GraphicalBoardCell implements Serializable{
	/**
	 * Constructor
	 * @param label
	 */
	public GraphicalGreenCell(String label) {
		super(label);
		super.setColor("Green");
		reChangeColor();
	}

	@Override
	public void changeColor() {
		this.setBackground(new Color(60, 230, 60));
	}

	@Override
	public void reChangeColor() {
		this.setBackground(new Color(5, 170, 15));
	}

}
