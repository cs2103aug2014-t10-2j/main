package interpreter;
/**
 * Class for Delete Name command, which contains a search string and a boolean
 * variable to indicate missing arguments. 
 * @author a0066754w
 *
 */
public class CommandDeleteName extends Command {
	String searchString = null;
	

	/**
	 * Constructor for the Search Name command object. It is initialised with a
	 * search string and a boolean variable to indicate if there are missing
	 * arguments.
	 * @param newSearchString the search string which is used to find a task
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDeleteName(String newSearchString, String userInput, 
			boolean hasMissingArgs) {
		super(Command.DELETE_NAME, userInput, hasMissingArgs);
		searchString = newSearchString;
	}
	
	/**
	 * Accessor for search string
	 * @return the search string used to find a task
	 */
	public String getSearchString() {
		return searchString;
	}

}
