package razeJangal.graphicalServer.GraphicalBoard.estateComponents;

import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * creates the area for chat between players
 * @author Pegah Jandaghi
 *
 */
public class ChatArea extends JPanel implements Serializable{
	private JTextField texts;
	private JLabel[] recieved;
	private int num;
	
	//Constructor
	public ChatArea(int numberOfPlayers){
		this.setLayout(new GridLayout(3, 1));
		this.texts = new JTextField();
		recieved = new JLabel[2];
		this.recieved[0] = new JLabel();
		this.recieved[1] = new JLabel();
		num = 0;
		this.add(this.recieved[0]);
		this.add(this.recieved[1]);
		this.add(texts);
		this.setBounds(1170, (numberOfPlayers + 1) * 65 + 160, 180, 60);
		this.repaint();
		this.setVisible(true);
	}
	
	//clear the written text from text box
	public void clear(){
		if (num == 0)
			this.recieved[num].setText(texts.getText());
		else
			addText(texts.getText());
		num++;
		this.texts.setText("");
		this.repaint();
	}
	
	//add a new text message
	private void addText(String message){
		recieved[0].setText(recieved[1].getText());
		recieved[1].setText(message);
	}
	
	//get the message written from player
	public String getText(){
		return this.texts.getText();
	}
	
	//show the other players message
	public void setMessage(String message){
		if (num == 0)
			this.recieved[num].setText(message);
		else
			addText(message);
		num++;
	}
}