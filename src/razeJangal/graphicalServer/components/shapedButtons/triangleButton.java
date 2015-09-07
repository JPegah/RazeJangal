package razeJangal.graphicalServer.components.shapedButtons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JButton;

/** 
 * Buttons shaped like a triangle 
 * @author Pegah Jandaghi
 *
 */
public class triangleButton extends JButton {
	/**
	 * Constructor
	 */
	public triangleButton() {
		super();
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
	}

	/**
	 * paint the button
	 */
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(getBackground());
		} else {
			g.setColor(getBackground());
		}
		int xPoints[] = { getSize().width / 2, 0, getSize().width };
		int yPoints[] = { 0, getSize().height, getSize().height };
		g.fillPolygon(xPoints, yPoints, xPoints.length);
		super.paintComponent(g);
	}

	/**
	 * panit the borders to look like triangle
	 */
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		int xPoints[] = { getSize().width / 2, 0, getSize().width };
		int yPoints[] = { 0, getSize().height, getSize().height };
		g.drawPolygon(xPoints, yPoints, xPoints.length);
	}

	Polygon polygon;
	/**
	 *  If the button has changed size, make a new shape object.
	 */
	public boolean contains(int x, int y) {
		if (polygon == null || !polygon.getBounds().equals(getBounds())) {
			int xPoints[] = { getSize().width / 2, 0, getSize().width };
			int yPoints[] = { 0, getSize().height, getSize().height };
			polygon = new Polygon(xPoints, yPoints, xPoints.length);
		}
		return polygon.contains(x, y);
	}
}