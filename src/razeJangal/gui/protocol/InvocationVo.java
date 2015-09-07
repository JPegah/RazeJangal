package razeJangal.gui.protocol;

import java.io.Serializable;
/**
 * Information of the method to be invoked
 * @author Pegah Jandaghi
 *
 */
public class InvocationVo implements Serializable {
	public String fullClassName;
	public String methodName;
	public Object[] parameters;

	public InvocationVo(String clazz, String method, Object... params) {
		this.fullClassName = clazz;
		this.methodName = method;
		this.parameters = params;
	}
}