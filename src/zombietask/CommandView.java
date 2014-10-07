package zombietask;
/**
 * Class for View command, which contains a string to indicate type of view and
 * a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandView extends Command {
	FORMAT viewType = null;
	
	/**
	 * Constructor for the View command object. It is initialised with a string
	 * indicating the type of view and a boolean variable to indicate if there
	 * are missing arguments.
	 * @param newViewType the type of view chosen to see tasks
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandView(FORMAT newViewType, String userInput, boolean hasMissingArgs) {
		super(Command.VIEW, userInput, hasMissingArgs);
		viewType = newViewType;
	}
	
	/**
	 * Accessor for view type
	 * @return the type of view chosen to see tasks
	 */
	public FORMAT getViewType() {
		return viewType;
	}
	
}
