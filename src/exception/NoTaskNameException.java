package exception;

/**
 * Exception for tasks with no task name
 * @author SP
 *
 */
public class NoTaskNameException extends Exception {
	public NoTaskNameException(String msg) {
		super(msg);
	}
}
