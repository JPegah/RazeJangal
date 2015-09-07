package razeJangal.gui.protocol;

import java.io.Serializable;
/**
 * confirmation of the client
 * @author Pegah Jandaghi
 *
 */
public class cheat implements Serializable{
	private boolean isCheat;
	public cheat(boolean i){
		isCheat = i;
	}
	
	public boolean getCheat(){
		return isCheat;
	}
}
