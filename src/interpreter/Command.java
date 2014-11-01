package interpreter;

import java.util.Arrays;
import java.util.List;

/**
 * Class to create command objects with command type and arguments. Super-class
 * of CommandAdd, CommandDelete, CommandUpdate, CommandView and CommandUndo.
 * 
 * @author SP
 * 
 */
public class Command {

	// static command types
	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String UPDATE = "update";
	public static final String VIEW = "view";
	public static final String UNDO = "undo";
	public static final String REDO = "redo";
	public static final String HELP = "help";
	public static final String DONE = "done";
	public static final String SEARCH = "search";
	public static final String SEARCH_NAME = "search-name";
	public static final String SEARCH_TIME = "search-time";
	public static final String SEARCH_TAG = "search-tag";
	public static final String SEARCH_LOCATION = "search-location";
	public static final String EXIT = "exit";

	// list for ease of checking valid commands
	public static List<String> validCommands = Arrays.asList(Command.ADD,
			Command.DELETE, Command.UPDATE, Command.VIEW, Command.UNDO,
			Command.REDO, Command.HELP, Command.DONE, Command.SEARCH, Command.SEARCH_NAME,
			Command.SEARCH_TIME, Command.EXIT);

	String commandType = null;
	String userInput = null;
	boolean hasMissingArgs = false;

	/**
	 * Constructor for command object. It is initialised with a string
	 * indicating the command type
	 * 
	 * @param newCommandType the type of command
	 * @param newUserInput the original user input
	 * @param newHasMissingArgs whether the user input has missing arguments
	 */
	public Command(String newCommandType, String newUserInput,
			boolean newHasMissingArgs) {
		commandType = newCommandType;
		userInput = newUserInput;
		hasMissingArgs = newHasMissingArgs;
	}

	/**
	 * Accessor for the type of command, which can be Add, Delete, Update, View
	 * or Undo.
	 * 
	 * @return commandType the type of command
	 */
	public String getCommandType() {
		return commandType;
	}

	/**
	 * Accessor for user input
	 * 
	 * @return the original user input for this command
	 */
	public String getUserInput() {
		return userInput;
	}

	/**
	 * Accessor for missing arguments
	 * 
	 * @return a boolean variable indicating if arguments are missing
	 */
	public boolean hasMissingArgs() {
		return hasMissingArgs;
	}

}
