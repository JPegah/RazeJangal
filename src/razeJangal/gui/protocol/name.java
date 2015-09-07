package razeJangal.gui.protocol;

import java.io.Serializable;
/**
 * contains the name of the player
 * @author Pegah Jandaghi
 *
 */
public class name implements Serializable{
	private String name;
	public name(String name){
		this.name = name;
	}
	
	public String getname(){
		return this.name;
	}
}
