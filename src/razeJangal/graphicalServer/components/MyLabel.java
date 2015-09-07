package razeJangal.graphicalServer.components;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * Label with a determined font and size
 * @author Pegah Jandaghi
 *
 */
public class MyLabel extends JLabel{

	/**
	 * Constructor
	 * @param texr
	 */
	public MyLabel(String texr){
		super(texr);
		this.setFont(new Font("Algerian", Font.PLAIN, 14));
	}
	
	/**
	 * Change the text of the label
	 * @param a
	 */
	public void showText(String a){
		this.setText(a);
	}

}