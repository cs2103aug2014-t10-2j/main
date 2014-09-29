import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

/**
 * Automated test driver for Command class
 * @author SP
 *
 */
public class CommandAtd {
	
	@Test
	public void testCommand() {
		// Test 1: valid argument
		Command undo = new Command("undo");
		assertEquals(Interpreter.UNDO, undo.getCommandType());
		
		// Test 2: invalid argument
		undo = new Command(null);
		assertEquals(null, undo.getCommandType());
	}
	
	@Test
	public void testCommandAdd() {
		// Test 1: valid arguments
		Calendar date = new GregorianCalendar(2014, 1, 1);
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("food");
		tags.add("important");
		CommandAdd add = new CommandAdd("eat lunch", date, tags, false);
		assertEquals(Interpreter.ADD, add.getCommandType());
		assertEquals("eat lunch", add.getTaskName());
		assertEquals(date, add.getDateTime());
		assertEquals(tags, add.getTags());
		assertFalse(add.hasMissingArgs());
		
		// Test 2: invalid/missing arguments
		add = new CommandAdd(null, null, null, true);
		assertEquals(null, add.getTaskName());
		assertEquals(null, add.getDateTime());
		assertEquals(null, add.getTags());
		assertTrue("", add.hasMissingArgs());
	}
	
	@Test
	public void testCommandDelete() {
		// Test 1: valid arguments
		CommandDelete delete = new CommandDelete(1, false);
		assertEquals(Interpreter.DELETE, delete.getCommandType());
		assertEquals(1, delete.getLineNo());
		assertFalse(delete.hasMissingArgs());
		
		// Test 2: invalid/missing arguments
		delete = new CommandDelete(-1, true);
		assertEquals(-1, delete.getLineNo());
		assertTrue(delete.hasMissingArgs());
	}
	
	@Test
	public void testCommandUpdate() {
		// Test 1: valid arguments
		CommandUpdate update = new CommandUpdate(1, false);
		assertEquals(Interpreter.UPDATE, update.getCommandType());
		assertEquals(1, update.getLineNo());
		assertFalse(update.hasMissingArgs());
		
		// Test 2: invalid/missing arguments
		update = new CommandUpdate(-1, true);
		assertEquals(-1, update.getLineNo());
		assertTrue(update.hasMissingArgs());
	}
	
	@Test
	public void testCommandView() {
		// Test 1: valid arguments
		CommandView view = new CommandView("calendar", false);
		assertEquals(Interpreter.VIEW, view.getCommandType());
		assertEquals("calendar", view.getViewType());
		assertFalse(view.hasMissingArgs());
		
		// Test 2: invalid/missing arguments
		view = new CommandView("", true);
		assertEquals("", view.getViewType());
		assertTrue(view.hasMissingArgs());
	}
}
