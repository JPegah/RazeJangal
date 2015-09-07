package razeJangal.graphicalServer.components.shapedButtons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

/**
 * Create the button with the round shape
 * @author Pegah Jandaghi
 *
 */
public class RoundButton extends JButton {

	/**
	 * Constructor
	 * @param label label of the Button
	 */
	public RoundButton(String label){
		super(label);
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
	}


	/**
	 * Paint the round background and label.
	 */
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(getBackground());
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width-1, getSize().height-1);

		// This call will paint the label and the focus rectangle.
		super.paintComponent(g);
  }

	/**
	 * Paint the border of the button using a simple stroke.
	 */
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width-1, getSize().height-1);
	}

	// Hit detection.
	Shape shape;
	public boolean contains(int x, int y) {
		// If the button has changed size, make a new shape object.
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
}