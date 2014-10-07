package zombietask;
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

	public static final int INVALID_NO = -1;

	/**
	 * Returns the command type based on the first word of the user input
	 * 
	 * @param userInput
	 *            the first word of the command from the user
	 * @return the command type, which is the first word in the user input or
	 *         ADD, if the first word is not a valid command
	 */
	public static String getCommandType(String firstWord) {
		if (Command.validCommands.contains(firstWord)) {
			return firstWord;
		} else {
			return Command.ADD;
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
				case Command.DELETE:
					if (userInputTokens.length > 1) {
						return getCommandDelete(userInputTokens[1], userInput);
					} else {
						return new CommandDelete(INVALID_NO, userInput, true);
					}
				case Command.UPDATE:
					if (userInputTokens.length > 1) {
						return getCommandUpdate(userInputTokens[1], userInput);
					} else {
						return new CommandUpdate(INVALID_NO, userInput, true);
					}
				case Command.VIEW:
					if (userInputTokens.length > 1) {
						return getCommandView(userInputTokens[1], userInput);
					} else {
						return new CommandView(UI.INVALID, userInput, true);
					}
				case Command.HELP:
					if (userInputTokens.length > 1) {
						return getCommandHelp(userInputTokens[1], userInput);
					} else {
						return new Command(null, userInput, true);
					}
				case Command.UNDO:
					return new Command(Command.UNDO, userInput, false);
				case Command.REDO:
					return new Command(Command.REDO, userInput, false);
				case Command.EXIT:
					return new Command(Command.EXIT, userInput, false);
				default:
					return getCommandAdd(userInput);
			}
		}
	}

	/**
	 * Creates a CommandView object based on user input
	 * 
	 * @param secondWord
	 *            the second word from the user input
	 * @param userInput
	 *            the original user input
	 * @return a CommandView object with a the view format and a boolean value
	 * to indicate missing arguments
	 */
	private static Command getCommandView(String secondWord, String userInput) {
		FORMAT viewType = UI.getFormat(secondWord);
		if (viewType == UI.INVALID) {
			return new CommandView(viewType, userInput, true);
		}
		return new CommandView(viewType, userInput, false);
	}
	
	/**
	 * Creates a CommandHelp object based on user input
	 * 
	 * @param secondWord
	 *            the second word from the user input
	 * @param userInput
	 *            the original user input
	 * @return a CommandHelp object with the command that the user needs help
	 * with and a boolean value to indicate missing arguments
	 */
	private static Command getCommandHelp(String secondWord, String userInput) {
		if (Command.validCommands.contains(secondWord)) {
			return new CommandHelp(secondWord, userInput, false);
		} else {
			return new CommandHelp(null, userInput, true);
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
	private static Calendar getDate(String userInput) throws Exception {
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
	private static ArrayList<String> getTags(String[] userInputWords) {
		ArrayList<String> tags = new ArrayList<String>();
		for (String word : userInputWords) {
			word = word.trim();
			if (word.length() > 1 && word.charAt(0) == '#') {
				tags.add(word);
			}
		}
		return tags;
	}

	/**
	 * Returns task name by removing time-related keywords and tags from the
	 * user input
	 * 
	 * @param userInput
	 *            the command entered by the user
	 * @param tags
	 *            a list of tags parsed from user input
	 * @return the task name, or a description of the task
	 * @throws Exception
	 *             no task name specified
	 */
	private static String getTaskName(String userInput, ArrayList<String> tags)
			throws Exception {
		// remove time-related keywords
		Parser parser = new Parser();
		String dateKeyWords = null;
		List<DateGroup> groups = parser.parse(userInput);
		for (DateGroup group : groups) {
			dateKeyWords = group.getText();
		}

		if (dateKeyWords != null && !dateKeyWords.isEmpty()) {
			userInput = userInput.replace(dateKeyWords + " ", "");
			userInput = userInput.replace(" " + dateKeyWords, "");
			userInput = userInput.replace(dateKeyWords, "");
		}

		// remove tags
		for (String tag : tags) {
			userInput = userInput.replaceAll(tag + " ", "");
			userInput = userInput.replaceAll(" " + tag, "");
			userInput = userInput.replaceAll(tag, "");
		}

		if (userInput.trim() == "") {
			throw new Exception("No task name.");
		}
		return userInput.trim();

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

		date = getDate(userInput);
		if (date == null) {
			hasMissingArgs = true; // indicates a floating task
		}

		tags = getTags(userInputWords);

		String userInputTemp = userInput.trim();
		if (userInputWords[0].equals(Command.ADD)) {
			userInputTemp = userInputTemp.substring(4);
		}

		taskName = getTaskName(userInputTemp, tags);

		return new CommandAdd(taskName, date, tags, userInput, hasMissingArgs);
	}

	/**
	 * Creates a CommandUpdate object from user input
	 * 
	 * @param secondWord
	 *            the second word of the user input
	 * @return a CommandUpdate object with integer value of line number and a
	 *         boolean value to indicate missing arguments
	 */
	private static CommandUpdate getCommandUpdate(String secondWord, String userInput) {
		try {
			int lineNo = Integer.parseInt(secondWord);
			return new CommandUpdate(lineNo, userInput, false);
		} catch (Exception e) { // invalid number
			return new CommandUpdate(INVALID_NO, userInput, true);
		}
	}

	/**
	 * Creates a CommandDelete object from user input
	 * 
	 * @param secondWord
	 *            the second word of the user input
	 * @return a CommandDelte object with integer value of line number and a
	 *         boolean value to indicate missing arguments
	 */
	private static CommandDelete getCommandDelete(String secondWord, 
			String userInput) {
		try {
			int lineNo = Integer.parseInt(secondWord);
			return new CommandDelete(lineNo, userInput, false);
		} catch (Exception e) { // invalid number
			return new CommandDelete(INVALID_NO, userInput, true);
		}
	}
}
