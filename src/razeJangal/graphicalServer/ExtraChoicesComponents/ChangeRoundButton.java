package razeJangal.graphicalServer.ExtraChoicesComponents;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

import razeJangal.graphicalServer.components.MyLabel;
/**
 * the button for changing the round
 * @author Pegah Jandaghi
 *
 */
public class ChangeRoundButton extends JButton implements ExtraChoices{

	private boolean highlighted;
	private boolean isVisible;
	private boolean isHighlitedlight;
	
	/**
	 * Constructor
	 */
	public ChangeRoundButton(){
		super();
		MyLabel tmp = new MyLabel("Change The Round Goal");
		tmp.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.add(tmp);
	}

	public void Highlight() {
		this.isHighlitedlight = true;
		this.setBackground(new Color(100, 175, 185));
	}

	public void lowLight() {
		this.isHighlitedlight = false;
		this.setBackground(new Color(50, 115, 120));
	}
	/**
	 * change the color of the button
	 */
	@Override
	public void changeStatus() {
		if (this.isHighlitedlight)
			this.lowLight();
		else
			this.Highlight();
	}

	@Override
	public void setVisible() {
		this.isVisible = true;
		this.setVisible(true);
	}

	@Override
	public void setInvisible() {
		this.isVisible = false;
		this.setVisible(false);
	}

}
