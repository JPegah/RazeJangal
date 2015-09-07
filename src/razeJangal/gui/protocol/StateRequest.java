package razeJangal.gui.protocol;

import java.io.Serializable;
/**
 * request to see the state (history of the game)
 * @author Pegah Jandaghi
 *
 */
public class StateRequest implements Serializable{
	private int now;
	private String request;
	public StateRequest(int now, String request){
		this.now = now;
		this.request = request;
	}
	public String getRequest() {
		return request;
	}

	public int getNow() {
		return now;
	}
}
