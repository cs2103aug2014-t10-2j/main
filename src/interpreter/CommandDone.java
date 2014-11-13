package interpreter;

import java.util.ArrayList;

/**
 * Class for Done command, which contains a string to 
 * indicate the task to be checked/unchecked as don and
 * a boolean variable to indicate missing arguments. 
//@author a0066754w
 */
public class CommandDone extends Command {
	ArrayList<String> lineCode = null;
	
	/**
	 * Constructor for the Done command object. It is initialised with a line
	 * number and a boolean variable to indicate if there are missing arguments.
	 * @param newLineCode the line code of the task to be updated
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDone(ArrayList<String> newLineCode, 
			String userInput, boolean hasMissingArgs) {
		super(Command.DONE, userInput, hasMissingArgs);
		lineCode = newLineCode;
	}
	
	/**
	 * Accessor for line codes
	 * @return the line codes of the task to be updated
	 */
	public ArrayList<String> getLineCode() {
		return lineCode;
	}
}
