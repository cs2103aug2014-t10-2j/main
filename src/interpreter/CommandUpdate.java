package interpreter;
/**
 * Class for Update command, which contains line number to identify task to
 * update, and a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandUpdate extends Command {
	int INVALID_NO = -1;
	int lineNo = INVALID_NO;
	CommandAdd updatedTask = null;
	
	/**
	 * Constructor for the Update command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineNo the line number of the task to be updated
	 * @param newUpdatedTask the new task to overwrite updated task
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandUpdate( int newLineNo, CommandAdd newUpdatedTask, 
			String userInput, boolean hasMissingArgs) {
		super(Command.UPDATE, userInput, hasMissingArgs);
		lineNo = newLineNo;
		updatedTask = newUpdatedTask;
	}
	
	/**
	 * Accessor for updated task
	 * @return new task for updating
	 */
	public CommandAdd getUpdatedTask() {
		return updatedTask;
	}
	
	/**
	 * Accessor for line number
	 * @return the line number of the task to be updated
	 */
	public int getLineNo() {
		return lineNo;
	}
	
}
