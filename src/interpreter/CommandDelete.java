package interpreter;

import java.util.ArrayList;

/**
 * Class for Delete command, which contains line codes to identify task to
 * delete, and a boolean variable to indicate missing arguments.
 * 
 * @author SP
 *
 */
public class CommandDelete extends Command {
	ArrayList<String> lineCode = null;

	/**
	 * Constructor for the Delete command object. It is initialised with a list
	 * of line codes and a boolean variable to indicate if there are missing
	 * arguments.
	 * 
	 * @param newLineCode the line code of the task to be deleted
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDelete(ArrayList<String> newLineCode, String userInput,
			boolean hasMissingArgs) {
		super(Command.DELETE, userInput, hasMissingArgs);
		lineCode = newLineCode;
	}

	/**
	 * Accessor for line code
	 * 
	 * @return a list of line codes of the tasks to be deleted
	 */
	public ArrayList<String> getLineCode() {
		return lineCode;
	}

}
