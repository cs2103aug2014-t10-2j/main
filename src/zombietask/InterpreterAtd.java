package zombietask;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

/**
 * Automated test driver for Interpreter class.
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
	public void testGetCommandAdd() throws Exception {
		
		// Test 1: Basic test
		Command command = Interpreter.getCommand("add today #food eat "
				+ "something1 #impt");
		assertEquals(Command.ADD, command.getCommandType());
		CommandAdd add = (CommandAdd) command;
		Calendar today = Calendar.getInstance();
		assertEquals(today.get(Calendar.YEAR), 
				add.getDateTime().get(Calendar.YEAR));
		assertEquals(today.get(Calendar.MONTH), 
				add.getDateTime().get(Calendar.MONTH));
		assertEquals(today.get(Calendar.DATE), 
				add.getDateTime().get(Calendar.DATE));
		assertEquals(today.get(Calendar.HOUR_OF_DAY), 
				add.getDateTime().get(Calendar.HOUR_OF_DAY));
		assertEquals(today.get(Calendar.MINUTE), 
				add.getDateTime().get(Calendar.MINUTE));
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#food");
		tags.add("#impt");
		assertEquals(tags, add.getTags());
		assertEquals("eat something1", add.getTaskName());
		assertEquals("add today #food eat something1 #impt", add.getUserInput());
		
		// Test 2: case-insensitivity and white space
		command = Interpreter.getCommand("   Add   30 Sep \n#food Eat #impt");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;
		assertEquals(30, add.getDateTime().get(Calendar.DATE));
		assertEquals(8, add.getDateTime().get(Calendar.MONTH));
		assertEquals(tags, add.getTags());
		assertEquals("Eat", add.getTaskName());
		assertEquals("   Add   30 Sep \n#food Eat #impt", add.getUserInput());

		
		// Test 3: unspecified command
		command = Interpreter.getCommand("30 Sep Lunch #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;
		assertEquals(30, add.getDateTime().get(Calendar.DATE));
		assertEquals(8, add.getDateTime().get(Calendar.MONTH));
		assertEquals("Lunch", add.getTaskName());
		assertEquals("30 Sep Lunch #impt #food", add.getUserInput());
		

		// Test 4: repeated dates
		command = Interpreter.getCommand("30 Sep Lunch on 30 sep #impt #food");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;
		assertEquals(30, add.getDateTime().get(Calendar.DATE));
		assertEquals(8, add.getDateTime().get(Calendar.MONTH));
		assertEquals("Lunch on 30 sep", add.getTaskName());
		assertEquals("30 Sep Lunch on 30 sep #impt #food", add.getUserInput());
		
		// Test 5: different dates
		command = Interpreter.getCommand("30 Sep read 9/11 book");
		assertEquals(Command.ADD, command.getCommandType());
		add = (CommandAdd) command;
		assertEquals(30, add.getDateTime().get(Calendar.DATE));
		assertEquals(8, add.getDateTime().get(Calendar.MONTH));
		assertEquals("read 9/11 book", add.getTaskName());
		assertEquals("30 Sep read 9/11 book", add.getUserInput());
	}
	
	@Test (expected = Exception.class)
	public void testGetCommandAddFloating() throws Exception {
		// Test 4: floating task, missing date
		Command	command = Interpreter.getCommand("     \n#food Eat #impt");
		CommandAdd add = (CommandAdd) command;
		assertTrue(add.hasMissingArgs());
		assertEquals("     \n#food Eat #impt", add.getUserInput());
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
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#food");
		assertEquals("go lunch", updatedTask.getTaskName());
		assertEquals(tags, updatedTask.getTags());
		assertEquals(8, updatedTask.getDateTime().get(Calendar.MONTH));
		assertEquals(21, updatedTask.getDateTime().get(Calendar.DATE));
		
		
		// Test 2: case-insensitivity
		command = Interpreter.getCommand("uPdATe 2 go lunch sep 21"
				+ " #food");
		assertEquals(Command.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(2, update.getLineNo());
		assertEquals("uPdATe 2 go lunch sep 21 #food", update.getUserInput());
		
		updatedTask = update.getUpdatedTask();
		tags.clear();
		tags.add("#food");
		assertEquals("go lunch", updatedTask.getTaskName());
		assertEquals(tags, updatedTask.getTags());
		assertEquals(8, updatedTask.getDateTime().get(Calendar.MONTH));
		assertEquals(21, updatedTask.getDateTime().get(Calendar.DATE));
		
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

}
