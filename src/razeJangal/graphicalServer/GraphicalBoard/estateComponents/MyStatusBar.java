package razeJangal.graphicalServer.GraphicalBoard.estateComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import razeJangal.graphicalServer.components.MyLabel;

/**
 * the status bar for showing messages 
 * @author Pegah Jandaghi
 *
 */
public class MyStatusBar extends JPanel implements Serializable{
	JPanel[] panels;
	JLabel[] txt;
	private JLabel ActiveLabel;

	/**
	 * Constructor
	 */
	public MyStatusBar() {
		this.setLayout(new GridLayout(3, 1));
		panels = new JPanel[3];
		txt = new JLabel[3];
		for (int i = 0; i < 3; i ++){
			txt[i] = new MyLabel("");
			panels[i] = new JPanel();
			panels[i].add(txt[i]);
			panels[i].setBackground(new Color(186, 234, 239));
			this.add(panels[i]);
		}
		
		setBounds(0, 0, 1200, 60);
		setVisible(true);
	}
	
	/**
	 * show the given text in second label
	 * @param text
	 * @param n
	 */
	private void setText(String text, int n){
		txt[n].setBounds(1100, 0, 50, 50);
		txt[n].setText(text);
		txt[n].repaint();
		ActiveLabel = txt[n];
		ActiveLabel.repaint();
		this.repaint();
		new Thread() {
			public void run() {
					while (ActiveLabel.getLocation().x >= 0) {
						ActiveLabel.setLocation(ActiveLabel.getLocation().x - 5,ActiveLabel.getLocation().y);
						ActiveLabel.repaint();
						try {
							Thread.sleep(1000/ 20);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if (ActiveLabel.getLocation().x <= 0){
						ActiveLabel.setText("");
						return;
					}
				
			};
		}.start();
	}
	
	/**
	 * The message when the player is moved to first place
	 * @param playerName
	 * @throws JavaLayerException
	 */
	public void showBlue(String playerName) throws JavaLayerException{
		setText(playerName + " is moved to first place", 1);
		File file = new File("loser.mp3");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedInputStream bis = new BufferedInputStream(fis);
		final AdvancedPlayer player = new AdvancedPlayer(bis);
		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}.start();
	}
	
	/**
	 * the message that shows the player's guess was right
	 * @param playerName
	 * @throws JavaLayerException
	 */
	public void rightGuess(String playerName) throws JavaLayerException{
		setText("Cogratulation " + playerName + "! your guess was right and you won this round", 1);
		File file = new File("clap.mp3");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
				return;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);
		final AdvancedPlayer player = new AdvancedPlayer(bis);
		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}.start();
	}
	
	/**
	 * The message that shows the next round is started
	 * @param n
	 */
	public void nextRound(int n){
		txt[2].setBounds(1100, 0, 50, 50);
		txt[2].setText("next round started" + " (round " + n+")");
		txt[2].repaint();
		this.repaint();
		new Thread() {
			public void run() {
					while (txt[2].getLocation().x >= 0) {
						txt[2].setLocation(txt[2].getLocation().x - 5,txt[2].getLocation().y);
						txt[2].repaint();
						try {
							Thread.sleep(1000/ 20);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if (txt[2].getLocation().x <= 0){
						txt[2].setText("");
						return;
					}
				
			};
		}.start();
	}
	
	/**
	 * the message when the round'goal is changed
	 */
	public void changeRound(){
		setText1("This round's goal is changed", 0);
	}

	/**
	 * the message when the player has seen behind an orange cell
	 * @param player
	 */
	public void seenOrange(String player){
	    setText1(player + " has seen behind orange", 0);
	}
	/**
	 * set the text in label 0
	 * @param text
	 * @param n
	 */
	private void setText1(String text, int n){
		txt[0].setBounds(1100, 0, 50, 50);
		txt[0].setText(text);
		txt[0].repaint();
		this.repaint();
		new Thread() {
			public void run() {
					while (txt[0].getLocation().x >= 0) {
						txt[0].setLocation(txt[0].getLocation().x - 5,txt[0].getLocation().y);
						txt[0].repaint();
						try {
							Thread.sleep(1000/ 20);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if (txt[0].getLocation().x <= 0){
						txt[0].setText("");
						return;
					}
				
			};
		}.start();
	}
	
	public void inPast(){
		txt[1].setText("This is in the Past");
		txt[1].setBounds(500, 0, 100, 50);
		txt[1].repaint();
	}
	
	public void notPast(){
		txt[1].setText("");
		txt[1].repaint();
	}
}
