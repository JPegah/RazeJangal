package razeJangal.gui.protocol;

import java.io.Serializable;
/**
 * contains the message and the sender of the chat
 * @author pega
 *
 */
public class Chat implements Serializable{
	private String from;
	private String to;
	private String text;
	
	public Chat(String to, String text){
		this.setTo(to);
		this.text = text;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public String getText() {
		return text;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
}
