package tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;

import interpreter.Command;
import interpreter.Interpreter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import storage.Storage;
import storage.StorageAPI;
import zombietask.ZombieTask;
import zombietask.ZombieTaskCommandHandler;

/**
 * To test ZombieTask's dependency on Interpreter
 * @author Ping 
 * 
 */
public class ZombieTaskInterpreterAtd {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		// System.setOut(new PrintStream(outContent));
	}

	@Before
	public void deleteFile() {
		new File("ZombieTest").delete();
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}

	@Test
	public void testAdd() throws Exception {

		// Test 1: Correct methods called by ZombieTask
		ZombieTask.testCommand("have lunch");
		String commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.ADD, commandCalled);
		
		ZombieTask.testCommand("view agenda");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.VIEW, commandCalled);
		
		ZombieTask.testCommand("delete f0");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.DELETE, commandCalled);
		
		ZombieTask.testCommand("add have lunch 2pm");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.ADD, commandCalled);
		
		ZombieTask.testCommand("update d0 have lunch 2pm >canteen");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.UPDATE, commandCalled);
		
		ZombieTask.testCommand("undo");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.UNDO, commandCalled);
		
		ZombieTask.testCommand("redo");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.REDO, commandCalled);
		
		ZombieTask.testCommand("help");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.HELP, commandCalled);
		
		ZombieTask.testCommand("search-name x");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.SEARCH_NAME, commandCalled);
		
		ZombieTask.testCommand("search-time 2:00pm to 4:00pm");
		commandCalled = ZombieTaskCommandHandler.getCommandCalled();
		assertEquals(Command.SEARCH_TIME, commandCalled);

	}
}
