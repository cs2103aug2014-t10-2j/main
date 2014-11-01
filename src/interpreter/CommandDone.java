package interpreter;
/**
 * Class for Done command, which contains a string to 
 * indicate the task to be checked/unchecked as don and
 * a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandDone extends Command {
	String lineCode = null;
	
	/**
	 * Constructor for the Done command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineCode the line code of the task to be updated
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDone(String newLineCode, 
			String userInput, boolean hasMissingArgs) {
		super(Command.DONE, userInput, hasMissingArgs);
		lineCode = newLineCode;
	}
	
	/**
	 * Accessor for line code
	 * @return the line code of the task to be updated
	 */
	public String getLineCode() {
		return lineCode;
	}
}
