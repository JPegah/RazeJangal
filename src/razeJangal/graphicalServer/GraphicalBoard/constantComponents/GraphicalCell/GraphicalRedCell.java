package razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell;

import java.awt.Color;
import java.io.Serializable;

/**
 * Cells with the color Red
 * @author Pegah Jandaghi
 *
 */
public class GraphicalRedCell extends GraphicalBoardCell implements Serializable{

	/**
	 * Constructor
	 * @param label
	 */
	public GraphicalRedCell(String label) {
		super(label);
		super.setColor("Red");
		reChangeColor();
	}

	@Override
	public void changeColor() {
		this.setBackground(new Color(225, 85, 85));
	}

	@Override
	public void reChangeColor() {
		this.setBackground(new Color(195, 35, 35));
	}
}
