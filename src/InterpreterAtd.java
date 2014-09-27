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
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("add"));
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("breakfast"));
		assertEquals(Interpreter.DELETE, Interpreter.getCommandType("delete"));
		assertEquals(Interpreter.UNDO, Interpreter.getCommandType("undo"));
		assertEquals(Interpreter.UPDATE, Interpreter.getCommandType("update"));
		assertEquals(Interpreter.VIEW, Interpreter.getCommandType("view"));
		
	}
	
	@Test
	public void testGetCommandAdd() throws Exception {
		
		// Test 1: Basic test
		Command command = Interpreter.getCommand("add today #food eat "
				+ "something #impt");
		assertEquals(Interpreter.ADD, command.getCommandType());
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
		assertEquals("eat something", add.getTaskName());
		
		// Test 2: case-insensitivity and white space
		command = Interpreter.getCommand("   Add   30 Sep \n#food Eat #impt");
		assertEquals(Interpreter.ADD, command.getCommandType());
		add = (CommandAdd) command;
		assertEquals(30, add.getDateTime().get(Calendar.DATE));
		assertEquals(8, add.getDateTime().get(Calendar.MONTH));
		assertEquals(tags, add.getTags());
		assertEquals("Eat", add.getTaskName());
		
		// Test 3: unspecified command
		command = Interpreter.getCommand("30 Sep Lunch #impt #food");
		assertEquals(Interpreter.ADD, command.getCommandType());
		add = (CommandAdd) command;
		assertEquals(30, add.getDateTime().get(Calendar.DATE));
		assertEquals(8, add.getDateTime().get(Calendar.MONTH));
		assertEquals("Lunch", add.getTaskName());
		
		// Test 4: floating task, missing date
		command = Interpreter.getCommand("     \n#food Eat #impt");
		add = (CommandAdd) command;
		assertTrue(add.hasMissingArgs());
	}
	
	@Test
	public void testGetCommandDelete() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("delete 5");
		assertEquals(Interpreter.DELETE, command.getCommandType());
		CommandDelete delete = (CommandDelete) command;
		assertEquals(5, delete.getLineNo());
		
		// Test 2: case-insensitivity
		command = Interpreter.getCommand("DELETE 5");
		assertEquals(Interpreter.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals(5, delete.getLineNo());
		
		// Test 3: invalid arguments
		command = Interpreter.getCommand("delete me");
		assertEquals(Interpreter.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals(Interpreter.INVALID_NO, delete.getLineNo());
		assertTrue(delete.hasMissingArgs());
		
		command = Interpreter.getCommand("delete");
		assertEquals(Interpreter.DELETE, command.getCommandType());
		delete = (CommandDelete) command;
		assertEquals(Interpreter.INVALID_NO, delete.getLineNo());
		assertTrue(delete.hasMissingArgs());
	}
	
	@Test
	public void testGetCommandUpdate() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("update 2");
		assertEquals(Interpreter.UPDATE, command.getCommandType());
		CommandUpdate update = (CommandUpdate) command;
		assertEquals(2, update.getLineNo());
		
		// Test 2: case-insensitivity
		command = Interpreter.getCommand("UpDate 2");
		assertEquals(Interpreter.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(2, update.getLineNo());
		
		// Test 3: invalid arguments
		command = Interpreter.getCommand("update me");
		assertEquals(Interpreter.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(Interpreter.INVALID_NO, update.getLineNo());
		assertTrue(update.hasMissingArgs());
		
		command = Interpreter.getCommand("update");
		assertEquals(Interpreter.UPDATE, command.getCommandType());
		update = (CommandUpdate) command;
		assertEquals(Interpreter.INVALID_NO, update.getLineNo());
		assertTrue(update.hasMissingArgs());

	}
	
	@Test
	public void testGetCommandView() throws Exception {
		// Test 1: Basic function
		Command command = Interpreter.getCommand("view -calendar");
		assertEquals(Interpreter.VIEW, command.getCommandType());
		CommandView view = (CommandView) command;
		assertEquals(Interpreter.CALENDAR, view.getViewType());
		
		// Test 2: invalid argument
		command = Interpreter.getCommand("view calendar");
		assertEquals(Interpreter.VIEW, command.getCommandType());
		view = (CommandView) command;
		assertEquals(Interpreter.INVALID, view.getViewType());
		
		command = Interpreter.getCommand("view");
		assertEquals(Interpreter.VIEW, command.getCommandType());
		view = (CommandView) command;
		assertEquals(Interpreter.INVALID, view.getViewType());
	}

}
