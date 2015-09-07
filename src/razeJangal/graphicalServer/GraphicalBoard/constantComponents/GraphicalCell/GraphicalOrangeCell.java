package razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Cells with the color Orange
 * @author Pegah Jandaghi
 *
 */
public class GraphicalOrangeCell extends GraphicalBoardCell implements Serializable{
	
	//char treasure;
	/**
	 * Constructor
	 * @param label
	 * @param t
	 */
	public GraphicalOrangeCell(String label) {
		super(label);
		super.setColor("Orange");
		//this.treasure = t;
		reChangeColor();
	}

	public boolean showGoal(char treasure){
		JOptionPane.showConfirmDialog(null, "is Behind this cell","See Treasure",  -1, 0, new ImageIcon(treasure + ".png"));
		return true;
	}
	@Override
	public void changeColor() {
		// TODO Auto-generated method stub
		this.setBackground(new Color(130, 230, 250));
	}

	@Override
	public void reChangeColor() {
		// TODO Auto-generated method stub
		this.setBackground(new Color(25, 160, 205));
	}

}
