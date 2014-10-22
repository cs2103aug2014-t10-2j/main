package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import interpreter.Interpreter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zombietask.ZombieTask;
import zombietask.ZombieTaskCommandHandler;

/**
 * 
 * Junit Tests for 
 * 
 * @author jellymac
 * 
 * @version 0.0.0a
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
		ZombieTask.initStorage(testInput);
		assertEquals("filename assignment test failed - empty string", ZombieTask.getStorage().getFileName(), DEFAULT_FILE);
	}
	
	@Test
	public void filenameTester02() {
		String[] testInput = new String[1];
		testInput[0] = TEST1_FILE;
		ZombieTask.initStorage(testInput);
		assertEquals("filename assignment test failed - correct input syntax", ZombieTask.getStorage().getFileName(), TEST1_FILE);
	}
	
	@Test
	public void filenameTester03() {
		String[] testInput = new String[1];
		testInput[0] = TEST2_FILE;
		ZombieTask.initStorage(testInput);
		assertEquals("filename assignment test failed - numeric string input", ZombieTask.getStorage().getFileName(), TEST2_FILE);
	}
	
	@Test
	public void filenameTester04() {
		String[] testInput = new String[1];
		testInput[0] = TEST2_FILE.concat(ESCAPE_CHAR_EMPTY);
		ZombieTask.initStorage(testInput);
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
	
	public void addCommandTester02() {
		fail("Not yet implemented");
	}
	
	@Test
	public void deleteCommandTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void viewCommandTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void updateCommandTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void invalidCommandTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void undoCommandTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void redoCommandTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void helpCommandTester() {
		fail("Not yet implemented");
	}
	
	/*
	 * CommandList Mutators Testers
	 */
	
	@Test
	public void addCommandToListTester() {
		fail("Not yet implemented");
	}
	
	@Test
	public void removeLastCommandFromListTester() {
		fail("Not yet implemented");
	}
	
	/*
	 * Storage Testing
	 */
	
	@Test
	public void storageTest() {
		fail("Not yet implemented");
	}
	
	/*
	 * Interpreter Testing
	 */
	
	@Test
	public void interpreterTest() {
		fail("Not yet implemented");
	}
	
	/*
	 * UI Testing
	 */
	
	@Test
	public void uiTest() {
		fail("Not yet implemented");
	}
	
}
