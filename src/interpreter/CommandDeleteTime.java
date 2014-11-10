package interpreter;

import java.util.Calendar;

/**
 * Class for Delete Time command, which contains 2 sets of date and time and a
 * boolean variable to indicate missing arguments. 
 * @author a0066754w
 *
 */
public class CommandDeleteTime extends Command {
	Calendar timeStart = null;
	Calendar timeEnd = null;
	
	/**
	 * Constructor for the Delete Time command object. It is initialised with a
	 * search string and a boolean variable to indicate if there are missing
	 * arguments.
	 * @param newTimeStart to find tasks which are later than this time
	 * @param newTimeEnd to find tasks which are earlier than this time
	 * @param userInput the original user input
	 * @param hasMissingArgs boolean variable to indicate missing arguments
	 */
	public CommandDeleteTime(Calendar newTimeStart, Calendar newTimeEnd, 
			String userInput, boolean hasMissingArgs) {
		super(Command.DELETE_TIME, userInput, hasMissingArgs);
		timeStart = newTimeStart;
		timeEnd = newTimeEnd;
	}
	
	/**
	 * Accessor for time start
	 * @return the starting time used to find a task
	 */
	public Calendar getTimeStart() {
		return timeStart;
	}
	
	/**
	 * Accessor for time end
	 * @return the ending time used to find a task
	 */
	public Calendar getTimeEnd() {
		return timeEnd;
	}
}
