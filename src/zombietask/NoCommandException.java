package zombietask;

/**
 * Exception for empty user input
 * @author SP
 *
 */
public class NoCommandException extends Exception {
	public NoCommandException(String msg) {
		super(msg);
	}
}
