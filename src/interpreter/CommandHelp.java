package interpreter;
/**
 * Class for Help command, which contains a string to indicate type of view and
 * a boolean variable to indicate missing arguments. 
//@author A0115329J
 *
 */
public class CommandHelp extends Command {
	String helpCommand = null;
	
	/**
	 * Constructor for the Help command object. It is initialised with a string
	 * indicating the command that the user needs help with and a boolean 
	 * variable to indicate if there are missing arguments.
	 * @param newHelpCommand the command that the user needs help with
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandHelp(String newHelpCommand, String userInput, boolean hasMissingArgs) {
		super(Command.HELP, userInput, hasMissingArgs);
		helpCommand = newHelpCommand;
	}
	
	/**
	 * Accessor for help command
	 * @return the command that the user needs help with
	 */
	public String getHelpCommand() {
		return helpCommand;
	}
	
}
