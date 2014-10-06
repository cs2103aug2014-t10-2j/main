package ZombieTask_StorageAPI;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	Calendar deadline; //= Calendar.getInstance();
	
	
	Task tempTask = new Task(taskName, deadline);
	@Before
	public void setUp() throws Exception {
		file = StorageAPI.createFile();
	/*	BufferedReader br = new BufferedReader(new FileReader(file));
		while(br.readLine()!=""){
			testStorage.delete(tempTask);
		}
		br.close();*/
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
	public void testSearchIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteTask() throws IOException {
		//testStorage.add(tempTask);
		testStorage.delete(tempTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = "[]";
		assertEquals(expected, result);
		//fail("Not yet implemented");
	}

	@Test
	public void testUpdateTaskTask() {
		fail("Not yet implemented");
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
