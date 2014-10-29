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

		// Test 1: Basic function
		ZombieTask.testCommand("add have lunch");
		ZombieTask.testCommand("view agenda");
		ZombieTask.testCommand("delete f0");
		ZombieTask.testCommand("add have lunch 2pm");
		ZombieTask.testCommand("update d0 have lunch 2pm >canteen");
		ZombieTask.testCommand("view agenda");
		ZombieTask.testCommand("undo");

	}
}
