import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

/**
 * Parses the command and returns a command object identifying the correct
 * command and arguments.
 * 
 * @author SP
 * 
 */
public class Interpreter {

	// valid command types
	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String UPDATE = "update";
	public static final String VIEW = "view";
	public static final String UNDO = "undo";

	/**
	 * Returns the command type based on the first word of the user input
	 * 
	 * @param userInput
	 *            the first word of the command from the user
	 * @return the command type, which is the first word in the user input or
	 *         ADD, if the first word is not a valid command
	 */
	public static String getCommandType(String firstWord) {
		List<String> validCommands = Arrays.asList(ADD, DELETE, UPDATE, VIEW,
				UNDO);
		if (validCommands.contains(firstWord)) {
			return firstWord;
		} else {
			return ADD;
		}
	}

	/**
	 * Parses the user input and returns a command object containing the command
	 * type and its arguments
	 * 
	 * @param userInput
	 *            the command from the user
	 * @return a command object with command type and arguments based on the
	 *         command type
	 * @throws Exception
	 *             for invalid user input, such as null or an empty string
	 */
	public static Command getCommand(String userInput) throws Exception {
		if (userInput == null || userInput.trim() == "") {
			throw new Exception("No command entered.");
		} else {

			String[] userInputTokens = userInput.trim().toLowerCase()
					.split(" ");

			String commandType = getCommandType(userInputTokens[0]);
			switch (commandType) {
				case DELETE:
					return getCommandDelete(userInput);
				case UPDATE:
					return getCommandUpdate(userInput);
				case VIEW:
					return getCommandUpdate(userInput);
				case UNDO:
					return new Command("undo");
				default:
					return getCommandAdd(userInput);
			}
		}
	}

	/**
	 * Returns date based on somewhat natural language
	 * 
	 * @param userInput
	 *            the command entered by user
	 * @return a calendar date object indicating the date and time
	 * @throws Exception
	 *             for unspecified date and time
	 */
	public static Calendar getDate(String userInput) throws Exception {
		Calendar cal = Calendar.getInstance();
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(userInput);
		for (DateGroup group : groups) {
			List<Date> dates = group.getDates();
			if (dates.isEmpty()) {
				throw new Exception("No date/time given.");
			} else {
				Date date = dates.get(0);
				cal.setTime(date);

				return cal;
			}
		}
		return null;
	}

	/**
	 * Returns tags from contained in the command entered by the user
	 * 
	 * @param userInputWords
	 *            an array of words entered by the user
	 * @return an arraylist of the tags contained in the command
	 */
	public static ArrayList<String> getTags(String[] userInputWords) {
		ArrayList<String> tags = new ArrayList<String>();
		for (String word : userInputWords) {
			if (word.charAt(0) == '#') {
				tags.add(word);
			}
		}
		return tags;
	}
	
	/**
	 * Returns task name by removing time-related keywords and tags from the
	 * user input
	 * @param userInput the command entered by the user
	 * @param tags a list of tags parsed from user input
	 * @return the task name, or a description of the task
	 * @throws Exception no task name specified
	 */
	public static String getTaskName(String userInput, ArrayList<String> tags) 
			throws Exception {
		// remove time-related keywords
		Parser parser = new Parser();
		String dateKeyWords = null;
		List<DateGroup> groups = parser.parse(userInput);
		for (DateGroup group : groups) {
			dateKeyWords = group.getText();
		}
			
		userInput = userInput.replace(dateKeyWords + " ", "");
		userInput = userInput.replace(" " + dateKeyWords, "");
		userInput = userInput.replace(dateKeyWords, "");
		// remove tags
		for (String tag: tags) {
			userInput = userInput.replaceAll(tag + " ", "");
			userInput = userInput.replaceAll(" " + tag, "");
			userInput = userInput.replaceAll(tag, "");
		}
		
		if (userInput.trim() == "") {
			throw new Exception("No task name.");
		}
		return userInput;

	}

	/**
	 * Creates a CommandAdd object from user input
	 * 
	 * @return a CommandAdd object which contains task name, date and time, a
	 *         list of tags and a boolean variable to indicate missing arguments
	 */
	private static CommandAdd getCommandAdd(String userInput) throws Exception {
		Calendar date = null;
		ArrayList<String> tags = new ArrayList<String>();
		Boolean hasMissingArgs = false;
		String taskName = null;
		String[] userInputWords = userInput.trim().toLowerCase().split(" ");

		try {
			date = getDate(userInput);
		} catch (Exception e) {
			hasMissingArgs = true;
		}

		tags = getTags(userInputWords);

		if (userInputWords[0].equals(ADD)) {
			userInput = userInput.substring(4);
		}
		
		taskName = getTaskName(userInput, tags);

		return new CommandAdd(taskName, date, tags, hasMissingArgs);
	}

	private static CommandUpdate getCommandUpdate(String userInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private static CommandDelete getCommandDelete(String userInput) {
		// TODO Auto-generated method stub
		return null;
	}
}
