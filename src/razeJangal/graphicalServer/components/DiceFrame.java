package razeJangal.graphicalServer.components;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * The diolog for confirm and change dice numbers
 * @author Pegah Jandaghi
 *
 */
public class DiceFrame {
	JDialog dialog;
	JLabel label1 = new JLabel("Enter DiceNumber");
	JTextField firstDice;
	JLabel label2 = new JLabel("Enter DiceNumber");
	JTextField SecondDice;
	JLabel Player;
	JPanel panel = new JPanel(new GridLayout(3, 2));

	/**
	 * Constructor
	 * @param dices default dice numbers
	 * @param playerName
	 */
	public DiceFrame(int[] dices, String playerName) {
		Player = new JLabel(playerName + " dices");
		firstDice = new JTextField("" + dices[0]);
		SecondDice = new JTextField("" + dices[1]);
		JButton button = new JButton("OK");
		dialog = new JDialog();
		panel.add(label1);
		panel.add(firstDice);
		panel.add(label2);
		panel.add(SecondDice);
		panel.add(Player);
		panel.add(button);
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.setTitle("Confirm dice");
		dialog.setModal(true);
		dialog.pack();
		dialog.setBounds(1150, 600, 200, 100);
		dialog.setBackground(new Color(0, 0, 0));

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dialog.dispose();
			}
		});
		dialog.setVisible(true);

	}

	/**
	 * get the confirmed dice numbers
	 * @return dice number
	 */
	public String getFirstDice() {
		return firstDice.getText();
	}


	/**
	 * get the confirmed dice numbers
	 * @return dice number
	 */
	public String getSecondDice() {
		return SecondDice.getText();
	}
}

