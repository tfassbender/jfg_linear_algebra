package net.jfabricationgames.linear_algebra;

/**
 * 
 * @author Tobias Fa�bender
 * @version Jul 9, 2016
 */
public class LinearAlgebraException extends RuntimeException {

	private static final long serialVersionUID = -3650811745064691723L;
	
	public LinearAlgebraException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	public LinearAlgebraException(String arg0) {
		super(arg0);
	}
}
