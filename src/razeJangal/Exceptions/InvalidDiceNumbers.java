package razeJangal.Exceptions;

import javax.swing.JOptionPane;

public class InvalidDiceNumbers extends RuntimeException{
	public InvalidDiceNumbers() {
		super("invalid dice Numbers");
	}

}
