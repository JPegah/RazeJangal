package razeJangal.gui.protocol;

import java.io.Serializable;

/**
 * the number of the state which client is in 
 * @author Pegah Jandgahi
 *
 */
public class stateNum implements Serializable, X{
	private int n;
	public stateNum(){
		n = 0;
	}
	public void next(){
		n++;
	}
	
	public void back(){
		n--;
	}
	
	public int getS(){
		return n;
	}
	
	public void setS(int n){
		this.n = n;
	}

}
