package de.unifrankfurt.texttechnologie.data;
/**
 * MatrixException if the given Matrix is not squared.
 * @author Timo Homburg
 *
 */
public class MatrixException extends Exception {
	/**The cause of the exception.*/
	private final String cause;
	/**
	 * Constructor for MatrixException.
	 * @param cause the cause of the exception
	 */
	public MatrixException(final String cause){
		this.cause=cause;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return this.cause;
	}
}
