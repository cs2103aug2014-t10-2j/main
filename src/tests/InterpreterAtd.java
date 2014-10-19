package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import interpreter.Command;
import interpreter.CommandAdd;
import interpreter.CommandDelete;
import interpreter.CommandHelp;
import interpreter.CommandSearchName;
import interpreter.CommandSearchTime;
import interpreter.CommandUpdate;
import interpreter.CommandView;
import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

import ui.UI;

/**
 * Automated test driver for Interpreter class.
 * 
 * @author SP
 * 
 */
public class InterpreterAtd {

	@Test
	public void testGetCommandType() {
		// Test 1: Basic function
		assertEquals(Command.ADD, Interpreter.getCommandType("add"));
		assertEquals(Command.ADD, Interpreter.getCommandType("breakfast"));
		assertEquals(Command.DELETE, Interpreter.getCommandType("delete"));
		assertEquals(Command.UNDO, Interpreter.getCommandType("undo"));
		assertEquals(Command.REDO, Interpreter.getCommandType("redo"));
		assertEquals(Command.UPDATE, Interpreter.getCommandType("update"));
		assertEquals(Command.HELP, Interpreter.getCommandType("help"));
		assertEquals(Command.EXIT, Interpreter.getCommandType("exit"));
	}

	@Test
	public void testGetCommandAddFloating() throws Exception {
		// to test parsing of floating tasks

		// Test 1: Basic test
		Command command = Interpreter.getCommand("add #food eat "
				+ "something #impt");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;
		testTags(add.getTags(), "#food #impt");

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		assertEquals("eat something", add.getTaskName());
		assertEquals("add #food eat something #impt", add.getUserInput());

		// Test 2: case-insensitivity and white space
		command = Interpreter.getCommand("   Add    \n#food Eat #impt");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		testTags(add.getTags(), "#food #impt");
		assertEquals("Eat", add.getTaskName());
		assertEquals("   Add    \n#food Eat #impt", add.getUserInput());

		// Test 3: unspecified command
		command = Interpreter.getCommand("Lunch #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		assertEquals("Lunch", add.getTaskName());
		assertEquals("Lunch #impt #food", add.getUserInput());

	}

	@Test
	public void testGetCommandAddDeadline() throws Exception {
		// to test parsing of deadline tasks

		// Test 1: Basic test
		Command command = Interpreter.getCommand("add today #food eat "
				+ "something #impt");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;

		Calendar today = Calendar.getInstance();
		testDate(add.getStartDate(), today);
		assertNull(add.getEndDate());

		testTags(add.getTags(), "#food #impt");
		assertEquals("eat something", add.getTaskName());
		assertEquals("add today #food eat something #impt", add.getUserInput());

		// Test 2: case-insensitivity and white space
		command = Interpreter.getCommand("   Add   30 Sep \n#food Eat #impt");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		testDate(add.getStartDate(), today.get(Calendar.YEAR), 8, 30,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		assertNull(add.getEndDate());

		testTags(add.getTags(), "#food #impt");
		assertEquals("Eat", add.getTaskName());
		assertEquals("   Add   30 Sep \n#food Eat #impt", add.getUserInput());

		// Test 3: unspecified command
		command = Interpreter.getCommand("30 Sep Lunch #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;
		testDate(add.getStartDate(), 2014, 8, 30,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		assertNull(add.getEndDate());
		assertEquals("Lunch", add.getTaskName());
		assertEquals("30 Sep Lunch #impt #food", add.getUserInput());

	}

	@Test
	public void testGetCommandAddTimed() throws Exception {
		// to test parsing of timed tasks

		// Test 1: Basic test
		Command command = Interpreter.getCommand("add today 1pm #food eat "
				+ "something #impt 2pm");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;
		Calendar today = Calendar.getInstance();
		testDate(add.getStartDate(), today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DATE), 13, 0, 0);
		testDate(add.getEndDate(), today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DATE), 14, 0, 0);

		testTags(add.getTags(), "#food #impt");
		assertEquals("eat something", add.getTaskName());
		assertEquals("add today 1pm #food eat something #impt 2pm",
				add.getUserInput());
	}

	/**
	 * Helper method to check dates
	 * 
	 * @param date date expected
	 * @param year year of the date expected
	 * @param month month of the date expected
	 * @param day day of the date expected
	 * @param hour hour of the time expected
	 * @param minute minute of the time expected
	 * @param second second of the time expected
	 */
	public static void testDate(Calendar date, int year, int month, int day,
			int hour, int minute, int second) {
		assertEquals(year, date.get(Calendar.YEAR));
		assertEquals(month, date.get(Calendar.MONTH));
		assertEquals(day, date.get(Calendar.DATE));
		assertEquals(hour, date.get(Calendar.HOUR_OF_DAY));
		assertEquals(minute, date.get(Calendar.MINUTE));
		assertEquals(second, date.get(Calendar.SECOND));
	}

	/**
	 * Helper method to check dates
	 * 
	 * @param date date to check
	 * @param dateExpected date expected
	 */
	public static void testDate(Calendar date, Calendar dateExpected) {
		assertEquals(dateExpected.get(Calendar.YEAR), date.get(Calendar.YEAR));
		assertEquals(dateExpected.get(Calendar.MONTH), date.get(Calendar.MONTH));
		assertEquals(dateExpected.get(Calendar.DATE), date.get(Calendar.DATE));
		assertEquals(dateExpected.get(Calendar.HOUR_OF_DAY),
				date.get(Calendar.HOUR_OF_DAY));
		assertEquals(dateExpected.get(Calendar.MINUTE),
				date.get(Calendar.MINUTE));
		assertEquals(dateExpected.get(Calendar.SECOND),
				date.get(Calendar.SECOND));
	}

	/**
	 * Helper method to test tags
	 * 
	 * @param tags tags obtained from test
	 * @param tagsExpected expected output of test, separated by spaces
	 */
	public static void testTags(ArrayList<String> tags, String tagsExpected) {
		String[] tagTokens = tagsExpected.split(" ");
		assertEquals(Arrays.asList(tagTokens), tags);
	}

	@Test
	public void testGetCommandDelete() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("delete 5");
		assertEquals(Command.DELETE, command.getCommandType());
		CommandDelete delete = (CommandDelete) command;
		assertEquals(5, delete.getLineNo());
		assertEquals("delete 5", delete.getUserInput());

		// Test 2: case-insensitivity
		command = Interpreter.getCommand("DELETE 5");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals(5, delete.getLineNo());
		assertEquals("DELETE 5", delete.getUserInput());

		// Test 3: invalid arguments
		command = Interpreter.getCommand("delete me");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals(Interpreter.INVALID_NO, delete.getLineNo());
		assertTrue(delete.hasMissingArgs());
		assertEquals("delete me", delete.getUserInput());

		command = Interpreter.getCommand("delete");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals(Interpreter.INVALID_NO, delete.getLineNo());
		assertTrue(delete.hasMissingArgs());
		assertEquals("delete", delete.getUserInput());
	}

	@Test
	public void testGetCommandUpdate() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("update 2 go lunch sep 21"
				+ " #food");
		assertEquals(Command.UPDATE, command.getCommandType());
		CommandUpdate update = (CommandUpdate) command;
		assertEquals(2, update.getLineNo());
		assertEquals("update 2 go lunch sep 21 #food", update.getUserInput());

		CommandAdd updatedTask = update.getUpdatedTask();
		testTags(updatedTask.getTags(), "#food");
		assertEquals("go lunch", updatedTask.getTaskName());
		Calendar today = Calendar.getInstance();
		testDate(updatedTask.getStartDate(), today.get(Calendar.YEAR), 8, 21,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));

		// Test 2: case-insensitivity
		command = Interpreter.getCommand("uPdATe 2 go lunch sep 21" + " #food");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(2, update.getLineNo());
		assertEquals("uPdATe 2 go lunch sep 21 #food", update.getUserInput());

		updatedTask = update.getUpdatedTask();
		assertEquals("go lunch", updatedTask.getTaskName());
		testTags(updatedTask.getTags(), "#food");
		testDate(updatedTask.getStartDate(), today.get(Calendar.YEAR), 8, 21,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));;

		// Test 3: invalid arguments
		command = Interpreter.getCommand("update me");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(Interpreter.INVALID_NO, update.getLineNo());
		assertTrue(update.hasMissingArgs());
		assertEquals("update me", update.getUserInput());

		command = Interpreter.getCommand("update");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(Interpreter.INVALID_NO, update.getLineNo());
		assertTrue(update.hasMissingArgs());
		assertEquals("update", update.getUserInput());

		command = Interpreter.getCommand("update 3");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(Interpreter.INVALID_NO, update.getLineNo());
		assertTrue(update.hasMissingArgs());
		assertEquals("update 3", update.getUserInput());

	}

	@Test
	public void testGetCommandView() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("view calendar");
		assertEquals(Command.VIEW, command.getCommandType());
		CommandView view = (CommandView) command;
		assertEquals(UI.CALENDAR, view.getViewType());
		assertEquals("view calendar", view.getUserInput());
		assertFalse(view.hasMissingArgs());

		// Test 2: invalid argument
		command = Interpreter.getCommand("view cal");
		assertEquals(Command.VIEW, command.getCommandType());
		view = (CommandView) command;
		assertEquals(UI.INVALID, view.getViewType());
		assertEquals("view cal", view.getUserInput());
		assertTrue(view.hasMissingArgs());

		command = Interpreter.getCommand("view");
		assertEquals(Command.VIEW, command.getCommandType());
		view = (CommandView) command;
		assertEquals(UI.INVALID, view.getViewType());
		assertEquals("view", view.getUserInput());
		assertTrue(view.hasMissingArgs());
	}

	@Test
	public void testGetCommandHelp() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("help add");
		assertEquals(Command.HELP, command.getCommandType());
		CommandHelp help = (CommandHelp) command;
		assertEquals(Command.ADD, help.getHelpCommand());
		assertEquals("help add", help.getUserInput());
		assertFalse(help.hasMissingArgs());

		// Test 2: invalid argument
		command = Interpreter.getCommand("help test");
		assertEquals(Command.HELP, command.getCommandType());
		help = (CommandHelp) command;
		assertEquals(null, help.getHelpCommand());
		assertEquals("help test", help.getUserInput());
		assertTrue(help.hasMissingArgs());

	}

	@Test
	public void testGetCommandSearchName() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("search-name hello");
		assertEquals(Command.SEARCH_NAME, command.getCommandType());
		CommandSearchName searchName = (CommandSearchName) command;
		assertEquals("hello", searchName.getSearchString());
		assertEquals("search-name hello", searchName.getUserInput());
		assertFalse(searchName.hasMissingArgs());
		
		// Test 2: Punctuation, case and spaces
		command = Interpreter.getCommand("search-name hello World!");
		assertEquals(Command.SEARCH_NAME, command.getCommandType());
		searchName = (CommandSearchName) command;
		assertEquals("hello World!", searchName.getSearchString());
		assertEquals("search-name hello World!", searchName.getUserInput());
		assertFalse(searchName.hasMissingArgs());

		// Test 2: invalid argument
		command = Interpreter.getCommand("search-name");
		assertEquals(Command.SEARCH_NAME, command.getCommandType());
		searchName = (CommandSearchName) command;
		assertEquals("search-name", searchName.getUserInput());
		assertTrue(searchName.hasMissingArgs());
	}

	@Test
	public void testGetCommandSearchTime() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("search-time aug 4 to sep 10");
		assertEquals(Command.SEARCH_TIME, command.getCommandType());
		CommandSearchTime searchTime = (CommandSearchTime) command;
		Calendar today = Calendar.getInstance();
		testDate(searchTime.getTimeStart(), today.get(Calendar.YEAR), 7, 4,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		testDate(searchTime.getTimeEnd(), today.get(Calendar.YEAR), 8, 10,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		assertFalse(searchTime.hasMissingArgs());
		
		// Test 2: Automatically order the time
		command = Interpreter.getCommand("search-time dec 4 to aug 10");
		assertEquals(Command.SEARCH_TIME, command.getCommandType());
		searchTime = (CommandSearchTime) command;
		testDate(searchTime.getTimeStart(), today.get(Calendar.YEAR), 7, 10,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		testDate(searchTime.getTimeEnd(), today.get(Calendar.YEAR), 11, 4,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		assertFalse(searchTime.hasMissingArgs());
		

		// Test 3: invalid argument
		command = Interpreter.getCommand("search-time hello");
		assertEquals(Command.SEARCH_TIME, command.getCommandType());
		searchTime = (CommandSearchTime) command;
		assertEquals("search-time hello", searchTime.getUserInput());
		assertTrue(searchTime.hasMissingArgs());
		
		command = Interpreter.getCommand("search-time aug 4");
		assertEquals(Command.SEARCH_TIME, command.getCommandType());
		searchTime = (CommandSearchTime) command;
		assertTrue(searchTime.hasMissingArgs());
	}

}