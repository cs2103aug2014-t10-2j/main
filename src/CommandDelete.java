/**
 * Class for Delete command, which contains line number to identify task to
 * delete, and a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandDelete extends Command{
	int INVALID_NO = -1;
	int lineNo = INVALID_NO;
	
	/**
	 * Constructor for the Delete command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineNo the line number of the task to be deleted
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDelete(int newLineNo, String userInput, boolean hasMissingArgs) {
		super(Command.DELETE, userInput, hasMissingArgs);
		lineNo = newLineNo;
	}
	
	/**
	 * Accessor for line number
	 * @return the line number of the task to be deleted
	 */
	public int getLineNo() {
		return lineNo;
	}
	
}
