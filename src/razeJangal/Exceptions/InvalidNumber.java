package razeJangal.Exceptions;

import javax.swing.JOptionPane;

public class InvalidNumber extends RuntimeException {
	public InvalidNumber() {
		super("invalid number");
	}
}
