package zombietask;
import java.util.ArrayList;

/**
 * Class for Add Floating Task command, which contains task name, a list of
 * tags and boolean variable to indicate missing arguments.
 * @author SP
 *
 */
public class CommandAddFloating extends Command {
	String taskName = null;
	ArrayList<String> tags = null;
	
	/**
	 * Constructor for the Add Timed Task command object. It is initialised
	 * with a task name, a list of tags and a boolean variable to indicate if
	 * there are missing arguments.
	 * @param newTaskName the task name of the new add command
	 * @param newTags the list of tags associated with the new add command
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandAddFloating(String newTaskName, ArrayList<String> newTags, 
			String userInput, boolean hasMissingArgs) {
		super(Command.ADD, userInput, hasMissingArgs);
		taskName = newTaskName;
		tags = newTags;
	}
	
	/**
	 * Accessor for task name of the new task
	 * @return the name of the task to be added
	 */
	public String getTaskName() {
		return taskName;
	}
	
	/**
	 * Accessor for tags of the new task
	 * @return an arraylist of the tags of the task to be added
	 */
	public ArrayList<String> getTags() {
		return tags;
	}
	
}
