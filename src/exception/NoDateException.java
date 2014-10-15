package exception;

/**
 * Exception for tasks with no date
 * @author SP
 *
 */
public class NoDateException extends Exception {
	public NoDateException(String msg) {
		super(msg);
	}
}
