import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Automated test driver for Interpreter class.
 * @author Link
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
		
		// Test 2: 2 or more words
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
		
		// Test 3: case-insensitivity
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("aDd 8 a"));
		assertEquals(Interpreter.ADD, Interpreter.getCommandType("diNNer!!"));
		assertEquals(Interpreter.DELETE, Interpreter.getCommandType("Delete"));
		assertEquals(Interpreter.UNDO, Interpreter.getCommandType("UNDO to"));
		assertEquals(Interpreter.UPDATE, Interpreter.getCommandType("upDate"));
		assertEquals(Interpreter.VIEW, Interpreter.getCommandType("vieW hi"));
		
	}

}
