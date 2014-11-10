package interpreter;
/**
 * Class for Update command, which contains line code to identify task to
 * update, and a boolean variable to indicate missing arguments. 
//@author A0115329J
 *
 */
public class CommandUpdate extends Command {
	String lineCode = null;
	CommandAdd updatedTask = null;
	
	/**
	 * Constructor for the Update command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineCode the line code of the task to be updated
	 * @param newUpdatedTask the new task to overwrite updated task
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandUpdate(String newLineCode, CommandAdd newUpdatedTask, 
			String userInput, boolean hasMissingArgs) {
		super(Command.UPDATE, userInput, hasMissingArgs);
		lineCode = newLineCode;
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
	 * Accessor for line code
	 * @return the line code of the task to be updated
	 */
	public String getLineCode() {
		return lineCode;
	}
	
}
