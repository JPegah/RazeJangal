package razeJangal.graphicalServer.ExtraChoicesComponents;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import javax.swing.JButton;

import razeJangal.graphicalServer.components.shapedButtons.triangleButton;

/**
 * the button for choosing cells in extra choices
 * @author Pegah Jandaghi
 *
 */
public class ArrowToCell extends triangleButton implements ExtraChoices{

	private int pointTo;
	private boolean highlighted;
	private boolean isVisible;
	private boolean isHighlighted;
	/**
	 * Construcotr
	 * @param label
	 */
	public ArrowToCell(String label) {
		super();
	}
	
	//setters and getters
	public void setPointTo(int n) {
		this.pointTo = n;
	}

	public int getPointTo() {
		return this.pointTo;
	}

	/** 
	 * highlight the color of the button
	 */
	public void Highlight() {
		this.isHighlighted = true;
		this.setBackground(new Color(0, 97, 193));
	}

	/**
	 * change the color of the button to normal
	 */
	public void lowLight() {
		this.isHighlighted = false;
		this.setBackground(new Color(0, 0, 160));
	}

	/**
	 * change the color of the button
	 */
	public void changeStatus() {
		if (this.isHighlighted)
			this.lowLight();
		else
			this.Highlight();
	}

	public void setVisible() {
		this.isVisible = true;
		this.setVisible(true);
	}

	public void setInvisible() {
		this.isVisible = false;
		this.setVisible(false);
	}
}
