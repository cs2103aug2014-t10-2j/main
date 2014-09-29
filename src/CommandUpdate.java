/**
 * Class for Update command, which contains line number to identify task to
 * update, and a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandUpdate extends Command {
	int INVALID_NO = -1;
	int lineNo = INVALID_NO;
	boolean missingArgs = false;
	
	/**
	 * Constructor for the Update command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineNo the line number of the task to be updated
	 * @param newMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandUpdate( int newLineNo, boolean newMissingArgs) {
		super(Interpreter.UPDATE);
		lineNo = newLineNo;
		missingArgs = newMissingArgs;
	}
	
	/**
	 * Accessor for line number
	 * @return the line number of the task to be updated
	 */
	public int getLineNo() {
		return lineNo;
	}
	
	/**
	 * Accessor for missing arguments
	 * @return a boolean variable indicating if arguments are missing
	 */
	public boolean hasMissingArgs() {
		return missingArgs;
	}
	
}
