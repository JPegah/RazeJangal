package razeJangal.Exceptions;

import javax.swing.JOptionPane;

public class InvalidPlayers extends RuntimeException{
	public InvalidPlayers(String s) {
		super(s);
	}
}
