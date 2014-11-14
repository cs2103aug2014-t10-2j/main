package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import interpreter.Command;
import interpreter.Interpreter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exception.NoCommandException;
import ui.GUI;
import ui.UI;
import zombietask.ZombieTask;
import zombietask.ZombieTaskCommandHandler;

/**
 * 
 * Junit Tests for 
 * 
//@author a0066754w
 *
 */



public class ZombieTaskAtd {
	
	/*
	 * Constants
	 */
	
	private static final String DEFAULT_FILE = "ZombieStorage.txt";
	private static final String TEST1_FILE = "myDataBase.json";
	private static final String TEST2_FILE = "100";
	private static final String EMPTY_STRING = "";
	private static final String ESCAPE_CHAR_EMPTY = "/";
	
	/*
	 * Initalisation statements for Automatic Testing
	 */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ZombieTask.initForTest();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		/*
		 * Delete all used files
		 */
		
		deleteFile(DEFAULT_FILE);
		deleteFile(TEST1_FILE);
		deleteFile(TEST2_FILE);
		
	}
	
	public static void deleteFile(String filename)
    {	
    	try{
    		File file = new File(filename);
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	/*
	 * Generic setup and teardown functions. To be deleted before final implementation
	 */

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Main Logic Testing
	 */
	
	@Test
	public void filenameTester01() {
		String[] testInput = new String[1];
		testInput[0] = EMPTY_STRING;
		ZombieTask.initStorage(testInput[0]);
		assertEquals("filename assignment test failed - empty string", ZombieTask.getStorage().getFileName(), DEFAULT_FILE);
	}
	
	@Test
	public void filenameTester02() {
		String[] testInput = new String[1];
		testInput[0] = TEST1_FILE;
		ZombieTask.initStorage(testInput[0]);
		assertEquals("filename assignment test failed - correct input syntax", ZombieTask.getStorage().getFileName(), TEST1_FILE);
	}
	
	@Test
	public void filenameTester03() {
		String[] testInput = new String[1];
		testInput[0] = TEST2_FILE;
		ZombieTask.initStorage(testInput[0]);
		assertEquals("filename assignment test failed - numeric string input", ZombieTask.getStorage().getFileName(), TEST2_FILE);
	}
	
	@Test
	public void filenameTester04() {
		String[] testInput = new String[1];
		testInput[0] = TEST2_FILE.concat(ESCAPE_CHAR_EMPTY);
		ZombieTask.initStorage(testInput[0]);
		assertEquals("filename assignment test failed - numeric and escape character input", ZombieTask.getStorage().getFileName(), TEST2_FILE);
	}
	
	/*
	 * Command Testers
	 */
	
	@Test
	public void addCommandTester01() {
		try{
			ZombieTask.setCurrentCommand(Interpreter.getCommand("add noob 13 Sep"));
			ZombieTaskCommandHandler.execute();
			ZombieTask.setCurrentCommand(Interpreter.getCommand("add noob 13 Sep"));
			ZombieTaskCommandHandler.execute();
		}catch (Exception err){
			fail("Good input - add 01 - Exception thrown");
		}
	}
	
	@Test
	public void helpCommandTester() throws NoCommandException {
		ZombieTask.setCurrentCommand(Interpreter.getCommand("help"));
	}
	
	/*
	 * CommandList Mutators Testers
	 */
	
	@Test
	public void undoTester() throws Exception {
		executeCommand("delete-name nooby");
		executeCommand("add nooby 16 Sep");
		executeCommand("undo");
		assertEquals("Undo", ZombieTask.getStorage().searchName("nooby").size(), 0);
	}
	
	@Test
	public void redoTester() throws Exception {
		executeCommand("delete-name noobydooby");
		executeCommand("add noobydooby 17 Sep");
		executeCommand("undo");
		executeCommand("redo");
		assertEquals("Undo", ZombieTask.getStorage().searchName("noobydooby").size(), 1);
	}
	
	private void executeCommand(String commandString) throws Exception{
		Command command = Interpreter.getCommand(commandString);
		ZombieTaskCommandHandler.execute(command, commandString);
	}
	
}
