package interpreter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import ui.FORMAT;
import ui.UI;

import com.joestelmach.natty.*;

import exception.NoCommandException;
import exception.NoTaskNameException;

/**
 * Parses the command and returns a command object identifying the correct
 * command and arguments.
 * 
 * @author SP
 * 
 */
public class Interpreter {

	public static final int INVALID_NO = -1;
	public static final char ESCAPE_CHAR = '*';

	// valid constants for updating floating, deadline and timed task
	public static final char UPDATE_F = 'f';
	public static final char UPDATE_D = 'd';
	public static final char UPDATE_T = 't';

	/**
	 * Returns the command type based on the first word of the user input
	 * 
	 * @param firstWord the first word of the command from the user
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
	 * @param userInput the command from the user
	 * @return a command object with command type and arguments based on the
	 *         command type
	 * @throws NoCommandException for invalid user input, such as null or an
	 *             empty string
	 */
	public static Command getCommand(String userInput)
			throws NoCommandException {
		if (userInput == null || userInput.trim() == "") {
			throw new NoCommandException("No command entered");
		} else {

			String[] userInputTokens = userInput.trim().toLowerCase()
					.split(" ");

			String commandType = getCommandType(userInputTokens[0]);
			switch (commandType) {
				case Command.DELETE:
					if (userInputTokens.length > 1) {
						return getCommandDelete(userInputTokens[1], userInput);
					} else {
						return new CommandDelete(null, userInput, true);
					}
				case Command.UPDATE:
					if (userInputTokens.length > 2) {
						return getCommandUpdate(userInputTokens[1], userInput);
					} else {
						return new CommandUpdate(null, null, userInput, true);
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
						return new CommandHelp(null, userInput, true);
					}
				case Command.SEARCH_NAME:
					if (userInputTokens.length > 1) {
						return getCommandSearchName(userInput);
					} else {
						return new CommandSearchName(null, userInput, true);
					}
				case Command.SEARCH_TIME:
					if (userInputTokens.length > 1) {
						return getCommandSearchTime(userInput);
					} else {
						return new CommandSearchTime(null, null, userInput,
								true);
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

	private static Command getCommandSearchTime(String userInput) {
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(userInput);

		if (groups.isEmpty()) {
			return new CommandSearchTime(null, null, userInput, true);
		} else {
			// get first date
			Date date1 = groups.get(0).getDates().get(0);
			Date date2 = null;
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();

			// get second date
			try {
				date2 = groups.get(0).getDates().get(1);
				cal2.setTime(date2);
			} catch (Exception e) {
				try {
					date2 = groups.get(1).getDates().get(0);
					cal2.setTime(date2);
				} catch (Exception e1) { // only 1 date
					return new CommandSearchTime(null, null, userInput, true);
				}
			}

			if (cal1.compareTo(cal2) < 0) { // date1 before date2
				return new CommandSearchTime(cal1, cal2, userInput, false);
			}
			return new CommandSearchTime(cal2, cal1, userInput, false);
		}
	}

	/**
	 * Creates a CommandSearch name object based on user input
	 * 
	 * @param searchString the string used to search for task
	 * @param userInput the original user input
	 * @return a CommandSearch object with a search string, the original input
	 *         and a boolean value to indicate missing arguments
	 */
	private static Command getCommandSearchName(String userInput) {
		String searchString = userInput.replaceFirst(Command.SEARCH_NAME + " ",
				"");
		return new CommandSearchName(searchString, userInput, false);
	}

	/**
	 * Creates a CommandView object based on user input
	 * 
	 * @param secondWord the second word from the user input
	 * @param userInput the original user input
	 * @return a CommandView object with the view format, the original input and
	 *         a boolean value to indicate missing arguments
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
	 * @param secondWord the second word from the user input
	 * @param userInput the original user input
	 * @return a CommandHelp object with the command that the user needs help
	 *         with, the original input and a boolean value to indicate missing
	 *         arguments
	 */
	private static Command getCommandHelp(String secondWord, String userInput) {
		if (Command.validCommands.contains(secondWord)) {
			return new CommandHelp(secondWord, userInput, false);
		} else {
			return new CommandHelp(null, userInput, true);
		}
	}

	/**
	 * Returns tags from contained in the command entered by the user
	 * 
	 * @param userInputWords an array of words entered by the user
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
	 * @param userInput the command entered by the user
	 * @param tags a list of tags parsed from user input
	 * @return the task name, or a description of the task
	 * @throws NoTaskNameException no task name specified
	 */
	private static String getTaskName(String userInput, ArrayList<String> tags)
			throws NoTaskNameException {

		// get and remove first time-related keyword
		Parser parser = new Parser();
		// remove escaped numbers and words
		String escapePattern = "\\s\\" + ESCAPE_CHAR + "\\S+";
		String userInputTemp = userInput;
		userInputTemp = userInputTemp.replaceAll(escapePattern, "");

		List<DateGroup> groups = parser.parse(userInputTemp);
		String dateKeywords = null;

		if (groups.isEmpty()) {
			dateKeywords = "";
		} else {
			dateKeywords = groups.get(0).getText();
		}

		if (dateKeywords != null && !dateKeywords.isEmpty()) {
			userInput = userInput.replaceFirst(dateKeywords + " ", "");
			userInput = userInput.replaceFirst(" " + dateKeywords, "");
			userInput = userInput.replaceFirst(dateKeywords, "");
		}

		// remove tags
		for (String tag : tags) {
			userInput = userInput.replaceAll(tag + " ", "");
			userInput = userInput.replaceAll(" " + tag, "");
			userInput = userInput.replaceAll(tag, "");
		}

		if (userInput.trim() == "") {
			throw new NoTaskNameException("No task name.");
		}
		return userInput.trim();

	}

	/**
	 * Creates a CommandAdd or CommandAddFloating or CommandAddTimed object from
	 * user input
	 * 
	 * @return a CommandAdd or CommandAddFloating or CommandAddTimed object
	 *         which contains task name, date and time, a list of tags, the
	 *         original input and a boolean variable to indicate missing
	 *         arguments
	 */
	private static Command getCommandAdd(String userInput) {

		ArrayList<String> tags = new ArrayList<String>();
		String taskName = null;
		String[] userInputWords = userInput.trim().toLowerCase().split(" ");

		// remove escaped numbers or words
		// remove escaped numbers and words
		String escapePattern = "\\s\\" + ESCAPE_CHAR + "\\S+";
		String userInputTemp = userInput;
		while (userInputTemp.contains(" " + ESCAPE_CHAR)) {
			userInputTemp = userInputTemp.replaceAll(escapePattern, "");
		}

		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(userInputTemp);

		tags = getTags(userInputWords);

		// remove leading and trailing spaces
		userInputTemp = userInput.trim();
		if (userInputWords[0].equals(Command.ADD)) {
			userInputTemp = userInputTemp.substring(4);
		}

		try {

			if (groups.isEmpty()) { // floating task

				taskName = getTaskName(userInputTemp, tags);
				taskName = taskName.replace(" " + ESCAPE_CHAR, " ");
				return new CommandAdd(taskName, null, null, tags, userInput,
						false);
			} else {

				// get first date
				Date date1 = groups.get(0).getDates().get(0);
				Date date2 = null;
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(date1);
				Calendar cal2 = Calendar.getInstance();

				// get second date
				try {
					date2 = groups.get(0).getDates().get(1);
					cal2.setTime(date2);

				} catch (Exception e) {
					try {
						date2 = groups.get(1).getDates().get(0);
						cal2.setTime(date2);
					} catch (Exception e1) { // only 1 date: deadline task
						taskName = getTaskName(userInputTemp, tags);
						taskName = taskName.replace(" " + ESCAPE_CHAR, " ");
						return new CommandAdd(taskName, cal1, null, tags,
								userInput, false);
					}
				}

				// this removes the first 2 time-related keywords
				taskName = getTaskName(userInputTemp, tags);
				ArrayList<String> tagsTemp = new ArrayList<String>();
				taskName = getTaskName(taskName, tagsTemp);
				taskName = taskName.replace(" " + ESCAPE_CHAR, " ");

				if (cal1.compareTo(cal2) < 0) { // date1 before date2
					return new CommandAdd(taskName, cal1, cal2, tags,
							userInput, false);
				}
				return new CommandAdd(taskName, cal2, cal1, tags, userInput,
						false);
			}
		} catch (NoTaskNameException e) {
			return new CommandAdd(null, null, null, null, userInput, true);
		}

	}

	/**
	 * Creates a CommandUpdate object from user input
	 * 
	 * @param secondWord the second word of the user input
	 * @return a CommandUpdate object with integer value of line number, the
	 *         original input and a boolean value to indicate missing arguments
	 * @throws Exception for invalid line number or date
	 */
	private static CommandUpdate getCommandUpdate(String secondWord,
			String userInput) {
		try {
			String lineCode = null;
			secondWord = secondWord.toLowerCase();
			boolean validSecondWord = secondWord.matches("[" + UPDATE_F
					+ UPDATE_D + UPDATE_T + "][0-9]+");

			if (validSecondWord) {
				lineCode = secondWord;
				// get third word onwards to parse as an add command
				int spaceIndex = userInput.indexOf(" ");
				String userInputTemp = userInput.substring(spaceIndex + 1);
				spaceIndex = userInputTemp.indexOf(" ");
				userInputTemp = userInputTemp.substring(spaceIndex + 1);

				CommandAdd updatedTask = (CommandAdd) getCommandAdd(userInputTemp);
				return new CommandUpdate(lineCode, updatedTask, userInput,
						false);
			} else {
				return new CommandUpdate(null, null, userInput, true);
			}

		} catch (Exception e) { // invalid date
			return new CommandUpdate(null, null, userInput, true);
		}
	}

	/**
	 * Creates a CommandDelete object from user input
	 * 
	 * @param secondWord the second word of the user input
	 * @return a CommandDelete object with integer value of line number, the
	 *         original input and a boolean value to indicate missing arguments
	 */
	private static CommandDelete getCommandDelete(String secondWord,
			String userInput) {
		secondWord = secondWord.toLowerCase();
		boolean validSecondWord = secondWord.matches("[" + UPDATE_F + UPDATE_D
				+ UPDATE_T + "][0-9]+");
		
		if (validSecondWord) {
			return new CommandDelete(secondWord, userInput, false);
		} else {
			return new CommandDelete(null, userInput, true);
		}
	}
}
