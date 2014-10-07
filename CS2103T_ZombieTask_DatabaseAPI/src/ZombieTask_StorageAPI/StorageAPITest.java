package ZombieTask_StorageAPI;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class StorageAPITest {
	StorageAPI testStorage = new StorageAPI();
	String filename = "ZombieStorage.txt";
	String filenameTest = "Text.txt";
	File file = null;
	String expected = null;
	String result = null;
	String taskName = "tempTask";
	String updateName = "updateName";
	int  testID = 0;
	Calendar deadline; //= Calendar.getInstance();
	Task tempTask = new Task(taskName, deadline);
	Task updateTask = new Task(updateName, deadline);
	
	@Before
	public void setUp() throws Exception {
		file = StorageAPI.createFile();
		
	    	FileWriter fw = new FileWriter(file,false);
			fw.write("");
			fw.flush();
			fw.close();
		

	
	}

	@Test
	public void testAddTask() throws IOException {
		
		testStorage.add(tempTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = "[{\"tags\":[],\"subtasks\":[],\"taskName\":\"tempTask\"}]";
		assertEquals(expected, result);
		//fail("Not yet implemented");
	}

	@Test
	public void testSearchIntArray() throws IOException {
		testStorage.add(tempTask);
		Task testResult = testStorage.search(testID);
		Task testExpected = tempTask;
		
		assertEquals(testExpected.getSubtask(),testResult.getSubtask());
		assertEquals(testExpected.getDeadline(),testResult.getDeadline());
		assertEquals(testExpected.getTags(),testResult.getTags());
		assertEquals(testExpected.getTaskName(),testResult.getTaskName());

		//fail("Not yet implemented");
	}

	@Test
	public void testDeleteTask() throws IOException {
		//testStorage.add(tempTask);
		testStorage.delete(updateTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = "[]";
		assertEquals(expected, result);
		//fail("Not yet implemented");
	}

	@Test
	public void testUpdateTaskTask() throws IOException {
		
		testStorage.update(tempTask, updateTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = "[{\"tags\":[],\"subtasks\":[],\"taskName\":\"updateName\"}]";
		assertEquals(expected, result);
		//fail("Not yet implemented");
	}

	@Test
	public void testSetFile() throws IOException {
		StorageAPI.setFile(filenameTest);
		file = StorageAPI.createFile();
		assertEquals(file.getName(),filenameTest);
		//fail("Not yet implemented");
	}

	@Test
	public void testCreateFile() throws IOException {
		file = StorageAPI.createFile();
		assertEquals(file.getName(),filename);
		//fail("Not yet implemented");
	}

}
