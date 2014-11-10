package interpreter;


/**
 * Class for Search Location command, which contains a search string and a boolean
 * variable to indicate missing arguments. 
 * @author a0066754w
 *
 */
public class CommandSearchLocation extends Command {
	String location = null;
	
	/**
	 * Constructor for the Search Location command object. It is initialised with a
	 * search string and a boolean variable to indicate if there are missing
	 * arguments.
	 * @param newLocation the search string which is used to find a task
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandSearchLocation(String newLocation, String userInput, 
			boolean hasMissingArgs) {
		super(Command.SEARCH_LOCATION, userInput, hasMissingArgs);
		location = newLocation;
	}
	
	/**
	 * Accessor for location string
	 * @return the search string used to find a task based on location
	 */
	public String getLocation() {
		return location;
	}
}
