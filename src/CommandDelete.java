/**
 * Class for Delete command, which contains line number to identify task to
 * delete, and a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandDelete extends Command{
	int INVALID_NO = -1;
	int lineNo = INVALID_NO;
	boolean missingArgs = false;
	
	/**
	 * Constructor for the Delete command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineNo the line number of the task to be deleted
	 * @param newMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDelete(int newLineNo, boolean newMissingArgs) {
		super(Interpreter.DELETE);
		lineNo = newLineNo;
		missingArgs = newMissingArgs;
	}
	
	/**
	 * Accessor for line number
	 * @return the line number of the task to be deleted
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
