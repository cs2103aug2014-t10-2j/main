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
				+ "something #impt @restaurant");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;

		testTags(add.getTags(), "#food #impt");
		assertEquals("@restaurant", add.getLocation());

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		assertEquals("eat something", add.getTaskName());
		assertEquals("add #food eat something #impt @restaurant",
				add.getUserInput());

		// Test 2: case-insensitivity (of command) and white space
		// case sensitivity of task name, tags and location
		command = Interpreter
				.getCommand("   Add    \n#Food @Canteen Eat #impt");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		testTags(add.getTags(), "#Food #impt");
		assertEquals("@Canteen", add.getLocation());

		assertEquals("Eat", add.getTaskName());
		assertEquals("   Add    \n#Food @Canteen Eat #impt", add.getUserInput());

		// Test 3: unspecified command
		command = Interpreter.getCommand("Lunch #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		assertEquals("Lunch", add.getTaskName());
		assertEquals("Lunch #impt #food", add.getUserInput());

		// Test 4: escape character
		command = Interpreter.getCommand("have \"22\" bananas #fruit #dessert");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		testTags(add.getTags(), "#fruit #dessert");
		assertEquals("have 22 bananas", add.getTaskName());
		assertEquals("have \"22\" bananas #fruit #dessert", add.getUserInput());

		command = Interpreter.getCommand("read \"today's\" papers");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		assertNull(add.getEndDate());

		assertEquals("read today's papers", add.getTaskName());
		assertEquals("read \"today's\" papers", add.getUserInput());

	}

	@Test
	public void testGetCommandAddDeadline() throws Exception {
		// to test parsing of deadline tasks

		// Test 1: Basic test
		Command command = Interpreter.getCommand("add today #food eat "
				+ "something #impt @home");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;

		Calendar today = Calendar.getInstance();
		assertNull(add.getStartDate());
		testDate(add.getEndDate(), today);

		testTags(add.getTags(), "#food #impt");
		assertEquals("@home", add.getLocation());

		assertEquals("eat something", add.getTaskName());
		assertEquals("add today #food eat something #impt @home",
				add.getUserInput());

		// Test 2: case-insensitivity and white space
		command = Interpreter.getCommand("   Add   30 Sep \n#food Eat #impt");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		testDate(add.getEndDate(), today.get(Calendar.YEAR), 8, 30,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));

		testTags(add.getTags(), "#food #impt");
		assertEquals("Eat", add.getTaskName());
		assertEquals("   Add   30 Sep \n#food Eat #impt", add.getUserInput());

		// Test 3: unspecified command
		command = Interpreter.getCommand("30 Sep Lunch @home #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		testDate(add.getEndDate(), 2014, 8, 30,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));

		testTags(add.getTags(), "#impt #food");
		assertEquals("@home", add.getLocation());
		assertEquals("Lunch", add.getTaskName());
		assertEquals("30 Sep Lunch @home #impt #food", add.getUserInput());

		// Test 4: escape character
		command = Interpreter
				.getCommand("have \"three\" bananas @sch tomorrow #fruit");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		tomorrow.setTime(tomorrow.getTime());

		assertNull(add.getStartDate());
		testDate(add.getEndDate(), tomorrow);

		testTags(add.getTags(), "#fruit");
		assertEquals("@sch", add.getLocation());

		assertEquals("have three bananas", add.getTaskName());
		assertEquals("have \"three\" bananas @sch tomorrow #fruit",
				add.getUserInput());

		command = Interpreter.getCommand("read \"Friday's\" papers tomorrow");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		assertNull(add.getStartDate());
		testDate(add.getEndDate(), tomorrow);

		assertEquals("read Friday's papers", add.getTaskName());
		assertEquals("read \"Friday's\" papers tomorrow", add.getUserInput());

	}

	@Test
	public void testGetCommandAddTimed() throws Exception {
		// to test parsing of timed tasks

		// Test 1: Basic test
		Command command = Interpreter
				.getCommand("add today 1pm to 2pm #food eat "
						+ "something #impt @sch");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;
		Calendar today = Calendar.getInstance();
		testDate(add.getStartDate(), today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DATE), 13, 0, 0);
		testDate(add.getEndDate(), today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DATE), 14, 0, 0);

		testTags(add.getTags(), "#food #impt");
		assertEquals("eat something", add.getTaskName());
		assertEquals("@sch", add.getLocation());
		assertEquals("add today 1pm to 2pm #food eat something #impt @sch",
				add.getUserInput());

		// Test 2: case-insensitivity and white space
		command = Interpreter
				.getCommand("   Add  30 Sep to 1 nov @house \n#food Eat");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		testDate(add.getStartDate(), today.get(Calendar.YEAR), 8, 30,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		testDate(add.getEndDate(), today.get(Calendar.YEAR), 10, 1,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));

		testTags(add.getTags(), "#food");
		assertEquals("@house", add.getLocation());
		assertEquals("Eat", add.getTaskName());
		assertEquals("   Add  30 Sep to 1 nov @house \n#food Eat",
				add.getUserInput());

		// Test 3: unspecified command
		command = Interpreter
				.getCommand("30 Sep 1:30 to 2 @home Lunch #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;
		testDate(add.getStartDate(), 2014, 8, 30, 1, 30, 0);
		testDate(add.getEndDate(), 2014, 8, 30, 2, 0, 0);
		testTags(add.getTags(), "#impt #food");
		assertEquals("@home", add.getLocation());
		assertEquals("Lunch", add.getTaskName());

		// Test 4: escape character
		command = Interpreter
				.getCommand("have \"three\" bananas tomorrow 5pm to 6pm");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		tomorrow.setTime(tomorrow.getTime());
		testDate(add.getStartDate(), tomorrow.get(Calendar.YEAR),
				tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE), 17,
				0, 0);
		testDate(add.getEndDate(), tomorrow.get(Calendar.YEAR),
				tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE), 18,
				0, 0);

		assertEquals("have three bananas", add.getTaskName());
		assertEquals("have \"three\" bananas tomorrow 5pm to 6pm",
				add.getUserInput());

		// Test 5: automatically order dates
		command = Interpreter
				.getCommand("drink \"three glasses 8D\" tomorrow 6pm to 5pm");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;

		testDate(add.getStartDate(), tomorrow.get(Calendar.YEAR),
				tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE), 17,
				0, 0);
		testDate(add.getEndDate(), tomorrow.get(Calendar.YEAR),
				tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE), 18,
				0, 0);

		assertEquals("drink three glasses 8D", add.getTaskName());
		assertEquals("drink \"three glasses 8D\" tomorrow 6pm to 5pm",
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
		Command command = Interpreter.getCommand("delete d5 f2");
		assertEquals(Command.DELETE, command.getCommandType());
		CommandDelete delete = (CommandDelete) command;
		assertEquals("d5", delete.getLineCode().get(0));
		assertEquals("f2", delete.getLineCode().get(1));
		assertEquals("delete d5 f2", delete.getUserInput());

		// Test 2: case-insensitivity
		command = Interpreter.getCommand("DELETE F5");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals("f5", delete.getLineCode().get(0));
		assertEquals("DELETE F5", delete.getUserInput());

		// Test 3: delete range
		command = Interpreter.getCommand("delete t5-t8");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals("t5", delete.getLineCode().get(0));
		assertEquals("t6", delete.getLineCode().get(1));
		assertEquals("t7", delete.getLineCode().get(2));
		assertEquals("t8", delete.getLineCode().get(3));
		assertEquals("delete t5-t8", delete.getUserInput());

		// reverse order
		command = Interpreter.getCommand("delete f4-f1");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals("f1", delete.getLineCode().get(0));
		assertEquals("f2", delete.getLineCode().get(1));
		assertEquals("f3", delete.getLineCode().get(2));
		assertEquals("f4", delete.getLineCode().get(3));
		assertEquals("delete f4-f1", delete.getUserInput());

		// Test 4: delete multiple
		command = Interpreter.getCommand("delete t4 t1");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals("t4", delete.getLineCode().get(0));
		assertEquals("t1", delete.getLineCode().get(1));
		assertEquals("delete t4 t1", delete.getUserInput());

		// Test 4: invalid arguments
		command = Interpreter.getCommand("delete me");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertNull(delete.getLineCode());
		assertTrue(delete.hasMissingArgs());
		assertEquals("delete me", delete.getUserInput());

		command = Interpreter.getCommand("delete");
		assertEquals(Command.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertNull(delete.getLineCode());
		assertTrue(delete.hasMissingArgs());
		assertEquals("delete", delete.getUserInput());
	}

	@Test
	public void testGetCommandUpdate() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter
				.getCommand("update d2 @sch go lunch sep 21 #food");
		assertEquals(Command.UPDATE, command.getCommandType());
		CommandUpdate update = (CommandUpdate) command;
		assertEquals("d2", update.getLineCode());

		CommandAdd updatedTask = update.getUpdatedTask();
		testTags(updatedTask.getTags(), "#food");
		assertEquals("@sch", updatedTask.getLocation());
		assertEquals("go lunch", updatedTask.getTaskName());
		Calendar today = Calendar.getInstance();
		testDate(updatedTask.getEndDate(), today.get(Calendar.YEAR), 8, 21,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));
		assertEquals("update d2 @sch go lunch sep 21 #food",
				update.getUserInput());

		// Test 2: Update by field
		// location
		command = Interpreter.getCommand("update d2 @sch");
		update = (CommandUpdate) command;
		updatedTask = update.getUpdatedTask();
		assertNull(updatedTask.getTaskName());
		assertTrue(updatedTask.getTags().isEmpty());
		assertEquals("@sch", updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		assertNull(updatedTask.getEndDate());

		// task name
		command = Interpreter.getCommand("update d2 have lunch");
		update = (CommandUpdate) command;
		updatedTask = update.getUpdatedTask();
		assertEquals("have lunch", updatedTask.getTaskName());
		assertTrue(updatedTask.getTags().isEmpty());
		assertNull(updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		assertNull(updatedTask.getEndDate());

		// time
		command = Interpreter.getCommand("update d2 9/11 2pm");
		update = (CommandUpdate) command;
		updatedTask = update.getUpdatedTask();
		assertNull(updatedTask.getTaskName());
		assertTrue(updatedTask.getTags().isEmpty());
		assertNull(updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		testDate(updatedTask.getEndDate(), today.get(Calendar.YEAR), 8, 11, 14,
				0, 0);

		// tag
		command = Interpreter.getCommand("update d2 #haha");
		update = (CommandUpdate) command;
		updatedTask = update.getUpdatedTask();
		assertNull(updatedTask.getTaskName());
		testTags(updatedTask.getTags(), "#haha");
		assertNull(updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		assertNull(updatedTask.getEndDate());
		
		
		// hybrid - date and tag
		command = Interpreter.getCommand("update d2 #impt 9/11 2pm");
		update = (CommandUpdate) command;
		updatedTask = update.getUpdatedTask();
		assertNull(updatedTask.getTaskName());
		testTags(updatedTask.getTags(), "#impt");
		assertNull(updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		testDate(updatedTask.getEndDate(), today.get(Calendar.YEAR), 8, 11, 14,
				0, 0);
		
		// hybrid - date and name
		command = Interpreter.getCommand("update d2 #impt homework");
		update = (CommandUpdate) command;
		updatedTask = update.getUpdatedTask();
		assertEquals("homework", updatedTask.getTaskName());
		testTags(updatedTask.getTags(), "#impt");
		assertNull(updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		assertNull(updatedTask.getEndDate());

		// Test 3: case-insensitivity

		// floating task
		command = Interpreter.getCommand("uPdATe D2 @sch go dinner #food");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals("d2", update.getLineCode());
		assertEquals("uPdATe D2 @sch go dinner #food", update.getUserInput());

		updatedTask = update.getUpdatedTask();
		assertEquals("go dinner", updatedTask.getTaskName());
		testTags(updatedTask.getTags(), "#food");
		assertEquals("@sch", updatedTask.getLocation());
		assertNull(updatedTask.getStartDate());
		assertNull(updatedTask.getEndDate());

		// deadline task
		command = Interpreter
				.getCommand("uPdATe F5 @sch go lunch sep 21 #food");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals("f5", update.getLineCode());
		assertEquals("uPdATe F5 @sch go lunch sep 21 #food",
				update.getUserInput());

		updatedTask = update.getUpdatedTask();
		assertEquals("go lunch", updatedTask.getTaskName());
		testTags(updatedTask.getTags(), "#food");
		assertEquals("@sch", updatedTask.getLocation());
		testDate(updatedTask.getEndDate(), today.get(Calendar.YEAR), 8, 21,
				today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE),
				today.get(Calendar.SECOND));

		// timed task
		command = Interpreter
				.getCommand("uPdATe T20 @sch go dinner 9pm to 10:30pm");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals("t20", update.getLineCode());
		assertEquals("uPdATe T20 @sch go dinner 9pm to 10:30pm",
				update.getUserInput());

		updatedTask = update.getUpdatedTask();
		assertEquals("go dinner", updatedTask.getTaskName());
		assertEquals("@sch", updatedTask.getLocation());
		testDate(updatedTask.getStartDate(), today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DATE), 21, 0, 0);
		testDate(updatedTask.getEndDate(), today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DATE), 22, 30, 0);

		// Test 3: invalid arguments
		command = Interpreter.getCommand("update E3");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertNull(update.getLineCode());
		assertTrue(update.hasMissingArgs());
		assertEquals("update E3", update.getUserInput());

		command = Interpreter.getCommand("update");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertNull(update.getLineCode());
		assertTrue(update.hasMissingArgs());
		assertEquals("update", update.getUserInput());

		command = Interpreter.getCommand("update 3");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertNull(update.getLineCode());
		assertTrue(update.hasMissingArgs());
		assertEquals("update 3", update.getUserInput());

	}

	@Test
	public void testGetCommandView() throws Exception {

		// Test 1: Basic function
		Command command = Interpreter.getCommand("view agenda");
		assertEquals(Command.VIEW, command.getCommandType());
		CommandView view = (CommandView) command;
		assertEquals(UI.AGENDA, view.getViewType());
		assertEquals("view agenda", view.getUserInput());
		assertFalse(view.hasMissingArgs());

		// Test 2: Case insensitivity
		command = Interpreter.getCommand("VIEW Agenda");
		assertEquals(Command.VIEW, command.getCommandType());
		view = (CommandView) command;
		assertEquals(UI.AGENDA, view.getViewType());
		assertEquals("VIEW Agenda", view.getUserInput());
		assertFalse(view.hasMissingArgs());

		// Test 3: invalid argument
		command = Interpreter.getCommand("view agen");
		assertEquals(Command.VIEW, command.getCommandType());
		view = (CommandView) command;
		assertEquals(UI.INVALID, view.getViewType());
		assertEquals("view agen", view.getUserInput());
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
