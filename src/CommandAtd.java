import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
		Command undo = new Command("undo");
		assertEquals("undo", undo.getCommandType());
	}
	
	@Test
	public void testCommandAdd() {
		Calendar date = new GregorianCalendar(2014, 1, 1);
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("food");
		tags.add("important");
		CommandAdd add = new CommandAdd("add", "eat lunch", date, tags, false);
		assertEquals("add", add.getCommandType());
		assertEquals("eat lunch", add.getTaskName());
		assertEquals(date, add.getDateTime());
		assertEquals(tags, add.getTags());
		assertFalse(add.hasMissingArgs());
	}
	
	@Test
	public void testCommandDelete() {
		CommandDelete delete = new CommandDelete("delete", 1, false);
		assertEquals("delete", delete.getCommandType());
		assertEquals(1, delete.getLineNo());
		assertFalse(delete.hasMissingArgs());
	}
	
	@Test
	public void testCommandUpdate() {
		CommandUpdate update = new CommandUpdate("update", 1, false);
		assertEquals("update", update.getCommandType());
		assertEquals(1, update.getLineNo());
		assertFalse(update.hasMissingArgs());
	}
	
	@Test
	public void testCommandView() {
		CommandView view = new CommandView("view", "calendar", false);
		assertEquals("view", view.getCommandType());
		assertEquals("calendar", view.getViewType());
		assertFalse(view.hasMissingArgs());
	}
}
