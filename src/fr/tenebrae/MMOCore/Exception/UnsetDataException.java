package fr.tenebrae.MMOCore.Exception;

public class UnsetDataException extends Exception {
	/**
	 * Exception for unset data in the DataBase
	 */
	private static final long serialVersionUID = 1L;

	public UnsetDataException() {}
	
	public UnsetDataException(String msg) {
		super(msg);
	}
}
