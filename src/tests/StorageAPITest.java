package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import storage.StorageAPI;
import zombietask.Task;

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
	Calendar date1 = new GregorianCalendar(2000,1,1) ;
	Calendar date2 = new GregorianCalendar(2000,1,2) ;
	
	Task tempTask = null; // new Task(taskName, deadline);
	Task updateTask = null; //new Task(updateName, deadline);
	Task task1 = null; //new Task(taskName, date1);
	Task task2 = null; //new Task(updateName, date2);
	
	@Before
	public void setUp() throws Exception {
		testStorage.createFile();
		file = testStorage.getFile();
		
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
	public void testSearchIntArray() throws Exception {
		testStorage.add(tempTask);
		Task testResult = testStorage.search(testID);
		Task testExpected = tempTask;
		
		assertEquals(testExpected.getSubtask(),testResult.getSubtask());
		assertEquals(testExpected.getEndTime(),testResult.getEndTime());
		assertEquals(testExpected.getTags(),testResult.getTags());
		assertEquals(testExpected.getTaskName(),testResult.getTaskName());

		//fail("Not yet implemented");
	}
	
	@Test
	public void testSearchTwoDate() throws Exception {
		testStorage.add(task1);
		testStorage.add(task2);
		ArrayList<Task> tempTest = new ArrayList<Task> ();
		tempTest = testStorage.search(date1, date2);
		ArrayList<Task> expectedTest= new ArrayList<Task>();
		expectedTest.add(task1);
		expectedTest.add(task2);
		testStorage.delete(task2);
		testStorage.delete(task1);
		assertEquals(expectedTest.get(0).getSubtask(),tempTest.get(0).getSubtask());
		assertEquals(expectedTest.get(0).getEndTime(),tempTest.get(0).getEndTime());
		assertEquals(expectedTest.get(0).getTags(),tempTest.get(0).getTags());
		assertEquals(expectedTest.get(0).getTaskName(),tempTest.get(0).getTaskName());
		assertEquals(expectedTest.get(1).getSubtask(),tempTest.get(1).getSubtask());
		assertEquals(expectedTest.get(1).getEndTime(),tempTest.get(1).getEndTime());
		assertEquals(expectedTest.get(1).getTags(),tempTest.get(1).getTags());
		assertEquals(expectedTest.get(1).getTaskName(),tempTest.get(1).getTaskName());
	}

	@Test
	public void testDeleteTask() throws IOException {
		//testStorage.add(tempTask);
		testStorage.delete(updateTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = null;
		assertEquals(expected, result);
		//fail("Not yet implemented");
	}

	/*
	
	@Test
	public void testUpdateTaskTask() throws Exception {
		
		testStorage.update(tempTask, updateTask);
		BufferedReader br = new BufferedReader(new FileReader(file));
		result = br.readLine();
		br.close();
		expected = "[{\"tags\":[],\"subtasks\":[],\"taskName\":\"updateName\"}]";
		assertEquals(expected, result);
		//fail("Not yet implemented");
	}
	
	*/

	@Test
	public void testDisplayAll() throws Exception{
		testStorage.add(tempTask);
		testStorage.add(updateTask);
		ArrayList <Task> tempTest = new ArrayList <Task> ();
		ArrayList <Task> expectedTest = new ArrayList <Task> ();
		tempTest = testStorage.getAllTasks();
		expectedTest.add(tempTask);
		expectedTest.add(updateTask);
		testStorage.delete(tempTask);
		testStorage.delete(updateTask);
		assertEquals(expectedTest.get(0).getSubtask(),tempTest.get(0).getSubtask());
		assertEquals(expectedTest.get(0).getEndTime(),tempTest.get(0).getEndTime());
		assertEquals(expectedTest.get(0).getTags(),tempTest.get(0).getTags());
		assertEquals(expectedTest.get(0).getTaskName(),tempTest.get(0).getTaskName());
		assertEquals(expectedTest.get(1).getSubtask(),tempTest.get(1).getSubtask());
		assertEquals(expectedTest.get(1).getEndTime(),tempTest.get(1).getEndTime());
		assertEquals(expectedTest.get(1).getTags(),tempTest.get(1).getTags());
		assertEquals(expectedTest.get(1).getTaskName(),tempTest.get(1).getTaskName());
	}

	@Test
	public void testSetFile() throws IOException {
		testStorage.setFile(filenameTest);
		testStorage.createFile();
		file = testStorage.getFile();
		
		assertEquals(file.getName(),filenameTest);
		//fail("Not yet implemented");
	}

	@Test
	public void testCreateFile() throws IOException {
		testStorage.createFile();
		file = testStorage.getFile();
		assertEquals(file.getName(),filename);
		//fail("Not yet implemented");
	}
	
	
	

}