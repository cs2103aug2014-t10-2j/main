package interpreter;
/**
 * Class for Delete command, which contains line code to identify task to
 * delete, and a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandDelete extends Command{
	String lineCode = null;
	
	/**
	 * Constructor for the Delete command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineCode the line code of the task to be deleted
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDelete(String newLineCode, String userInput, boolean hasMissingArgs) {
		super(Command.DELETE, userInput, hasMissingArgs);
		lineCode = newLineCode;
	}
	
	/**
	 * Accessor for line code
	 * @return the line code of the task to be deleted
	 */
	public String getLineCode() {
		return lineCode;
	}
	
}
