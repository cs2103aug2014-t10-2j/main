/**
 * Class for View command, which contains a string to indicate type of view and
 * a boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandView extends Command {
	String viewType = null;
	boolean missingArgs = false;
	
	/**
	 * Constructor for the View command object. It is initialised with a string
	 * indicating the type of view and a boolean variable to indicate if there
	 * are missing arguments.
	 * @param newViewType the type of view chosen to see tasks
	 * @param newMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandView(String newViewType, boolean newMissingArgs) {
		super(Interpreter.VIEW);
		viewType = newViewType;
		missingArgs = newMissingArgs;
	}
	
	/**
	 * Accessor for view type
	 * @return the type of view chosen to see tasks
	 */
	public String getViewType() {
		return viewType;
	}
	
	/**
	 * Accessor for missing arguments
	 * @return a boolean variable indicating if arguments are missing
	 */
	public boolean hasMissingArgs() {
		return missingArgs;
	}
	
}
