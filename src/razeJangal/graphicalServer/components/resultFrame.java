package razeJangal.graphicalServer.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * The Frame for showing the result of the game at the end
 * @author Pegah Jandaghi
 *
 */
public class resultFrame extends JFrame{
	private JPanel panel;
	private JDialog dialog;
	
	/**
	 * Constructor 
	 */
	public  resultFrame(){
		super();
		this.getContentPane().setPreferredSize(new Dimension(400, 600));
	}
	
	/**
	 * get The result and show them
	 * @param scores players scores
	 * @param players player names
	 * @param winners name of winners
	 */
	public void showRsult(int[] scores, String[] players, ArrayList<String> winners){
		dialog = new JDialog();
		this.panel = new JPanel();
		panel.setBounds(0, 0, 400, 600);
		panel.setLayout(new GridLayout(players.length + 4, 1));
		panel.add(new MyLabel("         Winners:       "));
		String res = "";
		for (int i = 0; i < winners.size(); i++){
			res += "             ";
			res += winners.get(i);
		}
		panel.add(new MyLabel(res));
		panel.add(new MyLabel("            Scores:       "));
		for (int i = 0; i < players.length; i++){
			panel.add(new MyLabel("          " + players[i] + "..............................................\t" + scores[i] + "   "));
		}
		this.add(panel);
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.setTitle("JDialog");
		dialog.setModal(true);
		dialog.pack();
		dialog.setSize(350, players.length * 50  + 150);
		dialog.setVisible(true);
	}
}
