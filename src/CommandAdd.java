import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class for Add command, which contains task name, date and time, a list of
 * tags and boolean variable to indicate missing arguments.
 * @author SP
 *
 */
public class CommandAdd extends Command {
	String taskName = null;
	Calendar dateTime = null;
	ArrayList<String> tags = null;
	
	/**
	 * Constructor for the Add command object. It is initialised with a task
	 * name, the date and time, a list of tags and a boolean variable to
	 * indicate if there are missing arguments.
	 * @param newTaskName the task name of the new add command
	 * @param newDateTime the date and time of the new add command
	 * @param newTags the list of tags associated with the new add command
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandAdd(String newTaskName, Calendar newDateTime, 
			ArrayList<String> newTags, String userInput, boolean hasMissingArgs) {
		super(Command.ADD, userInput, hasMissingArgs);
		taskName = newTaskName;
		dateTime = newDateTime;
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
	 * Accessor for date and time of the new task
	 * @return date and time of the task to be added
	 */
	public Calendar getDateTime() {
		return dateTime;
	}
	
	/**
	 * Accessor for tags of the new task
	 * @return an arraylist of the tags of the task to be added
	 */
	public ArrayList<String> getTags() {
		return tags;
	}
	
}
