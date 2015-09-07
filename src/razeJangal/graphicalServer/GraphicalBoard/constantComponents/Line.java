package razeJangal.graphicalServer.GraphicalBoard.constantComponents;

import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * Lines between cells
 * @author Pegah Jandaghi
 *
 */
public class Line extends JPanel{
	private int x1, y1, x2, y2;


	/**
	 * Constructor
	 * @param x1 start in x axis
	 * @param y1 start in y axis
	 * @param x2 end in x axis
	 * @param y2 end in y axis
	 * @param cell1 related cell
	 * @param cell2 related cell
	 */
	public Line(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 * draw the line
	 */
	public void paintComponent(Graphics g) {
		g.drawLine(x1, y1, x2, y2);
	}
	
}