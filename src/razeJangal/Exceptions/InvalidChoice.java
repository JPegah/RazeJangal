package razeJangal.Exceptions;

import javax.swing.JOptionPane;

public class InvalidChoice extends RuntimeException {
	public InvalidChoice() {
		JOptionPane.showConfirmDialog(null,
						"This cells is not in your possible choices. \nSo You can't move to this cell");
	}

}
