import static org.junit.Assert.assertEquals;

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
		assertEquals(today.get(Calendar.DATE), 
				add.getDateTime().get(Calendar.DATE));
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#food");
		tags.add("#impt");
		assertEquals(tags, add.getTags());
		assertEquals("eat something", add.getTaskName());
		
		// Test 2: 2 or more words
		/*
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("add 1"));
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("lunch too"));
		assertEquals(Interpreter.DELETE, 
				Interpreter.getCommandType("delete hello and update 1"));
		assertEquals(Interpreter.UNDO, 
				Interpreter.getCommandType("undo testing"));
		assertEquals(Interpreter.UPDATE, 
				Interpreter.getCommandType("update me on your status"));
		assertEquals(Interpreter.VIEW, 
				Interpreter.getCommandType("view calendar agenda timeline"));
		
		// Test 3: case-insensitivity and white space
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("  aDd 8 a"));
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("diNNer!!" ));
		assertEquals(Interpreter.DELETE, Interpreter.getCommandType("Delete"));
		assertEquals(Interpreter.UNDO, Interpreter.getCommandType("\t UNDO"));
		assertEquals(Interpreter.UPDATE, Interpreter.getCommandType("upDate"));
		assertEquals(Interpreter.VIEW, Interpreter.getCommandType("vieW  \n"));
		*/
	}
	
	@Test
	public void testGetDate() throws Exception {
		Interpreter.getDate("add eat lunch tomorrow");
	}

}
