package interpreter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class for Add command, which contains task name, 2 sets of date and time, a
 * list of tags , a location and boolean variable to indicate missing arguments.
 * 
//@author A0115329J
 * 
 */

public class CommandAdd extends Command {

	public static String DEFAULT_LOCATION = null; //"none";

	String taskName = null;
	Calendar startDate = null;
	Calendar endDate = null;
	ArrayList<String> tags = null;
	String location = DEFAULT_LOCATION;

	/**
	 * Constructor for the Add command object. It is initialised with a task
	 * name, start date and time, end date and time, a list of tags and a
	 * boolean variable to indicate if there are missing arguments.
	 * 
	 * @param newTaskName the task name of the new add command
	 * @param newStartDate the starting date and time of the task to add
	 * @param newEndDate the ending date and time of the task to add
	 * @param newTags the list of tags associated with the new add command
	 * @param newLocation the location of the new task to add
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandAdd(String newTaskName, Calendar newStartDate,
			Calendar newEndDate, ArrayList<String> newTags, String newLocation,
			String userInput, boolean hasMissingArgs) {
		super(Command.ADD, userInput, hasMissingArgs);
		taskName = newTaskName;
		startDate = newStartDate;
		endDate = newEndDate;
		tags = newTags;
		if (newLocation != null) {
			location = newLocation;
		}
	}

	/**
	 * Accessor for task name of the new task
	 * 
	 * @return the name of the task to be added
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * Accessor for starting date and time of the new task
	 * 
	 * @return starting date and time of the task to be added
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * Accessor for ending date and time of the new task
	 * 
	 * @return ending date and time of the task to be added
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Accessor for tags of the new task
	 * 
	 * @return an arraylist of the tags of the task to be added
	 */
	public ArrayList<String> getTags() {
		return tags;
	}

	/**
	 * Accessor for location of the new task
	 * 
	 * @return a string representing the location
	 */
	public String getLocation() {
		return location;
	}

}
